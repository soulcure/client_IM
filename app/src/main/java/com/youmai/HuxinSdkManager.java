package com.youmai;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.youmai.charger.ChargerApplication;
import com.youmai.config.AppConfig;
import com.youmai.service.HuxinService;
import com.youmai.socket.PduUtil;
import com.youmai.socket.ProtoCommandId;
import com.youmai.socket.ReceiveListener;
import com.youmai.util.AESCrypt;
import com.youmai.util.AppUtils;
import com.youmai.util.BCDUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by colin on 2016/7/15.
 * sdk 接口类
 */
public class HuxinSdkManager {
    private static final String TAG = "TcpClient";

    private static final int HANDLER_THREAD_INIT_CONFIG_START = 1;
    private static final int HANDLER_THREAD_AUTO_LOGIN = 2;

    private static HuxinSdkManager instance;


    private enum BIND_STATUS {
        IDLE, BINDING, BINDED
    }

    private HuxinService.HuxinServiceBinder huxinService = null;
    private BIND_STATUS binded = BIND_STATUS.IDLE;

    private Context mContext;
    private ChargerApplication app;

    private String publicKey;
    private boolean isRSA;
    private boolean isAES;

    private List<InitListener> mInitListenerList;
    private LoginStatusListener mLoginStatusListener;

    private ProcessHandler mProcessHandler;


    /**
     * SDK初始化结果监听器
     */
    public interface InitListener {
        void success();

        void fail();
    }

    public interface LoginStatusListener {
        void onKickOut();

        void onReLoginSuccess();
    }


    /**
     * 私有构造函数
     */
    private HuxinSdkManager() {
        mInitListenerList = new ArrayList<>();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 获取呼信sdk单例索引
     *
     * @return
     */
    public static HuxinSdkManager instance() {
        if (instance == null) {
            instance = new HuxinSdkManager();
        }
        return instance;
    }


    /**
     * 呼信sdk初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.init(context, null);
    }


    /**
     * 呼信sdk初始化
     *
     * @param context
     */
    public void init(final Context context, InitListener listener) {
        app = (ChargerApplication) context.getApplicationContext();
        mContext = context.getApplicationContext();

        String processName = AppUtils.getProcessName(context, android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName.equals(context.getPackageName());
            if (!defaultProcess) {
                return;
            }
        }

        if (listener != null) {
            mInitListenerList.add(listener);
        }

        if (binded == BIND_STATUS.IDLE) {
            binded = BIND_STATUS.BINDING;
            initHandler();

            mProcessHandler.sendEmptyMessage(HANDLER_THREAD_INIT_CONFIG_START);

        } else if (binded == BIND_STATUS.BINDING) {

            //do nothing

        } else if (binded == BIND_STATUS.BINDED) {
            for (InitListener item : mInitListenerList) {
                item.success();
            }
            mInitListenerList.clear();
        }
    }


    private void initWork(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), HuxinService.class);
        context.getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        Log.v(TAG, "HuxinSdkManager in init");

    }


    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public boolean isRSA() {
        return isRSA;
    }

    public void setRSA(boolean RSA) {
        isRSA = RSA;
    }

    public boolean isAES() {
        return isAES;
    }

    public void setAES(boolean AES) {
        isAES = AES;
    }

    /**
     * 呼信sdk销毁
     *
     * @param
     */
    public void destroy() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            binded = BIND_STATUS.IDLE;
            mContext.getApplicationContext().unbindService(serviceConnection);
        }

    }


    public LoginStatusListener getLoginStatusListener() {
        return mLoginStatusListener;
    }

    public void setLoginStatusListener(LoginStatusListener listener) {
        this.mLoginStatusListener = listener;
    }


    public void loginOut() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            clearUserData();
        }
    }

    /**
     * 判断SDK是否登录
     *
     * @return
     */
    public boolean isLogin() {
        boolean res = false;
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            res = huxinService.isLogin();
        }
        return res;
    }

    /**
     * 判断SDK是否登录
     *
     * @return
     */
    public void setLogin(boolean b) {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            huxinService.setLogin(b);
        }
    }


    private void initAppForMainProcess(Context context) {
        String processName = AppUtils.getProcessName(context, android.os.Process.myPid());
        Log.e("colin", "processName:" + processName);
        if (processName != null) {
            boolean defaultProcess = processName.equals(context.getPackageName());
            if (defaultProcess) {
                HuxinSdkManager.instance().init(context);
            }
        }
    }


    /**
     * 判断SDK服务是否已经绑定成功
     *
     * @return
     */
    public boolean isBinded() {
        boolean res = false;
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            res = true;
        }
        return res;
    }


    /**
     * 判断tcp是否连接成功
     *
     * @return
     */
    public boolean isConnect() {
        boolean res = false;
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            res = huxinService.isConnect();
        }
        return res;
    }


    /**
     * tcp 重新连接
     */
    public void reConnect() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            huxinService.reConnect();
        }
    }


    /**
     * 关闭tcp连接
     *
     * @return
     */
    public void close() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            huxinService.close();
        }
    }

    /**
     * 开启心跳
     *
     * @return
     */
    public void startHeartBeat() {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            huxinService.startHeartBeat();
        }
    }


    private void clearUserData() {
        close();
    }


    private void waitBindingProto(final short commandId, final ByteBuffer data) {
        init(mContext, new InitListener() {
            @Override
            public void success() {
                huxinService.sendProto(commandId, data);
            }


            @Override
            public void fail() {
                String log = "bind server fail!";
            }
        });
    }


    private void waitBindingProto(final short commandId, final ByteBuffer data,
                                  final short rspId, final ReceiveListener callback) {
        init(mContext, new InitListener() {
            @Override
            public void success() {
                huxinService.sendProto(commandId, data, rspId, callback);
            }


            @Override
            public void fail() {
                String log = "bind server fail!";
            }
        });
    }


    private void waitBindingNotify(final short commandId, final ReceiveListener callback) {
        init(mContext, new InitListener() {
            @Override
            public void success() {
                huxinService.setNotifyListener(commandId, callback);
            }

            @Override
            public void fail() {
                String log = "bind server fail!";
            }
        });
    }


    /**
     * 发送socket协议
     *
     * @param commandId 命令码
     * @param buffer    数据
     */
    public void sendProto(final short commandId, final ByteBuffer buffer) {
        if (mContext != null) {
            if (binded == BIND_STATUS.BINDED) {
                huxinService.sendProto(commandId, buffer);
            } else {
                waitBindingProto(commandId, buffer);
            }
        } else {
            throw new IllegalStateException("huxin sdk no init");
        }

    }


    /**
     * 发送socket协议
     *
     * @param commandId 命令码
     * @param buffer    回调
     */
    public void sendProto(final short commandId, final ByteBuffer buffer,
                          short rspId, final ReceiveListener callback) {
        if (mContext != null) {
            if (binded == BIND_STATUS.BINDED) {
                huxinService.sendProto(commandId, buffer, rspId, callback);
            } else {
                waitBindingProto(commandId, buffer, rspId, callback);
            }
        } else {
            throw new IllegalStateException("huxin sdk no init");
        }

    }


    public void setNotifyListener(final short commandId, final ReceiveListener callback) {
        if (mContext != null) {
            if (binded == BIND_STATUS.BINDED) {
                huxinService.setNotifyListener(commandId, callback);
            } else {
                waitBindingNotify(commandId, callback);
            }
        } else {
            throw new IllegalStateException("huxin sdk no init");
        }

    }

    public void clearNotifyListener(short commandId) {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            huxinService.clearNotifyListener(commandId);
        }
    }


    /**
     * req socket ip and port
     * tcp login
     */
    private void socketLogin(final String uuid) {
        String ip = AppConfig.getSocketHost();
        int port = AppConfig.getSocketPort();
        InetSocketAddress isa = new InetSocketAddress(ip, port);
        connectTcp(uuid, isa);
    }


    /**
     * 用户tcp协议重登录，仅仅用于测试
     *
     * @param uuid
     * @param isa
     */
    public void connectTcp(String uuid, InetSocketAddress isa) {
        if (mContext != null && binded == BIND_STATUS.BINDED) {
            huxinService.connectTcp(uuid, isa);
        }
    }


    /**
     * (CMD=2)充电桩参数整形设置/查询应答
     */
    public void chargerIntSetting(ReceiveListener callback) {
        short commandId = ProtoCommandId.CMD_CHARGER_INT_SETTING_REQ;
        short rspId = ProtoCommandId.CMD_CHARGER_INT_SETTING_RSP;

        ByteBuffer buffer = ProtoCommandId.sendBuffer();
        buffer.put((byte) 0);  //4类型  0-查询 1-设置
        buffer.putInt(1111);// 5设置/查询参数 启始地址 同设置命令地址
        buffer.put((byte) 0);  //6设置/查询个数 同设置命令地址
        buffer.put((byte) 0);  //6设置/查询结果 0表示成功，其它失败

        byte[] params = new byte[20];
        buffer.put(params);


        sendProto(commandId, buffer, rspId, callback);
    }


    /**
     * 充电桩时间设置
     * (CMD=4)充电桩参数字符形设置/查询应答
     */
    public void chargeTimeSetting(int index, byte[] params) {
        short commandId = ProtoCommandId.CMD_CHARGER_STR_SETTING_REQ;

        ByteBuffer buffer = ProtoCommandId.sendBuffer();
        buffer.put((byte) 1);  //4类型 0-查询1-设置
        buffer.putInt(index);  //5 设置/查询参数 启始地址 同设置命令地址
        buffer.put((byte) 0);  //6设置/查询结果  0表示成功，其它失败
        buffer.put(params);  //7 设置参数信息 N

        sendProto(commandId, buffer);
    }


    /**
     * (CMD=6)充电桩对后台控制命令应答
     */
    public void chargerControlSetting(ReceiveListener callback) {
        short commandId = ProtoCommandId.CMD_CHARGER_CONTROL_REQ;
        short rspId = ProtoCommandId.CMD_CHARGER_CONTROL_RSP;

        ByteBuffer buffer = ProtoCommandId.sendBuffer();
        buffer.put((byte) 0);  //4充电枪口
        buffer.putInt(1);// 5命令启始标志
        buffer.put((byte) 0);  //6命令个数
        buffer.put((byte) 0);  //7命令执行结果

        sendProto(commandId, buffer, rspId, callback);
    }


    /**
     * (CMD=8)充电桩对后台下发的充电桩开启充电控制应答
     */
    public void chargerOpenSetting() {
        short commandId = ProtoCommandId.CMD_CHARGER_OPEN_REQ;

        ByteBuffer buffer = ProtoCommandId.sendBuffer();
        buffer.put((byte) 0);  //4命令执行结果  0表示成功，其它失败

        sendProto(commandId, buffer);
    }


    /**
     * (CMD=10)充电桩上传命令请求（预留）
     */
    public void chargerUploadSetting(ReceiveListener callback) {
        short commandId = ProtoCommandId.CMD_UPLOAD_REQ;
        short rspId = ProtoCommandId.CMD_UPLOAD_RSP;

        ByteBuffer buffer = ProtoCommandId.sendBuffer();
        buffer.put((byte) 0);  //4充电枪口
        buffer.putInt(1);// 5请求启始地址 桩请求命令参数地址列表
        byte[] params = new byte[20];
        buffer.put(params);  // 6参数
        sendProto(commandId, buffer, rspId, callback);
    }


    /**
     * (CMD=102)充电桩上传心跳包信息
     */
    public void chargerHeart(ReceiveListener callback) {
        short commandId = ProtoCommandId.CMD_HEART_BEAT_REQ;
        short rspId = ProtoCommandId.CMD_HEART_BEAT_RSP;

        ByteBuffer buffer = ProtoCommandId.sendBuffer();
        buffer.putShort((short) 0);  //4心跳序号

        sendProto(commandId, buffer, rspId, callback);
    }


    /**
     * status 工作状态
     * (CMD=104)充电桩状态信息包上报
     */
    public void chargeInfoUpload(byte status, int chargerTime, int chargerKWh, ReceiveListener callback) {
        short commandId = ProtoCommandId.CMD_STATUS_REQ;
        short rspId = ProtoCommandId.CMD_STATUS_RSP;

        ByteBuffer buffer = ProtoCommandId.sendBuffer();
        buffer.put((byte) 1);  //4充电枪数量
        buffer.put((byte) 0);  //5充电口号  多枪编码从1开始，只有1个枪的机型这个数据可以为0
        buffer.put((byte) 1);  //6充电枪类型 1=直流； 2=交流；
        buffer.put(status);  //7工作状态 0- 空闲中 1- 正准备开始充电 2- 充电进行中 3- 充电结束 4- 启动失败 5- 预约状态 6- 系统故障(不能给汽车充电）
        buffer.put((byte) 50);  //8 当前SOC %(直流有效，交流无效)

        buffer.putInt(0); //9 当前最高告警编码  0-无告警，参见附录1 此字段不能判定是否可以给汽车充电的条件,系统告警是否可 以给车充电用字段 7 判断
        buffer.put((byte) 0);  //10 车连接状态  0- 断开1-半连接2-连接 直流目前只有0和2状态 交流目前有0、1、2三种状态
        buffer.putInt(1000); //11 本次充电累计充电费用 从本次充电开始到目 前的累计充电费用 (包括电费与服务费），这里是整型，要乘以0.01才能得到真实的金额

        buffer.putInt(0); //12  预留
        buffer.putInt(0); //13  预留

        buffer.putShort((short) 3);  //14  直流充电电压  充电有效（直流 ，交充电有效（直流 ，交充电有效（直流 ，交充电有效（直流 ，交置 0）
        buffer.putShort((short) 4);  //15  直流充电电流  充电有效（直流 ，交充电有效（直流 ，交充电有效（直流 ，交充电有效（直流 ，交置 0）
        buffer.putShort((short) 5);  //16  BMS需求电压   充电有效（直流 ，交充电有效（直流 ，交充电有效（直流 ，交充电有效（直流 ，交置 0）
        buffer.putShort((short) 6);  //17  BMS需求电压   充电有效（直流 ，交充电有效（直流 ，交充电有效（直流 ，交充电有效（直流 ，交置 0）

        buffer.put((byte) 1);  //18 BMS充电模式  1-恒压 2-恒流

        buffer.putShort((short) 6);  //19  交流A相充电电压  直流桩表示三相输入电压；
        buffer.putShort((short) 6);  //20  交流B相充电电压  直流桩表示三相输入电压；
        buffer.putShort((short) 6);  //21  交流C相充电电压  交流桩单相输入，此项无效
        buffer.putShort((short) 6);  //22  交流A相充电电流  直流桩表示三相输入电流；交流桩有单相和三相输入电流
        buffer.putShort((short) 6);  //23  交流B相充电电流  交流桩单相输入，此项无效
        buffer.putShort((short) 6);  //24  交流C相充电电流  交流桩单相输入，此项无效
        buffer.putShort((short) 6);  //25  剩余充电时间(min) 充电有效（直流有效 交流无效 ）

        buffer.putInt(chargerTime); //26  充电时长(秒） 状态为充电时才有效
        buffer.putInt(chargerKWh); //27  本次充电累计充电电量 (0.01kwh) 状态为充电时才有效
        buffer.putInt(0); //28  充电前电表读数 0.01kw
        buffer.putInt(0); //29  当前电表读数 0.01kw

        buffer.put((byte) 1);  //30 充电启动方式 0:本地刷卡启动 1:后台启动 2:本地管理员启动
        buffer.put((byte) 1);  //31 充电策略 0自动充满 1按时间充满 2定金额 3按电量充满
        buffer.putInt(1); //32 充电策略参数 时间单位为1秒 金额单位为0.01元 电量时单位为0.01kw
        buffer.put((byte) 1);  //33 预约标志 0- 无预约（无效） 1- 预约有效
        buffer.put(ProtoCommandId.getCardCode());  //34 充电/预约卡号

        buffer.put((byte) 1);  //35 预约超时时间  单位分钟
        buffer.putLong(1000);  //36 预约/开始充电开始时间  标准时间

        buffer.putInt(1); //37 充电前卡余额
        buffer.putInt(0); //38 预留
        buffer.putInt(0); //39 充电功率 O.lkW/BIT
        buffer.putInt(0); //40 系统变量3 预留
        buffer.putInt(0); //41 系统变量4 预留
        buffer.putInt(0); //42 系统变量5 预留

        buffer.put((byte) 1);  //43 出风口温度 偏移量-50，-50 - 200
        buffer.put((byte) 1);  //44 环境温度 偏移量-50，-50 - 200
        buffer.put((byte) 1);  //45 充电枪温度 偏移量-50，-50 - 200

        sendProto(commandId, buffer, rspId, callback);
    }


    /**
     * （CMD=106）充电桩签到信息上报
     * 充电桩鉴权登录
     *
     * @param callback
     */
    public void chargerAuth(ReceiveListener callback) {
        short commandId = ProtoCommandId.CMD_LOGIN_REQ;
        short rspId = ProtoCommandId.CMD_LOGIN_RSP;

        ByteBuffer buffer = ProtoCommandId.sendBuffer();

        byte[] codeBytes = "im message test".getBytes(Charset.forName("UTF-8"));
        buffer.putInt(codeBytes.length);
        buffer.put(codeBytes);

        //sendProto(commandId, buffer, rspId, callback);
        sendProto(commandId, buffer, commandId, callback);
    }

    /**
     * (CMD=202)充电桩上报充电记录信息
     */
    public void chargerHistory(ByteBuffer buffer, ReceiveListener callback) {
        short commandId = ProtoCommandId.CMD_LAST_CHARGE_INFO_REQ;
        short rspId = ProtoCommandId.CMD_LAST_CHARGE_INFO_RSP;
        sendProto(commandId, buffer, rspId, callback);
    }


    /**
     * (CMD=202)充电桩上报充电记录信息
     */
    public void chargerHistory(ReceiveListener callback) {
        short commandId = ProtoCommandId.CMD_LAST_CHARGE_INFO_REQ;
        short rspId = ProtoCommandId.CMD_LAST_CHARGE_INFO_RSP;

        long curTime = System.currentTimeMillis();

        ByteBuffer buffer = ProtoCommandId.sendBuffer();
        buffer.put((byte) 1);  //4充电枪位置类型  1-直流2-交流
        buffer.put((byte) 1);  //5充电枪口
        buffer.put(ProtoCommandId.getCardCode());  //6充电卡号
        buffer.putLong(BCDUtil.bcdTime(curTime - 10 * 1000));//7充电开始时间
        buffer.putLong(BCDUtil.bcdTime(curTime));//8充电结束时间
        buffer.putInt(20);//9充电时间长度 单位秒
        buffer.put((byte) 22);  //10开始SOC
        buffer.put((byte) 33);  //11结束SOC
        buffer.putInt(0);  //12充电结束原因
        buffer.putInt(20);  //13本次充电电量
        buffer.putInt(20);  //14充电前电表读数
        buffer.putInt(30);  //15充电后电表读数

        int price = app.getPrice();
        buffer.putInt(30 * price);  //16本次充电金额
        buffer.putInt(0);  //17预留
        buffer.putInt(300);  //18充电前卡余额
        buffer.putInt(2);  //19当前充电记录索引
        buffer.putInt(1);  //20总充电记录条目
        buffer.put((byte) 0);  //21 预留
        buffer.put((byte) 0);  //22 充电策略  0:充满为止 1:时间控制充电 2:金额控制充电 3:电量控制充电
        buffer.putInt(100);  //23 充电策略参数  时间单位为 1秒 金额单位为0.01元 电量时单位为0.01kw
        buffer.put(ProtoCommandId.getVinCode());  //24 车辆VIN
        buffer.putLong(145245);  //25 车牌号

        buffer.putShort((short) 2); //26 时段1充电电量
        buffer.putShort((short) 2); //27 时段1充电电量
        buffer.putShort((short) 2); //28 时段1充电电量
        buffer.putShort((short) 2); //29 时段1充电电量
        buffer.putShort((short) 2); //30 时段1充电电量
        buffer.putShort((short) 2); //31 时段1充电电量
        buffer.putShort((short) 2); //32 时段1充电电量
        buffer.putShort((short) 2); //33 时段1充电电量
        buffer.putShort((short) 2); //34 时段1充电电量
        buffer.putShort((short) 2); //35 时段1充电电量
        buffer.putShort((short) 2); //36 时段1充电电量
        buffer.putShort((short) 2); //37 时段1充电电量
        buffer.putShort((short) 2); //38 时段1充电电量
        buffer.putShort((short) 2); //39 时段1充电电量
        buffer.putShort((short) 2); //40 时段1充电电量
        buffer.putShort((short) 2); //41 时段1充电电量
        buffer.putShort((short) 2); //42 时段1充电电量
        buffer.putShort((short) 2); //43 时段1充电电量
        buffer.putShort((short) 2); //44 时段1充电电量
        buffer.putShort((short) 2); //45 时段1充电电量
        buffer.putShort((short) 2); //46 时段1充电电量
        buffer.putShort((short) 2); //47 时段1充电电量
        buffer.putShort((short) 2); //48 时段1充电电量
        buffer.putShort((short) 2); //49 时段1充电电量
        buffer.putShort((short) 2); //50 时段1充电电量
        buffer.putShort((short) 2); //51 时段1充电电量
        buffer.putShort((short) 2); //52 时段1充电电量
        buffer.putShort((short) 2); //53 时段1充电电量
        buffer.putShort((short) 2); //54 时段1充电电量
        buffer.putShort((short) 2); //55 时段1充电电量
        buffer.putShort((short) 2); //56 时段1充电电量
        buffer.putShort((short) 2); //57 时段1充电电量
        buffer.putShort((short) 2); //58 时段1充电电量
        buffer.putShort((short) 2); //59 时段1充电电量
        buffer.putShort((short) 2); //60 时段1充电电量
        buffer.putShort((short) 2); //61 时段1充电电量
        buffer.putShort((short) 2); //62 时段1充电电量
        buffer.putShort((short) 2); //63 时段1充电电量
        buffer.putShort((short) 2); //64 时段1充电电量
        buffer.putShort((short) 2); //65 时段1充电电量
        buffer.putShort((short) 2); //66 时段1充电电量
        buffer.putShort((short) 2); //67 时段1充电电量
        buffer.putShort((short) 2); //68 时段1充电电量
        buffer.putShort((short) 2); //69 时段1充电电量
        buffer.putShort((short) 2); //70 时段1充电电量
        buffer.putShort((short) 2); //71 时段1充电电量
        buffer.putShort((short) 2); //72 时段1充电电量
        buffer.putShort((short) 2); //73 时段1充电电量
        buffer.put((byte) 1); //74 启动方式 0：本地刷卡启动 1:后台启动 2：本地管理员启动
        buffer.put(ProtoCommandId.getOrderCode()); //75 充电流水号

        sendProto(commandId, buffer, rspId, callback);
    }


    /**
     * （CMD=1104）响应充电桩费用设置
     */
    public void chargeRateSetting() {
        short commandId = ProtoCommandId.CMD_ELEC_PRICE_REQ;

        ByteBuffer buffer = ProtoCommandId.sendBuffer();
        buffer.put((byte) 0);  //4设置/查询结果  0表示成功，其它失败

        sendProto(commandId, buffer);
    }


    /**
     * （CMD=1202）充电桩密码登入报文
     *
     * @param callback
     */
    public void chargerLoginByEncrypt(ReceiveListener callback) {
        short commandId = ProtoCommandId.CMD_ENCRYPTION_PACKAGE_REQ;
        short rspId = ProtoCommandId.CMD_ENCRYPTION_PACKAGE_RSP;

        ByteBuffer buffer = ByteBuffer.allocate(AppConfig.SEND_BUFFER_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort((short) 0);  // 1预留
        buffer.putShort((short) 0);  //2预留
        buffer.put(ProtoCommandId.getLoginPassWord()); //3 桩登入服务器密码
        buffer.putInt(new Random().nextInt());  //4 桩生成随机数

        byte[] key = AESCrypt.instance().getKeyCode();
        Log.d(TAG, commandId + " client set AES key:" + PduUtil.bytes2HexString(key));

        buffer.put(key); //5 AES加密密钥

        sendProto(commandId, buffer, rspId, callback);
    }


    /**
     * bind service callback
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof HuxinService.HuxinServiceBinder) {
                huxinService = (HuxinService.HuxinServiceBinder) service;
                binded = BIND_STATUS.BINDED;
                for (InitListener item : mInitListenerList) {
                    item.success();
                }
                mInitListenerList.clear();
                Log.v(TAG, "Service Connected...");
            }
        }

        // 连接服务失败后，该方法被调用
        @Override
        public void onServiceDisconnected(ComponentName name) {
            huxinService = null;
            binded = BIND_STATUS.IDLE;
            for (InitListener item : mInitListenerList) {
                item.fail();
            }
            mInitListenerList.clear();
            Log.e(TAG, "Service Failed...");
        }
    };

    private void autoLogin() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            mProcessHandler.sendEmptyMessage(HANDLER_THREAD_AUTO_LOGIN);
        }
    }


    /**
     * 线程初始化
     */
    private void initHandler() {
        if (mProcessHandler == null) {
            HandlerThread handlerThread = new HandlerThread(
                    "handler looper Thread");
            handlerThread.start();
            mProcessHandler = new ProcessHandler(handlerThread.getLooper());
        }
    }

    /**
     * 子线程handler,looper
     *
     * @author Administrator
     */
    private class ProcessHandler extends Handler {

        public ProcessHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_THREAD_INIT_CONFIG_START:
                    initWork(mContext);
                    break;
                case HANDLER_THREAD_AUTO_LOGIN:
                    break;
                default:
                    break;
            }

        }

    }


}

package com.youmai.charger;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blakequ.rsa.Base64Utils;
import com.blakequ.rsa.RSAProvider;
import com.google.zxing.WriterException;
import com.youmai.HuxinSdkManager;
import com.youmai.db.bean.ChargerBean;
import com.youmai.db.bean.PriceBean;
import com.youmai.socket.PduUtil;
import com.youmai.socket.ProtoCommandId;
import com.youmai.socket.ReceiveListener;
import com.youmai.util.AESCrypt;
import com.youmai.util.BCDUtil;
import com.youmai.util.SteamUtil;
import com.youmai.util.ZXingUtil;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TcpClient";

    private static final int CHARGER_INFO = 104;
    private static final int CHARGER_COMPLIED = 202;


    private ImageView imgQrCode;

    private int chargerTime;
    private int chargerKWH;

    private NormalHandler mHandler;

    private byte[] cardNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new NormalHandler(this);

        initView();

        setNotifyListener();
    }

    private void initView() {
        imgQrCode = findViewById(R.id.imgQrCode);

        findViewById(R.id.btn_add_db).setOnClickListener(this);
        findViewById(R.id.btn_reconnect).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_cmd202).setOnClickListener(this);
        findViewById(R.id.btn_test).setOnClickListener(this);
    }


    private void setNotifyListener() {

        //服务器下发充电桩字符型工作参数
        HuxinSdkManager.instance().setNotifyListener(ProtoCommandId.CMD_CHARGER_STR_SETTING_RSP,
                new ReceiveListener() {
                    @Override
                    public void OnRec(ByteBuffer buffer) {
                        handleStrSetting(buffer);
                    }
                });

        //服务器下发设置充电费率
        HuxinSdkManager.instance().setNotifyListener(ProtoCommandId.CMD_ELEC_PRICE_RSP,
                new ReceiveListener() {
                    @Override
                    public void OnRec(ByteBuffer buffer) {
                        handlePriceSetting(buffer);
                    }
                });


        //监听服务器下发充电桩开启充电控制命令
        HuxinSdkManager.instance().setNotifyListener(ProtoCommandId.CMD_CHARGER_OPEN_RSP,
                new ReceiveListener() {
                    @Override
                    public void OnRec(ByteBuffer buffer) {
                        handleOpenSetting(buffer);
                    }
                });
    }


    /**
     * 发送登录IM服务器请求
     */
    private void tcpLogin() {
        HuxinSdkManager.instance().chargerAuth(new ReceiveListener() {
            @Override
            public void OnRec(ByteBuffer buffer) {
                loginSuccess(buffer);
            }
        });
    }


    private void loginSuccess(ByteBuffer buffer) {
        Log.v(TAG, "签名签到成功");
        HuxinSdkManager.instance().setLogin(true);

        byte[] test = new byte[buffer.remaining()];
        buffer.get(test);
        Toast.makeText(mContext, new String(test, Charset.forName("UTF-8")), Toast.LENGTH_SHORT).show();
    }


    private void authSuccess() {
        //(CMD=102)充电桩上传心跳包信息
        HuxinSdkManager.instance().startHeartBeat();

        //(CMD=104)充电桩状态信息包上报
        HuxinSdkManager.instance().chargeInfoUpload((byte) 0, chargerTime,
                chargerKWH, new ReceiveListener() {
                    @Override
                    public void OnRec(ByteBuffer buffer) {
                        Log.v(TAG, "充电桩状态信息包上报成功");
                    }
                });

        //(CMD=202)充电桩上报充电记录信息
        HuxinSdkManager.instance().chargerHistory(new ReceiveListener() {
            @Override
            public void OnRec(ByteBuffer buffer) {
                Log.v(TAG, "充电桩上报充电记录信息成功");
            }
        });
    }

    private void handleStrSetting(ByteBuffer buffer) {
        Log.v(TAG, "收到后台服务器下发充电桩字符型工作参数");
        short test1 = buffer.getShort();  //1预留
        short test2 = buffer.getShort();  //2预留
        byte port = buffer.get();//3 类型 0-查询1-设置
        int paramIndex = buffer.getInt(); //4 设置/查询参数 启始地址
        short count = buffer.getShort(); //5 设置参数字节数
        byte[] params = new byte[count];
        buffer.get(params); //6 设置数据
        switch (paramIndex) {
            case 1:
                String chargerCode = SteamUtil.getString(params, "US-ASCII");
                Log.d(TAG, "server set chargerCode: " + chargerCode);
                break;
            case 2:
                long time = SteamUtil.getLongByBytes(params, false);
                Calendar calendar = BCDUtil.getTimeByBcd(time);
                Log.d(TAG, "server set synchronous system time: " + calendar.getTime());
                break;
            case 3:
                String adminCode = SteamUtil.getString(params, "US-ASCII");
                Log.d(TAG, "server set adminCode: " + adminCode);
                break;
            case 4:
                String operatorCode = SteamUtil.getString(params, "US-ASCII");
                Log.d(TAG, "server set adminCode: " + operatorCode);
                try {
                    Bitmap bitmap = ZXingUtil.encode(operatorCode, 200, 200);
                    imgQrCode.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    Log.e(TAG, e.toString());
                }
                break;
            case 5:
                //5 预留
                break;
            case 6:
                //6 桩登入服务器密码
                break;
            case 7:
                String qrCode = SteamUtil.getString(params, "US-ASCII");
                Log.d(TAG, "server set adminCode: " + qrCode);
                break;
            case 8:
                //8 预留 客户服务热线1 assic码
                break;
            case 9:
                //9 预留 客户服务热线2 assic码
                break;
            case 10:
                //10 预留 用户支付二维码  assic码
                break;
            case 11:
                String qrCodePre = SteamUtil.getString(params, "US-ASCII");
                Log.d(TAG, "server set adminCode: " + qrCodePre);
                try {
                    Bitmap bitmap = ZXingUtil.encode(qrCodePre, 200, 200);
                    imgQrCode.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    Log.e(TAG, e.toString());
                }
                break;
            case 12:
                //12 DLT645-2007电表地址
                break;
            default:
                break;
        }
        HuxinSdkManager.instance().chargeTimeSetting(paramIndex, params);
    }

    private void handlePriceSetting(ByteBuffer buffer) {
        Log.v(TAG, "收到后台服务器下发服务器下发设置充电费率");

        List<PriceBean> list = new ArrayList<>(12);
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率1
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率2
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率3
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率4
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率5
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率6
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率7
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率8
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率9
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率10
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率11
        list.add(new PriceBean(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt())); //费率12

        app.setPriceList(list);

        HuxinSdkManager.instance().chargeRateSetting();
    }

    private void handleOpenSetting(ByteBuffer buffer) {
        Log.v(TAG, "收到后台服务器下发充电桩开启充电控制命令");
        short test1 = buffer.getShort();  //1预留
        short test2 = buffer.getShort();  //2预留
        byte port = buffer.get();//3充电枪口 只有一机一桩此参数可为0；多枪的编号从1开始

        int type = buffer.getInt(); //4充电生效类型
        int pw = buffer.getInt(); //5界面充电停止密码 建议为用户卡号最后 6 位，若服务器没有下发此 位，若服务器没有下发此 位，若服务器没有下发此 字段，桩默认取充电卡 号 的 最 后 4 位 ， 如 卡 号 “ 1122334455667788 ”， 其最后 4位是“ 7788 ”， 而不是 0。
        int celue = buffer.getInt();//6 充电策略 0:充满为止 1:时间控制充电 2:金额控制充电 3:电量控制充电
        int chargeParam = buffer.getInt();//7 充电策略参数 时间单位为1秒 金额单位为0.01元 电量时单位为0.01kw
        long time = buffer.getLong();//8预约/定时启动时间  标准时间
        byte chaoshi = buffer.get();//9 预约超时时间 单位分钟

        cardNum = new byte[32];//10 用户卡号/用户识别号  ASSIC码，不够长度填'\0'
        buffer.get(cardNum);

        byte tips = buffer.get();//11 断网充电标志 0-不允许1-允许

        int offline = buffer.getInt();//12 离线可充电电量

        byte[] orderNum = new byte[32];//13 充电流水号 若后台没有下发，桩会自 若后台没有下发，桩会自 若后台没有下发，桩会自 动生成
        buffer.get(orderNum);

        //(CMD=8)充电桩对后台下发的充电桩开启充电控制应答
        HuxinSdkManager.instance().chargerOpenSetting();

        mHandler.sendEmptyMessageDelayed(CHARGER_INFO, 5 * 1000); //5s后上报充电信息

        mHandler.sendEmptyMessageDelayed(CHARGER_COMPLIED, 30 * 1000); //30s后上报充电完成
    }


    private void cmd202() {
        List<ChargerBean> list = app.getChargerList();
        for (ChargerBean item : list) {
            ByteBuffer sendBuffer = item.toBuffer();
            HuxinSdkManager.instance().chargerHistory(sendBuffer, new ReceiveListener() {
                @Override
                public void OnRec(ByteBuffer buffer) {
                    short test1 = buffer.getShort();
                    short test2 = buffer.getShort();
                    short test3 = buffer.get();
                    byte[] orderCode = new byte[32];
                    buffer.get(orderCode);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_db:
                app.setChargerData();
                break;
            case R.id.btn_reconnect:
                HuxinSdkManager.instance().reConnect();
                break;
            case R.id.btn_login:
                tcpLogin();
                break;
            case R.id.btn_cmd202:
                cmd202();
                break;
            case R.id.btn_test:
                test();
                break;
        }
    }

    private void test() {
        long curTime = System.currentTimeMillis();

        Calendar curCal = Calendar.getInstance(Locale.CHINA);
        curCal.setTimeInMillis(curTime);

        long bcdTime = BCDUtil.bcdTime(curTime);
        Log.d("colin", "cur Time:" + curCal.getTime().toString());
        //Log.d("colin", "bcdTime:" + bcdTime);
        Calendar calendar = BCDUtil.getTimeByBcd(bcdTime);
        Log.d("colin", "bcd Time:" + calendar.getTime().toString());

        try {
            String str = "hello";
            byte[] data = str.getBytes();

            String testData = PduUtil.bytes2HexString(data);

            Log.d("colin", "加密前数据string:" + str);
            Log.d("colin", "加密前数据byte[]:\n" + testData);

            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) pair.getPrivate();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) pair.getPublic();
            String publicKey = Base64Utils.encode(rsaPublicKey.getEncoded());
            String privateKey = Base64Utils.encode(rsaPrivateKey.getEncoded());

            byte[] encryptData = RSAProvider.encryptPublicKey(data, publicKey);
            byte[] decryptData = RSAProvider.decryptPrivateKey(encryptData, privateKey);

            String RSA_bytes = PduUtil.bytes2HexString(decryptData);
            String RSA_Str = new String(decryptData);

            Log.d("colin", "RSA 解密后数据 string:" + RSA_Str);
            Log.d("colin", "RSA 解密后数据byte[]:\n" + RSA_bytes);


            byte[] encryptData1 = AESCrypt.instance().encrypt(data);

            String AES_encrypt = PduUtil.bytes2HexString(encryptData1);

            byte[] decryptData1 = AESCrypt.instance().decrypt(encryptData1);

            String AES_bytes = PduUtil.bytes2HexString(decryptData1);
            String AES_Str = new String(decryptData1);

            Log.d("colin", "AES 加密后数据 byte[]:\n" + AES_encrypt);
            Log.d("colin", "AES 解密后数据 byte[]:\n" + AES_bytes);

            Log.d("colin", "AES 解密后数据 string:" + AES_Str);

            //byte[] params = {(byte) 0xAA, (byte) 0xF5, (byte) 0x89, 0x00, (byte) 0x8A, 0x01, (byte) 0xB2, 0x04, (byte) 0x8A, 0x33, 0x6F, (byte) 0x9C, (byte) 0xA3, 0x23, (byte) 0xFB, (byte) 0xD4, 0x3D, (byte) 0xFC, (byte) 0xB3, 0x38, (byte) 0xF9, (byte) 0xF9, 0x6A, (byte) 0xAF, (byte) 0xBB, 0x75, (byte) 0xE3, (byte) 0xAD, (byte) 0xB5, 0x2C, 0x66, 0x28, 0x4D, (byte) 0xBD, 0x54, (byte) 0xD2, (byte) 0xA9, (byte) 0xC5, (byte) 0xD3, 0x46, 0x7F, (byte) 0xC5, (byte) 0xF0, 0x1A, 0x17, 0x19, (byte) 0x8F, 0x57, 0x2D, (byte) 0xFB, 0x74, 0x4A, (byte) 0x86, 0x3E, 0x33, (byte) 0xCF, 0x73, (byte) 0xF7, 0x71, 0x5D, (byte) 0xF1, (byte) 0xDA, 0x29, 0x28, 0x72, 0x23, 0x04, 0x09, (byte) 0x90, 0x5C, (byte) 0xDD, (byte) 0xC3, (byte) 0xFA, (byte) 0x8D, 0x0C, 0x58, 0x4B, (byte) 0x9A, 0x25, 0x71, 0x46, 0x7A, 0x7D, 0x0B, 0x41, (byte) 0x9F, (byte) 0xF2, (byte) 0xFD, (byte) 0xDF, (byte) 0xF4, (byte) 0xDE, 0x24, 0x53, 0x33, 0x45, (byte) 0xAE, (byte) 0xA2, (byte) 0xDB, 0x6A, (byte) 0xCE, (byte) 0x86, 0x5D, 0x47, 0x36, (byte) 0xDB, 0x79, (byte) 0xC9, 0x45, (byte) 0xA0, (byte) 0xE1, (byte) 0xD4, 0x6F, 0x0D, 0x63, 0x03, 0x7E, 0x02, (byte) 0x9B, (byte) 0xC7, (byte) 0xD1, (byte) 0xA6, (byte) 0xC5, 0x08, 0x52, (byte) 0x80, (byte) 0xF0, (byte) 0xA5, (byte) 0xBB, (byte) 0xF1, 0x12, 0x33, 0x08, 0x58, 0x0A, (byte) 0xB6, 0x73, 0x40};
            short[] params = {0xAA, 0xF5, 0x69, 0x00, 0x0A, 0x01, 0x6A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x4E, 0x30, 0x31, 0x30, 0x31, 0x31, 0x36, 0x39, 0x38, 0x30, 0x31, 0x30, 0x30, 0x30, 0x30, 0x33, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xAA, 0x28, 0x00, 0x00, 0x00, 0x00, 0xE9, 0x03, 0x00, 0x00, 0x02, 0x0A, 0x00, 0x00, 0x01, 0x05, 0x01, 0xB8, 0x22, 0x00, 0x00, 0x95, 0x5F, 0xE1, 0x64, 0x68, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x30, 0x97, 0x9B, 0x5A, 0x1A, 0x00, 0xC9};
            int sum = 0;
            for (int i = 0; i < params.length; i++) {
                if (i > 5 && i < params.length - 1) {
                    sum = sum + params[i];
                }
            }

            Log.d("colin", "sum:" + sum);
            byte dataCheck = (byte) sum;

            String dataCheck1 = PduUtil.byte2HexString(dataCheck);

            Log.d("colin", "dataCheck:" + dataCheck);
            Log.d("colin", "dataCheck1:" + dataCheck1);

            byte test1 = -1;

            int temp = test1 & 0x000000FF;

            int test2 = temp;
            Log.d("colin", "test2:" + test2);


        } catch (Exception e) {
            Log.d("colin", e.toString());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(CHARGER_INFO);
    }

    private static class NormalHandler extends Handler {
        private final WeakReference<MainActivity> mTarget;

        NormalHandler(MainActivity target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity act = mTarget.get();
            switch (msg.what) {

                case CHARGER_INFO:
                    //(CMD=104)充电桩状态信息包上报
                    HuxinSdkManager.instance().chargeInfoUpload((byte) 2, act.chargerTime,
                            act.chargerKWH, new ReceiveListener() {
                                @Override
                                public void OnRec(ByteBuffer buffer) {
                                    Log.v(TAG, "充电桩上报充电信息");
                                }
                            });
                    act.chargerTime += 5;
                    act.chargerKWH += 0.5;
                    sendEmptyMessageDelayed(CHARGER_INFO, 5 * 1000); //每5s钟 上报充电信息
                    break;

                case CHARGER_COMPLIED:
                    removeMessages(CHARGER_INFO);

                    //(CMD=202)充电桩上报充电记录信息
                    HuxinSdkManager.instance().chargerHistory(new ReceiveListener() {
                        @Override
                        public void OnRec(ByteBuffer buffer) {
                            Log.v(TAG, "充电桩上报充电记录信息成功");
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }
}

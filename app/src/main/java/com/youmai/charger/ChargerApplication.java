package com.youmai.charger;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.youmai.HuxinSdkManager;
import com.youmai.db.bean.ChargerBean;
import com.youmai.db.bean.PriceBean;
import com.youmai.db.dao.ChargerBeanDao;
import com.youmai.db.dao.DaoMaster;
import com.youmai.db.dao.DaoSession;
import com.youmai.db.dao.PriceBeanDao;
import com.youmai.socket.ProtoCommandId;
import com.youmai.util.BCDUtil;

import org.greenrobot.greendao.database.Database;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ChargerApplication extends MultiDexApplication {

    private static final String TAG = "TcpClient";
    private static final String DB_NAME = "charger_db";

    private Context mContext;
    private DaoSession daoSession;

    private List<PriceBean> priceList;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Stetho.initializeWithDefaults(this);

        initGreenDao();
        //Huxin IM SDK初始化
        HuxinSdkManager.instance().init(this, new HuxinSdkManager.InitListener() {
            @Override
            public void success() {
                Toast.makeText(mContext, "初始化成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail() {
                Toast.makeText(mContext, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }


    public void setChargerData() {
        if (daoSession == null) {
            initGreenDao();
        }

        long curTime = System.currentTimeMillis();

        ChargerBeanDao dao = daoSession.getChargerBeanDao();
        ChargerBean note = new ChargerBean();

        note.setReserved1((short) 0); //1预留
        note.setReserved2((short) 0); //2预留
        note.setChargerCode(ProtoCommandId.getMatchCode());  //3充电桩编码
        note.setGunType((byte) 1);//4充电枪位置类型  1-直流2-交流
        note.setGunPort((byte) 1); //5充电枪口
        note.setChargerCode(ProtoCommandId.getCardCode());//6充电卡号
        note.setBeginTime(BCDUtil.bcdTime(curTime - 3600 * 1000));//7充电开始时间
        note.setStopTime(BCDUtil.bcdTime(curTime));//8充电结束时间
        note.setChargeTime(3600);// 9充电时间长度 单位秒
        note.setStartSOC((byte) 1);//10开始SOC
        note.setStopSOC((byte) 50);//11结束SOC
        note.setOverReason(1); //12充电结束原因
        note.setPower(20);   //13本次充电电量
        note.setStartReading(20);//14充电前电表读数
        note.setStopReading(40);//15充电后电表读数
        note.setAmount(100);//16本次充电金额
        note.setReserved17(0); //17预留
        note.setCardOverage(88); //18充电前卡余额
        note.setCardIndex(6); //19当前充电记录索引
        note.setRecord(1); //20总充电记录条目
        note.setReserved21((byte) 0); //21预留
        note.setStrategy((byte) 0); //22 充电策略  0:充满为止 1:时间控制充电 2:金额控制充电 3:电量控制充电
        note.setChargeParam(100); //23 充电策略参数  时间单位为 1秒 金额单位为0.01元 电量时单位为0.01kw
        note.setVinCode(ProtoCommandId.getVinCode()); //24 车辆VIN
        note.setNumberPlate(1542411);//25 车牌号
        note.setTime1((short) 2); //26 时段1充电电量
        note.setTime2((short) 2); //27 时段2充电电量
        note.setTime3((short) 2); //28 时段3充电电量
        note.setTime4((short) 2); //29 时段4充电电量
        note.setTime5((short) 2); //30 时段5充电电量
        note.setTime6((short) 2); //31 时段6充电电量
        note.setTime7((short) 2); //32 时段7充电电量
        note.setTime8((short) 3); //33 时段8充电电量
        note.setTime9((short) 3); //34 时段9充电电量
        note.setTime10((short) 3); //35 时段10充电电量
        note.setTime11((short) 3); //36 时段11充电电量
        note.setTime12((short) 3); //37 时段12充电电量
        note.setTime13((short) 3); //38 时段13充电电量
        note.setTime14((short) 3); //39 时段14充电电量
        note.setTime15((short) 3); //40 时段15充电电量
        note.setTime16((short) 4); //41 时段16充电电量
        note.setTime17((short) 4); //42 时段17充电电量
        note.setTime18((short) 4); //43 时段18充电电量
        note.setTime19((short) 4); //44 时段19充电电量
        note.setTime20((short) 4); //45 时段20充电电量
        note.setTime21((short) 4); //46 时段21充电电量
        note.setTime22((short) 4); //47 时段22充电电量
        note.setTime23((short) 4); //48 时段23充电电量
        note.setTime24((short) 4); //49 时段24充电电量
        note.setTime25((short) 4); //50 时段25充电电量
        note.setTime26((short) 4); //51 时段26充电电量
        note.setTime27((short) 5); //52 时段27充电电量
        note.setTime28((short) 5); //53 时段28充电电量
        note.setTime29((short) 5); //54 时段29充电电量
        note.setTime30((short) 5); //55 时段30充电电量
        note.setTime31((short) 5); //56 时段31充电电量
        note.setTime32((short) 5); //57 时段32充电电量
        note.setTime33((short) 5); //58 时段33充电电量
        note.setTime34((short) 5); //59 时段34充电电量
        note.setTime35((short) 5); //60 时段35充电电量
        note.setTime36((short) 5); //61 时段36充电电量
        note.setTime37((short) 6); //62 时段37充电电量
        note.setTime38((short) 6); //63 时段38充电电量
        note.setTime39((short) 6); //64 时段39充电电量
        note.setTime40((short) 6); //65 时段40充电电量
        note.setTime41((short) 6); //66 时段41充电电量
        note.setTime42((short) 6); //67 时段42充电电量
        note.setTime43((short) 6); //68 时段43充电电量
        note.setTime44((short) 6); //69 时段44充电电量
        note.setTime45((short) 6); //70 时段45充电电量
        note.setTime46((short) 6); //71 时段46充电电量
        note.setTime47((short) 7); //72 时段47充电电量
        note.setTime48((short) 7); //73 时段48充电电量
        note.setStartMethod((byte) 0); //74 启动方式 0：本地刷卡启动 1:后台启动 2：本地管理员启动
        note.setOrderCode(ProtoCommandId.getOrderCode()); //75 充电流水号

        dao.insert(note);
    }

    public List<ChargerBean> getChargerList() {
        if (daoSession == null) {
            initGreenDao();
        }
        ChargerBeanDao msgDao = daoSession.getChargerBeanDao();
        return msgDao.queryBuilder().build().list();
    }


    public List<PriceBean> getPriceList() {
        if (daoSession == null) {
            initGreenDao();
        }
        PriceBeanDao msgDao = daoSession.getPriceBeanDao();
        return msgDao.queryBuilder().build().list();
    }

    public void setPriceList(List<PriceBean> list) {
        if (daoSession == null) {
            initGreenDao();
        }
        priceList = null;

        PriceBeanDao msgDao = daoSession.getPriceBeanDao();
        msgDao.deleteAll();
        msgDao.updateInTx(list);
    }


    public int getPrice() {
        if (priceList == null || priceList.isEmpty()) {
            priceList = getPriceList();
        }
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        if (priceList != null) {
            for (PriceBean item : priceList) {
                int startHour = item.getStartHour();
                int startMin = item.getStartMin();
                int endHour = item.getEndHour();
                int endMin = item.getEndMin();

                boolean isStart = (startHour == hour && startMin <= min) || (startHour < hour);
                boolean isEnd = (endHour == hour && endMin > min) || (endHour > hour);

                if (isStart && isEnd) {
                    return item.getPrice();
                }
            }
        }
        Log.e(TAG, "未下发充电资费");
        return 0;
    }

}


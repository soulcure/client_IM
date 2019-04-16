package com.youmai.db.bean;

import com.youmai.config.AppConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Entity
public class ChargerBean {

    @Id
    private Long id; //主键id

    private short reserved1; //1 预留
    private short reserved2; //2 预留
    private byte[] chargerCode = new byte[32];   //3充电桩编码
    private byte gunType; //4充电枪位置类型  1-直流2-交流
    private byte gunPort; //5充电枪口
    private byte[] cardCode = new byte[32];   //6充电卡号
    private long beginTime; //7充电开始时间
    private long stopTime; //8充电结束时间
    private int chargeTime;// 9充电时间长度 单位秒

    private byte startSOC; //10开始SOC
    private byte stopSOC; //11结束SOC

    private int overReason; //12充电结束原因
    private int power;    //13本次充电电量

    private int startReading; //14充电前电表读数
    private int stopReading;  //15充电后电表读数
    private int amount;  //16本次充电金额
    private int reserved17;  //17预留
    private int cardOverage;  //18充电前卡余额
    private int cardIndex;  //19当前充电记录索引
    private int record;  //20总充电记录条目
    private byte reserved21; //21 预留
    private byte strategy; //22 充电策略  0:充满为止 1:时间控制充电 2:金额控制充电 3:电量控制充电
    private int chargeParam;  //23 充电策略参数  时间单位为 1秒 金额单位为0.01元 电量时单位为0.01kw
    private byte[] vinCode = new byte[17];   //24 车辆VIN
    private long numberPlate; ////25 车牌号

    private short time1; //26 时段1充电电量
    private short time2; //27 时段2充电电量
    private short time3; //28 时段3充电电量
    private short time4; //29 时段4充电电量
    private short time5; //30 时段5充电电量
    private short time6; //31 时段6充电电量
    private short time7; //32 时段7充电电量
    private short time8; //33 时段8充电电量
    private short time9; //34 时段9充电电量
    private short time10; //35 时段10充电电量
    private short time11; //36 时段11充电电量
    private short time12; //37 时段12充电电量
    private short time13; //38 时段13充电电量
    private short time14; //39 时段14充电电量
    private short time15; //40 时段15充电电量
    private short time16; //41 时段16充电电量
    private short time17; //42 时段17充电电量
    private short time18; //43 时段18充电电量
    private short time19; //44 时段19充电电量
    private short time20; //45 时段20充电电量
    private short time21; //46 时段21充电电量
    private short time22; //47 时段22充电电量
    private short time23; //48 时段23充电电量
    private short time24; //49 时段24充电电量
    private short time25; //50 时段25充电电量
    private short time26; //51 时段26充电电量
    private short time27; //52 时段27充电电量
    private short time28; //53 时段28充电电量
    private short time29; //54 时段29充电电量
    private short time30; //55 时段30充电电量
    private short time31; //56 时段31充电电量
    private short time32; //57 时段32充电电量
    private short time33; //58 时段33充电电量
    private short time34; //59 时段34充电电量
    private short time35; //60 时段35充电电量
    private short time36; //61 时段36充电电量
    private short time37; //62 时段37充电电量
    private short time38; //63 时段38充电电量
    private short time39; //64 时段39充电电量
    private short time40; //65 时段40充电电量
    private short time41; //66 时段41充电电量
    private short time42; //67 时段42充电电量
    private short time43; //68 时段43充电电量
    private short time44; //69 时段44充电电量
    private short time45; //70 时段45充电电量
    private short time46; //71 时段46充电电量
    private short time47; //72 时段47充电电量
    private short time48; //73 时段48充电电量

    private byte startMethod;//74 启动方式 0：本地刷卡启动 1:后台启动 2：本地管理员启动
    private byte[] orderCode = new byte[32];   //75 充电流水号

    @Generated(hash = 1192467064)
    public ChargerBean() {
    }

    @Generated(hash = 703942469)
    public ChargerBean(Long id, short reserved1, short reserved2,
                       byte[] chargerCode, byte gunType, byte gunPort, byte[] cardCode,
                       long beginTime, long stopTime, int chargeTime, byte startSOC,
                       byte stopSOC, int overReason, int power, int startReading,
                       int stopReading, int amount, int reserved17, int cardOverage,
                       int cardIndex, int record, byte reserved21, byte strategy,
                       int chargeParam, byte[] vinCode, long numberPlate, short time1,
                       short time2, short time3, short time4, short time5, short time6,
                       short time7, short time8, short time9, short time10, short time11,
                       short time12, short time13, short time14, short time15, short time16,
                       short time17, short time18, short time19, short time20, short time21,
                       short time22, short time23, short time24, short time25, short time26,
                       short time27, short time28, short time29, short time30, short time31,
                       short time32, short time33, short time34, short time35, short time36,
                       short time37, short time38, short time39, short time40, short time41,
                       short time42, short time43, short time44, short time45, short time46,
                       short time47, short time48, byte startMethod, byte[] orderCode) {
        this.id = id;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.chargerCode = chargerCode;
        this.gunType = gunType;
        this.gunPort = gunPort;
        this.cardCode = cardCode;
        this.beginTime = beginTime;
        this.stopTime = stopTime;
        this.chargeTime = chargeTime;
        this.startSOC = startSOC;
        this.stopSOC = stopSOC;
        this.overReason = overReason;
        this.power = power;
        this.startReading = startReading;
        this.stopReading = stopReading;
        this.amount = amount;
        this.reserved17 = reserved17;
        this.cardOverage = cardOverage;
        this.cardIndex = cardIndex;
        this.record = record;
        this.reserved21 = reserved21;
        this.strategy = strategy;
        this.chargeParam = chargeParam;
        this.vinCode = vinCode;
        this.numberPlate = numberPlate;
        this.time1 = time1;
        this.time2 = time2;
        this.time3 = time3;
        this.time4 = time4;
        this.time5 = time5;
        this.time6 = time6;
        this.time7 = time7;
        this.time8 = time8;
        this.time9 = time9;
        this.time10 = time10;
        this.time11 = time11;
        this.time12 = time12;
        this.time13 = time13;
        this.time14 = time14;
        this.time15 = time15;
        this.time16 = time16;
        this.time17 = time17;
        this.time18 = time18;
        this.time19 = time19;
        this.time20 = time20;
        this.time21 = time21;
        this.time22 = time22;
        this.time23 = time23;
        this.time24 = time24;
        this.time25 = time25;
        this.time26 = time26;
        this.time27 = time27;
        this.time28 = time28;
        this.time29 = time29;
        this.time30 = time30;
        this.time31 = time31;
        this.time32 = time32;
        this.time33 = time33;
        this.time34 = time34;
        this.time35 = time35;
        this.time36 = time36;
        this.time37 = time37;
        this.time38 = time38;
        this.time39 = time39;
        this.time40 = time40;
        this.time41 = time41;
        this.time42 = time42;
        this.time43 = time43;
        this.time44 = time44;
        this.time45 = time45;
        this.time46 = time46;
        this.time47 = time47;
        this.time48 = time48;
        this.startMethod = startMethod;
        this.orderCode = orderCode;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getReserved1() {
        return this.reserved1;
    }

    public void setReserved1(short reserved1) {
        this.reserved1 = reserved1;
    }

    public short getReserved2() {
        return this.reserved2;
    }

    public void setReserved2(short reserved2) {
        this.reserved2 = reserved2;
    }

    public byte[] getChargerCode() {
        return this.chargerCode;
    }

    public void setChargerCode(byte[] chargerCode) {
        this.chargerCode = chargerCode;
    }

    public byte getGunType() {
        return this.gunType;
    }

    public void setGunType(byte gunType) {
        this.gunType = gunType;
    }

    public byte getGunPort() {
        return this.gunPort;
    }

    public void setGunPort(byte gunPort) {
        this.gunPort = gunPort;
    }

    public byte[] getCardCode() {
        return this.cardCode;
    }

    public void setCardCode(byte[] cardCode) {
        this.cardCode = cardCode;
    }

    public long getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getStopTime() {
        return this.stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public int getChargeTime() {
        return this.chargeTime;
    }

    public void setChargeTime(int chargeTime) {
        this.chargeTime = chargeTime;
    }

    public byte getStartSOC() {
        return this.startSOC;
    }

    public void setStartSOC(byte startSOC) {
        this.startSOC = startSOC;
    }

    public byte getStopSOC() {
        return this.stopSOC;
    }

    public void setStopSOC(byte stopSOC) {
        this.stopSOC = stopSOC;
    }

    public int getOverReason() {
        return this.overReason;
    }

    public void setOverReason(int overReason) {
        this.overReason = overReason;
    }

    public int getPower() {
        return this.power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getStartReading() {
        return this.startReading;
    }

    public void setStartReading(int startReading) {
        this.startReading = startReading;
    }

    public int getStopReading() {
        return this.stopReading;
    }

    public void setStopReading(int stopReading) {
        this.stopReading = stopReading;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getReserved17() {
        return this.reserved17;
    }

    public void setReserved17(int reserved17) {
        this.reserved17 = reserved17;
    }

    public int getCardOverage() {
        return this.cardOverage;
    }

    public void setCardOverage(int cardOverage) {
        this.cardOverage = cardOverage;
    }

    public int getCardIndex() {
        return this.cardIndex;
    }

    public void setCardIndex(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public int getRecord() {
        return this.record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public byte getReserved21() {
        return this.reserved21;
    }

    public void setReserved21(byte reserved21) {
        this.reserved21 = reserved21;
    }

    public byte getStrategy() {
        return this.strategy;
    }

    public void setStrategy(byte strategy) {
        this.strategy = strategy;
    }

    public int getChargeParam() {
        return this.chargeParam;
    }

    public void setChargeParam(int chargeParam) {
        this.chargeParam = chargeParam;
    }

    public byte[] getVinCode() {
        return this.vinCode;
    }

    public void setVinCode(byte[] vinCode) {
        this.vinCode = vinCode;
    }

    public long getNumberPlate() {
        return this.numberPlate;
    }

    public void setNumberPlate(long numberPlate) {
        this.numberPlate = numberPlate;
    }

    public short getTime1() {
        return this.time1;
    }

    public void setTime1(short time1) {
        this.time1 = time1;
    }

    public short getTime2() {
        return this.time2;
    }

    public void setTime2(short time2) {
        this.time2 = time2;
    }

    public short getTime3() {
        return this.time3;
    }

    public void setTime3(short time3) {
        this.time3 = time3;
    }

    public short getTime4() {
        return this.time4;
    }

    public void setTime4(short time4) {
        this.time4 = time4;
    }

    public short getTime5() {
        return this.time5;
    }

    public void setTime5(short time5) {
        this.time5 = time5;
    }

    public short getTime6() {
        return this.time6;
    }

    public void setTime6(short time6) {
        this.time6 = time6;
    }

    public short getTime7() {
        return this.time7;
    }

    public void setTime7(short time7) {
        this.time7 = time7;
    }

    public short getTime8() {
        return this.time8;
    }

    public void setTime8(short time8) {
        this.time8 = time8;
    }

    public short getTime9() {
        return this.time9;
    }

    public void setTime9(short time9) {
        this.time9 = time9;
    }

    public short getTime10() {
        return this.time10;
    }

    public void setTime10(short time10) {
        this.time10 = time10;
    }

    public short getTime11() {
        return this.time11;
    }

    public void setTime11(short time11) {
        this.time11 = time11;
    }

    public short getTime12() {
        return this.time12;
    }

    public void setTime12(short time12) {
        this.time12 = time12;
    }

    public short getTime13() {
        return this.time13;
    }

    public void setTime13(short time13) {
        this.time13 = time13;
    }

    public short getTime14() {
        return this.time14;
    }

    public void setTime14(short time14) {
        this.time14 = time14;
    }

    public short getTime15() {
        return this.time15;
    }

    public void setTime15(short time15) {
        this.time15 = time15;
    }

    public short getTime16() {
        return this.time16;
    }

    public void setTime16(short time16) {
        this.time16 = time16;
    }

    public short getTime17() {
        return this.time17;
    }

    public void setTime17(short time17) {
        this.time17 = time17;
    }

    public short getTime18() {
        return this.time18;
    }

    public void setTime18(short time18) {
        this.time18 = time18;
    }

    public short getTime19() {
        return this.time19;
    }

    public void setTime19(short time19) {
        this.time19 = time19;
    }

    public short getTime20() {
        return this.time20;
    }

    public void setTime20(short time20) {
        this.time20 = time20;
    }

    public short getTime21() {
        return this.time21;
    }

    public void setTime21(short time21) {
        this.time21 = time21;
    }

    public short getTime22() {
        return this.time22;
    }

    public void setTime22(short time22) {
        this.time22 = time22;
    }

    public short getTime23() {
        return this.time23;
    }

    public void setTime23(short time23) {
        this.time23 = time23;
    }

    public short getTime24() {
        return this.time24;
    }

    public void setTime24(short time24) {
        this.time24 = time24;
    }

    public short getTime25() {
        return this.time25;
    }

    public void setTime25(short time25) {
        this.time25 = time25;
    }

    public short getTime26() {
        return this.time26;
    }

    public void setTime26(short time26) {
        this.time26 = time26;
    }

    public short getTime27() {
        return this.time27;
    }

    public void setTime27(short time27) {
        this.time27 = time27;
    }

    public short getTime28() {
        return this.time28;
    }

    public void setTime28(short time28) {
        this.time28 = time28;
    }

    public short getTime29() {
        return this.time29;
    }

    public void setTime29(short time29) {
        this.time29 = time29;
    }

    public short getTime30() {
        return this.time30;
    }

    public void setTime30(short time30) {
        this.time30 = time30;
    }

    public short getTime31() {
        return this.time31;
    }

    public void setTime31(short time31) {
        this.time31 = time31;
    }

    public short getTime32() {
        return this.time32;
    }

    public void setTime32(short time32) {
        this.time32 = time32;
    }

    public short getTime33() {
        return this.time33;
    }

    public void setTime33(short time33) {
        this.time33 = time33;
    }

    public short getTime34() {
        return this.time34;
    }

    public void setTime34(short time34) {
        this.time34 = time34;
    }

    public short getTime35() {
        return this.time35;
    }

    public void setTime35(short time35) {
        this.time35 = time35;
    }

    public short getTime36() {
        return this.time36;
    }

    public void setTime36(short time36) {
        this.time36 = time36;
    }

    public short getTime37() {
        return this.time37;
    }

    public void setTime37(short time37) {
        this.time37 = time37;
    }

    public short getTime38() {
        return this.time38;
    }

    public void setTime38(short time38) {
        this.time38 = time38;
    }

    public short getTime39() {
        return this.time39;
    }

    public void setTime39(short time39) {
        this.time39 = time39;
    }

    public short getTime40() {
        return this.time40;
    }

    public void setTime40(short time40) {
        this.time40 = time40;
    }

    public short getTime41() {
        return this.time41;
    }

    public void setTime41(short time41) {
        this.time41 = time41;
    }

    public short getTime42() {
        return this.time42;
    }

    public void setTime42(short time42) {
        this.time42 = time42;
    }

    public short getTime43() {
        return this.time43;
    }

    public void setTime43(short time43) {
        this.time43 = time43;
    }

    public short getTime44() {
        return this.time44;
    }

    public void setTime44(short time44) {
        this.time44 = time44;
    }

    public short getTime45() {
        return this.time45;
    }

    public void setTime45(short time45) {
        this.time45 = time45;
    }

    public short getTime46() {
        return this.time46;
    }

    public void setTime46(short time46) {
        this.time46 = time46;
    }

    public short getTime47() {
        return this.time47;
    }

    public void setTime47(short time47) {
        this.time47 = time47;
    }

    public short getTime48() {
        return this.time48;
    }

    public void setTime48(short time48) {
        this.time48 = time48;
    }

    public byte getStartMethod() {
        return this.startMethod;
    }

    public void setStartMethod(byte startMethod) {
        this.startMethod = startMethod;
    }

    public byte[] getOrderCode() {
        return this.orderCode;
    }

    public void setOrderCode(byte[] orderCode) {
        this.orderCode = orderCode;
    }


    public ByteBuffer toBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(AppConfig.SEND_BUFFER_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(reserved1);  // 1预留
        buffer.putShort(reserved2);  //2预留
        buffer.put(chargerCode);  //3充电桩编码

        buffer.put(gunType);  //4充电枪位置类型  1-直流2-交流
        buffer.put(gunPort);  //5充电枪口
        buffer.put(cardCode);  //6充电卡号
        buffer.putLong(beginTime);//7充电开始时间
        buffer.putLong(stopTime);//8充电结束时间


        buffer.putInt(chargeTime);//9充电时间长度 单位秒

        buffer.put(startSOC);  //10开始SOC
        buffer.put(stopSOC);  //11结束SOC

        buffer.putInt(overReason);  //12充电结束原因
        buffer.putInt(power);  //13本次充电电量
        buffer.putInt(startReading);  //14充电前电表读数
        buffer.putInt(stopReading);  //15充电后电表读数
        buffer.putInt(amount);  //16本次充电金额
        buffer.putInt(reserved17);  //17预留
        buffer.putInt(cardOverage);  //18充电前卡余额
        buffer.putInt(cardIndex);  //19当前充电记录索引
        buffer.putInt(record);  //20总充电记录条目

        buffer.put(reserved21);  //21 预留
        buffer.put(strategy);  //22 充电策略  0:充满为止 1:时间控制充电 2:金额控制充电 3:电量控制充电
        buffer.putInt(chargeParam);  //23 充电策略参数  时间单位为 1秒 金额单位为0.01元 电量时单位为0.01kw

        buffer.put(vinCode);  //24 车辆VIN
        buffer.putLong(numberPlate);  //25 车牌号

        buffer.putShort(time1); //26 时段1充电电量
        buffer.putShort(time2); //27 时段1充电电量
        buffer.putShort(time3); //28 时段1充电电量
        buffer.putShort(time4); //29 时段1充电电量
        buffer.putShort(time5); //30 时段1充电电量
        buffer.putShort(time6); //31 时段1充电电量
        buffer.putShort(time7); //32 时段1充电电量
        buffer.putShort(time8); //33 时段1充电电量
        buffer.putShort(time9); //34 时段1充电电量
        buffer.putShort(time10); //35 时段1充电电量
        buffer.putShort(time11); //36 时段1充电电量
        buffer.putShort(time12); //37 时段1充电电量
        buffer.putShort(time13); //38 时段1充电电量
        buffer.putShort(time14); //39 时段1充电电量
        buffer.putShort(time15); //40 时段1充电电量
        buffer.putShort(time16); //41 时段1充电电量
        buffer.putShort(time17); //42 时段1充电电量
        buffer.putShort(time18); //43 时段1充电电量
        buffer.putShort(time19); //44 时段1充电电量
        buffer.putShort(time20); //45 时段1充电电量
        buffer.putShort(time21); //46 时段1充电电量
        buffer.putShort(time22); //47 时段1充电电量
        buffer.putShort(time23); //48 时段1充电电量
        buffer.putShort(time24); //49 时段1充电电量
        buffer.putShort(time25); //50 时段1充电电量
        buffer.putShort(time26); //51 时段1充电电量
        buffer.putShort(time27); //52 时段1充电电量
        buffer.putShort(time28); //53 时段1充电电量
        buffer.putShort(time29); //54 时段1充电电量
        buffer.putShort(time30); //55 时段1充电电量
        buffer.putShort(time31); //56 时段1充电电量
        buffer.putShort(time32); //57 时段1充电电量
        buffer.putShort(time33); //58 时段1充电电量
        buffer.putShort(time34); //59 时段1充电电量
        buffer.putShort(time35); //60 时段1充电电量
        buffer.putShort(time36); //61 时段1充电电量
        buffer.putShort(time37); //62 时段1充电电量
        buffer.putShort(time38); //63 时段1充电电量
        buffer.putShort(time39); //64 时段1充电电量
        buffer.putShort(time40); //65 时段1充电电量
        buffer.putShort(time41); //66 时段1充电电量
        buffer.putShort(time42); //67 时段1充电电量
        buffer.putShort(time43); //68 时段1充电电量
        buffer.putShort(time44); //69 时段1充电电量
        buffer.putShort(time45); //70 时段1充电电量
        buffer.putShort(time46); //71 时段1充电电量
        buffer.putShort(time47); //72 时段1充电电量
        buffer.putShort(time48); //73 时段1充电电量
        buffer.put(startMethod); //74 启动方式 0：本地刷卡启动 1:后台启动 2：本地管理员启动
        buffer.put(orderCode); //75 充电流水号

        return buffer;
    }
}

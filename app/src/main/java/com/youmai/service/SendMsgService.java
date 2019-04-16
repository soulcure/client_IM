package com.youmai.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;


/**
 * 发送消息的服务
 * Created by fylder on 2017/11/9.
 */

public class SendMsgService extends IntentService {

    private static final String TAG = SendMsgService.class.getName();

    private static Context appContext;

    public static final String KEY_ID = "id";
    public static final String KEY_DATA = "data";
    public static final String KEY_DATA_FROM = "data_from";
    public static final String FROM_IM = "IM";

    public static final String NOT_NETWORK = "NOT_NETWORK";
    public static final String NOT_TCP_CONNECT = "NOT_TCP_CONNECT";//tcp还没连接成功

    public static final String ACTION_SEND_MSG = "service.send.msg";
    public static final String ACTION_UPDATE_MSG = "service.update.msg";
    public static final String ACTION_NEW_MSG = "action_new_msg";

    private long id;
    private boolean isGroup;
    private int groupType;
    private String groupName;

    private String imgWidth;
    private String imgHeight;

    public SendMsgService() {
        super("SendMsgService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String msgDataFrom = intent.getStringExtra(KEY_DATA_FROM);//消息从哪里发起

    }


}

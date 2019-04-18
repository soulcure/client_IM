package com.youmai.charger;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.support.v7.widget.RecyclerView;

import com.youmai.HuxinSdkManager;
import com.youmai.socket.ReceiveListener;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TcpClient";

    private static final int CHARGER_INFO = 104;
    private static final int CHARGER_COMPLIED = 202;


    private RecyclerView recycler_view;
    private EditText et_send;
    private ChatRecyclerAdapter mAdapter;

    private NormalHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new NormalHandler(this);

        initView();

        setNotifyListener();
    }

    private void initView() {
        et_send = findViewById(R.id.et_send);

        findViewById(R.id.btn_send).setOnClickListener(this);

        recycler_view = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);

        mAdapter = new ChatRecyclerAdapter(this);
        recycler_view.setAdapter(mAdapter);
    }

    public void setItem(String item) {
        mAdapter.setItem(item);
    }


    private void setNotifyListener() {

    }


    /**
     * 发送登录IM服务器请求
     */
    private void sendText(String msg) {
        HuxinSdkManager.instance().sendText(msg, new ReceiveListener() {
            @Override
            public void OnRec(ByteBuffer buffer) {
                loginSuccess(buffer);
            }
        });
    }


    private void loginSuccess(ByteBuffer buffer) {
        HuxinSdkManager.instance().setLogin(true);
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        String msg = new String(bytes, Charset.forName("UTF-8"));
        setItem(msg);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_send:
                String msg = et_send.getText().toString();
                if (!TextUtils.isEmpty(msg)) {
                    sendText(msg);
                }
                break;
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
                    break;
                case CHARGER_COMPLIED:
                    break;
                default:
                    break;
            }
        }
    }
}

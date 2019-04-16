package com.youmai.socket;

import android.util.Log;

import java.nio.ByteBuffer;

public abstract class ReceiveListener {

    public static final String TAG = "TcpClient";

    public static final int SOCKET_TIMEOUT = -1;

    private Runnable runnable;


    public ReceiveListener() {

        runnable = new Runnable() {
            @Override
            public void run() {
                onError(ReceiveListener.SOCKET_TIMEOUT);
            }
        };
    }


    public abstract void OnRec(ByteBuffer buffer);


    public void onError(int errCode) {
        if (errCode == ReceiveListener.SOCKET_TIMEOUT) {
            Log.e(TAG, "tcp rec package out of time default is 5 seconds");
        }

    }


    public Runnable getRunnable() {
        return runnable;
    }


}

package com.youmai.socket;

import android.util.Log;


import com.youmai.config.AppConfig;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public abstract class PduUtil {

    private static final String TAG = "TcpClient";

    public abstract void OnRec(PduBase pduBase);

    public abstract void OnCallback(short key, ByteBuffer buffer);

    public int ParsePdu(ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        if (buffer.limit() > PduBase.PDU_BASIC_LENGTH) {
            short begin = buffer.getShort(0);
            Log.v(TAG, "begin is " + begin);
            if (begin != PduBase.starFlag) {
                Log.v(TAG, "包头标识错误，丢弃数据，重新读socket数据 ，重新写缓冲区");
                buffer.clear();  //重新写缓冲区，clear()方法会清空整个缓冲区
                return 0;
            }
        } else {    //did not contain a start flag yet.continue read.
            Log.v(TAG, "没有达到包头标记长度，继续读socket数据");
            buffer.position(buffer.limit());
            buffer.limit(buffer.capacity());
            return -1;
        }

        if (buffer.limit() >= PduBase.PDU_HEADER_LENGTH) {
            //has full header
            int totalLength = PduBase.PDU_HEADER_LENGTH + buffer.getInt(PduBase.PDU_BODY_LENGTH_INDEX);
            if (totalLength <= buffer.limit()) {
                //has a full pack.
                byte[] packByte = new byte[totalLength];
                buffer.get(packByte);
                PduBase pduBase = buildPdu(packByte);
                buffer.compact();//compact()方法只会清除已经读过的数据
                //read to read.
                buffer.flip();  //准备从缓冲区中读取数据

                if (pduBase != null) {
                    OnRec(pduBase);
                }

                return totalLength;
            } else {
                Log.v(TAG, "没有达到完整包长度，继续读socket数据");
                buffer.position(buffer.limit());
                buffer.limit(buffer.capacity());
                return -1;
            }

        } else {
            Log.v(TAG, "没有达到完整包长度，继续读socket数据");
            buffer.position(buffer.limit());
            buffer.limit(buffer.capacity());
            return -1;
        }
    }


    private PduBase buildPdu(byte[] bytes) {
        PduBase units = new PduBase();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(bytes);//准备从缓冲区中读取数据
        buffer.flip();

        Log.d(TAG, "tcp rec package commandId:" + buffer.getShort(2));
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        Log.d(TAG, "tcp rec buffer:" + bytes2HexString(data));
        buffer.flip();

        if (buffer.getShort() == PduBase.starFlag) {
            short commandId = buffer.getShort();
            int length = buffer.getInt();

            units.length = length;
            units.commandId = commandId;

            Log.d(TAG, "tcp rec package params Length:" + length);

            if (length > 0) {
                units.params = new byte[length];
                buffer.get(units.params);
            }

            return units;

        } else {
            Log.e(TAG, "包头数据校验错误");
            return null;
        }

    }


    public ByteBuffer serializePdu(PduBase req) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(AppConfig.SEND_BUFFER_SIZE);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        byteBuffer.putShort(PduBase.starFlag);
        byteBuffer.putShort(req.commandId);
        byteBuffer.putInt(req.length);
        if (req.params != null) {
            byteBuffer.put(req.params);
        }
        return byteBuffer;

    }


    public static byte checksum(byte[] buffer) {
        int sum = 0;
        if (buffer == null) {
            return 0;
        }
        for (byte item : buffer) {
            sum = sum + item;
        }
        return (byte) sum;
    }

    public static String bytes2HexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        int length = b.length;
        for (int i = 0; i < length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append("0x").append(hex.toUpperCase());
            if (i < length - 1) {
                sb.append(',');
            }
        }
        return sb.toString();
    }

    public static String bytes2HexString1(byte[] b) {
        StringBuilder sb = new StringBuilder();
        int length = b.length;
        for (int i = 0; i < length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            /*sb.append("0x").append(hex.toUpperCase());
            if (i < length - 1) {
                sb.append(',');
            }*/
            sb.append(hex.toUpperCase());
            if (i < length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }


    public static String byte2HexString(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return "0x" + hex.toUpperCase();
    }

}

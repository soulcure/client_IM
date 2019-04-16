package com.youmai.socket;

/**
 * 帧头	     命令码	    包体长度	  包体参数
 * 2Bytes	4Byte	    4Byte	      NByte
 */
public class PduBase {

    /****************************************************
     * basic unit of data type length
     */
    public static final int PDU_BASIC_LENGTH = 2; //包头标识长度
    public static final int PDU_BODY_LENGTH_INDEX = 4; //标识包体数据长度的位置索引
    public static final int PDU_HEADER_LENGTH = 8;  //包头长度 （包头固定数据域长度总和，不包括动态数据域）

    /****************************************************
     * index 0. pos:[0-2) 帧头
     * the start flag of a pdu.
     */
    public static final short starFlag = (short) 0xFFAA;  //使用小端数据 0xAAFF 反转


    /****************************************************
     * index 4. pos:[6-8) 命令码
     */
    public short commandId;

    /****************************************************
     * index 1. pos:[2-4) 帧长度
     * 帧长度：起始域到校验和域整个报文长度
     */
    public int length;


    /****************************************************
     * index 5. pos:[8-8+n) 参数
     */
    public byte[] params;

}



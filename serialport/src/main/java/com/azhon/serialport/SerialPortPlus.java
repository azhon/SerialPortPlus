package com.azhon.serialport;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 项目名:    SPApp
 * 包名       com.azhon.serialport
 * 文件名:    SerialPortPlus
 * 创建时间:  2020/7/30 on 10:47
 * 描述:     TODO
 *
 * @author 阿钟
 */

public class SerialPortPlus extends SerialPort {

    private static final String TAG = "SerialPortPlus";
    private ReceiveDataListener receiveDataListener;
    private DataThread dataThread;

    public SerialPortPlus(String device, int baudRate) throws Exception {
        this(device, baudRate, 0);
    }

    /**
     * @param device   串口设备路径
     * @param baudRate 波特率，一般是9600
     * @param flags    传0就好
     */
    public SerialPortPlus(String device, int baudRate, int flags) throws Exception {
        super(new File(device), baudRate, flags);
        startReceiveData();
    }

    /**
     * @param device   串口设备路径
     * @param baudRate 波特率，一般是9600
     * @param flags    传0就好
     * @param parity   奇偶校验，0 None, 1 Odd, 2 Even
     * @param dataBits 数据位，5 ~ 8
     * @param stopBit  停止位，1 或 2
     */
    public SerialPortPlus(String device, int baudRate, int flags, int parity, int dataBits, int stopBit) throws Exception {
        super(new File(device), baudRate, flags, parity, dataBits, stopBit);
        startReceiveData();
    }

    /**
     * 发送数据
     */
    public void writeAndFlush(byte[] data) {
        try {
            OutputStream outputStream = getOutputStream();
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始接收串口数据
     */
    private void startReceiveData() {
        if (dataThread != null) {
            stopReceiveData();
        }
        dataThread = new DataThread();
        dataThread.setName("serial-port-read-thread");
        dataThread.start();
        Log.d(TAG, "start receive data thread ...");
    }

    /**
     * 停止接收串口数据
     */
    private void stopReceiveData() {
        if (dataThread != null) {
            dataThread.setReceiveData(false);
            dataThread.interrupt();
            dataThread = null;
            Log.d(TAG, "stop receive data thread ...");
        }
    }


    private class DataThread extends Thread {

        private boolean receiveData = true;

        @Override
        public void run() {
            try {
                InputStream is = getInputStream();
                while (receiveData) {
                    int count = is.available();
                    while (count == 0) {
                        Thread.sleep(500);
                        count = is.available();
                    }
                    byte[] data = new byte[count];
                    is.read(data);
                    ByteBuf byteBuf = Unpooled.wrappedBuffer(data);
                    if (receiveDataListener != null) {
                        receiveDataListener.receiveData(byteBuf);
                    }
                }
            } catch (InterruptedException e) {
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "receive data error :" + e.getMessage());
            }
        }

        public void setReceiveData(boolean receiveData) {
            this.receiveData = receiveData;
        }
    }


    public void setReceiveDataListener(ReceiveDataListener receiveDataListener) {
        this.receiveDataListener = receiveDataListener;
    }

    /**
     * 关闭串口
     */
    @Override
    public void close() {
        stopReceiveData();
        super.close();
    }
}

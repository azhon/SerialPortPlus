package com.azhon.serialport;

import io.netty.buffer.ByteBuf;

/**
 * 项目名:    SPApp
 * 包名       com.azhon.serialport
 * 文件名:    ReceiveDataListener
 * 创建时间:  2020/7/30 on 11:23
 * 描述:     TODO
 *
 * @author 阿钟
 */

public interface ReceiveDataListener {

    void receiveData(ByteBuf byteBuf);
}

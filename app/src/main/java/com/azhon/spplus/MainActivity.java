package com.azhon.spplus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.azhon.serialport.ReceiveDataListener;
import com.azhon.serialport.SerialPortPlus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/*
 * ┌───┐   ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐      ┌───┬───┬───┐
 * │Esc   │   │  F1  │  F2  │  F3  │  F4  │ │  F5  │  F6  │  F7  │  F8  │ │  F9  │  F10 │  F11 │  F12 │      │  P/S │  S L │  P/B │        ┌┐    ┌┐    ┌┐
 * └───┘   └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘      └───┴───┴───┘        └┘    └┘    └┘
 * ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐ ┌───┬───┬───┐ ┌───┬───┬───┬───┐
 * │~ `   │ ! 1  │ @ 2  │ # 3  │ $ 4  │ % 5  │ ^ 6  │ & 7  │ * 8  │ ( 9  │ ) 0  │ _ -  │ + =  │     BacSp    │ │  Ins │  Hom │  PUp │ │ N L  │   /  │  *   │  -   │
 * ├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤ ├───┼───┼───┤ ├───┼───┼───┼───┤
 * │ Tab      │   Q  │   W  │   E  │   R  │   T  │   Y  │   U  │   I  │   O  │   P  │ { [  │ } ]  │    | \   │ │  Del │  End │  PDn │ │   7  │   8  │   9  │      │
 * ├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤ └───┴───┴───┘ ├───┼───┼───┤   +  │
 * │ Caps       │   A  │   S  │   D  │   F  │   G  │   H  │   J  │   K  │   L  │  : ; │  " ' │      Enter     │                            │   4  │   5  │   6  │      │
 * ├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤         ┌───┐         ├───┼───┼───┼───┤
 * │ Shift          │   Z  │   X  │   C  │   V  │   B  │   N  │   M  │  < , │  > . │  ? / │        Shift       │         │  ↑  │         │   1  │   2  │   3  │      │
 * ├─────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤ ┌───┼───┼───┐ ├───┴───┼───┤   E││
 * │ Ctrl     │        │  Alt   │                      Space                   │   Alt  │        │        │  Ctrl  │ │  ←  │  ↓  │  →  │ │       0      │   .  │←─┘│
 * └─────┴────┴────┴───────────────────────┴────┴────┴────┴────┘ └───┴───┴───┘ └───────┴───┴───┘
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ReceiveDataListener {

    private static final String TAG = "MainActivity";

    private SerialPortPlus serialPortPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }


    private void initView() {
        findViewById(R.id.btn_open).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                open();
                break;
            case R.id.btn_close:
                close();
                break;
            case R.id.btn_send:
                sendData();
                break;
            default:
                break;
        }
    }

    /**
     * 打开串口
     */
    private void open() {
        try {
            serialPortPlus = new SerialPortPlus("/dev/ttyMT2", 9600);
            serialPortPlus.setReceiveDataListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收串口数据
     */
    @Override
    public void receiveData(ByteBuf byteBuf) {
        String hex = ByteBufUtil.hexDump(byteBuf).toUpperCase();
        Log.d(TAG, "收到的数据十六进制：" + hex);
        //解析数据
        int length = byteBuf.readableBytes();
        for (int i = 0; i < length; i++) {
            byte b = byteBuf.readByte();
            if (b == 0x3E) {

            }
        }
    }

    /**
     * 发送数据
     */
    private void sendData() {
        if (serialPortPlus == null) {
            return;
        }
        serialPortPlus.writeAndFlush(new byte[]{0x5E});
    }

    /**
     * 关闭串口
     */
    private void close() {
        if (serialPortPlus != null) {
            serialPortPlus.close();
            serialPortPlus = null;
        }
    }
}
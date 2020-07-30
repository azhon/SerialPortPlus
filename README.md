# SerialPortPlus
Android串口开发，基于谷歌官方的[android-serialport-api](https://github.com/cepr/android-serialport-api)增加支持设置奇偶校验、数据位和停止位

## 使用步骤

### 第一步：下载本项目，在你的项目中点击File —> New -> Import Module...然后选择serialport；然后在你的项目中依赖basicLib

### 第二步：打开串口

- 1.打开串口
 
```java
try {
    SerialPortPlus serialPortPlus = new SerialPortPlus("/dev/ttyMT2", 9600);
    //如果你需要设置奇偶校验、数据位和停止位
    //SerialPortPlus serialPortPlus = new SerialPortPlus("/dev/ttyMT2", 9600, 0, 1, 8, 1);
} catch (Exception e) {
    e.printStackTrace();
}
```

- 2.设置串口数据回调

```java
serialPortPlus.setReceiveDataListener(this);
```

### 第三步：发送数据

```java
serialPortPlus.writeAndFlush(data);
```

### 第四步：处理数据

```java
@Override
public void receiveData(ByteBuf byteBuf) {
    //将数据转成十六进制
    String hex = ByteBufUtil.hexDump(byteBuf).toUpperCase();
    //解析数据
    byte[] data = byteBuf.array();
}
```
#### 这里引入了Netty中的`ByteBuf`来处理串口数据，如果不懂的小伙伴可以先学习下`ByteBuf`；使用`ByteBuf`处理字节数据超乎你想象的舒服

### 第五步：关闭串口

```java
serialPortPlus.close();
```
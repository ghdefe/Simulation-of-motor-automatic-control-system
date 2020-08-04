package cn.zengChunMiao.clientPackage;

import cn.zengChunMiao.module.motorModule;

import java.io.*;
import java.net.Socket;
import java.util.Random;

/**
 * Description:
 * 客户端类，该类实现Runnable接口，一般新开一个线程来运行该类。实例化该类后，首先会新建一个客户端窗口对象，然后不断尝试构造一个socket连接。待获得连接后，每隔一个拍数会发送状态给服务器，然后从服务器更新控制信息，拿到控制信息后，得到新一拍的参数，然后更新显示窗口信息。
 * @author zeng
 * @date 15:11 2020/6/20
 */
public class client implements Runnable {
    final private static String host = "127.0.0.1";
    final private static int port = 55555;
    private static int objNum = 0;  //记录当前是对象几
    private clientWindow clientWindow;
    private boolean runFlag = true;
    public client() {
        objNum++;
        //实例化显示窗口
        this.clientWindow = new clientWindow(objNum,this);
    }

    @Override
    public void run() {
        Socket socket = connect(host, port);
        clientDo(socket);
    }

    private Socket connect(String host, int port) {
        try {
            clientWindow.setTextProgramer("正在连接服务器\n");
            return new Socket(host, port);
        } catch (IOException e) {
            return reConnect(host, port);
        }
    }

    private Socket reConnect(String host, int port) {

        Socket socket = null;
        while (socket == null) {
            try {
                clientWindow.setTextProgramer("服务器连接失败，1s后尝试重新连接\n");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            socket = connect(host, port);
        }
        return socket;
    }

        /**
            * 更改client线程状态
            * @author zeng
            * @date 1:34 2020/6/21
            * @param runFlag 关闭该线程时，使runFlag为false
            */
    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }

    /**
     * 该方法是对象模拟程序的核心，完成模拟对象状态发送和接收控制信息，而后根据控制信息得到新一节拍的模拟对象状态
     * @param socket 调用该方法前，需要先建立一个稳定的连接传进来使用
     */
    public void clientDo(Socket socket) {
        System.out.println("连接服务器成功");
        //首先构建一个对象
        motorModule mM = new motorModule();

        //然后发送对象
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (runFlag) {

                long time = System.currentTimeMillis();
                //1.发送对象当前拍数状态信息
                //加干扰，发送3次信号
                mM.setGUNow(mM.getGUNow() + new Random().nextFloat() - 0.5f);
                objectOutputStream.writeObject(mM);
                mM.setGUNow(mM.getGUNow() + new Random().nextFloat() - 0.5f);
                objectOutputStream.writeObject(mM);
                mM.setGUNow(mM.getGUNow() + new Random().nextFloat() - 0.5f);
                objectOutputStream.writeObject(mM);
                objectOutputStream.flush();

                //5.接收控制器传回来的控制信息
                //滤波，整理好数据再放回到mM
                float DU = 0;
                mM = (motorModule) objectInputStream.readObject();
                DU += mM.getDUNow();
                mM = (motorModule) objectInputStream.readObject();
                DU += mM.getDUNow();
                mM = (motorModule) objectInputStream.readObject();
                DU += mM.getDUNow();
                DU /= 3;
                mM.setDUNow(DU);

                //6.对象模拟，实际上是根据上一拍控制器信息得到上一拍的对象输出
                //    Ge(i-1) = Du(i);
                //    Gu(i) = 38.457*Ge(i-1) + 0.3177*Ge(i-2) + 0.2555*Gu(i-1);
                mM.setGerrLast(mM.getDUNow());
                mM.setGUNow((float) ( 38.457*mM.getGerrLast() + 0.3177*mM.getGerrLastLast() + 0.2555*mM.getGULast()));


//                //test
//                System.out.println("第" + mM.getNow() + "拍");
//                System.out.println("Ge：" + mM.getGerrNow());
//                System.out.println("GU：" + mM.getGUNow());
//                System.out.println("De：" + mM.getDerrNow());
//                System.out.println("DU：" + mM.getDUNow());

                //7.更新显示数据，并检查客户端转速输入是否发生改变
                mM.setR(clientWindow.update(mM.getDerrNow(), mM.getDUNow(), mM.getGerrNow(), mM.getGUNow()));    //调用run方法后，如果客户端输入改变mM的R也会发生改变

                mM.setNowAdd();
                if ((System.currentTimeMillis() - time) > 100)
                    System.out.println("系统暂停后重新开始");
                else
                    Thread.sleep(100 - (System.currentTimeMillis() - time));    //保持拍数稳定为100ms
            }
        } catch (ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("服务器断开连接");
            System.exit(-1);
        }

    }

}


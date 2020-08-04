package cn.zengChunMiao.serverPakage;

import cn.zengChunMiao.module.motorModule;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * 服务器类，同样实现了Runnable接口，可以新开一个线程来执行。
 *
 * @author zeng
 * @date 15:11 2020/6/20
 */
public class server implements Runnable {
    final private static int port = 55555;
    private int objNum = 1;
//    public static void main(String[] args) {
//        try {
//            ServerSocket serverSocket = new ServerSocket(port);
//            System.out.println("服务器开始运行");
//            while (true) {
//                Socket socket = serverSocket.accept();
//                new Thread(new cn.zengChunMiao.server.serverDo(socket,1)).start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 执行run方法时，会阻塞在监听端口accept()，每当收到一个新连接将会新开一个线程去处理
     * @author zeng
     * @date 15:28 2020/6/20
     */
    @Override
    public void run() {
        try {
            serverWindow serverWindow = new serverWindow();
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器开始运行");
            while (true) {
                Socket socket = serverSocket.accept(); //监听端口，如果没有连接则堵塞
                new Thread(new serverDo(socket, objNum, serverWindow)).start();   //收到一个新连接后就开启一个新线程去处理
                objNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 该类是控制程序的核心，完成接收模拟对象状态，然后根据状态得到控制信号，回传控制信号
 * @Date 15:29 2020/6/20
 */
class serverDo implements Runnable {
    private Socket socket;
    private int objNum; //记录当前线程是处理哪个对象的事务
    private serverWindow serverWindow;

    public serverDo(Socket socket, int objNum, serverWindow serverWindow) {
        this.socket = socket;
        this.objNum = objNum;
        this.serverWindow = serverWindow;
    }

    /**
    * @author zeng
    * @Description 该方法会死循环，每接收到一拍的状态，会进行滤波处理，然后通过控制器得到控制信息，回传给客户端，然后更新服务器窗口
    * @Date 15:30 2020/6/20
    */
    @Override
    public void run() {
        {
            //给定控制信号
            motorModule mM = new motorModule();
            try {
                //首先要接收对象
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                while (true) {
                        float GU = 0;
                        //2.接收当前拍对象状态信息
                        //滤波，整理好数据再放回到mM
                        mM = (motorModule) objectInputStream.readObject();
                        GU += mM.getGUNow();
                        mM = (motorModule) objectInputStream.readObject();
                        GU += mM.getGUNow();
                        mM = (motorModule) objectInputStream.readObject();
                        GU += mM.getGUNow();
                        GU /= 3;
                        mM.setGUNow(GU);

                        //3.根据当前拍对象状态信息，得到当前拍对象控制信息
                        //    De(i) = R(i-1) - Gu(i-1);
                        //    Du(i) = 0.9917*Du(i-1) + 0.0083*Du(i-2) + 0.026*De(i) - 0.0066*De(i-1);

                        mM.setDerrNow(mM.getRLast() - mM.getGULast());
                        mM.setDUNow((float) (0.9917 * mM.getDULast() + 0.0083 * mM.getDULastLast() + 0.026 * mM.getDerrNow() - 0.0066 * mM.getDerrLast()));


                        //4.回传控制信息，DU同样加上干扰
                        //干扰
                        mM.setGUNow(mM.getDUNow() + new Random().nextFloat() - 0.5f);
                        objectOutputStream.writeObject(mM);
                        mM.setGUNow(mM.getDUNow() + new Random().nextFloat() - 0.5f);
                        objectOutputStream.writeObject(mM);
                        mM.setGUNow(mM.getDUNow() + new Random().nextFloat() - 0.5f);
                        objectOutputStream.writeObject(mM);
                        objectOutputStream.flush();

                        //5.更新服务器窗口数据
                        serverWindow.update(objNum);
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}



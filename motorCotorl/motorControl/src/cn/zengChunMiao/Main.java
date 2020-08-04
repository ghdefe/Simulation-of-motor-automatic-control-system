package cn.zengChunMiao;

import cn.zengChunMiao.clientPackage.client;
import cn.zengChunMiao.serverPakage.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SpringLayout.WEST;

/**
 * 入口程序，也可成为主程序了。main函数将会显示一个窗口，用于开启服务器和客户端
 *
 * @author zeng
 * @date 15:41 2020/6/20
 */
public class Main extends JFrame {
    public Main() throws HeadlessException {
        setTitle("入口");

        clientBtnListener clientBtnListener = new clientBtnListener();
        JButton btnClient = new JButton("启动一个对象模拟程序");
        btnClient.addActionListener(clientBtnListener);

        serverBtnListener serverBtnListener = new serverBtnListener(this);
        JButton btnServer = new JButton("启动控制服务器");
        btnServer.addActionListener(serverBtnListener);
        setLayout(new BorderLayout());
        add(btnServer, WEST);
        add(btnClient);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 200);
        setVisible(true);
    }


    public static void main(String[] args) {
        new Main();
    }

}

/**
 * 客户端启动按钮事件
 */
class clientBtnListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new client()).start();
    }
}

/**
 * 服务器启动按钮事件
 */
class serverBtnListener implements ActionListener {
    private boolean serverState = false;
    private JFrame jf;

    public serverBtnListener(JFrame jf) {
        this.jf = jf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!serverState) {
            new Thread(new server()).start();
            serverState = true;
        } else {
            JDialog jDialog = new JDialog(jf, "服务器已经启动！", true);
            jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            jDialog.add(new JTextArea("服务器已启动"));
            jDialog.setSize(300, 200);
            jDialog.setLocationRelativeTo(null);
            jDialog.setVisible(true);
        }
    }
}
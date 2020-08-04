package cn.zengChunMiao.serverPakage;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;


/**
 * 服务器窗口
 *
 * @author zeng
 * @date 15:39 2020/6/20
 */
public class serverWindow extends JFrame {
    private JTextArea jTextArea;

    public serverWindow() throws HeadlessException {
        setTitle("控制服务器");
        setBounds(600, 200, 300, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        //显示当前控制器状态
        //文本区 -》 状态显示区
        JPanel stateShow = new JPanel();
        contentPane.add(stateShow);
        contentPane.setPreferredSize(new Dimension(0, 100));
        jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);
        contentPane.add(jTextArea);
        setVisible(true);
    }

    public static void main(String[] args) {
        serverWindow serverWindow = new serverWindow();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
            if (serverWindow.jTextArea.getLineCount() > 4) {
                serverWindow.jTextArea.setText("");
            }
            serverWindow.jTextArea.append(str + "\r\n");
        }
    }

    /**
     * 用于更新服务器窗口状态信息
     * @param objNum 当前是哪个对象发来的消息
     */
    public void update(int objNum) {
        if (jTextArea.getLineCount() > 30) {
            jTextArea.setText("");  //清空内容
        }
        jTextArea.append("收到对象" + objNum + "发来的消息并回传\r\n");
    }
}

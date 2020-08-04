package cn.zengChunMiao.clientPackage;

import cn.zengChunMiao.cartoonShow.TiChiLabel;
import cn.zengChunMiao.cartoonShow.curveLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.SpringLayout.*;

/**
* 客户端窗口
* @author zeng
* @date  15:33 2020/6/20
*/
public class clientWindow extends JFrame {
    private TiChiLabel tiChiLabel;
    private curveLabel curveLabel;
    private JTextArea jTextArea;
    private float vChange;
    private boolean runFlag = false;
    private JTextArea textProgramer;
    private client c;
    public clientWindow(int num,client c) throws HeadlessException {
        this.c = c;
        setTitle("对象" + num);
        //窗口关闭要同时释放太极线程和客户端线程
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                tiChiLabel.setRunFlag(false);   //关闭太极线程
                c.setRunFlag(false);

            }
        });
        Container contentPane = getContentPane();
        //文本区
        JPanel textA = new JPanel();
        textA.setPreferredSize(new Dimension(170, 0));
        textA.setLayout(new BorderLayout());
        textA.setBackground(Color.LIGHT_GRAY);
        contentPane.add(textA, WEST);

        //文本区 ---> 程序状态区
        JPanel programState = new JPanel();
        textA.add(programState,SOUTH);
        textProgramer = new JTextArea();
        textProgramer.setLineWrap(true);
        programState.add(textProgramer);
        textProgramer.setPreferredSize(new Dimension(170,100));
        textProgramer.append("程序初始化完成\n");
        textProgramer.append("等待服务器启动中...\n");


        //文本区 ---> 设置状态区
        JPanel state = new JPanel();
        textA.add(state, NORTH);
        state.setPreferredSize(new Dimension(170, 150));
        state.add(new JLabel("设置转速       ")); //提示标签
        JTextField jTextField = new JTextField(8);
        jTextField.setText("1");
        //限制输入为数字
        jTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (!(keyChar >= '0' && keyChar <= '9')) {
                    e.consume(); //缺点，不能控制赋值黏贴的内容
                }
            }
        });
        state.add(jTextField);
        JButton nChange = new JButton("修改");
        nChange.setPreferredSize(new Dimension(60, 20));
        nChange.addActionListener(e -> {
            this.vChange = Integer.parseInt(jTextField.getText());
            textProgramer.append("转速变化为 ："  + this.vChange + "\n");
        });
        state.add(nChange);

        JButton btnStart = new JButton("开始");
        btnStart.addActionListener(e -> {
            runFlag = true;
            textProgramer.append("电机启动\n");
        });
        state.add(btnStart);
        JButton btnStop = new JButton("停止");
        btnStop.addActionListener(e -> {
            runFlag = false;
            textProgramer.append("电机停止\n");
        });
        state.add(btnStop);
        JButton btnAdd = new JButton("转速加100");
        btnAdd.addActionListener(e -> {
            this.vChange += 100;
            textProgramer.append("输入加100\n当前转速为:" + this.vChange + "\n");
        });
        state.add(btnAdd);
        JButton btnSub = new JButton("转速减100");
        btnSub.addActionListener(e -> {
            this.vChange -= 100;
            textProgramer.append("输入减100\n当前转速为:" + this.vChange + "\n");
        });
        state.add(btnSub);



        //文本区 ---> 状态显示区
        JPanel stateShow = new JPanel();
        textA.add(stateShow);
        stateShow.setPreferredSize(new Dimension(170, 100));
        stateShow.add(new JLabel("当前系统参数"));
        jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);
        jTextArea.setText("控制器输入De: 0\r\n" +
                "控制器输出Du: 0\r\n" +
                "电机输入Ge: 0\r\n" +
                "电机输出Gu: 0\r\n");
        stateShow.add(jTextArea);



        //动画显示区
        JPanel cartoon = new JPanel();
        cartoon.setBackground(Color.GRAY);
        tiChiLabel = new TiChiLabel();
        tiChiLabel.setPreferredSize(new Dimension(300,300));
        cartoon.add(tiChiLabel);
        contentPane.add(cartoon);
        new Thread(tiChiLabel).start();


        //曲线显示区
        this.curveLabel = new curveLabel();
        JPanel curvePanel = new JPanel();
        curvePanel.setPreferredSize(new Dimension(0,200));
        curvePanel.add(curveLabel);
        contentPane.add(curvePanel,SOUTH);


        setBounds(200, 200, 650, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

    }

    /**
     * 新加一个程序状态显示
     * @date 15:34 2020/6/20
     * @param str 传进来加在状态栏显示
     */
    public void setTextProgramer(String str) {
        textProgramer.append(str);
    }

    /**
     * 该方法由client调用，用于更新客户端窗口
     * @date  15:36 2020/6/20
     * @param De 控制器输入
     * @param DU 控制器输出
     * @param Ge 电机输入
     * @param GU 电机输出
     * @return 新一拍的输入R
     */
    public int update(float De, float DU, float Ge, float GU) throws InterruptedException {
        //按下停止按钮时
        if (!runFlag)
        {
            tiChiLabel.setV(0);
            jTextArea.setText("控制器输入De: 0\r\n" +
                    "控制器输出Du: 0\r\n" +
                    "电机输入Ge: 0\r\n" +
                    "电机输出Gu: 0\r\n");
            while (!runFlag)
            {
                Thread.sleep(1000);
            }
        }

        //更新显示
        jTextArea.setText("控制器输入De:" + De + "\r\n" +
                "控制器输出Du:" + DU + "\r\n" +
                "电机输入Ge:" + Ge + "\r\n" +
                "电机输出Gu:" + GU + "\r\n");
        //更新程序状态区
        if (textProgramer.getLineCount() > 6)
        {
            textProgramer.setText("");
        }
        //更新动画速度
        tiChiLabel.setV((int) GU);
        //更新曲线
        curveLabel.setRY((int) vChange);
        curveLabel.setY((int) GU);
        curveLabel.run();

        //更新输入
        return (int) vChange;
    }


}

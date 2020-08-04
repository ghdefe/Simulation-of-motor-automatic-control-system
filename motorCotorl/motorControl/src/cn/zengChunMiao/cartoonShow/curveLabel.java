package cn.zengChunMiao.cartoonShow;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

/**
 * 曲线图
 *
 * @author zeng
 * @date 15:46 2020/6/20
 */
public class curveLabel extends JLabel {
    private int preX;
    private int preY;
    private int X;
    private int Y;
    private int preRY;
    private int RY;
    private BufferedImage bufferedImage;

    {
        bufferedImage = new BufferedImage(600, 200, BufferedImage.TYPE_INT_RGB);
        Graphics imgG = bufferedImage.getGraphics();
        imgG.setColor(Color.WHITE);
        imgG.fillRect(0, 0, 600, 200);
        preY = 180;
        preX = 0;
        X = 0;
        Y = 180;
        RY = 180;
        preRY = 180;
        drawAxis(imgG);
    }

    public curveLabel() {
        setPreferredSize(new Dimension(600, 200));
    }

    @Override
    public void paint(Graphics g) {
        //初始化背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        Graphics imgG = bufferedImage.getGraphics();
        if (X >= 600)   //超出显示范围后重画
        {
            X = 0;
            preX = 0;
            imgG.setColor(Color.WHITE);
            imgG.fillRect(0, 0, this.getWidth(), this.getHeight());
            drawAxis(imgG);
        }
        imgG.setColor(Color.GREEN);
        imgG.drawLine(preX, preY, preX, Y);
        imgG.drawLine(preX, Y, X, Y);
        imgG.setColor(Color.ORANGE);
        imgG.drawLine(preX, preRY, preX, RY);
        imgG.drawLine(preX, RY, X, RY);

        g.drawImage(bufferedImage, 0, 0, null);
        preX = X;
        preY = Y;
        preRY = RY;
        X += 10;
    }

    /**
     * 画坐标轴
     */
    private void drawAxis(Graphics imgG) {
        imgG.setColor(Color.BLACK);
        int X;
        int Y;
        imgG.drawLine(0, 180, 600, 180);
        imgG.drawLine(0, 180, 0, 0);
        X = 10;
        for (int i = 0; i < 60; i++) {
            imgG.drawLine(X, 180, X, 175);
            X += 10;
        }
        Y = 180;
        for (int i = 0; i < 60; i++) {
            imgG.drawLine(0, Y, 5, Y);
            Y -= 10;
        }
    }

    /**
     * 传入电机输入参数，更新曲线图
     *
     * @param y 电机输出大小
     * @author zeng
     * @date 15:48 2020/6/20
     */
    public void setY(int y) {
        Y = 180 - (y / 10);
    }

    /**
     * 传入设定输入参数，更新曲线图
     *
     * @param y 设定输入
     * @author zeng
     * @date 15:48 2020/6/20
     */
    public void setRY(int y) {
        RY = 180 - (y / 10);
    }

    public void run() {
        repaint();
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        Container contentPane = jFrame.getContentPane();
        JPanel jPanel = new JPanel();
        curveLabel curveLabel = new curveLabel();
        jPanel.add(curveLabel);
        jFrame.add(jPanel);
        jFrame.pack();
        jFrame.setVisible(true);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            curveLabel.setY(Integer.parseInt(scanner.nextLine()));
            curveLabel.run();
        }

    }
}

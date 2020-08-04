package cn.zengChunMiao.cartoonShow;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 画一个太极图，并实现转动。该类实现Runnable接口，单独开一个线程来运行它
 *
 * @author zeng
 * @date 15:43 2020/6/20
 */
public class TiChiLabel extends JLabel implements Runnable {
    private int V = 0;   //速度，持有this的对象可以修改它
    int i = 0;// 图片初始度数
    private BufferedImage image;    //画一张太极图
    private boolean runFlag = true;

    {
        int w = 300;
        int h = 300;
        // 创建一张缓存图片
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        // 填充背景颜色
        graphics.setColor(Color.GRAY);
        graphics.fillRect(0, 0, w, h);
        int R = 300;

        // 第一步：画二个大半圆
        graphics.setColor(Color.WHITE);
        graphics.fillArc(0, 0, R, R, 0, 180);
        graphics.setColor(Color.BLACK);
        graphics.fillArc(0, 0, R, R, 0, -180);

        // 第二步：画二个小半圆
        graphics.setColor(Color.WHITE);
        graphics.fillArc(0, 75, R / 2, R / 2, 0, -180);
        graphics.setColor(Color.BLACK);
        graphics.fillArc(150, 75, R / 2, R / 2, 0, 180);

        // 第三步：画二个小圆
        graphics.setColor(Color.WHITE);
        graphics.fillArc(215, 102, 20, 20, 0, 360);
        graphics.setColor(Color.BLACK);
        graphics.fillArc(65, 177, 20, 20, 0, 360);
    }

    /**
     * 更新太极的转动速度
     *
     * @param v 速度
     * @author zeng
     * @date 15:44 2020/6/20
     */
    public void setV(int v) {
        V = v;
    }

    /**
     * 关闭该线程的标志
     * @author zeng
     * @date 1:30 2020/6/21
     * @param runFlag 设置为false即可关闭该线程
     */
    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }


    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        // 填充背景颜色
        g2d.setColor(Color.GRAY);
        int w = 300;
        int h = 300;
        g2d.fillRect(0, 0, w, h);


        i += 2;
        if (i >= 360) {
            i = 0;
        }
        double theta = i * Math.PI / 180;
        g2d.rotate(theta, 150, 150);
        g2d.drawImage(image, 0, 0, null);

    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setBounds(10, 10, 600, 600);
        Container contentPane = jFrame.getContentPane();
        TiChiLabel tiChiLabel = new TiChiLabel();
        contentPane.add(tiChiLabel);
        jFrame.setVisible(true);
        while (true) {
            tiChiLabel.repaint();
        }
    }

    /**
     * 调用repaint方法实现太极转动
     *
     * @author zeng
     * @date 15:45 2020/6/20
     */
    @Override
    public void run() {
        while (runFlag) {
            //控制转动速度
            if (V <= 0) {
                try {
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                repaint();
                Thread.sleep(1000 / V);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ArithmeticException e) {
                System.out.println("发生一次除0异常，暂不理会");
            }
        }
    }

}

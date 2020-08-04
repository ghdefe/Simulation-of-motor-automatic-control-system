package cn.zengChunMiao.module;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 用于保存一个对象信息，最多保存10拍信息。并提供了各个参数相应的setter and getter
 *
 * @author zeng
 * @date 15:53 2020/6/20
 */
public class motorModule implements Serializable {
    private int now;    //当前拍数
    private float[] R;    //当前输入
    private float[] Gerr;   //模拟对象输入
    private float[] GU;     //模拟对象输出
    private float[] Derr;  //控制对象输入
    private float[] DU;    //控制对象输出


    /**
     * 最多记录10拍，大于10拍重新记录到0
     * 由于两个传递函数都只需要用到前一拍和当前拍的值，故只提供当前拍和上一拍的get
     */
    public motorModule() {
        //只需要10个长度，太长了socket传输速度太慢
        now = 0;
        R = new float[10];
        Gerr = new float[10];
        GU = new float[10];
        Derr = new float[10];
        DU = new float[10];
    }


    public int getNow() {
        return now;
    }

    public void setNowAdd() {
        if (now >= 9)
            now = 0;
        else
            now++;
    }

    public float getRLast() {
        return now != 0 ? R[now - 1] : R[9 - now];
    }

    public void setR(float r) {
        R[now] = r;
    }

    public float getGerrNow() {
        return Gerr[now];
    }

    public void setGerrLast(float GerrNow) {
        if (now == 0)
            Gerr[9] = GerrNow;
        else
            Gerr[now - 1] = GerrNow;
    }

    public float getGerrLast() {
        return now != 0 ? Gerr[now - 1] : Gerr[9];
    }

    public float getGerrLastLast() {
        return now > 1 ? Gerr[now - 1] : Gerr[9-now];
    }

    public float getGUNow() {
        return GU[now];
    }

    public void setGUNow(float GUNow) {
        GU[now] = GUNow;
    }

    public float getGULast() {
        return now != 0 ? GU[now - 1] : GU[9];
    }

    //控制器的setter and getter
    public float getDerrNow() {
        return Derr[now];
    }

    public void setDerrNow(float DerrNow) {
        Derr[now] = DerrNow;
    }

    public float getDerrLast() {
        return now != 0 ? Derr[now - 1] : Derr[9];
    }

    public float getDUNow() {
        return DU[now];
    }

    public void setDUNow(float DUNow) {
        DU[now] = DUNow;
    }

    public float getDULast() {
        return now != 0 ? DU[now - 1] : DU[9];
    }

    public float getDULastLast() {
        return now > 1 ? DU[now - 1] : DU[9-now];
    }

    @Override
    public String toString() {
        return "cn.zengChunMiao.module.motorModule{" +
                "now=" + now +
                ", Gerr=" + Arrays.toString(Gerr) +
                ", GU=" + Arrays.toString(GU) +
                ", Derr=" + Arrays.toString(Derr) +
                ", DU=" + Arrays.toString(DU) +
                '}';
    }

}

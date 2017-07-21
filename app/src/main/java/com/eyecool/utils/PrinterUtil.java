package com.eyecool.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.eyecool.bean.ComBean;
import com.printsdk.cmd.PrintCmd;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created date: 2017/7/1
 * Author:  Leslie
 *
 * 调用 美松串口热敏打印机 的工具类
 * 使用基础：
 * 1.类：ComBean.java,MyFunc.java,SerialHelper.java,TimeUtil.java,ToastUtil.java
 * 2.包：android_serialport_api 里面有 SerialPort.java,SerialPortFinder.java
 * 3.jar包：printsdk-v2.2.jar
 * 4.动态库文件：main/jniLibs/armeabi-v7a/libserial_port.so
 *
 * 使用方法：
 * 1.PrinterUtil.getInstance().init();  初始化打印设备。
 * 2.printerInstance.openComPort();     打开设备，printerInstance 工具类实例。
 * 3.printerInstance.printXxx();        打印具体内容，有多个方法。
 * 4.printerInstance.closeComPort();    关闭设备
 */

public class PrinterUtil {

    private static final String TAG = "PrinterUtil";
    //使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用
    private static volatile PrinterUtil mPrinter;
    private SerialControl comA;
    private boolean isOpenCom = false;

    public PrinterUtil() {

    }

    /**
     * 单例模式的最佳实现。内存占用地，效率高，线程安全，多线程操作原子性。
     *
     * @return
     */
    public static PrinterUtil getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (mPrinter == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (PrinterUtil.class) {
                //未初始化，则初始instance变量
                if (mPrinter == null) {
                    mPrinter = new PrinterUtil();
                }
            }
        }
        return mPrinter;
    }

    public boolean isOpenCom() {
        return isOpenCom;
    }

    /**
     * 初始化串口打印机设备，默认数据：1.串口:/dev/ttyS4; 2.波特率:115200;
     *
     * @return
     */
    public PrinterUtil init() {
        comA = new SerialControl();
        comA.setPort("/dev/ttyS4");
        comA.setBaudRate(115200);
        return mPrinter;
    }

    /**
     * 打开串口设备
     *
     * @return
     */
    public boolean OpenComPort() {
        try {
            comA.open();
            isOpenCom = true;
            return isOpenCom;
        } catch (SecurityException e) {
            ToastUtil.showToast("没有读写权限！");
            isOpenCom = false;
            return isOpenCom;
        } catch (IOException e) {
            ToastUtil.showToast("io流错误！");
            isOpenCom = false;
            return isOpenCom;
        } catch (InvalidParameterException e) {
            ToastUtil.showToast("参数错误！");
            isOpenCom = false;
            return isOpenCom;
        }
    }

    /**
     * 关闭串口设备
     */
    public void CloseComPort() {
        if (comA != null) {
            comA.stopSend();
            comA.close();
            isOpenCom = false;
        }
    }

    /**
     * 打印
     */
    public void printEyeCoolInfo() {
        if (comA.isOpen()) {
            comA.send(PrintCmd.SetClean());                                                 // 清理缓存，缺省模式
            //如果内容此行没有填满，0:左对齐;1:水平居中;2:右对齐
            comA.send(PrintCmd.SetAlignment(1));
            //第一个参数时字体的宽度，第二个参数时字体的高度
            comA.send(PrintCmd.SetSizetext(1, 1));
            comA.send(PrintCmd.PrintString("山东眼神智能科技欢迎您！", 0));

            try {
                PrintFeedCutpaper(10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.showToast("请打开打印机！");
            return;
        }
    }

    /**
     * 打印一维码
     * @param width
     * @param height
     * @param hrisize 条码显示字符字形 0: 12*24;    1:  9*17
     * @param hriseat 条码显示字符位置 0: 无;    1: 上;   2: 下;   3: 上下;
     * @param codeType 条码类型 0: UPC-A;   1: UPC-E;   2: EAN13;   3: EAN8;    4: CODE39;   5: ITF;
     *                 6: CODABAR;  7: Standard EAN13;  8: Standard EAN8;   9: CODE93;  10: CODE128;
     * @param strData 条码内容
     */
    public void print1DBar(int width,int height,int hrisize,int hriseat,int codeType,String strData){
        comA.send(PrintCmd.SetAlignment(1));
        comA.send(PrintCmd.Print1Dbar(width, height, hrisize, hriseat, codeType, strData));
        comA.send(PrintCmd.SetAlignment(0));
        // 走纸4行,再切纸,清理缓存
        try {
            PrintFeedCutpaper(4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试打印，有一定的格式
     * @param strTitle
     * @param order
     * @param visitorName
     * @param receiveTeacherName
     * @param techerPhoneNum
     * @param gender
     */
    public void printTest(String strTitle, int order,String visitorName,String receiveTeacherName,String techerPhoneNum,int gender) {
        if (comA.isOpen()) {
            comA.send(PrintCmd.SetClean());                                                 // 清理缓存，缺省模式
            //如果内容此行没有填满，0:左对齐;1:水平居中;2:右对齐
            comA.send(PrintCmd.SetAlignment(1));
            //第一个参数时字体的宽度，第二个参数时字体的高度
            comA.send(PrintCmd.SetSizetext(1, 1));
            //第二个参数，是否加换行指令。0:加换行指令; 2.不加换行指令(等到下一个换行指令才打印)
            comA.send(PrintCmd.PrintString(strTitle, 0));

            comA.send(PrintCmd.PrintFeedline(2));
            comA.send(PrintCmd.SetAlignment(1));
            comA.send(PrintCmd.SetSizetext(1, 1));
            comA.send(PrintCmd.PrintString(order + "", 0));

            comA.send(PrintCmd.SetSizetext(0, 0));
            comA.send(PrintCmd.PrintFeedline(2));
            comA.send(PrintCmd.SetAlignment(0));
            comA.send(PrintCmd.PrintString("尊敬的：", 0));

            comA.send(PrintCmd.PrintFeedline(2));
            comA.send(PrintCmd.SetAlignment(1));
            comA.send(PrintCmd.SetSizetext(1, 1));
            comA.send(PrintCmd.PrintString(visitorName, 0));
            comA.send(PrintCmd.SetSizetext(0,0));

            comA.send(PrintCmd.PrintFeedline(2));
            comA.send(PrintCmd.SetAlignment(2));
            comA.send(PrintCmd.PrintString((gender == 0 ? "大官人" : "女士") + "!", 0));


            comA.send(PrintCmd.PrintFeedline(2));
            comA.send(PrintCmd.SetAlignment(0));
            comA.send(PrintCmd.PrintString("您的接待技师：", 0));

            comA.send(PrintCmd.PrintFeedline(2));
            comA.send(PrintCmd.SetAlignment(2));
            comA.send(PrintCmd.PrintString(receiveTeacherName, 0));

            comA.send(PrintCmd.PrintFeedline(0));
            comA.send(PrintCmd.SetAlignment(2));
            comA.send(PrintCmd.PrintString("联系方式：  " + techerPhoneNum, 0));

            comA.send(PrintCmd.PrintFeedline(2));
            comA.send(PrintCmd.SetAlignment(2));
            comA.send(PrintCmd.PrintString(TimeUtil.getDayOfYear() + "  " + TimeUtil.getTimeOfDay(), 0));
            try {
                PrintFeedCutpaper(10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.showToast("请打开打印机！");
            return;
        }
    }

    /**
     * 打印二维码
     *
     * @param strData    二维码内容
     * @param leftMargin 左边距，取值 0-27， 单位 mm
     * @param unitLength 单位长度，即二维码大小，取值 1-8，(有些打印机型只支持 1-4)
     * @param roundMode  环绕模式，0:环绕 (混排，有些机型不支持)、1:立即打印 (不混排)
     */
    public void printQRCode(String strData, int leftMargin, int unitLength, int roundMode) {
        comA.send(PrintCmd.PrintQrcode(strData, leftMargin, unitLength, roundMode));
    }


    /**
     * @param iLine
     * @throws IOException 走纸换行，再切纸，清理缓存
     */
    private void PrintFeedCutpaper(int iLine) throws IOException {
        comA.send(PrintCmd.PrintFeedline(iLine));
        comA.send(PrintCmd.PrintCutpaper(0));
        comA.send(PrintCmd.SetClean());
    }

    private void printImgFile(){
        int[] data = getBitmapParamsData(getBitmapPath("logo100.bmp"));
        comA.send(PrintCmd.PrintDiskImagefile(data, 50, 50));
    }

    private int[] getBitmapParamsData(String imgPath) {
        Bitmap bm = BitmapFactory.decodeFile(imgPath, getBitmapOption(1)); // 将图片的长和宽缩小味原来的1/2
        int width = bm.getWidth();
        int  heigh = bm.getHeight();
        int iDataLen = width * heigh;
        int[] pixels = new int[iDataLen];
        bm.getPixels(pixels, 0, width, 0, 0, width, heigh);
        return pixels;
    }

    // 获取SDCard图片路径
    private String getBitmapPath(String fileName) {
        String imgPath = Environment.getExternalStorageDirectory().getPath() + "/fp" + "fp.jpg";
        return imgPath;
    }

    // BitmapOption 位图选项
    private static BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return options;
    }


    private class SerialControl extends SerialHelper {
        public SerialControl() {
        }

        @Override
        protected void onDataReceived(final ComBean ComRecData) {
        }
    }
}

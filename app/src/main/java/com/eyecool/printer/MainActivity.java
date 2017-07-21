package com.eyecool.printer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eyecool.utils.PrinterUtil;
import com.eyecool.utils.SoundUtil;
import com.eyecool.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    @Bind(R.id.bt_open_printer)
    Button btOpenPrinter;
    @Bind(R.id.bt_print)
    Button btPrint;
    private boolean isOpenCom = false;
    private PrinterUtil printerInstance;
    private int soundId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        soundId = SoundUtil.init(this, R.raw.alarm_beep_02);
        printerInstance = PrinterUtil.getInstance().init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundUtil.close(soundId);
        printerInstance.CloseComPort();
    }

    @OnClick({R.id.bt_open_printer, R.id.bt_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_open_printer:                                                            //打开打印机
                //播放打开提示音
                SoundUtil.play(soundId);
                if (isOpenCom) {
                    //如果已经打开了，就要关闭了。
                    printerInstance.CloseComPort();
                    btOpenPrinter.setBackground(getDrawable(R.drawable.bt_press_selector));
                    btOpenPrinter.setText("打开打印机");
                    btOpenPrinter.setTextColor(Color.BLACK);
                    isOpenCom = false;
                } else {
                    if (printerInstance.OpenComPort()) {
                        btOpenPrinter.setBackground(getDrawable(R.color.colorGreen));
                        btOpenPrinter.setText("已打开");
                        btOpenPrinter.setTextColor(Color.WHITE);
                        isOpenCom = true;



                    }
                }

                break;
            case R.id.bt_print:                                                                   //开始打印

                if (printerInstance.isOpenCom()) {
                    printerInstance.printTest("好客山东欢迎您",1001,"西门庆","潘金莲","13338383838",0);
                } else {
                    ToastUtil.showToast("请打开打印机！");
                    return;
                }
                break;
        }
    }
}

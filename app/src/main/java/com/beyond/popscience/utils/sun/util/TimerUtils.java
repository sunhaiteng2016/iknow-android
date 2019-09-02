package com.beyond.popscience.utils.sun.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.beyond.popscience.R;


public class TimerUtils {

    public static void  startTimer(final Context context, final TextView tvCode){
        new CountDownTimer(60000,1000){
            public void onTick(long millisUntilFinished) {

                tvCode.setText( millisUntilFinished / 1000+"s");
                tvCode.setEnabled(false);
                tvCode.setTextColor(context.getResources().getColor(R.color.text666));
            }

            public void onFinish() {
                tvCode.setText("重新发送");
                tvCode.setTextColor(context.getResources().getColor(R.color.blue2));
                tvCode.setEnabled(true);
            }
        }.start();

    }

}

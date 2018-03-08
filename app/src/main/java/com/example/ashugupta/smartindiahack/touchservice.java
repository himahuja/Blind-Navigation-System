package com.example.ashugupta.smartindiahack;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.concurrent.ScheduledExecutorService;

public class touchservice extends Service implements View.OnTouchListener {

    String shorturl1 ;
    private RelativeLayout Parent;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private boolean moving;
    private WindowManager wm;
    private int winHeight = 0;
    private int winWidth = 0;
    private GradientDrawable shape, shape2;
    private Handler handler;
    private Button menuButton;
    private ImageView button;
    private WindowManager.LayoutParams params;
    private GestureDetector gestureDetector;
    private boolean pd;
    private Double LONG = null, LAT = null;
    private LocationListener listener;
    private LocationManager lm;
    SharedPreferences sp;
    int nou;
    ScheduledExecutorService scheduler;
    SmsManager sms;
    Runnable runnableCode ;
    Handler handler2 ;
    int j ;


    private int status = 0;


    @Override
    public IBinder onBind(Intent intent) {
        return null;


    }


    @Override
    public void onCreate() {
        super.onCreate();

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext()) ;
        handler = new Handler();
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        //taking display heigth and width
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        winHeight = metrics.heightPixels;
        winWidth = metrics.widthPixels;

        Parent = new RelativeLayout(this);


        button = new ImageView(this);
        button.setImageResource(R.drawable.eye);
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        button.setOnTouchListener(this);
        button.setPadding(0,0, 0, 0);
        button.setMinimumWidth(winWidth);
        button.setMinimumHeight(winHeight);

        shape = new GradientDrawable();
        shape.setCornerRadius(20);
        shape.setStroke(5, getResources().getColor(R.color.white));
        shape.setColor(getResources().getColor(R.color.colorPrimary));

        shape2 = new GradientDrawable();
        shape2.setCornerRadius(20);
        shape2.setColor(getResources().getColor(R.color.yellow));
        shape2.setStroke(5, getResources().getColor(R.color.white));
        Parent.setBackground(shape2);

        Parent.setBackground(shape);


        final RelativeLayout.LayoutParams params_buttons = new RelativeLayout.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);


//        params_buttons.addRule(RelativeLayout.CENTER_IN_PARENT);


        Parent.addView(button, params_buttons);


        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSPARENT);


        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 200;
        params.y = 100;


        wm.addView(Parent, params);

        nou =sp.getInt("Nou", 0 ) ;





    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (gestureDetector.onTouchEvent(event)) {

//            Log.d("Test ", "touch");
//            Intent i=new Intent(this,Main2Activity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName("com.example.ashugupta.camerapreviewexample","com.example.ashugupta.camerapreviewexample.MainActivity"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setComponent(ComponentName.unflattenFromString("com.example.ashugupta.camerapreviewexample/com.example.ashugupta.camerapreviewexample.MainActivity"));
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

      this.stopSelf();
//            Toast.makeText(this, "Any action can be performed now", Toast.LENGTH_SHORT).show();
            return true;

        }
        else {

            switch (event.getAction()) {


                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
                case MotionEvent.ACTION_MOVE:
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);
                    wm.updateViewLayout(Parent, params);
                    return true;
            }

            Log.d("TesT : ", "TOUCH");

        }

        return false;


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Parent != null) wm.removeView(Parent);

    }


    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;

        }
    }

}





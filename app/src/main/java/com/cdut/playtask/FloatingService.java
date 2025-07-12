package com.cdut.playtask;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.*;
import android.view.View.OnTouchListener;
import androidx.annotation.Nullable;

public class FloatingService extends Service {

    private WindowManager wm;
    private View floatView;
    private WindowManager.LayoutParams params;

    private int lastX, lastY;
    private long downTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        initFloatingView();
    }

    private void initFloatingView() {
        floatView = LayoutInflater.from(this).inflate(R.layout.floating_ball, null);

        int flag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                flag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;

        // 默认初始位置
        params.x = 900;
        params.y = 20;

        wm.addView(floatView, params);

        floatView.setOnTouchListener(new OnTouchListener() {
            int startX, startY;
            long startTime;

            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) ev.getRawX();
                        startY = (int) ev.getRawY();
                        downTime = System.currentTimeMillis();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) ev.getRawX() - startX;
                        int dy = (int) ev.getRawY() - startY;
                        params.x += dx;
                        params.y += dy;
                        wm.updateViewLayout(floatView, params);
                        startX = (int) ev.getRawX();
                        startY = (int) ev.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        long duration = System.currentTimeMillis() - downTime;
                        int moveX = Math.abs((int) ev.getRawX() - startX);
                        int moveY = Math.abs((int) ev.getRawY() - startY);
                        if (duration < 200 && moveX < 10 && moveY < 10) {
                            // 这是点击，不是拖动
                            openChat();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void openChat() {
        Intent i = new Intent(this, AIChatActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatView != null) wm.removeView(floatView);
    }
}

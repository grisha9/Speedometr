package ru.rzn.myasoedov.speedometr.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by grisha on 17.06.15.
 */
public class CarModel implements Runnable{
    public static final String ACTION_CHANGE_SPEED = "ru.rzn.myasoedov.speedometr.model.ACTION_CHANGE_SPEED";
    public static final int DELAY = 100;
    public static final String SPEED = "speed";
    public static final int BREAK_RATIO = 10;
    public static final int THROTTLE_RATIO = 5;
    public static final int STOP_RATIO = 8;
    public static final double STOP_CONSTANT = 0.05;
    private int maxSpeed;
    private volatile float speed;
    private boolean isThrottle;
    private boolean isBreak;
    private Thread thread;
    private Context context;

    public CarModel(Context context, int maxSpeed) {
        this.maxSpeed = maxSpeed;
        this.context = context;
        init();
    }

    private void init() {
        this.speed = 0;
        this.isThrottle = false;
        this.isBreak = false;
    }

    public void start() {
        if (thread != null) {
            throw new IllegalStateException("Car is running. Need stop before start");
        }
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        thread.interrupt();
        thread = null;
        init();
    }

    public void setThrottle(boolean isThrottle) {
        this.isThrottle = isThrottle;
    }

    public void setBreak(boolean isBreak) {
        this.isBreak = isBreak;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            if (isThrottle) {
                speed += THROTTLE_RATIO * (1 - speed/maxSpeed);
                if (speed > maxSpeed) {
                    speed = maxSpeed;
                }
            } else if (isBreak) {
                speed -= BREAK_RATIO + STOP_RATIO * (speed/maxSpeed);
                if (speed < 0) {
                    speed = 0;
                }

            } else {
                if (speed > 0) {
                    speed -= STOP_CONSTANT + STOP_RATIO * (speed/maxSpeed);
                }
            }

            Intent intent = new Intent(ACTION_CHANGE_SPEED);
            intent.putExtra(SPEED, speed);
            context.sendBroadcast(intent);

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

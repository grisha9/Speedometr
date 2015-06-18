package ru.rzn.myasoedov.speedometr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import ru.rzn.myasoedov.speedometr.model.CarModel;
import ru.rzn.myasoedov.speedometr.view.SpeedometerView;


public class SpeedActivity extends ActionBarActivity {
    private BroadcastReceiver receiver;
    private SpeedometerView speedometerView;
    private CarModel carModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);

        speedometerView = (SpeedometerView) findViewById(R.id.speedometer);
        View  throttle = findViewById(R.id.throttle);
        throttle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        carModel.setThrottle(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        carModel.setThrottle(false);
                        break;
                }
                return false;
            }
        });

        View  breaks = findViewById(R.id.breaks);
        breaks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        carModel.setBreak(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        carModel.setBreak(false);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        carModel = new CarModel(this, SpeedometerView.MAX_SPEED);
        carModel.start();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case CarModel.ACTION_CHANGE_SPEED:
                        speedometerView.setSpeed((int) intent.getFloatExtra(CarModel.SPEED, 0));
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter(CarModel.ACTION_CHANGE_SPEED);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        carModel.stop();
        unregisterReceiver(receiver);
    }
}

package ru.rzn.myasoedov.speedometr.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import ru.rzn.myasoedov.speedometr.R;

/**
 * Created by grisha on 17.06.15.
 */
public class SpeedometerView extends View {
    public static final int START_ANGLE = 40;
    public static final int END_ANGLE = 320;
    public static final int ANGLE_DIFF = END_ANGLE - START_ANGLE;

    public static final int MAX_SPEED = 300;
    public static final int WIDTH = 5;
    public static final int TEXT_Y_OFFSET = 40;
    public static final int TEXT_X_OFFSET = 10;

    private int speed;
    private Paint paint;
    private Matrix matrix;
    private Paint paintText;

    public SpeedometerView(Context context) {
        super(context);
        init();
    }

    public SpeedometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpeedometerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        matrix = new Matrix();

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(WIDTH);

        paintText = new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setStrokeWidth(WIDTH);
        paintText.setTextSize(25);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x1 = getWidth() / 2;
        float y1 = getHeight() / 2;
        float x2 = x1;
        float y2 = y1 + getHeight() / 3;

        boolean isWidthGreat = getWidth() > getHeight();
        int left = isWidthGreat ? ((getWidth() - getHeight()) / 2) : 0;
        int top = isWidthGreat ? 0 : ((getHeight() - getWidth()) / 2);
        int right = isWidthGreat ? ((getWidth() - getHeight()) / 2) + getHeight() : getWidth();
        int bottom = isWidthGreat ? getHeight() : right + top;
        Drawable d = getResources().getDrawable(R.drawable.speed);
        d.setBounds(left, top, right, bottom);
        d.draw(canvas);

        canvas.drawText(String.valueOf(speed), getWidth() / 2 - TEXT_X_OFFSET,
                getHeight() / 2 + TEXT_Y_OFFSET, paintText);

        matrix.setRotate((float) START_ANGLE + (speed * ANGLE_DIFF * 1f / MAX_SPEED), getWidth() / 2,
                getHeight() / 2);
        float[] linePts = new float[] {x1, y1, x2, y2};
        matrix.mapPoints(linePts);
        canvas.drawLine(linePts [0], linePts [1], linePts [2], linePts [3], paint);
    }

    public void setSpeed(int speed) {
        if (this.speed != speed) {
            this.speed = speed;
            invalidate();
        }
    }
}

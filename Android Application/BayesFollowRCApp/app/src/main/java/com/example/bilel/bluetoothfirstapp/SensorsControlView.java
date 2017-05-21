package com.example.bilel.bluetoothfirstapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Bilel on 5/9/2017.
 */

public class SensorsControlView extends View
{

    private int[] sensorColor;

    public SensorsControlView(Context context) {
        super(context);
        Initialisation();
    }

    public SensorsControlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Initialisation();
    }

    public SensorsControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Initialisation();
    }

    public SensorsControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Initialisation();
    }

    public void Initialisation()
    {
        sensorColor = new int[4];
        for (int i=1; i<5; i++)
        {
            updateSensor(i, false);
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(sensorColor[0]);
        canvas.drawCircle(150,150,40,paint);
        paint.setColor(sensorColor[1]);
        canvas.drawCircle(250,150,40,paint);
        paint.setColor(sensorColor[2]);
        canvas.drawCircle(350,150,40,paint);
        paint.setColor(sensorColor[3]);
        canvas.drawCircle(450,150,40,paint);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(540, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(230, MeasureSpec.EXACTLY));
    }

    public void updateSensor(int number, boolean state)
    {
        sensorColor[number-1] = state?Color.argb(255,169,234,139):Color.argb(255,249,138,151);
    }


}
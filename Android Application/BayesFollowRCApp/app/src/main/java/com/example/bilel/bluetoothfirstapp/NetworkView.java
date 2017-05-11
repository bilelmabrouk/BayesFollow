package com.example.bilel.bluetoothfirstapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Bilel on 4/20/2017.
 */

public class NetworkView extends View {
    private int x;
    private Typeface font;
    private int[] sensorColor;
    private String dirMotor;
    private int percentMotor;
    private int colorMotor;
    public NetworkView(Context context) {
        super(context);
        Initialisation(null);
    }

    public NetworkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Initialisation(attrs);
    }

    public NetworkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Initialisation(attrs);
    }

    public NetworkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Initialisation(attrs);
    }

    private void Initialisation(@Nullable AttributeSet set)
    {
        x=100;
        font = Typeface.createFromAsset(getContext().getAssets(), "34668_ShowcardGothic.ttf");
        sensorColor = new int[4];
        for (int i=1; i<5; i++)
        {
            updateSensor(i,true);
        }
        updateMotor("F",100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Path fleche = new Path();
        fleche.moveTo(285,450);
        fleche.lineTo(60,250);
        fleche.moveTo(285,450);
        fleche.lineTo(210,150);
        fleche.moveTo(285,450);
        fleche.lineTo(360,150);
        fleche.moveTo(285,450);
        fleche.lineTo(510,250);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawPath(fleche,paint);


        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(sensorColor[0]);
        canvas.drawCircle(60,250,53,paint);
        paint.setColor(sensorColor[1]);
        canvas.drawCircle(210,150,53,paint);
        paint.setColor(sensorColor[2]);
        canvas.drawCircle(360,150,53,paint);
        paint.setColor(sensorColor[3]);
        canvas.drawCircle(510,250,53,paint);

        paint.setColor(colorMotor);
        canvas.drawCircle(285,450,77,paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(60,250,55,paint);
        canvas.drawCircle(210,150,55,paint);
        canvas.drawCircle(360,150,55,paint);
        canvas.drawCircle(510,250,55,paint);
        canvas.drawCircle(285,450,80,paint);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(font);
        paint.setTextSize(20);
        canvas.drawText("Sensor 1", 20,255,paint);
        canvas.drawText("Sensor 2", 170,155,paint);
        canvas.drawText("Sensor 3", 320,155,paint);
        canvas.drawText("Sensor 4", 470,255,paint);
        paint.setTextSize(30);
        canvas.drawText("Motor", 230,440,paint);
        paint.setTextSize(20);
        canvas.drawText(dirMotor + ": "+percentMotor+"%", 255,475,paint);


        int p1,p2,p3,p4x,p4y,debx,deby;
        double cos,sin,diff;
        paint.setColor(Color.argb(255,162,122,226)); //HEX is: #A27AE2

        //Triangle1
        Path triangle = new Path();
        p1 = 285-60;
        p2 = (int) Math.round(Math.sqrt(Math.pow(285-60,2)+Math.pow(450-250,2)));
        p3 = 450 - 250;
        p4x =  60 + (int)(55*((float)p1/p2));
        p4y =  250 + (int)(55*((float)p3/p2));
        debx = p4x;
        deby = p4y;
        triangle.moveTo(p4x,p4y);
        p4x = 122;
        p4y = 295;
        triangle.lineTo(p4x,p4y);
        p4x = 111;
        p4y = 306;
        triangle.lineTo(p4x,p4y);
        triangle.lineTo(debx,deby);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawPath(triangle,paint);

        //Triangle2
        p1 = 285-210;
        p2 = (int) Math.round(Math.sqrt(Math.pow(285-210,2)+Math.pow(450-150,2)));
        p3 = 450 - 150;
        p4x =  210 + (int)(55*((float)p1/p2));
        p4y =  150 + (int)(55*((float)p3/p2));
        debx = p4x;
        deby = p4y;
        triangle.moveTo(p4x,p4y);
        p4x=236;
        p4y=221;
        triangle.lineTo(p4x,p4y);
        p4x=220;
        p4y=225;
        triangle.lineTo(p4x,p4y);
        triangle.lineTo(debx,deby);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawPath(triangle,paint);

        //Triangle3
        p1 = 285-360;
        p2 = (int) Math.round(Math.sqrt(Math.pow(285-360,2)+Math.pow(450-150,2)));
        p3 = 450 - 150;
        p4x =  360 + (int)(55*((float)p1/p2));
        p4y =  150 + (int)(55*((float)p3/p2));
        debx = p4x;
        deby = p4y;
        triangle.moveTo(p4x,p4y);
        p4x = 350;
        p4y = 225;
        triangle.lineTo(p4x,p4y);
        p4x = 334;
        p4y = 221;
        triangle.lineTo(p4x,p4y);
        triangle.lineTo(debx,deby);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawPath(triangle,paint);

        //Triangle4
        p1 = 285-510;
        p2 = (int) Math.round(Math.sqrt(Math.pow(285-510,2)+Math.pow(450-250,2)));
        p3 = 450 - 250;
        p4x =  510 + (int)(55*((float)p1/p2));
        p4y =  250 + (int)(55*((float)p3/p2));
        debx = p4x;
        deby = p4y;
        triangle.moveTo(p4x,p4y);
        p4x = 460;
        p4y = 306;
        triangle.lineTo(p4x,p4y);
        p4x = 449;
        p4y = 293;
        triangle.lineTo(p4x,p4y);
        triangle.lineTo(debx,deby);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawPath(triangle,paint);

        /*
        paint.setStrokeWidth(5);
        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);
        paint2.setStyle(Paint.Style.FILL);

        canvas.drawCircle(100,100,50,paint);
        canvas.drawCircle(250,100,50,paint2);
        canvas.drawCircle(400,100,50,paint2);
        canvas.drawCircle(550,100,50,paint);
        */


        invalidate();
    }

    public void updateSensor(int number, boolean state)
    {
        sensorColor[number-1] = state?Color.argb(255,169,234,139):Color.argb(255,249,138,151);
    }

    public void updateMotor(String dir, int percent)
    {
        dirMotor = dir;
        percentMotor = percent;
        if (dirMotor.equals("S"))
            colorMotor = Color.argb(255,146,136,165);
        if (dirMotor.equals("F"))
            colorMotor = Color.WHITE;
        if (dirMotor.equals("R"))
            colorMotor = Color.argb(255,122,229,216);
        if (dirMotor.equals("L"))
            colorMotor = Color.argb(255,244,166,61);
        if (dirMotor.equals("B"))
            colorMotor = Color.argb(255,244,240,88);
        if (dirMotor.equals("FR"))
            colorMotor = Color.WHITE;
        if (dirMotor.equals("FL"))
            colorMotor = Color.argb(255,122,229,216);
        if (dirMotor.equals("BR"))
            colorMotor = Color.argb(255,244,240,88);
        if (dirMotor.equals("BL"))
            colorMotor = Color.argb(255,244,166,61);
    }

}

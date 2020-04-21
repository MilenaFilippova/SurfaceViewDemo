package com.example.surfaceviewdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


//сделаем отдельный класс для шариков
public class Balls
{
        float x, y, dx = 30, dy = 30, radius;
        int color;

        public Balls(float x, float y, float radius, int color)
        {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }

        public void check(float width, float height)
        {
            if(  ((y + dy - radius) <= 0) || ((y + dy + radius) >= height)  )
                dy = dy * -1;
            if ( ((x + dx - radius) <= 0) || ((x + dx + radius) >= width ))
                dx = dx * -1;

            y += dy;
            x += dx;
        }

        public  void drawBall (Canvas canvas)
        {
            Paint p = new Paint();
            p.setColor(color);
            canvas.drawCircle(x, y, radius, p);
            p.setColor(Color.BLACK);
           // canvas.drawText(Integer.toString(n), x, y, p);
        }
}



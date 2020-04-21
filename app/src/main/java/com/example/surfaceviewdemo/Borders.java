package com.example.surfaceviewdemo;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Borders {
    float top, bottom , left, right, R ,cx, cy;
    int color;

    public void coord(float dx, float dy)
    {
        this.top = dy - 60;
        this.bottom = dy + 60;
        this.left = dx - 60;
        this.right = dx + 60;
        double cis = 2;
        this.R = (float) (120 * Math.sqrt(cis) / 2);
        this.cx = dx;
        this.cy = dy;
    }

    public Borders(float x, float y, int color) {
        this.color = color;
        coord(x, y);
    }

    public void drawBorders(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(color);
        canvas.drawRect(left, top, right, bottom, p);
    }

    public boolean ballInsideBorders(float point_x, float point_y, float rad)
    {

        float d = (float)Math.sqrt((point_x - this.cx) * (point_x - this.cx) + (point_y - this.cy) * (point_y - this.cy));
        if (d <= R + rad)
        {
            return true;
        }
        return false;
    }

}

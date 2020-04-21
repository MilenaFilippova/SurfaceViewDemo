package com.example.surfaceviewdemo;

import android.view.SurfaceView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    SurfaceHolder holder;
    DrawThread thread;

    float width,height, x, y, radius;
    //сдвиг
    int dx = 20, dy = 20;

    int color1 = Color.RED;
    int color2 = Color.BLUE;
    //палитра цветов для замены
    int[] colors = {Color.RED, Color.BLUE, Color.BLACK,  Color.MAGENTA, Color.GRAY, Color.DKGRAY,Color.CYAN,  Color.YELLOW };

    //массив шариков
    ArrayList<Balls> balls = new ArrayList<>();

    //грани
    Borders borders;


    public void changeColor() {
        Random r = new Random();
        color1 = Color.rgb(r.nextInt(255),r.nextInt(255),r.nextInt(255));
        r = new Random();
        color2 = Color.rgb(r.nextInt(255),r.nextInt(255),r.nextInt(255));
    }



    class DrawThread extends Thread
    {

        //float x = 300, y = 300;
        Random r = new Random();
        Paint p = new Paint();

        boolean runFlag = true;

        // в конструкторе нужно передать holder для дальнейшего доступа к канве

       /* public DrawThread(SurfaceHolder holder) {
            this.holder = holder;
        }*/


        @Override
        public void run() {
            super.run();
            p.setColor(Color.YELLOW);

            // выполняем цикл (рисуем кадры) пока флаг включен
            while (runFlag)
            {
                //получаем канву
                Canvas c = holder.lockCanvas();
                // если успешно захватили канву
                if (c != null) {
                    c.drawColor(Color.WHITE);
                    //тк шарика у нас 2
                    for (int k = 0; k < 2; k++) {
                        //закрашиваем
                        balls.get(k).drawBall(c);
                    }
                    borders.drawBorders(c);

                    //проверим одинаковы ли теперь цвета шариков
                    if (balls.get(0).color == balls.get(1).color) {
                        //если да, то остановим поток через флаг
                        thread.flagOff();
                    }
                    for (int k =0; k<2; k++) {
                        balls.get(k).check(width, height);
                        float d = (float) Math.sqrt((balls.get(k).x - balls.get(k).x) * (balls.get(k).x - balls.get(k).x) + (balls.get(k).y - balls.get(k).y) * (balls.get(k).y - balls.get(k).y));
                        if (d <= balls.get(k).radius + balls.get(k).radius) {
                            float tempdx = balls.get(k).dx;
                            float tempdy = balls.get(k).dy;
                            balls.get(k).dx = balls.get(k).dx;
                            balls.get(k).dy = balls.get(k).dy;
                            balls.get(k).dy = tempdy;
                            balls.get(k).dx = tempdx;
                        }

                        if (borders.ballInsideBorders(balls.get(k).x, balls.get(k).y, balls.get(k).radius)) {
                            balls.get(k).dx *= -1;
                            balls.get(k).dy *= -1;
                            for (int j = 0; j < colors.length; j++)
                            {
                                if (balls.get(k).color == colors[j])
                                {
                                    if (j == colors.length - 1)
                                    {
                                        balls.get(k).color = colors[0];
                                    }
                                    else
                                    {
                                        balls.get(k).color = colors[j + 1];
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                   /* // случайные блуждания - сдвигаем координаты шарика в случайную сторону
                    x += r.nextFloat() * 10 - 5;
                    y += r.nextFloat() * 10 - 5;
                    c.drawCircle(x,y,30,p);*/
                    // нужна пауза на каждом кадре
                    //обработаем исключение
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {}

                    holder.unlockCanvasAndPost(c);


            }
            if (!runFlag)
            {
                Canvas c = holder.lockCanvas();
                Paint p = new Paint();
                p.setColor(Color.WHITE);
                p.setTextSize(96);
                try {
                    sleep(1000);
                }
                catch (InterruptedException e) {}
                c.drawColor(Color.BLACK);
                c.drawText("Game over", 140, height/2, p);
                holder.unlockCanvasAndPost(c);
            }
        }


        //выключаем поток через флаг
        public void flagOff() {
            runFlag = false;
        }
    }



    public MySurfaceView(Context context, AttributeSet attrs)
    {
        // укажем программе ,что класс MySurfaceView будет обрабатывать поверхности через SurfaceView
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //surfaceCreated используем после создания поверхности, чтобы запустить поток отрисовки
        //holder = surfaceHolder;
        //получим координаты экрана
        width = getWidth();
        height = getHeight();
        for (int i = 0;i < 2;i++) {
            radius = 20 + (int) (Math.random() * 100);

            //рисуем шарики

            Balls circle = new Balls(radius + (int) (Math.random() * (width - (radius + 20))), radius + (int) (Math.random() * (height - (radius + 20))), radius, colors[(int)(Math.random() * 8)]);
            balls.add(circle);
        }
        //рисуем границы
        borders = new Borders((int) width/2, (int) height/2, Color.GREEN);

        //holder = surfaceHolder;
        thread = new DrawThread();
        thread.start();

        // убеждаемся, что поток запускается
        Log.d("mytag", "DrawThread is running");

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // при изменении конфигурации поверхности поток нужно перезапустить
        thread.flagOff();
        holder = surfaceHolder;
        thread = new DrawThread();
        thread.start();
    }

    // поверхность уничтожается - поток останавливаем
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

        thread.flagOff();
    }
}

package kr.co.company.work3;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

class Ball{
    int x, y, xInc = 1, yInc = 1;
    int diameter;
    static int WIDTH = 1080, HEIGHT = 1920;

    public Ball(int d){
        this.diameter = d;

        //볼의 위치를 랜덤하게 설정
        x = (int) (Math.random() * (WIDTH - d)+3);
        y = (int) (Math.random() * (HEIGHT - d)+3);

        //한번에 움직이는 거리도 랜덤하게 설정
        xInc = (int) (Math.random() * 5+1);
        yInc = (int) (Math.random() * 5+1);
    }

    // 여기서 공을 그린다.
    public void paint(Canvas g){
        Paint paint = new Paint();

        //벽에 부딪히면 반사하게 한다.
        if(x<0||x> (WIDTH - diameter))
            xInc = -xInc;
        if(y<0||y> (HEIGHT - diameter))
            yInc = -yInc;

        //볼의 좌표를 갱신하다.
        x += xInc;
        y += yInc;

        //볼을 화면에 그린다.
        paint.setColor(Color.RED);
        g.drawCircle(x,y,diameter,paint);

    }
}
class Square{
    int x, y, xInc = 1, yInc = 1;
    int diameter;
    static int WIDTH = 1080, HEIGHT = 1920;

    public Square(int d){
        this.diameter = d;

        //네모의 위치를 랜덤하게 설정
        x = (int) (Math.random() * (WIDTH - d)+3);
        y = (int) (Math.random() * (HEIGHT - d)+3);

        //한번에 움직이는 거리도 랜덤하게 설정
        xInc = (int) (Math.random() * 5+1);
        yInc = (int) (Math.random() * 5+1);
    }

    // 여기서 네모를 그린다.
    public void paint(Canvas g){
        Paint paint = new Paint();

        //벽에 부딪히면 반사하게 한다.
        if(x<0||x> (WIDTH - diameter))
            xInc = -xInc;
        if(y<0||y> (HEIGHT - diameter))
            yInc = -yInc;

        //네모의 좌표를 갱신하다.
        x += xInc;
        y += yInc;

        //네모를 화면에 그린다.
        paint.setColor(Color.BLUE);
        Rect Square = new Rect(x-diameter, y-diameter, x+diameter, y+diameter);
        g.drawRect(Square, paint);

    }
}
// 서피스 뷰 정의
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public Ball basket1[] = new Ball[10];
    public Square basket2[] = new Square[10];
    private MyThread thread;

    public MySurfaceView(Context context){
        super(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        thread = new MyThread(holder);

        // Ball 객체를 생성하여서 배열에 넣는다.
        for(int i = 0; i<10; i++) {
            basket1[i] = new Ball(20);
        }
        for(int j = 0; j<10; j++){
            basket2[j] = new Square(20);
        }
    }

    public MyThread getThread(){
        return thread;
    }

    public void surfaceCreated(SurfaceHolder holder){
        //스레드를 시작한다.
        thread.setRunning(true);

        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;

        // 스레드를 중지한다.
        thread.setRunning(false);
        while (retry){
            try{
                thread.join();
                retry = false;
            }catch (InterruptedException e){
            }
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("방향을 바꾸시겠습니까?");
            alertDialogBuilder.setPositiveButton("circle",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            for (int i = 0; i < 10; i++) {
                                basket1[i].xInc = basket1[i].xInc * -1;
                                basket1[i].yInc = basket1[i].yInc * -1;
                            }
                        }
                    });
            alertDialogBuilder.setNegativeButton("sqaure",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < 10; i++) {
                                basket2[i].xInc = basket2[i].xInc * -1;
                                basket2[i].yInc = basket2[i].yInc * -1;
                            }
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return super.onTouchEvent(event);
    }
    public class MyThread extends Thread {

        private boolean mRun = false;
        private SurfaceHolder mSurfaceHolder;

        public MyThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    c.drawColor(Color.BLACK);
                    synchronized (mSurfaceHolder) {
                        for (Ball b : basket1) {
                            b.paint(c);
                        }
                        for (Square s : basket2) {
                            s.paint(c);
                        }
                    }
                } finally {
                    if (c != null) {
                        //캔버스의 로킹을 푼다.
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                //try{Thread.sleep(100)} catch(InterruptedException e){}
            }
        }

        public void setRunning(boolean b) {
            mRun = b;
        }
    }
}

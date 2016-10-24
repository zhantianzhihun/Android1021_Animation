package cn.dayuren.android1021_animation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2016/10/21.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Thread th = new Thread(this);
    private SurfaceHolder sfh;
    private int screenHeight, screenWidth;
    private static final int mVISIBLE = 1, mINVISIBLE = 0, rightUp = 2, rightDown = 3, leftDown = 4, leftUp = 5;
    private static int animationFlag = rightUp; // 移动图片起初位于中间布局的左上方,刚开始准备向布局的右上方移动
    private static boolean occurFlag = true; //  设置浅颜色图片的出现标志, 初始为不出现
    private Canvas canvas;
    private static final int delayTime = 10;
    private Paint paint, transparentPaint;
    private Resources res;
    private Bitmap bmpDong, bmpJing_1, bmpJing_2, bmpJing_3, bmpJing_4;
    private int bmp_x, bmp_y;
    private static final float offsetValue = 2;
    private static float pic_1_location[] = {0, 0};
    private static float pic_2_location[] = {0, 0};
    private static float pic_3_location[] = {0, 0};
    private static float pic_4_location[] = {0, 0};
    private static float pic_Dong_location[] = {0, 0};

    public MySurfaceView(Context context) {
        super(context);
        res = this.getResources();
//        bmpDong = BitmapFactory.decodeResource(res, R.mipmap.000);
//        bmpJing_1 = BitmapFactory.decodeResource(res, R.mipmap.001);
//        bmpJing_2 = BitmapFactory.decodeResource(res, R.mipmap.002);
//        bmpJing_3 = BitmapFactory.decodeResource(res, R.mipmap.003);
//        bmpJing_4 = BitmapFactory.decodeResource(res, R.mipmap.004);

        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);

        transparentPaint = new Paint();
        transparentPaint.setStyle(Paint.Style.STROKE);  //设置空心
        transparentPaint.setAlpha(0);
        transparentPaint.setAntiAlias(true);

        setFocusable(true);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        screenHeight = this.getHeight();
        screenWidth = this.getWidth();
        bmp_x = bmpDong.getWidth();
        bmp_y = bmpDong.getHeight();
        pic_1_location[0] = screenWidth / 2 - bmp_x;
        pic_1_location[1] = screenHeight / 2 - bmp_y;
        pic_Dong_location[0] = screenWidth / 2 - bmp_x;
        pic_Dong_location[1] = screenHeight / 2 - bmp_y;
        pic_2_location[0] = screenWidth / 2;
        pic_2_location[1] = screenHeight / 2 - bmp_y;
        pic_3_location[0] = screenWidth / 2;
        pic_3_location[1] = screenHeight / 2;
        pic_4_location[0] = screenWidth / 2 - bmp_x;
        pic_4_location[1] = screenHeight / 2;

        th.start();
    }

    public void draw() {
        try {
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.WHITE);

            if (occurFlag == true && animationFlag == rightUp) {
                drawBitmap1(canvas, mVISIBLE);
                drawBitmap2(canvas, mINVISIBLE);
                drawBitmap3(canvas, mINVISIBLE);
                drawBitmap4(canvas, mINVISIBLE);
            } else if (occurFlag == false && animationFlag == rightUp) {
                drawBitmap1(canvas, mINVISIBLE);
                drawBitmap2(canvas, mVISIBLE);
                drawBitmap3(canvas, mVISIBLE);
                drawBitmap4(canvas, mVISIBLE);
            }
            if (occurFlag == true && animationFlag == rightDown) {
                drawBitmap2(canvas, mVISIBLE);
                drawBitmap1(canvas, mVISIBLE);
                drawBitmap3(canvas, mINVISIBLE);
                drawBitmap4(canvas, mINVISIBLE);
            } else if (occurFlag == false && animationFlag == rightDown) {
                drawBitmap1(canvas, mINVISIBLE);
                drawBitmap2(canvas, mINVISIBLE);
                drawBitmap3(canvas, mVISIBLE);
                drawBitmap4(canvas, mVISIBLE);
            }
            if (occurFlag == true && animationFlag == leftDown) {
                drawBitmap3(canvas, mVISIBLE);
                drawBitmap1(canvas, mVISIBLE);
                drawBitmap2(canvas, mVISIBLE);
                drawBitmap4(canvas, mINVISIBLE);
            } else if (occurFlag == false && animationFlag == leftDown) {
                drawBitmap3(canvas, mINVISIBLE);
                drawBitmap2(canvas, mINVISIBLE);
                drawBitmap1(canvas, mINVISIBLE);
                drawBitmap4(canvas, mVISIBLE);
            }
            if (occurFlag == true && animationFlag == leftUp) {
                drawBitmap4(canvas, mVISIBLE);
                drawBitmap2(canvas, mVISIBLE);
                drawBitmap3(canvas, mVISIBLE);
                drawBitmap1(canvas, mVISIBLE);
                if (pic_Dong_location[1] == pic_1_location[1]) {
                    occurFlag = false;         //如果移动的图片回归到左上角位置，则开始浅颜色图片消失的过程
                }
            } else if (occurFlag == false && animationFlag == leftUp) {
                drawBitmap4(canvas, mINVISIBLE);
                drawBitmap2(canvas, mINVISIBLE);
                drawBitmap3(canvas, mINVISIBLE);
                drawBitmap1(canvas, mINVISIBLE);
                if (pic_Dong_location[1] == pic_1_location[1]) {
                    occurFlag = true;         //如果移动的图片回归到左上角位置，则开始浅颜色图片出现的过程
                }
            }

            drawBitmapDong(canvas, mVISIBLE);

            canvas.save();
        } catch (Exception ex) {
        } finally {
            if (canvas != null) {
                sfh.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void cycle() {
        if (animationFlag == rightUp && pic_Dong_location[0] < pic_2_location[0]) {
            pic_Dong_location[0] += offsetValue;
        } else if (animationFlag == rightUp) {
            animationFlag = rightDown;  //接着向布局的右下方移动，即向下移动
        }
        if (animationFlag == rightDown && pic_Dong_location[1] < pic_3_location[1]) {
            pic_Dong_location[1] += offsetValue;
        } else if (animationFlag == rightDown) {
            animationFlag = leftDown;
        }
        if (animationFlag == leftDown && pic_Dong_location[0] > pic_4_location[0]) {
            pic_Dong_location[0] -= offsetValue;
        } else if (animationFlag == leftDown) {
            animationFlag = leftUp;
        }
        if (animationFlag == leftUp && pic_Dong_location[1] > pic_1_location[1]) {
            pic_Dong_location[1] -= offsetValue;
        } else if (animationFlag == leftUp) {
            animationFlag = rightUp;
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            draw();
            cycle();
            try {
                Thread.sleep(delayTime);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void drawBitmap1(Canvas canvas, int visibleOrNot) {
        if (visibleOrNot == 0) {
            canvas.drawBitmap(bmpJing_1, pic_1_location[0], pic_1_location[1], transparentPaint);
        } else if (visibleOrNot == 1) {
            canvas.drawBitmap(bmpJing_1, pic_1_location[0], pic_1_location[1], paint);
        }
    }

    private void drawBitmap2(Canvas canvas, int visibleOrNot) {
        if (visibleOrNot == 0) {
            canvas.drawBitmap(bmpJing_2, pic_2_location[0], pic_2_location[1], transparentPaint);
        } else if (visibleOrNot == 1) {
            canvas.drawBitmap(bmpJing_2, pic_2_location[0], pic_2_location[1], paint);
        }
    }

    private void drawBitmap3(Canvas canvas, int visibleOrNot) {
        if (visibleOrNot == 0) {
            canvas.drawBitmap(bmpJing_3, pic_3_location[0], pic_3_location[1], transparentPaint);
        } else if (visibleOrNot == 1) {
            canvas.drawBitmap(bmpJing_3, pic_3_location[0], pic_3_location[1], paint);
        }
    }

    private void drawBitmap4(Canvas canvas, int visibleOrNot) {
        if (visibleOrNot == 0) {
            canvas.drawBitmap(bmpJing_4, pic_4_location[0], pic_4_location[1], transparentPaint);
        } else if (visibleOrNot == 1) {
            canvas.drawBitmap(bmpJing_4, pic_4_location[0], pic_4_location[1], paint);
        }
    }

    private void drawBitmapDong(Canvas canvas, int visibleOrNot) {
        if (visibleOrNot == 0) {
            canvas.drawBitmap(bmpDong, pic_Dong_location[0], pic_Dong_location[1], transparentPaint);
        } else if (visibleOrNot == 1) {
            canvas.drawBitmap(bmpDong, pic_Dong_location[0], pic_Dong_location[1], paint);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }


}
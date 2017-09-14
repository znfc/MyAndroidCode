package linlin.com.luckypansurfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by zhaopenglin on 2017/9/13.
 */

public class LuckyPanView extends SurfaceView implements SurfaceHolder.Callback ,Runnable{


    private SurfaceHolder mSFHolder;    //定义SurfaceHolder的变量
    private Canvas mCanvas;    //与SurfaceHolder绑定的Canvas
    private Thread t;    //用于绘制的线程
    private boolean isRunning = false;    //线程的控制开关
    //抽奖的文字
    private String[] mStrs = new String[] {"单反相机","iPad","恭喜发财","iPhone","妹子一只","恭喜发财"};
    //每个盘块的颜色
    private int[] mColors = new int[] { 0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01 };
    //与文字对应的图片
    private int[] mImgs = new int[]{R.drawable.action,R.drawable.meizu,R.drawable.iphone
            ,R.drawable.moba,R.drawable.sports,R.drawable.other};

    private Bitmap[] mImgBitmap;    //与文字对用图片的bitmap
    private int mItemCount = 6;    //盘块的个数
    private RectF mRange = new RectF();    //绘制盘块的范围
    private int mRadius;    //圆的直径
    private Paint mArcPaint;    //绘制盘块的画笔
    private Paint mTextPaint;    //绘制盘块的画笔
    private double mSpeed;    //滚动的速度
    boolean isStart = true;    //是否在转
    private volatile float mStartAngle = 0;
    private boolean isShouldEnd;    //是否点击了停止
    private int mCenter;    //控件的中心位置
    //控件的padding，这里我们认为4个padding的值一致，以paddingleft为标准
    private int mPadding;
    //背景图的bitmap
    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.background);
    //文字的大小
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
            ,20,getResources().getDisplayMetrics());

    public LuckyPanView(Context context) {
        this(context,null);
    }

    //我们在构造中设置了Callback回调，然后通过成员变量，大家应该也能看得出来每个变量的作用，以及可能有的代码快。
    public LuckyPanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSFHolder = getHolder();
        mSFHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredWidth(),getMeasuredHeight());
        //获取圆形的直径
        mRadius = width - getPaddingLeft()- getPaddingRight();
        //padding值
        mPadding = getPaddingLeft();
        //中心点
        mCenter = width / 2;
        setMeasuredDimension(width,width);
    }

    //surfaceCreated我们初始化了绘制需要用到的变量，以及开启了线程。
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        Log.i(LOGTOOLS.ZHAO11,"surfaceCreated");
        //初始化绘制圆弧的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        //初始化绘制文字的画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(0xFFffffff);
        mTextPaint.setTextSize(mTextSize);
        //圆弧的绘制范围
        mRange = new RectF(getPaddingLeft(),getPaddingLeft(),
                mRadius+getPaddingLeft(),mRadius+getPaddingLeft());
        //初始化图片
        mImgBitmap = new Bitmap[mItemCount];
        for(int i=0; i<mItemCount;i++){
            mImgBitmap[i] = BitmapFactory.decodeResource(getResources(),mImgs[i]);
        }

        //开启线程
        isRunning = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i(LOGTOOLS.ZHAO11,"surfaceDestroyed");
        isRunning = false;
    }

    @Override
    public void run() {

        //不断的进行draw
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();

            try {
                if(end - start < 50){
                    Thread.sleep(50 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //可以看到我们的run里面调用了draw，和上面模版一致。
    //使用通过 mHolder.lockCanvas();获得我们的Canvas，然后就可以尽情的绘制了。
    private void draw(){
        try {
            //获得canvas
            mCanvas = mSFHolder.lockCanvas();
            if(mCanvas != null){
                //绘制背景图
                drawBg();

                //绘制每个块块，每个块块上的文本，每个块块上的图片
                float tmpAngle = mStartAngle;
                float sweepAngle = (float)(360/mItemCount);
                for(int i = 0; i < mItemCount; i++){
                    //绘制块块
                    mArcPaint.setColor(mColors[i]);
                    mCanvas.drawArc(mRange,tmpAngle,sweepAngle,true,mArcPaint);
                    //绘制文本
                    drawText(tmpAngle,sweepAngle,mStrs[i]);
                    //绘制Icon
                    drawIcon(tmpAngle,i);

                    tmpAngle += sweepAngle;
                }
                //如果mSpeed不等于0，则相当于在滚动
                mStartAngle += mSpeed;

                //点击停止时，设置mSpeed为递减，为0值转盘停止
                if(isShouldEnd){
                    mSpeed -= 1;
                }
                if(mSpeed <= 0){
                    mSpeed = 0;
                    isShouldEnd = false;
                }
                //根据当前旋转的mStartAngle计算当前滚动到的区域
                calInExactArea(mStartAngle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(mCanvas != null){
                mSFHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void calInExactArea(float mStartAngle) {
    }

    //绘制图片
    private void drawIcon(float startAngle, int i) {

        //设置图片的宽度为直径的1/8
        int imgWidth = mRadius / 8;
        float angle = (float) ((30 + startAngle)*(Math.PI/180));

        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));

        //确定绘制图片的位置
        Rect rect = new Rect(x - imgWidth /2 , y - imgWidth /2 , x + imgWidth /2 , y + imgWidth /2);
        mCanvas.drawBitmap(mImgBitmap[i] ,null,rect,null);
    }

    //绘制文本
    private void drawText(float startAngle, float sweepAngle, String mStr) {

        Path path = new Path();
        path.addArc(mRange,startAngle,sweepAngle);
        float textWidth = mTextPaint.measureText(mStr);
        //利用水平偏移让文字居中
        float hOffset = (float)(mRadius*Math.PI / mItemCount / 2 - textWidth / 2);//水平偏移
        float vOffset = mRadius / 2 / 6;//垂直偏移
        mCanvas.drawTextOnPath(mStr,path,hOffset,vOffset,mTextPaint);
        /*利用Path，添加入一个Arc，然后设置水平和垂直的偏移量，垂直偏移量就是当前Arc朝着圆心移动的距离；水平偏移量，
        就是顺时针去旋转，我们偏移了 (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);目的是为了文字居中。
        mRadius * Math.PI 是圆的周长；周长/ mItemCount / 2 是每个Arc的一半的长度；
        拿Arc一半的长度减去textWidth / 2，就把文字设置居中了。最后，用过path去绘制文本即可。*/
    }

    //根据当前旋转的mStartAngle计算当前滚动到的区域 绘制背景，不重要，完全为了美观
    private void drawBg(){

        mCanvas.drawColor(0xFFFFFFFF);
        mCanvas.drawBitmap(mBgBitmap,null,new Rect(mPadding/2 , mPadding/2,
                getMeasuredWidth() - mPadding/2,getMeasuredWidth() - mPadding/2),null);
    }

    //点击开始旋转
    public void luckyStart(int luckyIndex){
        //每项角度大小
        float angle = (float) (360 / mItemCount);
        //中奖角度范围（因为指针向上，所以水平第一项旋转到指针指向，需要旋转210-270）
        float from = 270 - (luckyIndex + 1)*angle;
        float to = from + angle;
        float targetFrom = 4 * 360 + from;

        float v1 = (float) (Math.sqrt(1*1+8*1*targetFrom) - 1)/2;
        float targetTo = 4*360+to;
        float v2 = (float)(Math.sqrt(1*1+8*1*targetTo) - 1);

        mSpeed = (float)(v1 + Math.random()*(v2 - v1));
        isShouldEnd =false;
    }

    public void luckyEnd(){
        mStartAngle = 0;
        isShouldEnd = true;
    }

    public  boolean isStart(){
        isStart = !isStart;
        return isStart;
    }

    public  boolean isShouldEnd(){
        return isShouldEnd;
    }
}

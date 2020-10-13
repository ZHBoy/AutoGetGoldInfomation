package com.metaapp.autogetgoldinfomation;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author zhou_hao
 * @date 2020/10/13
 * @description: 进度得金币的view
 * 线程安全的View，可直接在线程中更新进度
 */
public class RoundProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;
    /**
     * 背景圆的半径
     */
    private float backWidth;
    /**
     * 背景圆的颜色
     */
    private int backColor;
    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max = CommonDef.INSTANCE.getMaxTime();

    /**
     * 当前进度
     */
    private int progress = CommonDef.INSTANCE.getCurrentTime();
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;
    /**
     * 中心图片选择
     */
    private Drawable cententIcon;
    /**
     * 中心图片宽
     */
    private int iconWidth;
    /**
     * 中心图片高
     */
    private int iconHeigh;
    /**
     * 中心文字
     */
    private String textvalue;
    public static final int STROKE = 0;
    public static final int FILL = 1;
    private ValueAnimator valueAnimator;
    private SuccessListener listener;
    private AnimListener animlistener;
    private boolean isCancel = false;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.WHITE);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 28);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, false);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        backWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_backWidth, 60);
        backColor = mTypedArray.getColor(R.styleable.RoundProgressBar_backColor, Color.GREEN);
        cententIcon = mTypedArray.getDrawable(R.styleable.RoundProgressBar_centen_icon);
        iconWidth = mTypedArray.getInteger(R.styleable.RoundProgressBar_iconWidth, 34);
        iconHeigh = mTypedArray.getInteger(R.styleable.RoundProgressBar_iconHeigh, 34);
        textvalue = mTypedArray.getString(R.styleable.RoundProgressBar_textValue);
        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆
         */
        int centre = getWidth() / 2; //获取圆心的x坐标
        paint.setColor(backColor);//设置背景颜色
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centre, centre, centre, paint); //画出圆
        /**
         * 画出圆环
         */
        int radius = (int) (backWidth / 2 - roundWidth / 2) - 2; //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth - 2); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        /**
         * 画中心图片
         */
        Bitmap bitmap = ((BitmapDrawable) cententIcon).getBitmap();
        Bitmap newBitmap = changeBitmapSize(bitmap);
        canvas.drawBitmap(newBitmap, centre - newBitmap.getHeight() / 2, centre - newBitmap.getWidth() / 2, paint);

        /**
         * 画圆弧 ，画圆环的进度
         */

        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius - 1, centre - radius - 1, centre
                + radius + 1, centre + radius + 1);  //用于定义的圆弧的形状和大小的界限
        progress = getProgress();
        max = getMax();
        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 270, 360 * progress / max, false, paint);  //根据进度画圆弧
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //根据进度画圆弧
                break;
            }
        }
        /**
         * 画金币数
         */
        paint.setStrokeWidth(2);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        if (textIsDisplayable && style == STROKE) {
            float textWidth = paint.measureText(textvalue);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
            canvas.drawText(textvalue, centre - textWidth / 2, centre + textSize / 2, paint); //画出进度百分比
        } else {
            if (animlistener != null) {
                animlistener.onAnimEnd(0);
            }
        }
        this.setScaleX(1.0f);
        this.setScaleY(1.0f);

    }

    private Bitmap changeBitmapSize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //设置想要的大小
        int newWidth = DisplayUtil.dip2px(getContext(), iconWidth);
        int newHeight = DisplayUtil.dip2px(getContext(), iconHeigh);

        //计算压缩的比率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        //获取新的bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.getWidth();
        bitmap.getHeight();
        return bitmap;
    }

    public int getMax() {
        max = CommonDef.INSTANCE.getMaxTime();
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
        CommonDef.INSTANCE.setMaxTime(max);
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public int getProgress() {
        progress = CommonDef.INSTANCE.getCurrentTime();
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public void setProgress(int progress) {
        /*if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }*/
        if (progress > CommonDef.INSTANCE.getMaxTime()) {
            progress = CommonDef.INSTANCE.getMaxTime();
        }
        if (progress <= CommonDef.INSTANCE.getMaxTime()) {
            this.progress = progress;
            CommonDef.INSTANCE.setCurrentTime((int) progress);
            postInvalidate();
        }

    }

    public void setAnimEndListener(AnimListener listener) {
        this.animlistener = listener;
    }

    public ValueAnimator getPlayPauseAnim() {
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator = null;
        }
        valueAnimator = ValueAnimator.ofInt((int) getProgress(), getMax());
        valueAnimator.setDuration(getMax() - (int) getProgress());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
                CommonDef.INSTANCE.setCurrentTime((int) progress);
                RoundProgressBar.this.setProgress(progress);
                RoundProgressBar.this.invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                textIsDisplayable = false;
                setCententIcon(getResources().getDrawable(R.drawable.popover_gold));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isCancel) {
                    isCancel = false;
                    return;
                }
                setProgress(max);
                animlistener.onAnimStart();
                setTextvalue("");
                textIsDisplayable = true;
                setCententIcon(getResources().getDrawable(R.drawable.popover_gold));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isCancel = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return valueAnimator;
    }

    public void play() {
        getPlayPauseAnim();
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        valueAnimator.start();
    }

    public void pause() {
//        ValueAnimator playPauseAnim = getPlayPauseAnim();
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void clicked(Activity activity, SuccessListener listener) {
        this.listener = listener;
        /**动画结束前去请求接口**/
        clickedFinish();

    }

    int integrals = 10;

    public void clickedFinish() {
        this.setScaleX(1.0f);
        this.setScaleY(1.0f);
        integrals = 10;
        animlistener.onAnimEnd(integrals);
        setProgress(0);

//        ToastUtil.INSTANCE.showCustomToast("阅读奖励\n领取成功，财运币+" + integrals);
        setMax(CommonDef.INSTANCE.getMaxTime());
        textIsDisplayable = false;
        setCententIcon(getResources().getDrawable(R.drawable.popover_gold));
        CommonDef.INSTANCE.setCurrentTime(0);
        play();
        if (listener != null)
            listener.success();

    }

    public Drawable getCententIcon() {
        return cententIcon;
    }

    public void setCententIcon(Drawable cententIcon) {
        this.cententIcon = cententIcon;
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public String getTextvalue() {
        return textvalue;
    }

    public void setTextvalue(String textvalue) {
        this.textvalue = textvalue;
    }

    public interface SuccessListener {
        public void success();
    }

    public interface AnimListener {
        public void onAnimStart();

        public void onAnimEnd(int integrals);
    }
}

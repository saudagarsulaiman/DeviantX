package net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;

/**
 * 带有小尖角的直线指示器
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class TriangularPagerIndicator extends View implements IPagerIndicator {
    private List<PositionData> mPositionDataList;
    private Paint mPaint;
    private int mLineHeight;
    private int mLineColor;
    private int mTriangleHeight;
    private int mTriangleWidth;
    private boolean mReverse;
    private float mYOffset;

    private Path mPath = new Path();
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private float mAnchorX;

    public TriangularPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLineHeight = UIUtil.dip2px(context, 5);
        mTriangleWidth = UIUtil.dip2px(context, 50);
        mTriangleHeight = UIUtil.dip2px(context, 15);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mLineColor);
        mPaint.setDither(true);                    // set the dither to true
        mPaint.setStyle(Paint.Style.FILL);       // set to FILL
        mPaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        mPaint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        mPaint.setPathEffect(new CornerPathEffect(20));   // set the path effect when they join.
        mPaint.setAntiAlias(true);
        if (mReverse) {
            canvas.drawRect(0, getHeight() - mYOffset - mTriangleHeight, getWidth(), getHeight() - mYOffset - mTriangleHeight + mLineHeight, mPaint);
        } else {
            canvas.drawRect(0, getHeight() - mLineHeight - mYOffset, getWidth(), getHeight() - mYOffset, mPaint);
            //  canvas.drawCircle(getHeight() - mLineHeight - mYOffset, getHeight(), 10, mPaint);
        }
        mPath.reset();
        if (mReverse) {
            mPath.moveTo(mAnchorX - mTriangleWidth / 2, getHeight() - mYOffset - mTriangleHeight);
            mPath.lineTo(mAnchorX, getHeight() - mYOffset);
            mPath.lineTo(mAnchorX + mTriangleWidth / 2, getHeight() - mYOffset - mTriangleHeight);
        } else {
            mPath.moveTo(mAnchorX - mTriangleWidth / 2, getHeight() - mYOffset);
            mPath.lineTo(mAnchorX, getHeight() - mTriangleHeight - mYOffset);
            mPath.lineTo(mAnchorX + mTriangleWidth / 2, getHeight() - mYOffset);
        }
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        // 计算颜色
        if (position == 0) {
            mLineColor = Color.parseColor("#ed871a");
        } else if (position == 1) {
            mLineColor = Color.parseColor("#01C6DB");
        } else if (position == 2) {
            mLineColor = Color.parseColor("#8B00DC");
        } else if (position == 3) {
            mLineColor = Color.parseColor("#64182A");
        }

        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算锚点位置
        PositionData current = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position);
        PositionData next = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position + 1);

        float leftX = current.mLeft + (current.mRight - current.mLeft) / 2;
        float rightX = next.mLeft + (next.mRight - next.mLeft) / 2;

        mAnchorX = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);

        invalidate();
    }

    public void setInvalidate() {
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    public int getLineHeight() {
        return mLineHeight;
    }

    public void setLineHeight(int lineHeight) {
        mLineHeight = lineHeight;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
    }

    public int getTriangleHeight() {
        return mTriangleHeight;
    }

    public void setTriangleHeight(int triangleHeight) {
        mTriangleHeight = triangleHeight;
    }

    public int getTriangleWidth() {
        return mTriangleWidth;
    }

    public void setTriangleWidth(int triangleWidth) {
        mTriangleWidth = triangleWidth;
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public boolean isReverse() {
        return mReverse;
    }

    public void setReverse(boolean reverse) {
        mReverse = reverse;
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float yOffset) {
        mYOffset = yOffset;
    }
}

/*
 * Copyright (C) 2015 Sumy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sumy.backgroundprogressdrawable;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import java.util.Arrays;

/**
 * BackgroundProgressDrawable
 * <p/>
 * 这个 Drawable 将会帮助你将背景填充为一个进度条，进度条可以根据进度值改变颜色和大小。
 * <p/>
 *
 * @author Sumy <sunmingjian8@gmail.com>
 */
public class BackgroundProgressDrawable extends Drawable {

    /**
     * 正常模式（水平），进度条的长度会随着进度的变化而变化
     */
    public static final int MODE_NORMAL_HORIZONTAL = 0;
    /**
     * 正常模式（垂直），进度条的高度会随着进度的变化而变化
     */
    public static final int MODE_NORMAL_VERTICAL = 1;
    /**
     * 全屏模式，进度条将会充满整个背景，只有颜色进行变化
     */
    public static final int MODE_FULL = 2;

    /**
     * Debug调试标记
     */
    public static final String TAG = "BackgroundProgressDrawable";
    /**
     * 画笔
     */
    private final Paint mPaint = new Paint();
    /**
     * 进度条绘图的范围
     */
    private final RectF mDrawRectF = new RectF();

    /**
     * 当前进度条的进度
     */
    private float mProgress = 0;
    /**
     * 动画进行到的进度
     */
    private float mProgressPer = 0;
    /**
     * 进度条的最大值
     */
    private float mMax;

    /**
     * 进度条模式
     */
    private int mMode = MODE_NORMAL_HORIZONTAL;
    /**
     * 进度条动画持续时间
     */
    private int mDuration;
    /**
     * 进度条颜色选取器
     */
    private ColorPicker mColorPicker;

    /**
     * 创建一个新的对象，不建议使用构造函数，请使用Builder对象
     *
     * @param mode        显示模式
     * @param max         最大值
     * @param colorPicker 颜色选择器
     * @param alpha       透明度
     * @param duration    动画持续时间
     */
    private BackgroundProgressDrawable(int mode, float max, ColorPicker colorPicker, int alpha, int duration) {
        this.mMode = mode;
        this.mMax = max;
        this.mColorPicker = colorPicker;
        this.mDuration = duration;
        this.mPaint.setAlpha(alpha);
    }


    @Override
    public void draw(Canvas canvas) {

        // 设置当前画笔的颜色
        mPaint.setColor(mColorPicker.getColor(1.0f * mProgressPer / mMax));

        // 根据模式设置绘图的区域
        Rect bounds = getBounds();
        if (mMode == MODE_NORMAL_HORIZONTAL) {
            mDrawRectF.set(bounds.left, bounds.top, bounds.left + 1.0f * (bounds.right - bounds.left) / mMax * mProgressPer, bounds.bottom);
        } else if (mMode == MODE_NORMAL_VERTICAL) {
            mDrawRectF.set(bounds.left, bounds.bottom + 1.0f * (bounds.top - bounds.bottom) / mMax * mProgressPer, bounds.right, bounds.bottom);
        } else if (mMode == MODE_FULL) {
            mDrawRectF.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        }

        // 绘制进度条
        canvas.drawRect(mDrawRectF, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return 1 - mPaint.getAlpha();
    }

    /**
     * 获取当前的进度值
     *
     * @return 当前进度值
     */
    public float getProgress() {
        return mProgress;
    }

    /**
     * 设置进度条的进度值
     *
     * @param progress 进度条的进度值
     */
    public void setProgress(float progress) {
        this.mProgress = progress;
        // 将进度值限定在 [0, mMax] 之间
        if (this.mProgress > this.mMax) {
            this.mProgress = this.mMax;
        }
        if (this.mProgress < 0) {
            this.mProgress = 0;
        }
        // 开始颜色变换动画
        startAnimation();
    }

    /**
     * 直接设置进度值，不播放过渡动画
     *
     * @param progress 进度条进度值
     */
    public void setProgressNative(float progress) {
        this.mProgressPer = progress;
        this.mProgress = progress;
        invalidateSelf();
    }

    /**
     * 设置进度条的最大值
     *
     * @param max 进度条最大值
     */
    public void setMax(float max) {
        this.mMax = max;
    }

    /**
     * 获取进度条的最大值
     *
     * @return
     */
    public float getMax() {
        return this.mMax;
    }

    /**
     * 设置动画持续时间
     *
     * @param duration 动画的持续时间
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    /**
     * 获取动画持续时间
     *
     * @return 动画的持续时间
     */
    public int getDuration() {
        return this.mDuration;
    }

    /**
     * 颜色变换动画
     */
    private void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mProgressPer, mProgress);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 设置动画进度条
                mProgressPer = (float) animation.getAnimatedValue();
                // 重绘自己
                invalidateSelf();
            }
        });
        valueAnimator.setDuration(mDuration).start();
    }

    /**
     * 颜色选择器类，根据进度选取相应的颜色
     * <p/>
     * 颜色选择器需要提供两组对应的数组（进度值数组和颜色值数组），进度值数组与颜色值数组一一对应，表示进行到当前的进度需要选择当期的颜色值，进度值之间的颜色通过线性计算获得。
     * <p/>
     * 进度值数组为 [0.0f..1.0f] 之间的一串数字，表示当前进度值占进度条最大值的比，引入进度值数组时进度值会按照从大到小重新排序。<p/>
     * 颜色值数组为 与进度值数组个数相对应的颜色值。
     * <p/>
     *
     * @author Sumy <sunmingjian8@gmail.com>
     */
    public static class ColorPicker {
        /**
         * 进度值数组
         */
        private float progress[];
        /**
         * 颜色值数组
         */
        private int color[];

        /**
         * 通过进度值数组和颜色值数组创建一个颜色选择器
         *
         * @param progress 进度值数组，不能为空，长度不能为0
         * @param color    颜色值数组，不能为空，长度不能为0
         */
        public ColorPicker(float progress[], int color[]) {
            if (progress == null || color == null) {
                throw new IllegalArgumentException();
            }
            if (progress.length != color.length) {
                throw new IllegalArgumentException();
            }
            if (progress.length == 0) {
                throw new IllegalArgumentException();
            }
            Arrays.sort(progress); // 保证数值从小到大
            // Arrays.sort(color);
            this.progress = progress;
            this.color = color;
        }

        /**
         * 通过进度值获取颜色值
         *
         * @param progress 进度值
         * @return 获取到的颜色值
         */
        public int getColor(float progress) {
            // 如果只有一个颜色值，返回
            if (this.progress.length == 1) {
                return this.color[0];
            }

            // 如果进度值超出范围，返回范围边缘的颜色值
            if (progress <= this.progress[0]) {
                return this.color[0];
            }
            if (progress >= this.progress[this.progress.length - 1]) {
                return this.color[this.color.length - 1];
            }

            // 枚举寻找进度值的范围，通过在范围内线性计算颜色值
            for (int i = 0; i < this.progress.length - 1; i++) {
                if (progress >= this.progress[i] && this.progress[i + 1] >= progress) {
                    float ratio = (progress - this.progress[i]) / (this.progress[i + 1] - this.progress[i]);
                    int r = (int) (Color.red(this.color[i]) + ratio * (Color.red(this.color[i + 1]) - Color.red(this.color[i])));
                    int g = (int) (Color.green(this.color[i]) + ratio * (Color.green(this.color[i + 1]) - Color.green(this.color[i])));
                    int b = (int) (Color.blue(this.color[i]) + ratio * (Color.blue(this.color[i + 1]) - Color.blue(this.color[i])));
                    return Color.rgb(r, g, b);
                }
            }
            return Color.BLACK;
        }
    }

    /**
     * 创建器类，帮助管理 BackgroundProgressDrawable 的创建工作
     *
     * @author Sumy <sunmingjian8@gmail.com>
     */
    public static class Builder {
        /**
         * 进度条的最大值
         */
        private float max = 100;
        /**
         * 进度条的模式
         */
        private int mode = MODE_NORMAL_HORIZONTAL;
        /**
         * 进度条的颜色选择器
         */
        private ColorPicker colorPicker = new ColorPicker(new float[]{0.0f, 0.5f, 1.0f}, new int[]{Color.RED, Color.YELLOW, Color.GREEN});
        /**
         * 进度条动画持续时间
         */
        private int duration = 1000;
        /**
         * 进度条的透明度
         */
        private int alpha = 255;

        public Builder() {
        }

        /**
         * 设置进度条的最大值，默认100
         *
         * @param max 最大值
         * @return 创建器对象
         */
        public Builder setMax(int max) {
            this.max = max;
            return this;
        }

        /**
         * 设置进度条的模式，默认 水平正常
         *
         * @param mode 进度条模式
         * @return 创建器对象
         */
        public Builder setMode(int mode) {
            this.mode = mode;
            return this;
        }

        /**
         * 设置颜色选择器，默认 [0.0f-红, 0.5f-黄, 1.0f-绿]
         *
         * @param colorPicker 颜色选择器
         * @return 创建器对象
         */
        public Builder setColorPicker(ColorPicker colorPicker) {
            this.colorPicker = colorPicker;
            return this;
        }

        /**
         * 设置动画持续时间，默认 1000毫秒
         *
         * @param duration 动画持续时间
         * @return 创建器对象
         */
        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        /**
         * 设置透明度，默认 不透明
         *
         * @param alpha 透明度
         * @return 创建器对象
         */
        public Builder setAlpha(int alpha) {
            this.alpha = alpha;
            return this;
        }

        /**
         * 根据设定值创建一个对象
         *
         * @return 被创建的对象
         */
        public BackgroundProgressDrawable create() {
            return new BackgroundProgressDrawable(mode, max, colorPicker, alpha, duration);
        }
    }
}

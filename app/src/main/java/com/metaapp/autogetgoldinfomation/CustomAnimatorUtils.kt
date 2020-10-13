package com.metaapp.autogetgoldinfomation

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.*

/**
 * @author zhou_hao
 * @date 2020/10/13
 * @description: 自定义动画类
 */
object CustomAnimatorUtils {

    /**
     * 抖动动画 X轴方向抖动
     */
    fun shakeAnimatorX(view: View): ObjectAnimator {
        val delta = view.resources.getDimensionPixelOffset(R.dimen.dp_8)

        val pvhTranslateX = PropertyValuesHolder.ofKeyframe(
            View.TRANSLATION_X,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.10f, (-delta).toFloat()),
            Keyframe.ofFloat(.26f, delta.toFloat()),
            Keyframe.ofFloat(.42f, (-delta).toFloat()),
            Keyframe.ofFloat(.58f, delta.toFloat()),
            Keyframe.ofFloat(.74f, (-delta).toFloat()),
            Keyframe.ofFloat(.90f, delta.toFloat()),
            Keyframe.ofFloat(1f, 0f)
        )
        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).setDuration(1000)
    }


    /**
     * 抖动动画 Y轴方向抖动
     */
    fun shakeAnimatorY(view: View): ObjectAnimator {
        val delta = view.resources.getDimensionPixelOffset(R.dimen.dp_8)

        val pvhTranslateX = PropertyValuesHolder.ofKeyframe(
            View.TRANSLATION_Y,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.10f, (-delta).toFloat()),
            Keyframe.ofFloat(.26f, delta.toFloat()),
            Keyframe.ofFloat(.42f, (-delta).toFloat()),
            Keyframe.ofFloat(.58f, delta.toFloat()),
            Keyframe.ofFloat(.74f, (-delta).toFloat()),
            Keyframe.ofFloat(.90f, delta.toFloat()),
            Keyframe.ofFloat(1f, 0f)
        )
        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).setDuration(1000)
    }


    /**
     * 抖动动画
     */
    fun shakeAnimator(view: View): ObjectAnimator {
        return shakeAnimator(view, 1f)
    }

    private fun shakeAnimator(view: View, shakeFactor: Float): ObjectAnimator {

        val pvhScaleX = PropertyValuesHolder.ofKeyframe(
            View.SCALE_X,
            Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(.1f, .9f),
            Keyframe.ofFloat(.2f, .9f),
            Keyframe.ofFloat(.3f, 1.1f),
            Keyframe.ofFloat(.4f, 1.1f),
            Keyframe.ofFloat(.5f, 1.1f),
            Keyframe.ofFloat(.6f, 1.1f),
            Keyframe.ofFloat(.7f, 1.1f),
            Keyframe.ofFloat(.8f, 1.1f),
            Keyframe.ofFloat(.9f, 1.1f),
            Keyframe.ofFloat(1f, 1f)
        )

        val pvhScaleY = PropertyValuesHolder.ofKeyframe(
            View.SCALE_Y,
            Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(.1f, .9f),
            Keyframe.ofFloat(.2f, .9f),
            Keyframe.ofFloat(.3f, 1.1f),
            Keyframe.ofFloat(.4f, 1.1f),
            Keyframe.ofFloat(.5f, 1.1f),
            Keyframe.ofFloat(.6f, 1.1f),
            Keyframe.ofFloat(.7f, 1.1f),
            Keyframe.ofFloat(.8f, 1.1f),
            Keyframe.ofFloat(.9f, 1.1f),
            Keyframe.ofFloat(1f, 1f)
        )

        val pvhRotate = PropertyValuesHolder.ofKeyframe(
            View.ROTATION,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.1f, -3f * shakeFactor),
            Keyframe.ofFloat(.2f, -3f * shakeFactor),
            Keyframe.ofFloat(.3f, 3f * shakeFactor),
            Keyframe.ofFloat(.4f, -3f * shakeFactor),
            Keyframe.ofFloat(.5f, 3f * shakeFactor),
            Keyframe.ofFloat(.6f, -3f * shakeFactor),
            Keyframe.ofFloat(.7f, 3f * shakeFactor),
            Keyframe.ofFloat(.8f, -3f * shakeFactor),
            Keyframe.ofFloat(.9f, 3f * shakeFactor),
            Keyframe.ofFloat(1f, 0f)
        )

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).setDuration(1200)
    }

    private fun fadeIn(view: View, startAlpha: Float, endAlpha: Float, duration: Long) {
        if (view.visibility == View.VISIBLE) return

        view.visibility = View.VISIBLE
        val animation = AlphaAnimation(startAlpha, endAlpha)
        animation.duration = duration
        view.startAnimation(animation)
    }

    /**
     * 闪动的动画
     */
    fun scaleShandong(view: View?) {
        val animation = ScaleAnimation(
            1f,
            1.2f,
            1f,
            1.2f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.duration = 500
        animation.repeatCount = 5
        animation.repeatMode = Animation.REVERSE
        view?.startAnimation(animation)
    }

}
package com.metaapp.autogetgoldinfomation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

/**
 * @author zhou_hao
 * @date 2020/10/13
 * @description: 视频咨询类获取金币的demo
 */
class MainActivity : AppCompatActivity() {

    var canStartTime = true
    var time: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nopeAnimator = CustomAnimatorUtils.shakeAnimator(countdown)
        nopeAnimator.repeatCount = ValueAnimator.INFINITE
        countdown.setAnimEndListener(object : RoundProgressBar.AnimListener {
            override fun onAnimEnd(integrals: Int) {
                nopeAnimator.cancel()
                if (integrals == 0) {
                    nopeAnimator.cancel()
                    return
                }
                award_anim.visibility = View.VISIBLE
                award_anim.text = "+$integrals"
                val animator =
                    ObjectAnimator.ofFloat(
                        award_anim,
                        "TranslationY",
                        -DisplayUtil.dip2px(this@MainActivity, 80f) + 0.5f
                    ).setDuration(2000)
                val alpha = ObjectAnimator.ofFloat(award_anim, "alpha", 1.0f, 0f)
                    .setDuration(2000)
                val set = AnimatorSet()
                set.play(animator).with(alpha)
                set.start()
                set.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        award_anim.visibility = View.GONE
                        award_anim.translationY = 0f
                        award_anim.alpha = 1.0f
                        nopeAnimator.cancel()
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {

                    }

                })
            }

            override fun onAnimStart() {
                nopeAnimator.start()
                countdown.performClick()
            }
        })
        countdown.setOnClickListener {

            if (countdown.progress.toInt() == countdown.max) {
                countdown.clicked(this, null)
            } else {
                Toast.makeText(this, "倒计时结束后才能的到金币哦", Toast.LENGTH_SHORT)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        countdown.play()
    }

    override fun onPause() {
        super.onPause()
        countdown.pause()
    }

    /**
     * 当多少秒不与页面交互时停止倒计时(简单的逻辑，根据业务自己实现)
     */
//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        val clickY = ev!!.y
//        val h = DisplayUtil.getScreenHeight(this) * 0.8
//        if (clickY < h) {
//            if (ev.action == MotionEvent.ACTION_MOVE) {
//                countdown.pause()
//
//            } else if (ev.action == MotionEvent.ACTION_UP) {
//                countdown.play()
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//
//    }
}
package aris.kots.nameanimalplantobject.ui

import android.animation.*
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintSet
import aris.kots.nameanimalplantobject.R
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Build
import android.util.Log
import android.view.animation.*
import android.widget.Toast
import android.animation.Animator
import android.animation.AnimatorListenerAdapter




class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName + "!"

    private var constraintsetdefault = ConstraintSet()
    private var constraintsetNew = ConstraintSet()
    private var defaultLayout = true
    private var drawFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initClickListeners()

        constraintsetdefault.clone(constraintLayout)
        constraintsetNew.clone(this, R.layout.activity_main_op)

        fingerButton.setOnClickListener {

            TransitionManager.beginDelayedTransition(constraintLayout)
            placeholder.setContentId(emptyView.id)

            if(defaultLayout){
                constraintsetNew.applyTo(constraintLayout)
                defaultLayout=false
            }
            else{
                constraintsetdefault.applyTo(constraintLayout)
                defaultLayout=true
            }
        }
        liloImage.setOnClickListener {  startAnimation()}

        // DRAW VIEW

        liloImage.afterMeasured {
            drawFinished = true
            startAnimation()
        }
    }

    private fun startAnimation() {

        if(!drawFinished)
            return

        val floatingAnimationSet = AnimationSet(true)
//        floatingAnimationSet.addAnimation(AnimationUtils.loadAnimation(this,
//                R.anim.floating_animation))
        floatingAnimationSet.addAnimation(AnimationUtils.loadAnimation(this,
                R.anim.floating_animation_translate))
        liloImage.animation = floatingAnimationSet

        liloImage.startAnimation(floatingAnimationSet)

//        val valueAnimator = ValueAnimator.ofFloat(0f, liloImage.measuredWidth.toFloat()/4)
//        val valueAnimator2 = ValueAnimator.ofFloat( liloImage.measuredWidth.toFloat()/4,0f)
//        valueAnimator2.addUpdateListener {
//            liloImage.translationX= it.animatedValue as Float
//
//
//        }
//        valueAnimator.addUpdateListener {
//            liloImage.translationX = it.animatedValue as Float
//
//        }
//        valueAnimator.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationRepeat(p0: Animator?) {
//            }
//
//            override fun onAnimationCancel(p0: Animator?) {
//            }
//
//            override fun onAnimationStart(p0: Animator?) {
//            }
//
//            override fun onAnimationEnd(animation: Animator) {
//                valueAnimator2.start()
//            }
//        })
//        valueAnimator2.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationRepeat(p0: Animator?) {
//            }
//
//            override fun onAnimationCancel(p0: Animator?) {
//            }
//
//            override fun onAnimationStart(p0: Animator?) {
//            }
//
//            override fun onAnimationEnd(animation: Animator) {
//                valueAnimator.start()
//            }
//        })
//        valueAnimator.interpolator = LinearInterpolator()
//        valueAnimator.duration = 5000
//        valueAnimator2.interpolator = LinearInterpolator()
//        valueAnimator2.duration = 2000
//
//        valueAnimator.start()
    }

    private fun initClickListeners() {
        setClickListener(marioImage)
        setClickListener(mickeyImage)
        setClickListener(pikatsouImage)
//        setClickListener(liloImage)
        setClickListener(worioImage)
        setClickListener(idiotImage)
    }

    private fun setClickListener(view: View) {
        view.setOnClickListener {
            if(defaultLayout)
                displayView(view)
        }
    }

    private fun displayView(view: View) {
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(0.5f)

        TransitionManager.beginDelayedTransition(constraintLayout, transition)
        if (placeholder.content == view)
            placeholder.setContentId(emptyView.id)
        else
            placeholder.setContentId(view.id)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    inline fun <T: View> T.afterMeasured(crossinline f: T.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }
}

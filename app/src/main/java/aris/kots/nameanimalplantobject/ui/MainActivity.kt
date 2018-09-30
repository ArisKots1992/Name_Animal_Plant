package aris.kots.nameanimalplantobject.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import aris.kots.nameanimalplantobject.R
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName + "!"

    private var constraintsetdefault = ConstraintSet()
    private var constraintsetNew = ConstraintSet()
    private var defaultLayout = true

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

    }

    private fun initClickListeners() {
        setClickListener(marioImage)
        setClickListener(mickeyImage)
        setClickListener(pikatsouImage)
        setClickListener(liloImage)
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

}

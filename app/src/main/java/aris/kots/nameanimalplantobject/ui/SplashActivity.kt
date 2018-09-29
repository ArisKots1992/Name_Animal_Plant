package aris.kots.nameanimalplantobject.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import aris.kots.nameanimalplantobject.R
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lol.text="Aristotelis"
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}

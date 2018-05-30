package com.tophatch.fullscreenperf

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openGlSwitch.setOnCheckedChangeListener { _, isChecked ->
            glSurfaceView.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }
}

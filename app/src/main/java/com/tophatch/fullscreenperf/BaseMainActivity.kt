package com.tophatch.fullscreenperf

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}

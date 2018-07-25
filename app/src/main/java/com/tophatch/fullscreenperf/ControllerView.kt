package com.tophatch.fullscreenperf

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View

class ControllerView(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    init {
        View.inflate(context, R.layout.activity_main_content, this)
    }
}

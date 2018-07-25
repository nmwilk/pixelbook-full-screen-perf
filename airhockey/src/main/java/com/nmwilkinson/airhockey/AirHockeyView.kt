package com.nmwilkinson.airhockey

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class AirHockeyView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val radius = context.resources.getDimension(R.dimen.ball_radius)

    private lateinit var xBounds: IntRange
    private lateinit var yBounds: IntRange

    private val physics = BasicPhysics()

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            physics.resetBall(e.x, e.y)
            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            physics.resetBall(e2.x, e2.y)
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            physics.flingBall(velocityX / 1000f, velocityY / 1000f)
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }
    private val gestureDetector = GestureDetector(context, gestureListener)

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.DKGRAY
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean =
            gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        physics.resetBall(w / 2f, h / 2f)

        xBounds = 0..width
        yBounds = 0..height
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        physics.updatePosition(xBounds, yBounds)

        canvas?.drawCircle(physics.ballX, physics.ballY, radius, paint)

        invalidate()
    }
}
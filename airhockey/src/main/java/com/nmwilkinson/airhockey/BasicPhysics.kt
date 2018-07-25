package com.nmwilkinson.airhockey

import android.graphics.PointF
import android.os.SystemClock

class BasicPhysics {
    val ballX: Float
        get() {
            return ballPos.x
        }

    val ballY: Float
        get() {
            return ballPos.y
        }

    fun resetBall(x: Float, y: Float) {
        ballPos.set(x, y)
        flingVelocity = null
    }

    private var flingTime: Long = 0


    fun flingBall(vx: Float, vy: Float) {
        flingVelocity = PointF(vx, vy)
        flingTime = time()
        prevTime = time()
    }

    fun updatePosition(xBounds: IntRange, yBounds: IntRange) {
        flingVelocity?.let { vel ->
            val now = time()
            val duration = now - prevTime

            if (ballPos.x + vel.x * duration !in xBounds) {
                vel.x = -vel.x
            }

            if (ballPos.y + vel.y * duration !in yBounds) {
                vel.y = -vel.y
            }

            ballPos.x += vel.x * duration
            ballPos.y += vel.y * duration

            prevTime = now
        }
    }

    private fun time() = SystemClock.uptimeMillis()

    private var prevTime: Long = 0
    private val ballPos = PointF()
    private var flingVelocity: PointF? = null
}
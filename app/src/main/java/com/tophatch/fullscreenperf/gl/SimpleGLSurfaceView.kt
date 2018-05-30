package com.tophatch.fullscreenperf.gl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class SimpleGLSurfaceView(context: Context, attributeSet: AttributeSet) : GLSurfaceView(context, attributeSet) {
    private val renderer: GLSurfaceView.Renderer

    init {
        setEGLContextClientVersion(3)

        renderer = SimpleGLRenderer()

        setRenderer(renderer)
    }

    class SimpleGLRenderer : GLSurfaceView.Renderer {
        private var triangle: Triangle? = null

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES30.glClearColor(1f, 1f, 1f, 1f)
            triangle = Triangle()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)
        }

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

            triangle?.draw()
        }
    }

    class Triangle {

        private val vertexBuffer: FloatBuffer

        // Set color with red, green, blue and alpha (opacity) values
        internal var color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1f)

        private val program: Int

        init {
            // initialize vertex byte buffer for shape coordinates
            val bb = ByteBuffer.allocateDirect(
                    // (number of coordinate values * 4 bytes per float)
                    triangleCoords.size * 4)
            // use the device hardware's native byte order
            bb.order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            vertexBuffer = bb.asFloatBuffer()
            // add the coordinates to the FloatBuffer
            vertexBuffer.put(triangleCoords)
            // set the buffer to read the first coordinate
            vertexBuffer.position(0)

            val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode)
            val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode)

            // create empty OpenGL ES Program
            program = GLES30.glCreateProgram()

            // add the vertex shader to program
            GLES30.glAttachShader(program, vertexShader)

            // add the fragment shader to program
            GLES30.glAttachShader(program, fragmentShader)

            // creates OpenGL ES program executables
            GLES30.glLinkProgram(program)
        }

        private var colorHandle: Int = 0
        private var positionHandle: Int = 0

        private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX

        private val vertexStride = COORDS_PER_VERTEX * 4

        fun draw() {
            // Add program to OpenGL ES environment
            GLES20.glUseProgram(program)

            // get handle to vertex shader's vPosition member
            positionHandle = GLES30.glGetAttribLocation(program, "vPosition")

            // Enable a handle to the triangle vertices
            GLES30.glEnableVertexAttribArray(positionHandle)

            // Prepare the triangle coordinate data
            GLES30.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                    GLES30.GL_FLOAT, false,
                    vertexStride, vertexBuffer)

            // get handle to fragment shader's vColor member
            colorHandle = GLES30.glGetUniformLocation(program, "vColor")

            // Set color for drawing the triangle
            GLES30.glUniform4fv(colorHandle, 1, color, 0)

            // Draw the triangle
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES30.glDisableVertexAttribArray(positionHandle)
        }

        companion object {

            // number of coordinates per vertex in this array
            internal const val COORDS_PER_VERTEX = 3
            internal var triangleCoords = floatArrayOf(// in counterclockwise order:
                    0.0f, 0.622008459f, 0.0f, // top
                    -0.5f, -0.311004243f, 0.0f, // bottom left
                    0.5f, -0.311004243f, 0.0f  // bottom right
            )

            private const val vertexShaderCode = "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}"

            private const val fragmentShaderCode = (
                    "precision mediump float;" +
                            "uniform vec4 vColor;" +
                            "void main() {" +
                            "  gl_FragColor = vColor;" +
                            "}")

            fun loadShader(type: Int, shaderCode: String): Int {
                // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
                // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
                val shader = GLES20.glCreateShader(type)

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)

                return shader
            }
        }
    }
}

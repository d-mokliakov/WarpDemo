package localhost.dmokliakov.warpdemo

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glViewport
import android.opengl.GLES30.GL_RG
import android.opengl.GLES30.GL_RG16F
import android.opengl.GLSurfaceView
import localhost.dmokliakov.warpdemo.ext.uniform1f
import localhost.dmokliakov.warpdemo.ext.uniform4f
import localhost.dmokliakov.warpdemo.gl.GlFrameBuffer
import localhost.dmokliakov.warpdemo.gl.GlProgram
import localhost.dmokliakov.warpdemo.gl.GlTexture
import localhost.dmokliakov.warpdemo.gl.GlTexturedPlaneMesh
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer(private val context: Context) : GLSurfaceView.Renderer {

    private val newUvProgram = GlProgram()
    private val mapUvProgram = GlProgram()
    private val warpProgram = object : GlProgram() {
        val gesture = uniform4f("u_Gesture") // x, y, dx, dy
        val size = uniform1f("u_Size").also {
            it.value = 0.1f
        }
        val pressure = uniform1f("u_Pressure").also {
            it.value = 0.5f
        }
    }
    private val drawTextureProgram = GlProgram()

    private val mesh = GlTexturedPlaneMesh()
    private val originalTexture = GlTexture()
    private val uvTexture1 = GlTexture()
    private val uvTexture2 = GlTexture()
    private val frameBuffer = GlFrameBuffer()
    private var screenWidth = 1
    private var screenHeight = 1

    fun onScroll(x: Float, y: Float, dx: Float, dy: Float) {
        warpProgram.gesture.value = floatArrayOf(x, y, dx, dy)
    }

    fun setBrushSize(size: Float) {
        warpProgram.size.value = size
    }

    fun setBrushPressure(pressure: Float) {
        warpProgram.pressure.value = pressure
    }

    var uvPreviewMode = false

    fun resetUV() {
        frameBuffer.use(uvTexture1) {
            newUvProgram.use {
                mesh.use {
                    mesh.draw()
                }
            }
        }
        frameBuffer.use(uvTexture2) {
            newUvProgram.use {
                mesh.use {
                    mesh.draw()
                }
            }
        }
    }

    val flippedTexture = GlTexture()

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        mapUvProgram.resolve(context, R.raw.upside_down_vert, R.raw.map_uv_frag)
        newUvProgram.resolve(context, R.raw.default_vert, R.raw.new_uv_frag)
        warpProgram.resolve(context, R.raw.default_vert, R.raw.warp_frag)
        drawTextureProgram.resolve(context, R.raw.upside_down_vert, R.raw.default_frag)
        val bitmap = BitmapFactory.decodeStream(context.assets.open("tex2.jpg"))
        flippedTexture.resolve(bitmap)
        originalTexture.resolve(flippedTexture.width, flippedTexture.height)
        bitmap.recycle()
        uvTexture1.resolve(originalTexture.width, originalTexture.height, GL_RG, GL_RG16F, GL_FLOAT)
        uvTexture2.resolve(originalTexture.width, originalTexture.height, GL_RG, GL_RG16F, GL_FLOAT)
        mesh.resolve(drawTextureProgram)
        frameBuffer.resolve()

        frameBuffer.use(originalTexture) {
            drawTextureProgram.use {
                mesh.use {
                    flippedTexture.use(this, 0, "u_Texture") {
                        mesh.draw()
                    }
                }
            }
        }
        flippedTexture.destroy()
        resetUV()
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
    }

    private var readUvTexture: GlTexture = uvTexture1
    private var writeUvTexture: GlTexture = uvTexture2

    private fun swapUvTextures() {
        readUvTexture = if (readUvTexture === uvTexture1) uvTexture2 else uvTexture1
        writeUvTexture = if (writeUvTexture === uvTexture1) uvTexture2 else uvTexture1
    }

    override fun onDrawFrame(gl: GL10) {
        glClearColor(1f, 1f, 1f, 1f)
        glClear(GL_COLOR_BUFFER_BIT)

        frameBuffer.use(writeUvTexture) {
            warpProgram.use {
                mesh.use {
                    readUvTexture.use(this, 0, "u_Texture") {
                        mesh.draw()
                    }
                }
            }
        }

        swapUvTextures()

        glViewport(0, 0, screenWidth, screenHeight)

        if (uvPreviewMode) {
            drawTextureProgram.use {
                mesh.use {
                    readUvTexture.use(this, 0, "u_Texture") {
                        mesh.draw()
                    }
                }
            }
        } else {
            mapUvProgram.use {
                mesh.use {
                    originalTexture.bind(this, 0, "u_Texture")
                    readUvTexture.bind(this, 1, "u_UvTexture")
                    mesh.draw()
                    originalTexture.unbind()
                    readUvTexture.unbind()
                }
            }
        }
    }

}

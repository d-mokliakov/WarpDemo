package localhost.dmokliakov.warpdemo.gl

import android.graphics.Bitmap
import android.opengl.GLES20.GL_CLAMP_TO_EDGE
import android.opengl.GLES20.GL_LINEAR
import android.opengl.GLES20.GL_RGBA
import android.opengl.GLES20.GL_TEXTURE0
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES20.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES20.GL_TEXTURE_WRAP_S
import android.opengl.GLES20.GL_TEXTURE_WRAP_T
import android.opengl.GLES20.GL_UNSIGNED_BYTE
import android.opengl.GLES20.glActiveTexture
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glDeleteTextures
import android.opengl.GLES20.glGenTextures
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glTexImage2D
import android.opengl.GLES20.glTexParameteri
import android.opengl.GLES20.glUniform1i
import android.opengl.GLUtils
import java.nio.Buffer
import java.nio.ByteBuffer

class GlTexture(
    private val minFilter: Int = GL_LINEAR,
    private val magFilter: Int = GL_LINEAR,
    private val wrapS: Int = GL_CLAMP_TO_EDGE,
    private val wrapT: Int = GL_CLAMP_TO_EDGE,
) {
    var handle = -1
        private set

    var width: Int = 0
        private set
    var height: Int = 0
        private set

    fun resolve(bitmap: Bitmap) {
        glActiveTexture(GL_TEXTURE0) // Not sure if switching to texture unit 0 is necessary here.
        handle = IntArray(1).also { glGenTextures(1, it, 0) }[0]
        glBindTexture(GL_TEXTURE_2D, handle)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT)
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
        width = bitmap.width
        height = bitmap.height
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    fun resolve(
        width: Int,
        height: Int,
        format: Int = GL_RGBA,
        internalFormat: Int = GL_RGBA,
        type: Int = GL_UNSIGNED_BYTE
    ) {
        glActiveTexture(GL_TEXTURE0) // Not sure if switching to texture unit 0 is necessary here.
        handle = IntArray(1).also { glGenTextures(1, it, 0) }[0]
        glBindTexture(GL_TEXTURE_2D, handle)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT)
        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            internalFormat,
            width,
            height,
            0,
            format,
            type,
            null
        )
        this.width = width
        this.height = height
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    fun bind(program: GlProgram, unit: Int, uniformName: String) {
        glActiveTexture(GL_TEXTURE0 + unit)
        glBindTexture(GL_TEXTURE_2D, handle)
        glUniform1i(glGetUniformLocation(program.handle, uniformName), unit)
    }

    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    inline fun use(
        program: GlProgram,
        unit: Int,
        uniformName: String,
        block: GlTexture.() -> Unit
    ) {
        bind(program, unit, uniformName)
        block()
        unbind()
    }

    fun destroy() {
        glDeleteTextures(1, intArrayOf(handle), 0)
    }
}
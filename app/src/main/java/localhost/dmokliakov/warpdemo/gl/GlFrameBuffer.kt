package localhost.dmokliakov.warpdemo.gl

import android.opengl.GLES20.GL_COLOR_ATTACHMENT0
import android.opengl.GLES20.GL_FRAMEBUFFER
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.glBindFramebuffer
import android.opengl.GLES20.glDeleteFramebuffers
import android.opengl.GLES20.glFramebufferTexture2D
import android.opengl.GLES20.glGenFramebuffers
import android.opengl.GLES20.glViewport

class GlFrameBuffer {

    var handle: Int = 0
        private set

    fun resolve() {
        handle = IntArray(1).also { glGenFramebuffers(1, it, 0) }[0]
    }

    fun bind(texture: GlTexture) {
        glViewport(0, 0, texture.width, texture.height)
        glBindFramebuffer(GL_FRAMEBUFFER, handle)
        glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D,
            texture.handle,
            0
        )
    }

    fun unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    inline fun use(targetTexture: GlTexture, block: () -> Unit) {
        bind(targetTexture)
        block()
        unbind()
    }

    fun destroy() {
        glDeleteFramebuffers(1, intArrayOf(handle), 0)
    }

}
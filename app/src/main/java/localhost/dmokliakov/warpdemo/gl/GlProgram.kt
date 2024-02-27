package localhost.dmokliakov.warpdemo.gl

import android.content.Context
import android.opengl.GLES20.*
import androidx.annotation.RawRes
import localhost.dmokliakov.warpdemo.ext.createShader
import localhost.dmokliakov.warpdemo.util.createProgram

open class GlProgram {

    var handle: Int = 0
        private set

    var uniforms = mutableListOf<GlUniform<*>>()

    fun resolve(vertexShaderHandle: Int, fragmentShaderHandle: Int) {
        handle = createProgram(vertexShaderHandle, fragmentShaderHandle)
        glUseProgram(handle)
        uniforms.forEach {
            it.resolve(this)
        }
        glUseProgram(0)
    }

    fun resolve(
        context: Context,
        @RawRes vertexShaderResId: Int,
        @RawRes fragmentShaderResId: Int
    ) {
        resolve(
            context.createShader(GL_VERTEX_SHADER, vertexShaderResId),
            context.createShader(GL_FRAGMENT_SHADER, fragmentShaderResId)
        )
    }

    fun bind() {
        glUseProgram(handle)
        uniforms.forEach {
            it.bind()
        }
    }

    fun unbind() {
        glUseProgram(0)
    }

    inline fun use(block: GlProgram.() -> Unit) {
        bind()
        block()
        unbind()
    }
    
    fun destroy() {
        glDeleteProgram(handle)
    }
}
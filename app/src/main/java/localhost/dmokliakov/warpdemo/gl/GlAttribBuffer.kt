package localhost.dmokliakov.warpdemo.gl

import android.opengl.GLES20.*
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

abstract class GlAttribBuffer<T>(
    private val attribName: String,
    private val data: Array<T>,
    private val vertexSize: Int,
    private val isNormalized: Boolean,
    private val stride: Int,
) {

    protected abstract val glType: Int

    protected abstract val buffer: Buffer

    var handle: Int = 0
        private set

    fun resolve(program: GlProgram) {
        handle = glGetAttribLocation(program.handle, attribName)
    }

    fun bind() {
        glVertexAttribPointer(
            handle,
            vertexSize,
            glType,
            isNormalized,
            stride,
            buffer
        )
        glEnableVertexAttribArray(handle)
    }

    fun unbind() {
        glDisableVertexAttribArray(handle)
    }

}

class GlFloatAttribBuffer(
    attribName: String,
    data: Array<Float>,
    size: Int,
    isNormalized: Boolean,
    stride: Int,
) : GlAttribBuffer<Float>(
    attribName,
    data,
    size,
    isNormalized,
    stride
) {
    override val glType = GL_FLOAT

    override val buffer: FloatBuffer = ByteBuffer.allocateDirect(data.size * Float.SIZE_BYTES).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(data.toFloatArray())
            position(0)
        }
    }
}

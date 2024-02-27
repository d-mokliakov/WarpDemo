package localhost.dmokliakov.warpdemo.gl

import android.opengl.GLES20.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer

abstract class GlMesh(
    private vararg var attribBuffers: GlAttribBuffer<*>
) {

    fun resolve(program: GlProgram) {
        attribBuffers.forEach {
            it.resolve(program)
        }
    }

    fun bind() {
        attribBuffers.forEach {
            it.bind()
        }
    }

    fun unbind() {
        attribBuffers.forEach {
            it.unbind()
        }
    }

    inline fun use(block: () -> Unit) {
        bind()
        block()
        unbind()
    }

    abstract fun draw()
}

class GlTexturedPlaneMesh(
    positionAttribName: String = "a_Position",
    texCoordAttribName: String = "a_TexCoord"
) : GlMesh(
    GlFloatAttribBuffer(
        positionAttribName,
        QUADRANT_COORDINATES,
        COORDINATES_PER_VERTEX,
        false,
        VERTEX_STRIDE
    ),
    GlFloatAttribBuffer(
        texCoordAttribName,
        TEXTURE_COORDINATES,
        COORDINATES_PER_VERTEX,
        false,
        VERTEX_STRIDE
    )
) {

    companion object {
        private const val COORDINATES_PER_VERTEX = 2
        private const val VERTEX_STRIDE: Int = COORDINATES_PER_VERTEX * 4

        // The coordinates of the quadrant in 2D space.
        private val QUADRANT_COORDINATES = arrayOf(
            -1f, 1f, // Top left.
            -1f, -1f, // Bottom left.
            1f, -1f, // Bottom right.
            1f, 1f, // Top right.
        )
        // The texture mapping coordinates inside the quadrant in range [0, 1].
        private val TEXTURE_COORDINATES = arrayOf(
            0f, 1f, // Top left.
            0f, 0f, // Bottom left.
            1f, 0f, // Bottom right.
            1f, 1f, // Top right.
        )
        // The order of vertex rendering.
        private val DRAW_ORDER = shortArrayOf(
            0, 1, 2, // First triangle vertex indices: top left, bottom left, bottom right.
            0, 2, 3, // Second triangle vertex indices: top left, bottom right, top right.
        )
    }

    private val drawOrderBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(DRAW_ORDER.size * Short.SIZE_BYTES).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(DRAW_ORDER)
                position(0)
            }
        }

    override fun draw() {
        glDrawElements(
            GL_TRIANGLES,
            DRAW_ORDER.size,
            GL_UNSIGNED_SHORT,
            drawOrderBuffer
        )
    }
}
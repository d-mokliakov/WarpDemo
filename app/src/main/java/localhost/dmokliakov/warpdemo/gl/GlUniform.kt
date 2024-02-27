package localhost.dmokliakov.warpdemo.gl

import android.graphics.Color
import android.opengl.GLES20.*

abstract class GlUniform<T>(
    private val name: String,
) {

    var value: T? = null

    var location: Int = 0
        protected set

    fun resolve(program: GlProgram) {
        location = glGetUniformLocation(program.handle, name)
    }

    abstract fun bind()
}

class GlUniform1f(name: String) : GlUniform<Float>(name) {

    override fun bind() {
        val value = value ?: return
        glUniform1f(location, value)
    }
}

class GlUniform2f(name: String) : GlUniform<FloatArray>(name) {

    override fun bind() {
        val value = value ?: return
        glUniform2f(location, value[0], value[1])
    }
}

class GlUniform3f(name: String) : GlUniform<FloatArray>(name) {

    override fun bind() {
        val value = value ?: return
        glUniform3f(location, value[0], value[1], value[2])
    }
}

class GlUniform4f(name: String) : GlUniform<FloatArray>(name) {

    override fun bind() {
        val value = value ?: return
        glUniform4f(location, value[0], value[1], value[2], value[3])
    }
}

class GlUniformColor(name: String) : GlUniform<Color>(name) {

    override fun bind() {
        val value = value ?: return
        glUniform4f(
            location,
            value.red(),
            value.green(),
            value.blue(),
            value.alpha()
        )
    }
}
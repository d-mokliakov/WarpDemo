package localhost.dmokliakov.warpdemo.ext

import localhost.dmokliakov.warpdemo.gl.GlUniformColor
import localhost.dmokliakov.warpdemo.gl.GlMesh
import localhost.dmokliakov.warpdemo.gl.GlProgram
import localhost.dmokliakov.warpdemo.gl.GlTexture
import localhost.dmokliakov.warpdemo.gl.GlUniform1f
import localhost.dmokliakov.warpdemo.gl.GlUniform2f
import localhost.dmokliakov.warpdemo.gl.GlUniform3f
import localhost.dmokliakov.warpdemo.gl.GlUniform4f

fun GlProgram.withMesh(mesh: GlMesh, block: GlMesh.() -> Unit) {
    mesh.bind()
    block(mesh)
    mesh.unbind()
}

fun GlProgram.withTexture(texture: GlTexture, unit: Int, uniform: String, block: GlTexture.() -> Unit) {
    texture.bind(this, unit, uniform)
    block(texture)
    texture.unbind()
}

fun GlProgram.uniform1f(name: String): GlUniform1f {
    return GlUniform1f(name).also(uniforms::add)
}

fun GlProgram.uniform2f(name: String): GlUniform2f {
    return GlUniform2f(name).also(uniforms::add)
}

fun GlProgram.uniform3f(name: String): GlUniform3f {
    return GlUniform3f(name).also(uniforms::add)
}

fun GlProgram.uniform4f(name: String): GlUniform4f {
    return GlUniform4f(name).also(uniforms::add)
}

fun GlProgram.uniformColor(name: String): GlUniformColor {
    return GlUniformColor(name).also(uniforms::add)
}
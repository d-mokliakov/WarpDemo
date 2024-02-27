package localhost.dmokliakov.warpdemo.util

import android.opengl.GLES20.GL_COMPILE_STATUS
import android.opengl.GLES20.GL_LINK_STATUS
import android.opengl.GLES20.glAttachShader
import android.opengl.GLES20.glCompileShader
import android.opengl.GLES20.glCreateProgram
import android.opengl.GLES20.glCreateShader
import android.opengl.GLES20.glDeleteProgram
import android.opengl.GLES20.glDeleteShader
import android.opengl.GLES20.glGetProgramInfoLog
import android.opengl.GLES20.glGetProgramiv
import android.opengl.GLES20.glGetShaderInfoLog
import android.opengl.GLES20.glGetShaderiv
import android.opengl.GLES20.glLinkProgram
import android.opengl.GLES20.glShaderSource
import android.util.Log

fun createProgram(vertexShaderHandle: Int, fragmentShaderHandle: Int): Int {
    val programHandle = glCreateProgram()
    if (programHandle == 0) {
        return 0
    }
    glAttachShader(programHandle, vertexShaderHandle)
    glAttachShader(programHandle, fragmentShaderHandle)
    glLinkProgram(programHandle)
    val linkStatus = IntArray(1)
    glGetProgramiv(programHandle, GL_LINK_STATUS, linkStatus, 0)
    if (linkStatus[0] == 0) {
        Log.e("ShaderUtils::createProgram", "Shader link error log:\n" + glGetProgramInfoLog(programHandle))
        glDeleteProgram(programHandle)
        return 0
    }
    return programHandle
}

fun createShader(type: Int, shaderText: String?): Int {
    val shaderHandle = glCreateShader(type)
    if (shaderHandle == 0) {
        return 0
    }
    glShaderSource(shaderHandle, shaderText)
    glCompileShader(shaderHandle)
    val compileStatus = IntArray(1)
    glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, compileStatus, 0)
    if (compileStatus[0] == 0) {
        Log.e("ShaderUtils::createShader", "Shader compile error log: \n" + glGetShaderInfoLog(shaderHandle))
        glDeleteShader(shaderHandle)
        return 0
    }
    return shaderHandle
}
package localhost.dmokliakov.warpdemo.ext

import android.content.Context
import androidx.annotation.RawRes
import java.io.BufferedReader


fun Context.readRawResourceAsString(resourceId: Int) =
    resources.openRawResource(resourceId).bufferedReader().use(BufferedReader::readText)

fun Context.createShader(type: Int, @RawRes shaderResId: Int): Int =
    localhost.dmokliakov.warpdemo.util.createShader(type, readRawResourceAsString(shaderResId))

fun Context.createShader(
    type: Int,
    @RawRes shaderResId: Int,
    vararg replacements: Pair<String, String>,
): Int {
    var shader = readRawResourceAsString(shaderResId)
    replacements.forEach {
        shader = shader.replace(it.first, it.second)
    }
    return localhost.dmokliakov.warpdemo.util.createShader(type, shader)
}
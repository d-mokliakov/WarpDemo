package localhost.dmokliakov.warpdemo

import android.annotation.SuppressLint
import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import localhost.dmokliakov.warpdemo.databinding.ActivityMainBinding
import localhost.dmokliakov.warpdemo.ext.doOnChange

class MainActivity : Activity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val renderer = Renderer(this@MainActivity)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {

            vSurface.setEGLContextClientVersion(3)
            vSurface.setRenderer(renderer)
            vSurface.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

            vSurface.setOnTouchListener(
                RendererTouchListener(
                    this@MainActivity,
                    vSurface,
                    renderer
                )
            )
            btnReset.setOnClickListener {
                runOnGlThread {
                    renderer.resetUV()
                }
            }
            swUvPreview.setOnCheckedChangeListener { _, isChecked ->
                renderer.uvPreviewMode = isChecked
                vSurface.requestRender()
            }
            sbSize.doOnChange { progress ->
                renderer.setBrushSize(progress / 100f)
                vSurface.requestRender()
            }
            sbPressure.doOnChange { progress ->
                renderer.setBrushPressure(progress / 100f)
                vSurface.requestRender()
            }
        }
    }

    private fun runOnGlThread(action: () -> Unit) = with(binding) {
        vSurface.queueEvent(action)
        vSurface.requestRender()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
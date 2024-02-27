package localhost.dmokliakov.warpdemo

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View

class RendererTouchListener(
    private val context: Context,
    private val surfaceView: GLSurfaceView,
    private val renderer: Renderer
): View.OnTouchListener {
    private val detector = GestureDetector(
        context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                renderer.onScroll(
                    e2.x / surfaceView.width,
                    e2.y / surfaceView.height,
                    distanceX / surfaceView.width,
                    distanceY / surfaceView.height
                )
                return true
            }
        })


    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            renderer.onScroll(0f, 0f, 0f, 0f)
        }
        val detectorResult = detector.onTouchEvent(event)
        surfaceView.requestRender()
        return detectorResult
    }
}
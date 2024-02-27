package localhost.dmokliakov.warpdemo.util

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

open class OnSeekBarChangeAdapter: OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) = Unit

    override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
}
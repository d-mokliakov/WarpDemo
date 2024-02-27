package localhost.dmokliakov.warpdemo.ext

import android.widget.SeekBar
import localhost.dmokliakov.warpdemo.util.OnSeekBarChangeAdapter

inline fun SeekBar.doOnChange(crossinline block: (Int) -> Unit) {
    setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            block(progress)
        }
    })
}
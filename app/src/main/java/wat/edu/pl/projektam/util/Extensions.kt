package wat.edu.pl.projektam.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun Fragment.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(requireView(), message, duration).show()
}

fun Long.toFormattedDuration(): String {
    val totalSeconds = this / 1000
    val hours   = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) "%d:%02d:%02d".format(hours, minutes, seconds)
    else "%d:%02d".format(minutes, seconds)
}

fun Double.toFormattedDistance(): String {
    return if (this >= 1000) "%.2f km".format(this / 1000)
    else "${this.roundToInt()} m"
}

fun Date.toDisplayString(): String =
    SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(this)

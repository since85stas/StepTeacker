package stas.batura.stepteacker

import android.widget.TextView
import androidx.databinding.BindingAdapter
import stas.batura.stepteacker.data.room.Step
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("pressureTextBind")
fun TextView.pressureTextBind(step: Step) {
    text = step.pressure.toString()
}

@BindingAdapter("timeBind")
fun TextView.timeBind(step: Step) {
    val formatter = SimpleDateFormat("dd/MM HH:mm");
    val dateString = formatter.format( Date(step.time));
    text = dateString
//    text = pressure.time.toString()
}

@BindingAdapter("rainPowBind")
fun TextView.rainPowBind(step: Step) {
    text = step.rainPower.toString()
}

@BindingAdapter("altBind")
fun TextView.alt(step: Step) {
    text = step.altitude.toString()
}

//@BindingAdapter("timeRainBind")
//fun TextView.timeRainBind(rain: Rain) {
//    val formatter = SimpleDateFormat("dd/MM hh:mm");
//    val dateString = formatter.format( Date(rain.time));
//    text = dateString
//}

//@BindingAdapter("rainStartTextBind")
//fun TextView.rainStartTextBind(rain: Rain) {
//    if (rain.isStarted
//    ) {
//        text = "Start"
//    } else if (rain.isEnded) {
//        text = "Stop"
//    }
//}

//@BindingAdapter("rainStartButtBind")
//fun Button.rainStartButtBind(rain: Rain?) {
//    if (rain != null) {
//        if (rain.isStarted) {
//            visibility = View.GONE
//        } else {
//            visibility = View.VISIBLE
//        }
//    } else {
//        visibility = View.VISIBLE
//    }
//}

//@BindingAdapter("rainStopButtBind")
//fun Button.rainStopButtBind(rain: Rain?) {
//    if (rain != null) {
//    if (rain.isStarted) {
//        visibility = View.VISIBLE
//    } else {
//        visibility = View.GONE
//    }
//    } else {
//        visibility = View.GONE
//    }
//}


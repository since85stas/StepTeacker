package stas.batura.stepteacker

import android.widget.TextView
import androidx.databinding.BindingAdapter
import stas.batura.stepteacker.data.room.Step
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*



@BindingAdapter("setStepCount")
fun TextView.setStepCount(steps: Int) {
    text = steps.toString()
}

@BindingAdapter("setStepCountList")
fun TextView.setStepCount(list: List<Step>) {
    val stringBuilder = StringBuilder()
    for (step in list) {
        stringBuilder.append(step.steps).append(" ")
    }
    text = stringBuilder.toString()
}
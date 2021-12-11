package stas.batura.stepteacker.ui.graph

import android.widget.TextView
import androidx.databinding.BindingAdapter
import stas.batura.stepteacker.data.room.Step
import java.lang.StringBuilder

@BindingAdapter("setStepCountList")
fun TextView.setStepCountList(list: List<Step>?) {
    list?.apply {
        val stringBuilder = StringBuilder()
        for (step in this) {
            stringBuilder.append(step.steps).append(" ")
        }
        text = stringBuilder.toString()
    }
}
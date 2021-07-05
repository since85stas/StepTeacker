package stas.batura.stepteacker

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*



@BindingAdapter("setStepCount")
fun TextView.setStepCount(steps: Int) {
    text = steps.toString()
}
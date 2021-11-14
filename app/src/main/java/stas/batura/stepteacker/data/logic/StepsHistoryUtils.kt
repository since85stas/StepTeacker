package stas.batura.stepteacker.data.logic

import stas.batura.stepteacker.data.room.Step

/**
 * Вычисление количество шагов за день по списку записей Steps из Бд.
 */
fun getStepsInDay(steps: List<Step>): Int {
    val stepsDel = arrayOfNulls<Int?>(steps.size-1)
    for (i in 0 until steps.size-1) {
        stepsDel[i] = steps[i+1].steps - steps[i].steps
    }
    var sum = 0
    for (del in stepsDel) {
        if (del != null && del > 0) {
            sum = sum + del
        }
    }
    return sum
}
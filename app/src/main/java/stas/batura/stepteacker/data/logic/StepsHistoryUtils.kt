package stas.batura.stepteacker.data.logic

import stas.batura.stepteacker.data.room.Step

/**
 * Вычисление количества шагов за день по списку записей Steps из Бд.
 */
fun getStepsInDay(steps: List<Step>): Int {
    if (steps.size > 1) {
        val stepsDel = arrayOfNulls<Int?>(steps.size - 1)
        for (i in 0 until steps.size - 1) {
            stepsDel[i] = steps[i + 1].steps - steps[i].steps
        }
        var sum = 0
        for (del in stepsDel) {
            if (del != null && del > 0) {
                sum = sum + del
            }
        }
        return sum
    } else {
        return 0
    }
}

/**
 * Вычисление непрерывного распределения изменения шагов по Steps из Бд.
 */
fun getStepsSequenceInDay(steps: List<Step>): IntArray {
    if (steps.size > 1) {
        val stepsDel = IntArray(steps.size-1) {0}
        stepsDel[0] = steps[1].steps - steps[0].steps
        for (i in 1 until steps.size - 1) {
            if (steps[i+1].steps > steps[i].steps) {
                stepsDel[i] = stepsDel[i-1] + steps[i + 1].steps - steps[i].steps
            } else {
                stepsDel[i] = stepsDel[i-1]!! + steps[i+1].steps
            }
        }
        return stepsDel
    } else {
        return intArrayOf(0)
    }
}
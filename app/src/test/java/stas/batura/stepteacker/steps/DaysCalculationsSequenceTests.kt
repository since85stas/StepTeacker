package stas.batura.stepteacker.steps

import org.junit.Test
import org.junit.Assert
import stas.batura.stepteacker.data.logic.getStepsInDay
import stas.batura.stepteacker.data.logic.getStepsSequenceInDay
import stas.batura.stepteacker.data.room.Step

class DaysCalculationsSequenceTests {

    @Test
    fun steps_correct_sequense() {
        val steps = mutableListOf<Step>(
            Step(steps = 1),
            Step(steps = 2),
            Step(steps = 3),
            Step(steps = 4)
        )
//        Assert.assertEquals()
        Assert.assertArrayEquals(arrayOf(1,2,3).toIntArray(), getStepsSequenceInDay(steps))
    }

}
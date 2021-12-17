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
        Assert.assertArrayEquals(arrayOf(1,2,3).toIntArray(), getStepsSequenceInDay(steps))
    }

    @Test
    fun steps_correct_sequense2() {
        val steps = mutableListOf<Step>(
            Step(steps = 3),
            Step(steps = 4),
            Step(steps = 5),
            Step(steps = 6),
            Step(steps = 10),
            Step(steps = 11),
            Step(steps = 12),

        )
        Assert.assertArrayEquals(arrayOf(1,2,3,7,8,9).toIntArray(), getStepsSequenceInDay(steps))
    }

    @Test
    fun steps_correct_sequense3() {
        val steps = mutableListOf<Step>(
            Step(steps = 3),
            Step(steps = 4),
            Step(steps = 5),
            Step(steps = 6),
            Step(steps = 10),
            Step(steps = 11),
            Step(steps = 12),
            Step(steps = 22),
            Step(steps = 25),
            Step(steps = 27),
            )
        Assert.assertArrayEquals(arrayOf(1,2,3,7,8,9,19,22,24).toIntArray(), getStepsSequenceInDay(steps))
    }
}
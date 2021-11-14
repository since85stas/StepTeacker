package stas.batura.stepteacker.steps

import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import stas.batura.stepteacker.data.logic.getStepsInDay
import stas.batura.stepteacker.data.room.Step

class DaysCalculationTests {

    @Test
    fun steps_byPlusOne_count_isCorrect() {
        val steps = mutableListOf<Step>(
            Step(steps = 1),
            Step(steps = 2),
            Step(steps = 3),
            Step(steps = 4)
        )
        assertEquals(3, getStepsInDay(steps))
    }

    @Test
    fun steps_byPlusOne_fromTen_count_isCorrect() {
        val steps = mutableListOf<Step>(
            Step(steps = 10),
            Step(steps = 11),
            Step(steps = 12),
            Step(steps = 13),
            Step(steps = 14),
            Step(steps = 15),
        )
        assertEquals(5, getStepsInDay(steps))
    }

    @Test
    fun steps_byPlusFrom_fromNull_count_isCorrect() {
        val steps = mutableListOf<Step>(
            Step(steps = 0),
            Step(steps = 1),
            Step(steps = 7),
            Step(steps = 8),
            Step(steps = 11),
            Step(steps = 12),
        )
        assertEquals(1+6+1+3+1, getStepsInDay(steps))
    }

}
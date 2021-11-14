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

}
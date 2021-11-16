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

    @Test
    fun steps_byPlusFrom_from111_big_count_isCorrect() {
        val steps = mutableListOf<Step>(
            Step(steps = 111),
            Step(steps = 213),
            )
        assertEquals(102, getStepsInDay(steps))
    }

    @Test
    fun steps_byPlusFrom_from111_count_isCorrect() {
        val steps = mutableListOf<Step>(
            Step(steps = 111),
            Step(steps = 113),
            Step(steps = 119),
            Step(steps = 129),

        )
        assertEquals(2+6+10, getStepsInDay(steps))
    }

    @Test
    fun steps_one_record_count_isCorrect() {
        val steps = mutableListOf<Step>(
            Step(steps = 111),
        )
        assertEquals(0, getStepsInDay(steps))
    }

    @Test
    fun steps_record_two_intervals_count_isCorrect() {
        val steps = mutableListOf<Step>(
            Step(steps = 0),
            Step(steps = 1),
            Step(steps = 7),
            Step(steps = 8),

            Step(steps = 0),
            Step(steps = 4),
            Step(steps = 5),
            Step(steps = 6),
        )
        assertEquals(8 + 6, getStepsInDay(steps))
    }

//    @Test
//    fun steps_byPlusFrom_from111_count_isCorrect() {
//        val steps = mutableListOf<Step>(
//            Step(steps = 0),
//            Step(steps = 1),
//            Step(steps = 7),
//            Step(steps = 8),
//
//            Step(steps = 0),
//            Step(steps = 4),
//            Step(steps = 5),
//            Step(steps = 6),
//            )
//        assertEquals(2+6+10, getStepsInDay(steps))
//    }
}
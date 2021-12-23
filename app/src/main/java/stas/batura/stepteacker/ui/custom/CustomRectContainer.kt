package stas.batura.stepteacker.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CustomRectContainer(context: Context, attrs: AttributeSet): View(context, attrs) {

    // Paint object for coloring and styling
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Some colors for the face background, eyes and mouth.
    private var bordOuterColor = Color.BLACK
    private var eyesColor = Color.BLACK
    private var mouthColor = Color.BLACK
    private var bordInnerColor = Color.BLACK

    // Face border width in pixels
    private var borderWidth = 4

    // View size in pixels
    private var size = 320

    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)

        drawFaceBackground(canvas)
//        drawEyes(canvas)
        drawMouth(canvas)
    }


    private fun drawFaceBackground(canvas: Canvas) {
        // 1
        paint.color = bordOuterColor
        paint.style = Paint.Style.STROKE

        // 2
        val radius = size / 2f

        // 3
//        canvas.drawCircle(size / 2f, size / 2f, radius, paint)
        val rectOuter = Rect(0,0,size,size)
        canvas.drawRect(rectOuter, paint)

        // 4
        paint.color = bordInnerColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth*1.0f

        val rectInner = Rect(borderWidth,borderWidth,size-borderWidth,size-borderWidth)
        canvas.drawRect(rectInner, paint)

        // 5
//        canvas.drawCircle(size*1.0f, size*1.0f, radius *2, paint)

    }

    private fun drawEyes(canvas: Canvas) {
        // 1
        paint.color = eyesColor
        paint.style = Paint.Style.FILL

        // 2
        val leftEyeRect = RectF(size * 0.32f, size * 0.23f, size * 0.43f, size * 0.50f)

        canvas.drawOval(leftEyeRect, paint)

        // 3
        val rightEyeRect = RectF(size * 0.57f, size * 0.23f, size * 0.68f, size * 0.50f)

        canvas.drawOval(rightEyeRect, paint)
    }

    private fun drawMouth(canvas: Canvas) {

    }

}
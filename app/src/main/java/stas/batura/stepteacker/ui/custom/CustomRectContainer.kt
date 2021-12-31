package stas.batura.stepteacker.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CustomRectContainer(context: Context, attrs: AttributeSet): View(context, attrs) {

    companion object {
        private const val DEFAULT_FILL_COLOR = Color.YELLOW
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_STEP_LIMIT = 10000
        private const val DEFAULT_CURRENT_STEPS = 1000
    }

    // Paint object for coloring and styling
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Some colors for the face background, eyes and mouth.
    private var bordOuterColor = DEFAULT_BORDER_COLOR
    private var fillerColor = DEFAULT_FILL_COLOR

    // Face border width in pixels
    private var borderWidth = 2*0.01f

    // View size in pixels
    private var size = 320

    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)

        drawFaceBackground(canvas)
//        drawEyes(canvas)
        drawMouth(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 1
        size = Math.min(measuredWidth, measuredHeight)
// 2
        setMeasuredDimension(size, size)
    }

    private fun drawFaceBackground(canvas: Canvas) {
        // 1
        paint.color = bordOuterColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = width*borderWidth
        // 2
        val radius = size / 2f

        // 3
//        canvas.drawCircle(size / 2f, size / 2f, radius, paint)
        val rectOuter = Rect(0,0,size,size)
        canvas.drawRect(rectOuter, paint)

        // 5
//        canvas.drawCircle(size*1.0f, size*1.0f, radius *2, paint)

    }

    private fun drawEyes(canvas: Canvas) {
        // 1
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
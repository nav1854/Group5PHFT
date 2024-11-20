package com.example.group5phft

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ProgressGraphView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    private val progressData = mutableListOf<Pair<Float, Float>>()  // List of (X, Y) pairs for the graph

    init {
        paint.color = Color.BLUE
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
    }

    // Method to set progress data for the graph (can be called from the activity)
    fun setProgressData(data: List<Pair<Float, Float>>) {
        progressData.clear()
        progressData.addAll(data)
        invalidate()  // Redraw the view with new data
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (progressData.isEmpty()) return

        // Draw the axis
        paint.color = Color.BLACK
        canvas.drawLine(50f, height - 50f, width - 50f, height - 50f, paint) // X-axis
        canvas.drawLine(50f, height - 50f, 50f, 50f, paint)  // Y-axis

        // Draw the progress line (using the data points)
        paint.color = Color.BLUE
        for (i in 0 until progressData.size - 1) {
            val start = progressData[i]
            val end = progressData[i + 1]
            canvas.drawLine(
                mapToScreenX(start.first),
                mapToScreenY(start.second),
                mapToScreenX(end.first),
                mapToScreenY(end.second),
                paint
            )
        }
    }

    // Map X-axis values to the screen width
    private fun mapToScreenX(x: Float): Float {
        return 50f + (x * (width - 100f) / 10f) // Assuming data range 0-10 for X
    }

    // Map Y-axis values to the screen height
    private fun mapToScreenY(y: Float): Float {
        return height - 50f - (y * (height - 100f) / 10f) // Assuming data range 0-10 for Y
    }
}

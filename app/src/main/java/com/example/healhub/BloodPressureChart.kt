package com.example.healhub

import android.content.Context
import android.graphics.Color as GColor
import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.healhub.database.CareRecord
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun BloodPressureChart(data: List<CareRecord>) {
    AndroidView(factory = { context: Context ->
        val chart = LineChart(context)
        val entries = data.mapIndexedNotNull { index, record ->
            val parts = record.bloodPressure.split("/")
            val systolic = parts.getOrNull(0)?.toFloatOrNull()
            if (systolic != null) Entry(index.toFloat(), systolic) else null
        }

        val dataSet = LineDataSet(entries, "Systolic BP").apply {
            color = GColor.BLUE
            valueTextColor = GColor.BLACK
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(GColor.RED)
            valueTextSize = 10f
            setDrawFilled(true)
        }

        chart.data = LineData(dataSet)
        chart.axisRight.isEnabled = false
        chart.axisLeft.textColor = GColor.BLACK
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            textColor = GColor.BLACK
        }
        chart.description = Description().apply {
            text = "Blood Pressure Over Time"
            textColor = GColor.DKGRAY
            textSize = 12f
        }
        chart.legend.textColor = GColor.BLACK
        chart.setNoDataText("No BP data")
        chart.setNoDataTextColor(GColor.RED)
        chart.animateY(500)
        chart.invalidate()
        chart
    }, modifier = Modifier)
}
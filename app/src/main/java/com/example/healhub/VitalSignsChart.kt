package com.example.healhub

import android.content.Context
import android.graphics.Color as GColor
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.healhub.ui.dataClasses.Care
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun VitalSignsChart(data: List<Care>) {
    AndroidView(factory = { context: Context ->
        val chart = LineChart(context)

        val dateLabels = data.map { it.fechaRegistro }

        val bpSystolicSet = LineDataSet(data.mapIndexedNotNull { index, care ->
            care.constantesVitales?.sistolicaTA?.let { Entry(index.toFloat(), it) }
        }, "Tensión Sistólica").apply {
            color = GColor.MAGENTA
            setCircleColor(GColor.MAGENTA)
            setDrawValues(false)
            lineWidth = 2.5f
            circleRadius = 4f
        }

        val bpDiastolicSet = LineDataSet(data.mapIndexedNotNull { index, care ->
            care.constantesVitales?.diastolicaTA?.let { Entry(index.toFloat(), it) }
        }, "Tensión Diastólica").apply {
            color = GColor.GREEN
            setCircleColor(GColor.GREEN)
            setDrawValues(false)
            lineWidth = 2.5f
            circleRadius = 4f
        }

        val pulseSet = LineDataSet(data.mapIndexedNotNull { index, care ->
            care.constantesVitales?.pulso?.toFloat()?.let { Entry(index.toFloat(), it) }
        }, "Pulso").apply {
            color = GColor.BLACK
            setCircleColor(GColor.BLACK)
            setDrawValues(false)
            lineWidth = 2.5f
            circleRadius = 4f
        }

        val tempSet = LineDataSet(data.mapIndexedNotNull { index, care ->
            care.constantesVitales?.temperatura?.let { Entry(index.toFloat(), it) }
        }, "Temperatura").apply {
            color = GColor.RED
            setCircleColor(GColor.RED)
            setDrawValues(false)
            lineWidth = 2.5f
            circleRadius = 4f
        }

        val respSet = LineDataSet(data.mapIndexedNotNull { index, care ->
            care.constantesVitales?.frecuenciaRespiratoria?.toFloat()?.let { Entry(index.toFloat(), it) }
        }, "Respiración").apply {
            color = GColor.BLUE
            setCircleColor(GColor.BLUE)
            setDrawValues(false)
            lineWidth = 2.5f
            circleRadius = 4f
        }

        val oxySet = LineDataSet(data.mapIndexedNotNull { index, care ->
            care.constantesVitales?.saturacionOxigeno?.toFloat()?.let { Entry(index.toFloat(), it) }
        }, "O₂ Saturación").apply {
            color = GColor.GRAY
            setCircleColor(GColor.GRAY)
            setDrawValues(false)
            lineWidth = 2.5f
            circleRadius = 4f
        }

        val dataSets = listOf(
            bpSystolicSet, bpDiastolicSet, pulseSet, tempSet, respSet, oxySet
        ).filter { it.entryCount > 0 }

        chart.data = LineData(dataSets)

        // ✅ 自动计算 Y 轴范围
        val allValues = dataSets.flatMap { set ->
            (0 until set.entryCount).map { i -> set.getEntryForIndex(i).y }
        }
        val minY = (allValues.minOrNull() ?: 0f) - 10f
        val maxY = (allValues.maxOrNull() ?: 100f) + 10f

        chart.axisLeft.apply {
            axisMinimum = minY
            axisMaximum = maxY
            textColor = GColor.BLACK
            setDrawGridLines(true)
            gridColor = GColor.LTGRAY
            gridLineWidth = 0.7f
        }

        chart.axisRight.isEnabled = false

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = GColor.BLACK
            setDrawGridLines(true)
            gridColor = GColor.LTGRAY
            gridLineWidth = 0.7f
            valueFormatter = IndexAxisValueFormatter(dateLabels)
        }

        chart.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
            textColor = GColor.DKGRAY
            textSize = 12f
            xEntrySpace = 12f
            yEntrySpace = 6f
            setDrawInside(false)
        }

        chart.description = Description().apply {
            text = "Gráfica de constantes vitales"
            textColor = GColor.DKGRAY
            textSize = 12f
        }

        chart.setExtraOffsets(24f, 36f, 24f, 24f)
        chart.setViewPortOffsets(16f, 16f, 16f, 16f)

        chart.setVisibleXRangeMaximum(4f)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setScaleXEnabled(true)
        chart.setScaleYEnabled(true)
        chart.setPinchZoom(true)

        chart.animateY(600)
        chart.invalidate()

        chart
    }, modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)) // Aumenta la altura visible de la gráfica)
}
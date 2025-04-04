package com.example.healhub

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.example.healhub.R

import com.example.healhub.database.CareRecord
import com.example.healhub.database.AppDatabase


class BloodPressureMarker(
    context: Context,
    private val records: List<CareRecord>
) : MarkerView(context, R.layout.bp_marker_view) {

    private val markerText: TextView = findViewById(R.id.markerText)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val index = e?.x?.toInt() ?: 0
        val record = records.getOrNull(index)
        markerText.text = record?.let {
            "Date: ${it.date}\nBP: ${it.bloodPressure}\nNote: ${it.note}"
        } ?: ""
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}

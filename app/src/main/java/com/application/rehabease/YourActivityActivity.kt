package com.application.rehabease

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidplot.xy.BoundaryMode
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.StepMode
import com.androidplot.xy.XYGraphWidget
import com.androidplot.xy.XYPlot
import com.androidplot.xy.XYSeries
import com.application.common.ActivityUtils
import com.application.customization.DialogActivity
import com.application.other.ArrayListOperations
import com.application.other.AssessValues
import com.application.other.DateOperations
import com.application.service.TrainingsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition

class YourActivityActivity : AppCompatActivity() {
    private var trainingsService: TrainingsService = TrainingsService()
    private lateinit var trainingDates: ArrayList<String>
    private lateinit var trainingDurations: ArrayList<Number>
    private lateinit var activityPlot: XYPlot
    private lateinit var xLabelTextView: TextView
    private lateinit var yLabelTextView: TextView
    private lateinit var titleNumberOfDaysTextView: TextView
    private lateinit var numberOfDays: EditText
    private lateinit var refreshImageView: ImageView
    private lateinit var activityCommentTitleTextView: TextView
    private lateinit var activityCommentTextView: TextView
    private lateinit var externalProgressBar: ProgressBar
    private lateinit var internalProgressBar: ProgressBar
    private lateinit var loadingDataTextView: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_activity)
        ActivityUtils.actionBarSetup(this)
        xLabelTextView = findViewById(R.id.x_label_text)
        yLabelTextView = findViewById(R.id.y_label_text)
        titleNumberOfDaysTextView = findViewById(R.id.display_activity_text)
        numberOfDays = findViewById(R.id.activity_from_last_x_days_input)
        activityPlot = findViewById(R.id.activity_plot)
        refreshImageView = findViewById(R.id.refresh_plot)
        activityCommentTitleTextView = findViewById(R.id.activity_comment_title_text)
        activityCommentTextView = findViewById(R.id.activity_comment_text)
        externalProgressBar = findViewById(R.id.external_progress_bar)
        internalProgressBar = findViewById(R.id.internal_progress_bar)
        loadingDataTextView = findViewById(R.id.loading_data_text)
        setupTrainingModels {
            closeLoading()
            setupPlot()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupTrainingModels(onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                trainingDates = trainingsService.fetchDates()
                trainingDurations = trainingsService.fetchDurations()
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupPlot() {
        numberOfDays.setOnClickListener {
            displayPicker()
        }
        generatePlot(7)
        refreshImageView.setOnClickListener {
            val returnBack = numberOfDays.text.toString()
            if (returnBack == "") {
                Toast.makeText(this, "Choose number of days!", Toast.LENGTH_SHORT).show()
            } else {
                activityPlot.clear()
                generatePlot(returnBack.toInt())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun generatePlot(returnBack: Number) {
        val domainLabels = DateOperations.getDatesFromCurrent(returnBack)
        val durationMap = mutableMapOf<String, Int>()
        for (i in trainingDates.indices) {
            val date = trainingDates[i]
            val duration = trainingDurations[i].toInt()
            durationMap[date] = durationMap.getOrDefault(date, 0) + duration
        }
        val seriesNumber = ArrayList<Number>()
        for (label in domainLabels) {
            val duration = durationMap.getOrDefault(label, 0)
            seriesNumber.add(duration)
        }
        val series : XYSeries = SimpleXYSeries(seriesNumber, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "")
        // format the series
        val seriesFormat = LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels)
        // seriesFormat.interpolationParams = CatmullRomInterpolator.Params(15, CatmullRomInterpolator.Type.Centripetal)
        // add the series to the plot
        activityPlot.addSeries(series, seriesFormat)
        // calculate the highest value in the series
        val highestValue = ArrayListOperations.getMaxValue(seriesNumber)
        // set the range for Y-axis labels
        activityPlot.setRangeBoundaries(0, highestValue!! + 10, BoundaryMode.FIXED)
        activityPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 10.0)
        // hide the legend
        activityPlot.legend.isVisible = false
        // set up the X-axis labels to show dates/nothing
        activityPlot.setDomainBoundaries(0, domainLabels.size - 1, BoundaryMode.FIXED)
        activityPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1.0)
        activityPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
            override fun format(obj: Any?, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
                val i = Math.round((obj as Number).toFloat())
                // put `domainLabels.getOrNull(i) ?: ""` in () to display dates as labels
                return toAppendTo.append("")
            }
            override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                return null
            }
        }
        // remove grid lines if the domain is more than 21 values
        if (returnBack.toInt() > 21) {
            activityPlot.graph.domainGridLinePaint = null
            activityPlot.graph.rangeGridLinePaint = null
        } else {
            val gridPaint = Paint().apply { color = 0xFF000000.toInt()
                strokeWidth = 2f
                style = Paint.Style.STROKE
            }
            activityPlot.graph.domainGridLinePaint = gridPaint
            activityPlot.graph.rangeGridLinePaint = gridPaint
        }
        // set the X-label
        xLabelTextView.text = "Last $returnBack days"
        // draw a plot
        activityPlot.redraw()
        // change comment on user's activity
        activityCommentTitleTextView.text = AssessValues.evaluateActivity(seriesNumber[seriesNumber.size - 1].toDouble())[0]
        activityCommentTextView.text = AssessValues.evaluateActivity(seriesNumber[seriesNumber.size - 1].toDouble())[1]
    }

    private fun displayPicker() {
        val dialog = DialogActivity(this, R.layout.dialog_number_of_days_picker)
        val view = dialog.getDialog()
        val daysPicker = view.findViewById<NumberPicker>(R.id.days_picker)
        val setDaysButton = view.findViewById<Button>(R.id.set_days_button)
        daysPicker.minValue = 1
        daysPicker.maxValue = 90
        val currentValue = 1
        daysPicker.value = currentValue
        setDaysButton.setOnClickListener {
            val selectedValue = daysPicker.value
            numberOfDays.text = Editable.Factory.getInstance().newEditable(selectedValue.toString())
            dialog.closeDialog()
        }
    }

    private fun closeLoading() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                loadingDataTextView.visibility = View.GONE
                internalProgressBar.visibility = View.GONE
                externalProgressBar.visibility = View.GONE
                activityPlot.visibility = View.VISIBLE
                xLabelTextView.visibility = View.VISIBLE
                yLabelTextView.visibility = View.VISIBLE
                titleNumberOfDaysTextView.visibility = View.VISIBLE
                numberOfDays.visibility = View.VISIBLE
                refreshImageView.visibility = View.VISIBLE
                activityCommentTitleTextView.visibility = View.VISIBLE
                activityCommentTextView.visibility = View.VISIBLE
                activityPlot.startAnimation(fadeIn)
                xLabelTextView.startAnimation(fadeIn)
                yLabelTextView.startAnimation(fadeIn)
                titleNumberOfDaysTextView.startAnimation(fadeIn)
                numberOfDays.startAnimation(fadeIn)
                refreshImageView.startAnimation(fadeIn)
                activityCommentTitleTextView.startAnimation(fadeIn)
                activityCommentTextView.startAnimation(fadeIn)
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        loadingDataTextView.startAnimation(fadeOut)
        internalProgressBar.startAnimation(fadeOut)
        externalProgressBar.startAnimation(fadeOut)
    }
}
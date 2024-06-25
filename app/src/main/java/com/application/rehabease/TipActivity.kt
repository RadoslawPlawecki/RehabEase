package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.application.rehabease.ServiceOpenAIAPI.TipViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TipActivity : AppCompatActivity() {
    private lateinit var openMenu: ImageView
    private lateinit var badButton: Button
    private lateinit var decentButton: Button
    private lateinit var goodButton: Button
    private val tipViewModel: TipViewModel by viewModels()
    private lateinit var tipTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip)

        tipTextView = findViewById(R.id.tip_text)

        // Observe live data for tips
        tipViewModel.tipLiveData.observe(this, Observer { tip ->
            tipTextView.text = tip
        })

        // Fetch the tip of the day
        tipViewModel.fetchTipOfTheDay()

        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@TipActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        badButton = findViewById(R.id.bad)
        decentButton = findViewById(R.id.decent)
        goodButton = findViewById(R.id.good)

        badButton.setOnClickListener { recordFeedback("bad") }
        decentButton.setOnClickListener { recordFeedback("decent") }
        goodButton.setOnClickListener { recordFeedback("good") }
    }

    private fun recordFeedback(rating: String) {
        MainScope().launch {
            val alreadyRated = tipViewModel.wasFeedbackGivenToday()
            val feedbackMessage: String

            if (alreadyRated) {
                feedbackMessage = getString(R.string.already_rated_message)
            } else {
                tipViewModel.recordFeedback(rating)
                feedbackMessage = getString(R.string.thank_you_for_your_feedback)
            }

            val intent = Intent(this@TipActivity, ThankYouActivity::class.java).apply {
                putExtra("FEEDBACK_MESSAGE", feedbackMessage)
            }
            startActivity(intent)
        }
    }
}
package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.application.rehabease.openAIIntegration.TipViewModel

class TipActivity : AppCompatActivity() {
    private lateinit var openMenu: ImageView
    private lateinit var badButton: Button
    private lateinit var decentButton: Button
    private lateinit var goodButton: Button
    private val tipViewModel: TipViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip)
        val tipTextView: TextView = findViewById(R.id.tip_text)
        tipViewModel.tipLiveData.observe(this, Observer { tip ->
            tipTextView.text = tip
        })
        tipViewModel.fetchTipOfTheDay()

        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@TipActivity, MenuActivity::class.java)
            startActivity(intent)
        }
        badButton = findViewById(R.id.bad)
        decentButton = findViewById(R.id.decent)
        goodButton = findViewById(R.id.good)

        val feedbackButtonListener = { view: android.view.View ->
            val intent = Intent(this@TipActivity, ThankYouActivity::class.java)
            intent.putExtra("TITLE", "Tip of the day!")
            startActivity(intent)
        }

        badButton.setOnClickListener(feedbackButtonListener)
        decentButton.setOnClickListener(feedbackButtonListener)
        goodButton.setOnClickListener(feedbackButtonListener)
    }

}
package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TipActivity: AppCompatActivity() {
    private lateinit var openMenu: ImageView
    private lateinit var badButton: Button
    private lateinit var decentButton: Button
    private lateinit var goodButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip)
        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@TipActivity, MenuActivity::class.java)
            startActivity(intent)
        }
        badButton = findViewById(R.id.bad)
        decentButton = findViewById(R.id.decent)
        goodButton = findViewById(R.id.good)
        badButton.setOnClickListener {
            val intent = Intent(this@TipActivity, ThankYouActivity::class.java)
            startActivity(intent)
        }
        decentButton.setOnClickListener {
            val intent = Intent(this@TipActivity, ThankYouActivity::class.java)
            startActivity(intent)
        }
        goodButton.setOnClickListener {
            val intent = Intent(this@TipActivity, ThankYouActivity::class.java)
            startActivity(intent)
        }
    }
}
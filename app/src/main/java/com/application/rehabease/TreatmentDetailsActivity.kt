package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class TreatmentDetailsActivity: AppCompatActivity() {
    private lateinit var openMenu: ImageView
    private lateinit var spinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private var selectedItem = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_details)
        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@TreatmentDetailsActivity, MenuActivity::class.java)
            startActivity(intent)
        }
        spinner = findViewById(R.id.injuries)
        adapter = ArrayAdapter.createFromResource(this, R.array.injuries, R.layout.my_selected_item)
        adapter.setDropDownViewResource(R.layout.simple_spinner_item)
        spinner.setAdapter(adapter)
    }
}
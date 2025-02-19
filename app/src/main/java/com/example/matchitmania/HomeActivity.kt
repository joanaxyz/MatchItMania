package com.example.matchitmania



import android.app.Activity
import android.content.Intent
import android.os.Bundle
import buttons.MButton

class HomeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val settingsButton = findViewById<MButton>(R.id.settingsButton)

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }


}
package com.example.matchitmania

import android.app.Activity
import android.os.Bundle

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

//        val gradientDrawable = GradientDrawable(
//            GradientDrawable.Orientation.BOTTOM_TOP,
//            intArrayOf(
//                ContextCompat.getColor(this, R.color.MYellow), // Inside Activity
//                ContextCompat.getColor(this, R.color.MDarkBlue)
//            )
//        )
//
//        gradientDrawable.cornerRadius = 20 * Resources.getSystem().displayMetrics.density
//        gradientDrawable.setStroke(10, Color.BLACK) // Border color
//
//        val view = findViewById<TextView>(R.id.tvSettings)
//        view.background = gradientDrawable

    }
}
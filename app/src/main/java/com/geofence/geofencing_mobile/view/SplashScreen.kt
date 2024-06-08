package com.geofence.geofencing_mobile.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.geofence.geofencing_mobile.R


/**
 * SplashScreen activity to display a splash screen while the app loads.
 */
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display mode for this activity.
        enableEdgeToEdge()

        // Set the content view to the layout defined in activity_splash_screen.xml.
        setContentView(R.layout.activity_splash_screen)

        // Set up a listener to handle window insets for edge-to-edge display.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Delay the start of the main activity (HomeActivity) by 2000 milliseconds (2 seconds).
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 2000)
    }
}

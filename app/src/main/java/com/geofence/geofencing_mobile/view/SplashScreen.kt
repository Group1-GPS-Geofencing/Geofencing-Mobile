package com.geofence.geofencing_mobile.view

// Import necessary Android libraries.
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

// SuppressLint annotation to suppress the custom splash screen warning.
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    // onCreate is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display mode for this activity.
        enableEdgeToEdge()

        // Set the content view to the layout defined in activity_splash_screen.xml.
        setContentView(R.layout.activity_splash_screen)

        // Set up a listener to handle window insets for edge-to-edge display.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Get the system bars insets (status bar, navigation bar).
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Set padding for the view to avoid overlapping with system bars.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Delay the start of the main activity (HomeActivity) by 2000 milliseconds (2 seconds).
        Handler(Looper.getMainLooper()).postDelayed({
            // Start the HomeActivity.
            startActivity(Intent(this, HomeActivity::class.java))
            // Finish the current activity (SplashScreen) to remove it from the back stack.
            finish()
        }, 2000)
    }
}

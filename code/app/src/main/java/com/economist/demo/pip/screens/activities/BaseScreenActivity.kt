package com.economist.demo.pip.screens.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import com.economist.demo.R
import com.economist.demo.pip.screens.fragments.ScreensFragment

@androidx.annotation.OptIn(UnstableApi::class)
class BaseScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_base)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_base, ScreensFragment())
                .commit()
        }
    }


    fun launchPlayerActivity() {
        val intent = Intent(this, VideoPlayerFullScreenActivity::class.java)
        startActivity(intent)
    }
}




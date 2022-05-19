package com.example.artists.presentations.artists

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.artists.R

class MainArtistsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ArtistsListFragment.newInstance())
                .commitNow()
        }
    }
}
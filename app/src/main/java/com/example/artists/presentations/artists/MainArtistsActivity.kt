package com.example.artists.presentations.artists

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo3.rx3.Rx3Apollo
import com.example.artists.R
import com.example.artists.networking.apolloClient
import com.example.artists.schedulers.AppSchedulerProvider
import com.example.rocketreserver.ArtistsQuery

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

    override fun onStart() {
        super.onStart()
        val schedulerProvider = AppSchedulerProvider()
        apolloClient.query(ArtistsQuery()).run {
            Rx3Apollo.single(this)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { Log.d("artists", "success! : ${it.data.toString()}") },
                    { Log.d("artists", "error! : $it") }
                )
        }
    }
}
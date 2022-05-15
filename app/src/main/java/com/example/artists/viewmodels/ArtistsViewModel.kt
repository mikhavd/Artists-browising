package com.example.artists.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.rx3.Rx3Apollo
import com.example.artists.networking.apolloClient
import com.example.artists.schedulers.SchedulerProvider
import com.example.rocketreserver.ArtistsQuery

/**
 * TODO
 * @author Mikhail Avdeev (mvavdeev@sberbank.ru)
 */
class ArtistsViewModel(
    private val schedulerProvider: SchedulerProvider,
) : ViewModel() {

    //The internal obtained list for brochures to display
    private val _artists = MutableLiveData<List<Artist>>()

    // The external immutable LiveData for artists to display
    val artists: LiveData<List<Artist>> = _artists

    init {
        loadInitialArtists()
    }

    private fun loadInitialArtists() {
        Rx3Apollo.single(apolloClient.query(ArtistsQuery()))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { parseInitialArtistsResponse(it) },
                { onFailure(it) }
            )
    }

    private fun parseInitialArtistsResponse(artistsResponse: ApolloResponse<ArtistsQuery.Data>) {
        Log.d(TAG, "Success ${artistsResponse.data}")
        TODO("Not yet implemented")
    }

    private fun onFailure(throwable: Throwable) {
        try {
            //todo _status.postValue(BrochuresApiStatus.ERROR)
            //todo _brochures.postValue(emptyList())
            Log.d(TAG, "throwable = " + throwable.message)
        } catch (e: Throwable) {
            Log.d(TAG, "throwable = " + e.message)
        }
    }

    companion object {

        private const val TAG = "Artists"
    }
}
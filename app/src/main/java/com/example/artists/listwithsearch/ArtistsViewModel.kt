package com.example.artists.listwithsearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.artists.listwithsearch.schedulers.SchedulerProvider
import com.example.artists.networking.ArtistsApiService
import com.example.artists.networking.pojos.ArtistsResponse

/**
 * TODO
 * @author Mikhail Avdeev (mvavdeev@sberbank.ru)
 */
class ArtistsViewModel(
    private val artistsApiService: ArtistsApiService,
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
        artistsApiService.getArtistsList()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            //todo .doOnSubscribe { _status.postValue(BrochuresApiStatus.LOADING) }
            .subscribe(
                this::parseInitialArtistsResponse,
                this::onFailure)
    }

    private fun parseInitialArtistsResponse(artistsResponse: ArtistsResponse) {
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
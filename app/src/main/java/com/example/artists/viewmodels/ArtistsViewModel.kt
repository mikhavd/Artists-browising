package com.example.artists.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.rx3.Rx3Apollo
import com.example.artists.networking.apolloClient
import com.example.artists.schedulers.SchedulerProvider
import com.example.rocketreserver.ArtistsByNameQuery
import java.util.Locale

/**
 * ViewModel for a [ArtistsListFragment];
 * Providing list of artists corresponding to the search query
 *
 * @author Mikhail Avdeev (mvavdeev@sberbank.ru)
 */
class ArtistsViewModel(
    private val schedulerProvider: SchedulerProvider,
) : ViewModel() {

    //todo
    private val _searchQuery = MutableLiveData(EMPTY_STRING)

    // mediatorLiveData for binding searchQuery and artists list together
    private val searchQueryMediatorData: MediatorLiveData<String> = MediatorLiveData<String>()

    private var currentSearchQuery = EMPTY_STRING

    //The internal obtained list for artists to display
    private val _artists = MutableLiveData<List<Artist>>()

    // The external immutable LiveData for artists to display
    val artists: LiveData<List<Artist>> = _artists

    init {
        //todo searchQueryMediatorData.addSource(_searchQuery, ::loadArtists)
    }

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == currentSearchQuery) return
        //todo nextPageHandler.reset()
        //todo _searchQuery.postValue(input)
        currentSearchQuery = input.also { loadArtists(it, after = Optional.Present(null)) }
    }

    private fun loadArtists(searchQuery: String, limit: Int = DEFAULT_LIMIT, after: Optional<String?> ) {
        apolloClient.query(ArtistsByNameQuery(searchQuery, limit, after)).run {
            Rx3Apollo.single(this)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    ::parseArtistsResponse,
                    ::onFailure
                )
        }
    }

    private fun loadArtistsPaginated(searchQuery: String) {
    }

    private fun parseArtistsResponse(artistsResponse: ApolloResponse<ArtistsByNameQuery.Data>) {
        Log.d(TAG, "Success ${artistsResponse.data}")
        artistsResponse.data?.search?.artists?.nodes?.filterNotNull()?.apply {
            this.map { node ->
                Artist(node.name)
            }.let { list ->
                _artists.postValue(list)
            }
        }
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

    fun obtainMoreArtists() {
    }

    @Suppress("UNCHECKED_CAST")
    class ArtistsViewModelFactory(
        private val schedulerProvider: SchedulerProvider,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ArtistsViewModel(schedulerProvider) as T
        }
    }

    companion object {

        private const val TAG = "Artists"
        private val EMPTY_STRING: String = ""
        private const val DEFAULT_LIMIT: Int = 15
    }
}
package com.example.artists.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
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

    private var currentSearchQuery = EMPTY_STRING

    //The internal obtained list for artists to display
    private val _artists = MutableLiveData<List<Artist>>()

    // The external immutable LiveData for artists to display
    val artists: LiveData<List<Artist>> = _artists


    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == currentSearchQuery) return
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

    private fun parseArtistsResponse(artistsResponse: ApolloResponse<ArtistsByNameQuery.Data>) {
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
            Log.d(TAG, "throwable = " + throwable.message)
        } catch (e: Throwable) {
            Log.d(TAG, "throwable = " + e.message)
        }
    }

    fun obtainMoreArtists() {
        //todo
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
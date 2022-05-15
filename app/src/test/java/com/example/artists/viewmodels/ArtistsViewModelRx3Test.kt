package com.example.artists.viewmodels

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.apollographql.apollo3.rx3.Rx3Apollo
import com.example.artists.networking.apolloClient
import com.example.rocketreserver.ArtistsQuery
import io.mockk.mockk
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [ArtistsViewModelRx3]
 * @author Mikhail Avdeev (avdeev.m92@gmail.com)
 */
class ArtistsViewModelRx3Test {

    private val artistsNodesListObserver = mockk<Observer<List<ArtistsQuery.Node>>>(relaxed = true)

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    //todo private val statusObserver = mockk<Observer<ArtistsApiStatus>>(relaxed = true)
    private val artistsObserver = mockk<Observer<List<Artist>>>(relaxed = true)
    private lateinit var viewModelRx3: ArtistsViewModelRx3
    private var loader: ClassLoader = ClassLoader.getSystemClassLoader()
    private val testSchedulerProvider = TestSchedulerProvider()
    //todo check if needed private val fullArtistsResponse = Files.lines(Paths.get(loader.getResource(FULL_SHELF_FILE_PATH).toURI())).parallel().collect(Collectors.joining()).run { //todo adapter.fromJson(this) }
    //todo check if needed private val testSingleArtist = Files.lines(Paths.get(loader.getResource(SINGLE_BROCHURE_FILE_PATH).toURI())).parallel().collect(Collectors.joining()).run {  //todo adapter.fromJson(this) }

    @Before
    fun setupTest() {
    }

    @Test
    fun responseTest() {
        var artistData: ArtistsQuery.Data? = null
        val schedulerProvider = TestSchedulerProvider()
        apolloClient.query(ArtistsQuery()).run {
            Rx3Apollo.single(this)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { artistData = it.data },//Log.d("artists", "success! : ${it.data.toString()}") },
                    { Log.d("artists", "error! : $it") }
                )
        }
        assertNotNull(artistData)
    }

    @Test
    fun parseTestResponse() {
        viewModelRx3 = ArtistsViewModelRx3(testSchedulerProvider)
        //todo viewModel.status.observeForever(statusObserver)
        viewModelRx3.artistsNodesList.observeForever(artistsNodesListObserver)
        viewModelRx3.artistsNodesList.observeForever { println("artistsData: $it") }
        io.mockk.verify {
            artistsNodesListObserver.onChanged(
                listOf(
                    ArtistsQuery.Node(
                        id = "QXJ0aXN0OmMzODc5MDVlLWExODUtNDMwMC04ZmZmLTcxODM3ZDgzODhiNw==",
                        name = "John Myer",
                        disambiguation = null
                    )
                )
            )
        }
    }

    @Test
    fun getLoadingStatusForNonObtainedResponse() {
        viewModelRx3 = ArtistsViewModelRx3(testSchedulerProvider)
        //todo viewModel.status.observeForever(statusObserver)
        //todo verify { statusObserver.onChanged(ArtistsApiStatus.LOADING) }
    }

    @Test
    fun parseEmptyResponse() {
        viewModelRx3 = ArtistsViewModelRx3(testSchedulerProvider)
        //todo viewModel.status.observeForever(statusObserver)
        viewModelRx3.artists.observeForever(artistsObserver)
        //todo verify { statusObserver.onChanged(ArtistsApiStatus.DONE) }
        viewModelRx3.artists.value
    }

    @Test
    fun parseSingleArtist() {
        //todo
        viewModelRx3 = ArtistsViewModelRx3(testSchedulerProvider)
        //todo viewModel.status.observeForever(statusObserver)
        viewModelRx3.artists.observeForever(artistsObserver)
        //todo verify { statusObserver.onChanged(ArtistsApiStatus.DONE) }
        viewModelRx3.artists.value?.let { list ->
            val expectedArtist = Artist(
                id = "id",
                name = "name",
                disambiguation = null)
            val actualArtist = list[0]
            assertEquals(expectedArtist, actualArtist)
        } ?: Assert.fail("Should not have thrown any exception")
    }

    companion object {
        private const val FULL_SHELF_FILE_PATH = "test_shelf_response.json"
        private const val SINGLE_BROCHURE_FILE_PATH = "test_single_brochure_response.json"
    }
}
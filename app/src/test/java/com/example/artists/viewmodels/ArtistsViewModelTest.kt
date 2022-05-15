package com.example.artists.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

/**
 * Tests for [ArtistsViewModel]
 * @author Mikhail Avdeev (avdeev.m92@gmail.com)
 */
class ArtistsViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    //todo private val statusObserver = mockk<Observer<ArtistsApiStatus>>(relaxed = true)
    private val artistsObserver = mockk<Observer<List<Artist>>>(relaxed = true)
    private lateinit var viewModel: ArtistsViewModel
    private var loader: ClassLoader = ClassLoader.getSystemClassLoader()
    private val testSchedulerProvider = TestSchedulerProvider()
    private val fullArtistsResponse =
        Files.lines(Paths.get(loader.getResource(FULL_SHELF_FILE_PATH).toURI()))
            .parallel()
            .collect(Collectors.joining()).run { //todo adapter.fromJson(this)
                 }
    private val testSingleArtist =
        Files.lines(Paths.get(loader.getResource(SINGLE_BROCHURE_FILE_PATH).toURI()))
            .parallel()
            .collect(Collectors.joining()).run {  //todo adapter.fromJson(this)
            }

    @Before
    fun setupTest() {
    }

    @Test
    fun getLoadingStatusForNonObtainedResponse() {
        viewModel = ArtistsViewModel(testSchedulerProvider)
        //todo viewModel.status.observeForever(statusObserver)
        //todo verify { statusObserver.onChanged(ArtistsApiStatus.LOADING) }
    }

    @Test
    fun parseEmptyResponse() {
        viewModel = ArtistsViewModel(testSchedulerProvider)
        //todo viewModel.status.observeForever(statusObserver)
        viewModel.artists.observeForever(artistsObserver)
        //todo verify { statusObserver.onChanged(ArtistsApiStatus.DONE) }
        verify { artistsObserver.onChanged(emptyList()) }
    }

    @Test
    fun parseFullTestResponse() {
        viewModel = ArtistsViewModel(testSchedulerProvider)
        //todo viewModel.status.observeForever(statusObserver)
        viewModel.artists.observeForever(artistsObserver)
        //todo verify { statusObserver.onChanged(ArtistsApiStatus.DONE) }
        viewModel.artists.value?.let { list ->
            Assert.assertFalse(list.isEmpty())
        } ?: Assert.fail("Should not have thrown any exception")
    }

    @Test
    fun parseSingleArtist() {
        //todo
        viewModel = ArtistsViewModel(testSchedulerProvider)
        //todo viewModel.status.observeForever(statusObserver)
        viewModel.artists.observeForever(artistsObserver)
        //todo verify { statusObserver.onChanged(ArtistsApiStatus.DONE) }
        viewModel.artists.value?.let { list ->
            val expectedArtist = Artist(
                id ="id",
                name = "name",
                disambiguation = null )
            val actualArtist = list[0]
            assertEquals(expectedArtist, actualArtist)
        } ?: Assert.fail("Should not have thrown any exception")
    }

    companion object {
        private const val FULL_SHELF_FILE_PATH = "test_shelf_response.json"
        private const val SINGLE_BROCHURE_FILE_PATH = "test_single_brochure_response.json"
    }
}
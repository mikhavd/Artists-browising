package com.example.artists.presentations.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.artists.R
import com.example.artists.schedulers.AppSchedulerProvider
import com.example.artists.viewmodels.Artist
import com.example.artists.viewmodels.ArtistsViewModelRx3

/**
 * A fragment representing a list of Items.
 *
 * @author Mikhail Avdeev (avdeev.m92@gmail.com)
 */
class ArtistsListFragment : Fragment() {

    private val viewModel: ArtistsViewModelRx3 by viewModels {
        ArtistsViewModelRx3.ArtistsViewModelFactory(
            AppSchedulerProvider()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_items_artists_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.artists.observe(viewLifecycleOwner, ::createArtistsAdapter)
    }

    private fun createArtistsAdapter(list: List<Artist>) {
        (view as RecyclerView).adapter = ArtistsRecyclerViewAdapter(list)
    }

    companion object {

        fun newInstance() = ArtistsListFragment()
    }
}
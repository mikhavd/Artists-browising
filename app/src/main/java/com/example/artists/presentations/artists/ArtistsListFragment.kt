package com.example.artists.presentations.artists

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.artists.R
import com.example.artists.schedulers.AppSchedulerProvider
import com.example.artists.utils.autoCleared
import com.example.artists.viewmodels.Artist
import com.example.artists.viewmodels.ArtistsViewModel

/**
 * A fragment representing a list of Items.
 *
 * @author Mikhail Avdeev (avdeev.m92@gmail.com)
 */
class ArtistsListFragment : Fragment() {

    private var noResultsMessageView: TextView by autoCleared()
    private var searchQueryEditText: EditText by autoCleared()
    private var artistsRecyclerView: RecyclerView by autoCleared()
    private val artistsViewModel: ArtistsViewModel by viewModels {
        ArtistsViewModel.ArtistsViewModelFactory(
            AppSchedulerProvider()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artists, container, false)
        artistsRecyclerView = view.findViewById(R.id.artists_list)
        searchQueryEditText = view.findViewById(R.id.query_input)
        noResultsMessageView = view.findViewById(R.id.no_results_text_view)
        // Set the adapter
        artistsRecyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artistsViewModel.artists.observe(viewLifecycleOwner, ::createArtistsAdapter)
        initSearchInputListener()
    }

    private fun createArtistsAdapter(list: List<Artist>) {
        artistsRecyclerView.adapter = ArtistsRecyclerViewAdapter(list)
    }

    private fun initSearchInputListener() {
        searchQueryEditText.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        searchQueryEditText.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val query = searchQueryEditText.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        artistsViewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    companion object {

        fun newInstance() = ArtistsListFragment()
    }
}
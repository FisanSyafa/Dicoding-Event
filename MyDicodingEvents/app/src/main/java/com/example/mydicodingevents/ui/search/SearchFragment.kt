package com.example.mydicodingevents.ui.search

import android.content.Intent
import com.example.mydicodingevents.data.remote.response.ListEventsItem
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydicodingevents.databinding.FragmentSearchBinding
import com.example.mydicodingevents.ui.DetailEventActivity
import com.example.mydicodingevents.ui.EventAdapter
import com.example.mydicodingevents.ui.EventViewModel

@Suppress("SameParameterValue")
class SearchFragment : Fragment()  {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val eventViewModel: EventViewModel by viewModels()

    companion object{
        private const val TAG = "Search Fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventAdapter = EventAdapter(object : EventAdapter.OnItemClickCallback {
            override fun onItemClick(event: ListEventsItem) {
                val intent = Intent(requireContext(), DetailEventActivity::class.java)
                intent.putExtra(DetailEventActivity.EXTRA_EVENT, event)
                startActivity(intent)
            }
        })

        binding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = eventAdapter
        }

        eventViewModel.searchResults.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
            binding.progressBar.visibility = View.GONE
        }

        eventViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        eventViewModel.messageError.observe(viewLifecycleOwner) { isError ->
            if (isError != null) {
                showErrorToast(isError)
            }
        }

        with(binding) {
            searchView.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val query = s?.toString()?.trim()
                    if (!query.isNullOrEmpty()) {
                        try {
                            eventViewModel.searchEvents(query)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error : ${e.message}")
                        }
                    }
                }
            })

            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    Toast.makeText(requireContext(), searchView.text, Toast.LENGTH_SHORT)
                        .show()
                    false
                }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
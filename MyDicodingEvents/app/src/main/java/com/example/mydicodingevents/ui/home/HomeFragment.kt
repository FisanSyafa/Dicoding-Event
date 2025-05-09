package com.example.mydicodingevents.ui.home

import com.example.mydicodingevents.data.remote.response.ListEventsItem
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydicodingevents.databinding.FragmentHomeBinding
import com.example.mydicodingevents.ui.DetailEventActivity
import com.example.mydicodingevents.ui.EventAdapter
import com.example.mydicodingevents.ui.EventViewModel

@Suppress("SameParameterValue")
class HomeFragment : Fragment(), EventAdapter.OnItemClickCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerViews()
        observeViewModel()

        return root
    }

    private fun setupRecyclerViews() {
        val finishedLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvent.layoutManager = finishedLayoutManager

        val upcomingLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvFinishedEvent.layoutManager = upcomingLayoutManager
    }

    private fun observeViewModel() {
        eventViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            setUpcomingEventData(events)
        }

        eventViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            setFinishedEventData(events)
        }

        eventViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        eventViewModel.messageError.observe(viewLifecycleOwner) { isError ->
            if (isError != null) {
                showErrorToast(isError)
            }
        }

        eventViewModel.findEvents()
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setUpcomingEventData(events: List<ListEventsItem>) {
        val adapter = EventAdapter(this)
        adapter.submitList(events)
        binding.rvUpcomingEvent.adapter = adapter
    }

    private fun setFinishedEventData(events: List<ListEventsItem>) {
        val adapter = EventAdapter(this)
        adapter.submitList(events)
        binding.rvFinishedEvent.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onItemClick(event: ListEventsItem) {
        val intent = Intent(requireContext(), DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT, event)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
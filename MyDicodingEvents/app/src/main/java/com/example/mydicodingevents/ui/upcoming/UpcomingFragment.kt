package com.example.mydicodingevents.ui.upcoming

import com.example.mydicodingevents.data.remote.response.ListEventsItem
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydicodingevents.ui.EventAdapter
import com.example.mydicodingevents.databinding.FragmentUpcomingBinding
import com.example.mydicodingevents.ui.DetailEventActivity
import com.example.mydicodingevents.ui.EventViewModel

@Suppress("unused", "SameParameterValue", "RedundantSuppression")
class UpcomingFragment : Fragment(), EventAdapter.OnItemClickCallback {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel: EventViewModel by viewModels()

    companion object{
        private const val NO_INTERNET = "No network connection. Please check your internet."
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val upcomingLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvUpcomingList.layoutManager = upcomingLayoutManager

        observeViewModel()

        return root
    }

    private fun observeViewModel() {
        eventViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            setEventData(events)
        }

        eventViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        eventViewModel.messageError.observe(viewLifecycleOwner) { isError ->
            if (isError != null) {
                showErrorToast(isError)
            }
        }

        eventViewModel.findUpcomingEvent()
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(event: ListEventsItem) {
        val intent = Intent(requireContext(), DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT, event)
        startActivity(intent)
    }

    private fun setEventData(event: List<ListEventsItem>) {
        val adapter = EventAdapter(this)
        adapter.submitList(event)
        binding.rvUpcomingList.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
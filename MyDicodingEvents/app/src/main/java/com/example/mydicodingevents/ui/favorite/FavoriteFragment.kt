package com.example.mydicodingevents.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydicodingevents.data.local.entity.EventFavEntity
import com.example.mydicodingevents.data.remote.response.ListEventsItem
import com.example.mydicodingevents.databinding.FragmentFavoriteBinding
import com.example.mydicodingevents.ui.DetailEventActivity
import com.example.mydicodingevents.ui.EventAdapter
import com.example.mydicodingevents.ui.FavoriteModelFactory

class FavoriteFragment : Fragment(), EventAdapter.OnItemClickCallback {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: FavoriteModelFactory
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        factory = FavoriteModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFav.layoutManager = layoutManager

        observeViewModel()

        return root
    }

    private fun observeViewModel() {
        viewModel.getAllFavorite()?.observe(viewLifecycleOwner) { favoriteEvents ->
            setEventData(favoriteEvents)
        }

    }

    private fun setEventData(e: List<EventFavEntity>) {
        val event = e.map { favoriteEvent -> favoriteEvent.isListEventsItem() }
        val adapter = EventAdapter(this)
        adapter.submitList(event)
        binding.rvFav.adapter = adapter
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
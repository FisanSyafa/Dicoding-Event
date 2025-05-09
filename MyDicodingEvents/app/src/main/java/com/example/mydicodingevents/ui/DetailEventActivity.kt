package com.example.mydicodingevents.ui

import com.example.mydicodingevents.data.remote.response.ListEventsItem
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mydicodingevents.R
import com.example.mydicodingevents.databinding.ActivityDetailEventBinding
import com.example.mydicodingevents.ui.favorite.FavoriteViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var factory: FavoriteModelFactory
    private lateinit var eventViewModel: FavoriteViewModel

    companion object {
        const val EXTRA_EVENT = "extra_event"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Event"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ivImage = binding.ivImage
        val tvEventName = binding.tvEventName
        val tvOwnerName = binding.tvOwnerName
        val tvTimeBegin = binding.tvTimeBegin
        val tvQuota = binding.tvQuota
        val tvDescription = binding.tvDescription
        val btnRegister = binding.btnRegister
        val favoriteFab = binding.favoriteFab

        val event: ListEventsItem? = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_EVENT, ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_EVENT)
        }

        if (event != null) {
            factory = FavoriteModelFactory.getInstance(this)
            eventViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
            val oriDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val targetDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
            val date = oriDateFormat.parse(event.beginTime)
            val dateFormat = date?.let { targetDateFormat.format(it) }

            eventViewModel.getFavoriteById(event.id)?.observe(this) { favoriteEvent ->
                var isFavorite = favoriteEvent != null
                favoriteFab.setImageResource(if (isFavorite) R.drawable.ic_favorite_24dp else R.drawable.ic_favorite_border_24dp)
                if (!isFavorite) {
                    favoriteFab.setOnClickListener {
                        isFavorite = true
                        eventViewModel.insertFavorite(event.isEventFavEntity())
                        Toast.makeText(this, "Event added to favorite", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    favoriteFab.setOnClickListener {
                        isFavorite = false
                        eventViewModel.deleteFavoriteById(event.id)
                        Toast.makeText(this, "Event removed from favorite", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            tvEventName.text = event.name
            tvOwnerName.text = event.ownerName
            tvTimeBegin.text = dateFormat
            tvQuota.text = getString(R.string.quota, event.quota - event.registrants)
            tvDescription.text = HtmlCompat.fromHtml(
                getString(R.string.description, event.description),
                HtmlCompat.FROM_HTML_MODE_LEGACY)
            Glide.with(this)
                .load(event.imageLogo)
                .into(ivImage)

            btnRegister.setOnClickListener {
                val url = event.link //
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                }
                startActivity(intent)
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
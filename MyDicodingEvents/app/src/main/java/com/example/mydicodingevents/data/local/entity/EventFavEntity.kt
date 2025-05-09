package com.example.mydicodingevents.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mydicodingevents.data.remote.response.ListEventsItem
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class EventFavEntity(
    @PrimaryKey
    var id: Int,
    var name: String,
    val summary: String,
    val mediaCover: String,
    val registrants: Int,
    val imageLogo: String,
    val link: String,
    val description: String,
    val ownerName: String,
    val cityName: String,
    val quota: Int,
    val beginTime: String,
    val endTime: String,
    val category: String
) : Parcelable {

    fun isListEventsItem(): ListEventsItem {
        return ListEventsItem(
            id = id,
            name = name,
            summary = summary,
            mediaCover = mediaCover,
            registrants = registrants,
            quota = quota,
            description = description,
            ownerName = ownerName,
            imageLogo = imageLogo,
            beginTime = beginTime,
            link = link,
            cityName = cityName,
            category = category,
            endTime = endTime
        )
    }
}
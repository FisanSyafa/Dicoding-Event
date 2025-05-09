package com.example.mydicodingevents.data.remote.response

import android.os.Parcelable
import com.example.mydicodingevents.data.local.entity.EventFavEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventResponse(

	val listEvents: List<ListEventsItem>,
	val error: Boolean,
	val message: String
) : Parcelable

@Parcelize
data class ListEventsItem(
	val summary: String,
	val mediaCover: String,
	val registrants: Int,
	val imageLogo: String,
	val link: String,
	val description: String,
	val ownerName: String,
	val cityName: String,
	val quota: Int,
	val name: String,
	val id: Int,
	val beginTime: String,
	val endTime: String,
	val category: String
) : Parcelable{
	fun isEventFavEntity(): EventFavEntity {
		return EventFavEntity(
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

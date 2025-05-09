package com.example.mydicodingevents.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mydicodingevents.data.local.entity.EventFavEntity

@Dao
interface EventDao {
    @Query("SELECT * from EventFavEntity ORDER BY id ASC")
    fun getAllFavorite(): LiveData<List<EventFavEntity>>?

    @Query("SELECT * FROM EventFavEntity WHERE id = :id")
    fun getFavoriteById(id: Int): LiveData<EventFavEntity>?

    @Query("DELETE FROM EventFavEntity WHERE id = :id")
    fun deleteFavoriteById(id: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(eventFavEntity: EventFavEntity)
}
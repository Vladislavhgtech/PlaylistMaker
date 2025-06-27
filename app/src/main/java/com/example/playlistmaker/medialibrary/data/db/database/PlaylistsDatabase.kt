package com.example.playlistmaker.medialibrary.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.playlistmaker.medialibrary.data.db.dao.PlaylistDao
import com.example.playlistmaker.medialibrary.data.db.dao.TrackAndPlaylistDao
import com.example.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.example.playlistmaker.medialibrary.data.db.entity.TrackAndPlaylistEntity

@Database(version = 1, entities = [PlaylistEntity::class, TrackAndPlaylistEntity::class])
abstract class PlaylistsDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao

    abstract fun trackPlaylistDao(): TrackAndPlaylistDao
}
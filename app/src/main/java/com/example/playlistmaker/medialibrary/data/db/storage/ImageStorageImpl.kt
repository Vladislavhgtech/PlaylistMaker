package com.example.playlistmaker.medialibrary.data.db.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

import com.example.playlistmaker.medialibrary.domain.others.ImageStorage

class ImageStorageImpl(private val context: Context) : ImageStorage {

    override fun saveImageToPrivateStorage(uri: Uri): String {

        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "krasavchik"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val imageName = Calendar.getInstance().time.toString()
        val file = File(filePath, imageName)

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 40, outputStream)

        return imageName
    }

    override fun getImageFromPrivateStorage(imageName: String): Uri {

        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "krasavchik"
        )
        val file = File(filePath, imageName)
        return file.toUri()
    }
}
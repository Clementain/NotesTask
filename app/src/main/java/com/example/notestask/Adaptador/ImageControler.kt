package com.example.notestask.Adaptador

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.notestask.Fragmentos.FragmentoCrearNotas
import java.io.File

object ImageControler {
    fun selectPhotoFromGallery(activity: FragmentoCrearNotas, code: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    fun saveImage(context: Context, id: Long, uri: Uri, tipo: String) {
        val file = File(context.filesDir, tipo + id.toString())

        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()!!

        file.writeBytes(bytes)
    }

    fun getImageUri(context: Context, id: String): Uri {
        val file = File(context.filesDir, id)

        return if (file.exists()) Uri.fromFile(file)
        else Uri.parse("android.resource://com.example.notestask/drawable/ic_launcher_background")
    }

    fun deleteImage(context: Context, id: Long) {
        val file = File(context.filesDir, id.toString())
        file.delete()
    }
}
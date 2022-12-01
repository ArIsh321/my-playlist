package com.test.myplaylist.extension

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import org.apache.commons.io.FileUtils
import java.io.File

fun Context.getMimeType( uri: Uri): String? {
    //Check uri format to avoid null
    val extension: String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        //If scheme is a content
        val mime = MimeTypeMap.getSingleton()
        mime.getExtensionFromMimeType(this.contentResolver.getType(uri))
    } else {
        //If scheme is a File
        //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
        MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
    }
    return extension
}

 fun Context.createTmpFileFromUri(
    uri: Uri,
    fileName: String,
    mimeType: String
): File? {
    return try {
        val stream = this.contentResolver.openInputStream(uri)
        val file = File.createTempFile(fileName, mimeType, this.cacheDir)
        FileUtils.copyInputStreamToFile(stream, file)
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

 fun Context.deleteTempFiles(file: File = this.cacheDir): Boolean {
    if (file.isDirectory) {
        val files = file.listFiles()
        if (files != null) {
            for (f in files) {
                if (f.isDirectory) {
                    deleteTempFiles(f)
                } else {
                    f.delete()
                }
            }
        }
    }
    return file.delete()
}




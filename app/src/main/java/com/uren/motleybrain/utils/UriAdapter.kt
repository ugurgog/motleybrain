package com.uren.motleybrain.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils

import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.common.util.IOUtils

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.Objects

@SuppressLint("Registered")
class UriAdapter : AppCompatActivity() {
    companion object {

        fun getPathFromGalleryUri(context: Context, uri: Uri): String? {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (DocumentsContract.isDocumentUri(context, uri)) {
                    // ExternalStorageProvider
                    if (isExternalStorageDocument(uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split =
                            docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val type = split[0]

                        if ("primary".equals(type, ignoreCase = true)) {
                            return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                        }

                        // TODO handle non-primary volumes
                    } else if (isDownloadsDocument(uri)) {

                        try {
                            val id = DocumentsContract.getDocumentId(uri)
                            val contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                java.lang.Long.valueOf(id)
                            )

                            return getDataColumn(context, contentUri, null, null)
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }

                    } else if (isMediaDocument(uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split =
                            docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val type = split[0]

                        var contentUri: Uri? = null
                        if ("image" == type) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        } else if ("video" == type) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        } else if ("audio" == type) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }

                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])

                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }// MediaProvider
                    // DownloadsProvider
                } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

                    // Return the remote address
                    return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                        context,
                        uri,
                        null,
                        null
                    )

                } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                    return uri.path
                }// File
                // MediaStore (and general)
            } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )

            } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                return uri.path
            }// File

            return null
        }


        fun getDataColumn(
            context: Context,
            uri: Uri?,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {

            val column = "_data"
            val projection = arrayOf(column)
            context.contentResolver.query(
                uri!!,
                projection,
                selection,
                selectionArgs,
                null
            )!!.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            }
            return null
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "Title",
                null
            )
            return Uri.parse(path)
        }

        fun getRealPathFromURI(contentUri: Uri, context: Context): String? {
            var res: String? = null
            try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = context.contentResolver.query(contentUri, proj, null, null, null)

                if (cursor == null) {
                    res = contentUri.path
                } else if (cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    res = cursor.getString(column_index)
                }
                Objects.requireNonNull<Cursor>(cursor).close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return res
        }

        fun getFilePathFromURI(context: Context, contentUri: Uri, mediaType: Int): String? {
            val fileName = getFileName(context, contentUri)
            if (!TextUtils.isEmpty(fileName)) {
                val copyFile = FileAdapter.getOutputMediaFile(mediaType)
                copy(context, contentUri, copyFile)
                return Objects.requireNonNull(copyFile)?.absolutePath
            }
            return null
        }

        fun getFileName(context: Context, uri: Uri?): String? {
            var fileName: String? = null

            if (uri == null) return null
            val path = uri.path
            val cut = Objects.requireNonNull<String>(path).lastIndexOf('/')
            if (cut != -1) {
                fileName = path!!.substring(cut + 1)
            }
            return fileName
        }

        fun copy(context: Context, srcUri: Uri, dstFile: File?) {
            try {
                val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
                val outputStream = FileOutputStream(dstFile!!)
                IOUtils.copyStream(inputStream, outputStream)
                inputStream.close()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}

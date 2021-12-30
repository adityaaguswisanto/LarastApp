package com.larast.larast.data.helper

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.larast.larast.BuildConfig
import com.larast.larast.R
import com.larast.larast.data.network.Resource
import com.larast.larast.ui.auth.forgot.ForgotFragment
import com.larast.larast.ui.auth.login.LoginFragment
import com.larast.larast.ui.home.HomeActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/** For Make Toast a View */
fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/** For Make Launch Intent a Activity */
fun <A : Activity> Activity.launchActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

/** For Make alert Dialog a View */
fun Context.alertDialog(
    msg: String,
    positive: String,
    negative: String,
    listener: DialogInterface.OnClickListener
) {
    val alert = AlertDialog.Builder(this)
    alert.setTitle("Apakah Anda Yakin ?")
    alert.setMessage(msg)
    alert.setPositiveButton(
        positive, listener
    )
    alert.setNegativeButton(
        negative, null
    )
    val alertDialog = alert.create()
    alertDialog.show()
}

/** For Make Url from Api */
fun urlAssets() = "${BuildConfig.BASE_URL}/foto/"

/** For Make Visible, Gone and Invisible a View */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

/** For Make Glide a View */
fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .error(R.drawable.ic_broken)
        .into(this)
}

/** For Make Permission External Storage*/
fun Fragment.permissionExternalStorage() = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) {
    if (it)
        Log.i("Message", "Info: Is Granted")
    else
        Log.i("Message", "Info: Is Denied")
}

/** For Make Download Filing*/
fun Fragment.downloadPdf(url: String) {
    val request = DownloadManager.Request(Uri.parse("${urlAssets()}${url}"))
    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
    request.setTitle("Surat Pengantar")
    request.setDescription("Surat Pengantar sedang di download...")

    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setDestinationInExternalPublicDir(
        Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}.pdf"
    )
    val dm = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    dm.enqueue(request)
    requireContext().toast("Mendownload surat pengantar...")
}

/** For Make Qr Code*/
fun ImageView.createQrCode(code: String?) {
    val writer = QRCodeWriter()
    try {
        val bitMatrix = writer.encode(code, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        setImageBitmap(bmp)
    } catch (e: WriterException) {
        e.printStackTrace()
    }
}

/** For Make Format Date*/
fun formatDate(dateString: String): String? {
    try {
        var sd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sd.timeZone = TimeZone.getTimeZone("GMT")
        val d: Date = sd.parse(dateString)
        sd = SimpleDateFormat("dd MMMM yyyy")
        return sd.format(d)
    } catch (e: ParseException) {
    }
    return ""
}

fun formatDateWithTime(dateString: String): String? {
    try {
        var sd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sd.timeZone = TimeZone.getTimeZone("GMT")
        val d: Date = sd.parse(dateString)
        sd = SimpleDateFormat("dd MMMM yyyy - HH:mm:ss")
        return sd.format(d)
    } catch (e: ParseException) {
    }
    return ""
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.logout() {
    (activity as HomeActivity).performLogout()
}

/** For Make Multipart Filing a Image Post */
fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> requireView().snackbar(
            "Please check your internet connection", retry
        )
        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackbar("Email atau password salah")
            } else {
                logout()
            }
        }
        failure.errorCode == 500 -> {
            if (this is ForgotFragment) {
                requireView().snackbar("Kami tidak dapat menemukan pengguna dengan alamat email tersebut.")
            }
        }
        failure.errorCode == 422 -> {
            if (this is ForgotFragment) {
                requireView().snackbar("Mohon tunggu sebentar dan coba kembali.")
            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }
    }
}
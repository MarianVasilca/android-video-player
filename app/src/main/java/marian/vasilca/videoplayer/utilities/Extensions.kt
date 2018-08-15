package marian.vasilca.videoplayer.utilities

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat


fun String.hasPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, this) == PackageManager.PERMISSION_GRANTED

fun String.toUri(): Uri = Uri.parse(this)
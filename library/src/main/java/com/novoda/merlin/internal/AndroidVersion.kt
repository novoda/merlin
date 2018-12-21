package com.novoda.merlin.internal

import android.os.Build

class AndroidVersion(val androidVersion: Int = Build.VERSION.SDK_INT) {

    fun isLollipopOrHigher(): Boolean = androidVersion >= Build.VERSION_CODES.LOLLIPOP

}

package com.myapplication.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutId: Int) : View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

inline fun <reified T : Activity> Activity.takeMeThere(iWillBeNoMore: Boolean = true, bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)

    bundle?.let {
        intent.putExtras(bundle)
    }

    startActivity(intent)

    if(iWillBeNoMore) {
        finish()
    }


}
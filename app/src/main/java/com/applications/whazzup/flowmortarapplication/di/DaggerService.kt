package com.applications.whazzup.flowmortarapplication.di

import android.content.Context

object DaggerService {

    val SERVICE_NAME = "SERVICE_NAME"

    fun <T> getDaggerComponent(context: Context): T {
        @Suppress("UNCHECKED_CAST")
        return context.getSystemService(SERVICE_NAME) as T
    }
}
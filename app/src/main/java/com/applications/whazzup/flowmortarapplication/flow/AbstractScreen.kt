package com.applications.whazzup.flowmortarapplication.flow

import android.util.Log
import com.applications.whazzup.flowmortarapplication.mortar.ScreenScoper
import flow.ClassKey


abstract class AbstractScreen<in T> : ClassKey() {

    val scopeName: String
        get() = javaClass.name

    abstract fun createScreenComponent(parentComponent: T): Any

    fun unregisterScope() {
        Log.e(TAG, "unregisterScope: " + this.scopeName)
        ScreenScoper.destroyScreenScope(scopeName)
    }

    fun unregisterScopeByName(scopeName : String){
        ScreenScoper.destroyScreenScope(scopeName)
    }

    val layoutResId: Int
        get() {
            val layout: Int

            val screen: Screen?
            screen = this.javaClass.getAnnotation(Screen::class.java)
            if (screen == null) {
                throw IllegalStateException("@ScreenAnnotations is missing on screen " + scopeName)
            } else {
                layout = screen.value
            }
            return layout
        }

    companion object {
        val TAG = "ABSTRACT_SCREEN"
    }
}
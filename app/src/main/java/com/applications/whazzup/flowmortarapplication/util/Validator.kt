package com.applications.whazzup.flowmortarapplication.util

import android.text.Editable
import java.util.regex.Pattern


class Validator {

    companion object {
        @JvmStatic
        fun isValidateEmail(s: Editable): Boolean {
            val pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
            val matcher = pattern.matcher(s)
            return matcher.matches()
        }
        @JvmStatic
        fun isValidateLogin(s : Editable) : Boolean{
            val pattern = Pattern.compile("([a-zA-z0-9\\d\\u005f]){3,}")
            val matcher = pattern.matcher(s)
            return matcher.matches()
        }
        @JvmStatic
        fun isValidatePassword(s : Editable): Boolean {
            val pattern = Pattern.compile("([a-zA-z0-9\\d]){8,}")
            val matcher = pattern.matcher(s)
            return matcher.matches()
        }
    }

}
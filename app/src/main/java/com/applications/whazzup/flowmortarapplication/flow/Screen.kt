package com.applications.whazzup.flowmortarapplication.flow

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by Alex on 21.09.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class Screen(val value: Int)
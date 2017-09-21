package com.applications.whazzup.flowmortarapplication.di

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope
import kotlin.reflect.KClass

@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class DaggerScope(val value: KClass<*>)
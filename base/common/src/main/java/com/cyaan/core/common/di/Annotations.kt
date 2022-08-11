package com.cyaan.core.common.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoTokenRetrofit


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SimpleRetrofit
package com.techadive.pixabay.common

import android.content.Context
import javax.inject.Inject

class MarketManager @Inject constructor(
    context: Context
) {
    val language: String = context.resources.configuration.locales[0].country
}
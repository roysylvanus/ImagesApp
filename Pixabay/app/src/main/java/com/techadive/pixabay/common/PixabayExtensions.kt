package com.techadive.pixabay.common

fun String.convertToList(): List<String> =
    this.split(", ").map { it.trim() }
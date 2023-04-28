package com.mtc.finalproject_bootcamp.utils

class Constants {
    companion object {
        const val BASE_URL = "http://kasimadalan.pe.hu/"
        const val FOOD_IMAGE_URL = "http://kasimadalan.pe.hu/yemekler/resimler/"
        const val FIREBASE_FAVORITES_DB = "favorites"
        val turkishCharsRegex = Regex("[\u0080-\uFFFF]")
    }
}
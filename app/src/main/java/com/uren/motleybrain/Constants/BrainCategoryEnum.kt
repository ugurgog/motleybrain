package com.uren.motleybrain.Constants

import android.content.Context
import com.uren.motleybrain.R

enum class BrainCategoryEnum private constructor(  
    val id: Int,
    val levelTitle: Int,
    val levelDesc: Int
) {

    AWFUL_BRAIN(1, R.string.AWFUL_LEVEL, R.string.AWFUL_LEVEL_DESC),
    POOR_BRAIN(2, R.string.POOR_LEVEL, R.string.POOR_LEVEL_DESC),
    NORMAL_BRAIN(3, R.string.NORMAL_LEVEL, R.string.NORMAL_LEVEL_DESC),
    GOOD_BRAIN(4, R.string.GOOD_LEVEL, R.string.GOOD_LEVEL_DESC),
    SUPER_BRAIN(5, R.string.SUPER_LEVEL, R.string.SUPER_LEVEL_DESC),
    MEGA_BRAIN(6, R.string.MEGA_LEVEL, R.string.MEGA_LEVEL_DESC);


    companion object {

        fun getById(id: Int): BrainCategoryEnum? {
            for (e in values()) {
                if (e.id == id)
                    return e
            }
            return null
        }
    }
}

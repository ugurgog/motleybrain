package com.uren.motleybrain.Constants

enum class LoginMethodEnum private constructor(val id: Int) {

    EMAIL(1),
    GOOGLE(2);


    companion object {

        fun getById(id: Int): LoginMethodEnum? {
            for (e in values()) {
                if (e.id == id)
                    return e
            }
            return null
        }
    }
}

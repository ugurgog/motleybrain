package com.uren.motleybrain.Models

import java.io.Serializable

class LoginUser : Serializable {

    var userId: String? = null
    var email: String? = null
    var name: String? = null
    var profilePhotoUrl: String? = null

    init {
        this.userId = ""
        this.email = ""
        this.name = ""
        this.profilePhotoUrl = ""
    }
}

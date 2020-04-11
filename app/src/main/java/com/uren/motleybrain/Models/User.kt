package com.uren.motleybrain.Models

import java.time.LocalDateTime
import java.util.Date

class User {
    var id: String? = null
    var name: String? = null
    var email: String? = null
    var profilePhotoUrl: String? = null
    var createDate: LocalDateTime? = null
    var updateDate: LocalDateTime? = null
    var isAdmin: Boolean = false


}

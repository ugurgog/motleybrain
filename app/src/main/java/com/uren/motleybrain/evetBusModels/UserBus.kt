package com.uren.motleybrain.evetBusModels

import com.uren.motleybrain.Models.User

class UserBus(user: User) {

    var user: User
        internal set

    init {
        this.user = user
    }
}

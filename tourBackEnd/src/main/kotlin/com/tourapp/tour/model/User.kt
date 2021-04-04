package com.tourapp.tour.model

import java.util.Date


data class User(var id: Int = -1, var name:String ="", var email: String="", var birthDate: Date = Date(),
                var password: String = "", var country: Country= Country())
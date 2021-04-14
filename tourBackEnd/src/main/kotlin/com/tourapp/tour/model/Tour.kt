package com.tourapp.tour.model

import java.util.*
import kotlin.collections.ArrayList

data class Tour(var id:Int = -1, var name: String = "", var description: String="", var rating: Int = -1,
                var leaveDate: Date? = Date(0,0,1), var returnDate: Date? = Date(4000,0,0),
                var price: Double = 0.0, var seats: Int = -1, var country: Country = Country(),
                var category: Category = Category(), var activities: ArrayList<Activity> = ArrayList(),
                var reviews: ArrayList<Review> = ArrayList())
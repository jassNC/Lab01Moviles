package com.tourapp.tour.model

data class Reservation(var id: Int = -1, var seats: Int = -1, var user: User = User(), var tour: Tour = Tour())
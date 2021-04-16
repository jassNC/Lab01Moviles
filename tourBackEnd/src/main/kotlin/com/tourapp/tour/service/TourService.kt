package com.tourapp.tour.service

import com.tourapp.tour.dao.TourDao
import com.tourapp.tour.model.*
import java.util.*
import kotlin.collections.ArrayList

class TourService {
    fun getCountries():ArrayList<Country>{
        return TourDao.getCountries()
    }

    fun registerUser(user: User):Boolean{
        return TourDao.putUser(user);
    }

    fun getUser(user: User): User? {
        return TourDao.getUser(user)
    }

    fun getTours():ArrayList<Tour>{
        return  TourDao.getTours()
    }

    fun getToursFiltered(tour: Tour): ArrayList<Tour>{
        return TourDao.getToursFiltered(tour)
    }

    fun getActivities(): ArrayList<Activity>{
        return TourDao.getActivities()
    }

    fun addFav(user: User): Boolean{
        return TourDao.addFav(user)
    }

    fun removeFav(user: User): Boolean{
        return TourDao.removeFav(user)
    }

    fun getFavs(user: User): ArrayList<Int>{
        return TourDao.getFavs(user)
    }

    fun getTourById(tour: Tour): Tour{
        return TourDao.getTourById(tour)
    }

    fun putReservation(reservation: Reservation): Boolean{
        return TourDao.putReservation(reservation)
    }

    fun getLinks(tour: Tour): ArrayList<String>{
        return TourDao.getLinks(tour)
    }


}
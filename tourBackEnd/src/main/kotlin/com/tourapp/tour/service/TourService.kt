package com.tourapp.tour.service

import com.tourapp.tour.dao.TourDao
import com.tourapp.tour.model.Activity
import com.tourapp.tour.model.Country
import com.tourapp.tour.model.Tour
import com.tourapp.tour.model.User
import java.util.*
import kotlin.collections.ArrayList

class TourService {
    fun getCountries():ArrayList<Country>{
        return TourDao.getCountries()
    }

    fun registerUser(user: User):Boolean{
        return TourDao.putUser(user);
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




}
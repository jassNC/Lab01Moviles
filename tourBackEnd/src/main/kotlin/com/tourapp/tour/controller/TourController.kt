package com.tourapp.tour.controller


import com.tourapp.tour.model.Country
import com.tourapp.tour.model.Tour
import com.tourapp.tour.model.User
import com.tourapp.tour.service.TourService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestBody

import org.springframework.web.bind.annotation.RequestMethod

import org.springframework.web.bind.annotation.RequestMapping
import java.io.File

@CrossOrigin
@RestController
@RequestMapping("/tourApi")
class TourController {
    val service = TourService()

    @RequestMapping(value =["/getUser"], method = [RequestMethod.POST])
    fun getUser(@RequestBody user: User): ResponseEntity<User>{
        println(user)
        return ResponseEntity.ok(service.getUser(user))
    }

    @RequestMapping(value = ["/putUser"], method = [RequestMethod.POST])
    fun putUser(@RequestBody user: User):ResponseEntity<Boolean> {
        println(user)
        return ResponseEntity.ok(service.registerUser(user));
    }

    @RequestMapping(value = ["/addFav"], method = [RequestMethod.POST])
    fun addFav(@RequestBody user: User):ResponseEntity<Boolean> {
        println(user)
        return ResponseEntity.ok(service.addFav(user));
    }

    @RequestMapping(value = ["/removeFav"], method = [RequestMethod.POST])
    fun removeFav(@RequestBody user: User):ResponseEntity<Boolean> {
        println(user)
        return ResponseEntity.ok(service.removeFav(user));
    }

    @RequestMapping(value = ["/getFavs"], method = [RequestMethod.POST])
    fun getFavs(@RequestBody user: User): ResponseEntity<ArrayList<Int>>{
        println("dasaddad")
        return ResponseEntity.ok(service.getFavs(user));
    }

    @RequestMapping(value =["/getCountries"], method = [RequestMethod.GET])
    fun getCountries(): ResponseEntity<ArrayList<Country>>{
        return ResponseEntity.ok(service.getCountries())
    }

    @RequestMapping(value = ["/getTours"], method = [RequestMethod.GET])
    fun getTours():ResponseEntity<ArrayList<Tour>> {
        return ResponseEntity.ok(service.getTours())
    }

    @RequestMapping(value = ["/getToursFiltered"], method = [RequestMethod.POST])
    fun getToursFiltered(@RequestBody tour: Tour):ResponseEntity<ArrayList<Tour>> {
        println(tour)
        return ResponseEntity.ok(service.getToursFiltered(tour))
    }

    @RequestMapping(value = ["/getTourById"], method = [RequestMethod.POST])
    fun getTourById(@RequestBody tour: Tour):ResponseEntity<Tour> {
        println(tour)
        return ResponseEntity.ok(service.getTourById(tour))
    }


    @RequestMapping(value = ["/helloWorld/{name}"], method = [(RequestMethod.GET)])
    fun getHelloWordMessageWithName(
        @PathVariable("name") name: String
    ): ResponseEntity<Any> =
        if (name != "Cristian") {
            ResponseEntity.ok(
                HelloResponse(
                    message = "Hello $name",
                    name = name
                )
            )
        } else {
            ResponseEntity.badRequest().body("I am Cristian")
        }
}

data class HelloResponse(
    val message: String,
    val name: String
)
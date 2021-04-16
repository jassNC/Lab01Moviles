package com.tourapp.tour.dao

import com.tourapp.tour.model.*
import java.sql.*
import java.time.LocalDate

import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Date
import kotlin.collections.ArrayList


object TourDao {
    private val username = "root"
    private val password = "root"
    private val host = "127.0.0.1"
    private val port = "3306"
    private var conn: Connection? = this.getConnection()

    @JvmStatic fun main(args: Array<String>) {
        getConnection()
        getCountries()
    }

    fun getCountries(): ArrayList<Country> {
        var stmt: Statement?
        var resultset: ResultSet?
        var result = ArrayList<Country>()

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.country;")

            while (resultset!!.next()) {
                result.add(Country(resultset.getInt("ID"),resultset.getString("NAME")))
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        println(getCountryById(5))
        return result;
    }

    fun getCountryById(id: Int): Country{
        var stmt: Statement?
        var resultset: ResultSet?
        var result = Country()

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.country WHERE ID = ${id};")

            if(resultset!!.next()) {
                result = Country(resultset.getInt("ID"),resultset.getString("NAME"))
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return result;
    }

    fun getCategoryById(id: Int): Category{
        var stmt: Statement?
        var resultset: ResultSet?
        var result = Category()

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.CATEGORY WHERE ID = ${id};")

            if(resultset!!.next()) {
                result = Category(resultset.getInt("ID"),resultset.getString("NAME"))
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return result;
    }

    fun getActivities(): ArrayList<Activity> {
        var stmt: Statement?
        var resultset: ResultSet?
        var result = ArrayList<Activity>()

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.ACTIVITY;")

            while (resultset!!.next()) {
                result.add(Activity(resultset.getInt("ID"),resultset.getString("BODY")))
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        println(getCountryById(5))
        return result;
    }

    fun getActivitiesByTourId(tourId: Int):ArrayList<Activity>{
        var stmt: Statement?
        var resultset: ResultSet?
        var result = ArrayList<Activity>()

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.TOUR_ACTIVITIES WHERE TOUR_ID = ${tourId};")

            while(resultset!!.next()) {
                result.add(Activity(resultset.getInt("AC_ID"),resultset.getString("AC_BODY")))
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return result;
    }

    fun getTours(): ArrayList<Tour>{
        var stmt: Statement?
        var resultset: ResultSet?
        var result = ArrayList<Tour>()

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.TOUR")

            while(resultset!!.next()) {
                result.add(Tour(resultset.getInt("ID"), resultset.getString("NAME"),
                                resultset.getString("DESCRIPTION"), resultset.getInt("RATING"),
                                resultset.getDate("LEAVE_DATE"), resultset.getDate("RETURN_DATE"),
                                resultset.getDouble("PRICE"), resultset.getInt("SEATS"),
                                this.getCountryById(resultset.getInt("COUNTRY_FK")),
                                this.getCategoryById(resultset.getInt("CATEGORY_FK")),
                                this.getActivitiesByTourId(resultset.getInt("ID")),
                    this.getReviewsByTourId(resultset.getInt("ID")))
                            )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return result;
    }

    fun getToursFiltered(tour: Tour): ArrayList<Tour>{
        var stmt: Statement?
        var resultset: ResultSet?
        var result = ArrayList<Tour>()
        if(tour.leaveDate == null){
            tour.leaveDate= Date(0,0,1)
        }
        if(tour.returnDate == null){
            tour.returnDate = Date(4000,0,0)
        }
        if(tour.country.name == null){
            tour.country.name = ""
        }
        var lDate =java.sql.Date(tour.leaveDate!!.time)
        var rDate = java.sql.Date(tour.returnDate!!.time)

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.TOUR T INNER JOIN tourdb.COUNTRY C"+
                    " WHERE C.ID = T.COUNTRY_FK AND T.LEAVE_DATE > STR_TO_DATE('$lDate','%Y-%m-%d')" +
                    "AND T.RETURN_DATE < STR_TO_DATE('$rDate','%Y-%m-%d') AND"+
                    " UPPER(C.NAME) LIKE '%${tour.country.name}%'")

            while(resultset!!.next()) {
                result.add(Tour(resultset.getInt("ID"), resultset.getString("NAME"),
                    resultset.getString("DESCRIPTION"), resultset.getInt("RATING"),
                    resultset.getDate("LEAVE_DATE"), resultset.getDate("RETURN_DATE"),
                    resultset.getDouble("PRICE"), resultset.getInt("SEATS"),
                    this.getCountryById(resultset.getInt("COUNTRY_FK")),
                    this.getCategoryById(resultset.getInt("CATEGORY_FK")),
                    this.getActivitiesByTourId(resultset.getInt("ID")),
                    this.getReviewsByTourId(resultset.getInt("ID")))
                )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return result;
    }

    fun getReviewsByTourId(id: Int): ArrayList<Review>{
        var stmt: Statement?
        var resultset: ResultSet?
        var result = ArrayList<Review>()

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.REVIEW WHERE TOUR_FK = ${id};")

            while (resultset!!.next()) {
                result.add(Review(1,resultset.getString("BODY")))
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return result;
    }

    fun getTourById(tour: Tour): Tour{
        var stmt: Statement?
        var resultset: ResultSet?
        var result = Tour()
        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.TOUR WHERE ID = ${tour.id}")

            if(resultset!!.next()) {
                result = (Tour(resultset.getInt("ID"), resultset.getString("NAME"),
                    resultset.getString("DESCRIPTION"), resultset.getInt("RATING"),
                    resultset.getDate("LEAVE_DATE"), resultset.getDate("RETURN_DATE"),
                    resultset.getDouble("PRICE"), resultset.getInt("SEATS"),
                    this.getCountryById(resultset.getInt("COUNTRY_FK")),
                    this.getCategoryById(resultset.getInt("CATEGORY_FK")),
                    this.getActivitiesByTourId(resultset.getInt("ID")),
                    this.getReviewsByTourId(resultset.getInt("ID")))
                )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return result;
    }

    fun putUser(user: User): Boolean{
        var stmt: Statement?
        var date = java.sql.Date(user.birthDate.time)
        println(date)
        try {
            stmt = conn!!.prepareCall("CALL tourdb.PUT_USER(${user.id},'${user.name}','${user.email}'," +
                                        "STR_TO_DATE('$date','%Y-%m-%d'),'${user.password}','${user.country.name}')")
            stmt!!.executeQuery()
        } catch (ex: SQLException) {
            ex.printStackTrace()
            return false
        }
        return true
    }

    fun getUser(user: User): User?{
        var stmt: Statement?
        var resultset: ResultSet?
        var result: User? = null
        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.user WHERE EMAIL = '${user.email}' AND PASSWORD = '${user.password}';")
            if(resultset!!.next()) {
                result = User(resultset.getInt("ID"),resultset.getString("NAME"),
                    resultset.getString("EMAIL"), resultset.getDate("BIRTH_DATE"),
                    resultset.getString("PASSWORD"),
                    this.getCountryById(resultset.getInt("COUNTRY_FK"))
                )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return result;
    }

    fun addFav(user: User): Boolean{
        var stmt: Statement?
        try {
            stmt = conn!!.prepareCall("CALL tourdb.ADD_TO_FAV(${user.id},${user.favs[0].id})")
            stmt!!.executeQuery()
        } catch (ex: SQLException) {
            ex.printStackTrace()
            return false
        }
        return true
    }

    fun removeFav(user: User): Boolean{
        var stmt: Statement?
        try {
            stmt = conn!!.prepareCall("CALL tourdb.REMOVE_FROM_FAV(${user.id},${user.favs[0].id})")
            stmt!!.executeQuery()
        } catch (ex: SQLException) {
            ex.printStackTrace()
            return false
        }
        return true
    }

    fun getFavs(user: User): ArrayList<Int>{
        var stmt: Statement?
        var resultset: ResultSet?
        var result = ArrayList<Int>()

        println("SELECT * FROM tourdb.FAVORITE WHERE USER_ID = ${user.id};")

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.FAVORITE WHERE USER_ID = ${user.id};")

            while (resultset!!.next()) {
                result.add(resultset.getInt("TOUR_ID"))
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return result;
    }

    fun putReservation(reservation: Reservation): Boolean{
        var stmt: Statement?
        try {
            stmt = conn!!.prepareCall("CALL tourdb.PUT_RESERVATION(${reservation.seats}," +
                    "${reservation.user.id},${reservation.tour.id})")
            stmt!!.executeQuery()
        } catch (ex: SQLException) {
            ex.printStackTrace()
            return false
        }
        return true
    }

    fun getLinks(tour: Tour): ArrayList<String>{
        var stmt: Statement?
        var resultset: ResultSet?
        var result = ArrayList<String>()

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.IMAGE WHERE TOUR_FK = ${tour.id};")

            while (resultset!!.next()) {
                result.add(resultset.getString("LINK"))
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return result;
    }

    fun getConnection():Connection? {
        val connectionProps = Properties()
        connectionProps.put("user", username)
        connectionProps.put("password", password)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance()
            return DriverManager.getConnection("jdbc:mysql://$host:$port/",connectionProps)
        }catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null;
    }

}

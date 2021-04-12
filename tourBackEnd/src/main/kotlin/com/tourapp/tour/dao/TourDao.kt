package com.tourapp.tour.dao

import com.tourapp.tour.model.*
import java.sql.*
import java.time.LocalDate

import java.time.format.DateTimeFormatter
import java.util.*
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
                                this.getActivitiesByTourId(resultset.getInt("ID")))
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
        var lDate = "${tour.leaveDate.day},${tour.leaveDate.month},${tour.leaveDate.year}"
        var rDate = "${tour.returnDate.day},${tour.returnDate.month},${tour.returnDate.year}"
        var countryReq:String?=null
        if(tour.country.id>0){
            countryReq = "AND COUNTRY = ${tour.country.id}"
        }

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SELECT * FROM tourdb.TOUR WHERE LEAVE_DATE = STR_TO_DATE('$lDate','%d,%m,%Y')" +
                                            "AND RETURN_DATE = STR_TO_DATE('$rDate','%d,%m,%Y') +$countryReq")

            while(resultset!!.next()) {
                result.add(Tour(resultset.getInt("ID"), resultset.getString("NAME"),
                    resultset.getString("DESCRIPTION"), resultset.getInt("RATING"),
                    resultset.getDate("LEAVE_DATE"), resultset.getDate("RETURN_DATE"),
                    resultset.getDouble("PRICE"), resultset.getInt("SEATS"),
                    this.getCountryById(resultset.getInt("COUNTRY_FK")),
                    this.getCategoryById(resultset.getInt("CATEGORY_FK")),
                    this.getActivitiesByTourId(resultset.getInt("ID")))
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









    /*
    fun executeMySQLQuery() {
        var stmt: Statement? = null
        var resultset: ResultSet? = null

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SHOW DATABASES;")

            if (stmt.execute("SHOW DATABASES;")) {
                resultset = stmt.resultSet
            }

            while (resultset!!.next()) {
                println(resultset.getString("Database"))
            }
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } finally {
            // release resources
            if (resultset != null) {
                try {
                    resultset.close()
                } catch (sqlEx: SQLException) {
                }

                resultset = null
            }

            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }

                stmt = null
            }

            if (conn != null) {
                try {
                    conn!!.close()
                } catch (sqlEx: SQLException) {
                }

                conn = null
            }
        }
    }
    */
}

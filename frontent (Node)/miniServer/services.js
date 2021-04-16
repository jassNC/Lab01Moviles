const fetch = require("node-fetch");

async function registerUser(user){
    console.log(user)
    let promise = await fetch('http://localhost:8080/tourApi/putUser', {
        method: "POST",
        body: JSON.stringify(user),
        headers: {
            'Content-Type': 'application/json'
        }
    })

    return await promise.text()==="true"
}

async function addFav(userId, tourId){
    data ={
        id: userId,
        favs: [{id:tourId}]
    }
    let promise = await fetch('http://localhost:8080/tourApi/addFav', {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    return await promise.text()==="true"
}

async function addReservation(reservation){
    let promise = await fetch('http://localhost:8080/tourApi/putReservation', {
        method: "POST",
        body: JSON.stringify(reservation),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    return await promise.text()==="true"
}

async function getFavs(userId){
    data ={
        id: userId
    }
    let promise = await fetch('http://localhost:8080/tourApi/getFavs', {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    return await promise.json()
}

async function removeFav(userId, tourId){
    data ={
        id: userId,
        favs: [{id:tourId}]
    }
    let promise = await fetch('http://localhost:8080/tourApi/removeFav', {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    return await promise.text()==="true"
}

async function logUser(email, password){
    let user = {
        email: email,
        password: password
    }

    let promise = await fetch('http://localhost:8080/tourApi/getUser',{
        method:"POST",
        body:JSON.stringify(user),
        headers: {
            'Content-Type': 'application/json'
        }
    })

    try{
        let loggedUser = await promise.json();
        return loggedUser
    }catch(err){
        return"nonExistentUser"
    }
}

async function getTourById(tourID){
    let tour = {
        id: tourID
    }

    let promise = await fetch('http://localhost:8080/tourApi/getTourById',{
        method:"POST",
        body:JSON.stringify(tour),
        headers: {
            'Content-Type': 'application/json'
        }
    })

    return await promise.json()
}


exports.logUser = logUser
exports.registerUser = registerUser
exports.addFav = addFav
exports.removeFav = removeFav
exports.getFavs = getFavs
exports.getTourById = getTourById
exports.addReservation = addReservation
const fetch = require("node-fetch");

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


exports.logUser = logUser
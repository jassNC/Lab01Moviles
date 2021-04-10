window.onload = function () {
    document.getElementById("loginBtn").addEventListener("click", logUser);
    document.getElementById("searchBtn").addEventListener("click", searchTours);
};

async function logUser(){

    let user = {
        email:$("#login-name").val(),
        password:$("#login-pass").val()
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
        console.log(loggedUser)
    }catch(err){
        console.log("usuario no existe")
    }
}

async function searchTours(){
    let promise = await fetch('http://localhost:8080/tourApi/getTours')
    tours = await promise.json()
    console.log($("#tm-section-1").html(""));

}
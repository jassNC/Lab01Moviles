window.onload = function () {
    searchTours()
    updateNavBar()
    document.getElementById("loginBtn").addEventListener("click", logUser);
    document.getElementById("searchBtn").addEventListener("click", searchTours);
};

async function updateNavBar() {
    let user = await getLoggedUser()
    let options
    if (user.name != "noUser") {
        options = "<ul><li><a href='#'>" + user.name + "</a></li><li><a href='carrito.html'>Carrito</a></li><li><a id = 'logoutOpt'>Logout</a></li></ul>"
        $("#navBar").html(options)
        document.getElementById("logoutOpt").addEventListener("click", logout);
    } else {
        options = "<ul><li><a href='#openModal'>Iniciar Sesión</a></li><li><a href='registroUsuario.html'>Registrarse</a></li></ul>"
        $("#navBar").html(options)
    }
}

function logout() {
    fetch("http://localhost:3000/logout")
    window.location = 'http://localhost:3000';
}

async function logUser() {
    let user = {
        email: $("#login-name").val(),
        password: $("#login-pass").val()
    }

    let promise = await fetch("http://localhost:3000/login?username=" + user.email + "&password=" + user.password)
    window.location = 'http://localhost:3000';
    console.log(promise.text())
}

async function getLoggedUser() {
    let promise = await fetch("http://localhost:3000/getLoggedUser")
    return promise.json()
}

async function searchTours() {
    let tour = {
        leaveDate: $("#inputCheckIn").val(),
        returnDate: $("#inputCheckOut").val(),
        country: { name: $("#inputCity").val(), }
    }
    let user = await getLoggedUser()

    let promise = await fetch('http://localhost:8080/tourApi/getToursFiltered', {
        method: "POST",
        body: JSON.stringify(tour),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    let promiseFavs = (await fetch("http://localhost:3000/getFavs"))
    let favs = await promiseFavs.json()
    let tours = await promise.json()
    $("#tourCards_div").html("")
    tours.forEach(async function(element){
        var image = await getImage(element.id)
        $("#tourCards_div").append(getCard(element, user))
        document.getElementById("but" + element.id).addEventListener("click", function () { loadTour(element.id) });
        document.getElementById('img'+element.id).src=image
        if (user.name != "noUser") {
            document.getElementById("toggle-heart" + element.id).addEventListener("click", function () { addFavTour(element.id) });
            if(favs.includes(element.id)){
                document.getElementById("toggle-heart" + element.id).checked = true
            }
        }
    });
}

async function loadTour(tour) {
    await fetch("http://localhost:3000/putTour?tourId=" + tour)
    window.location = 'http://localhost:3000/viaje.html';
}

function addFavTour(tourId) {
    if (document.getElementById("toggle-heart" + tourId).checked) {
        fetch("http://localhost:3000/addFav?tourId=" + tourId)
    } else {
        fetch("http://localhost:3000/removeFav?tourId=" + tourId)
    }
}

function getCard(tour, user) {
    let heart = ""
    if (user.name != "noUser") {
        heart = "<input id='toggle-heart" + tour.id + "' type='checkbox'/><label for='toggle-heart" + tour.id + "' aria-label='like'>❤</label>"
    }
    let imageId = 'img'+tour.id
    let card = "<div class='card'> <div class='row '> <div class='col-md-7 px-3'> <div class='card-block px-6'>"
        + "<h4 class='card-title'>" + tour.name + " </h4>" + heart + "<p class='card-text'></p>"
        + "<p class='card-text'>" + '⭐'.repeat(tour.rating) + "</p> <p class='card-text'>" + tour.reviews.length + " reviews</p> <p class='card-text'>$" + tour.price + " por persona</p>"
        + "<br> <button id='but" + tour.id + "' class='mt-auto btn btn-primary'>Ver más</button></div></div> <div class='col-md-5'>"
        + "<div id='ImageneTest' class='magene slide' data-ride='imagene'><div class='imagene-inner'>"
        + "<div class='imagene-item active'><img id='"+imageId+"' class='d-block' width='400' height='250'/>"
        + "</div></div></div></div></div></div>"
        
    return card;
}

async function getImage(tourId){
    var tour = {id:tourId}
    var promise = await fetch("http://localhost:8080/tourApi/getLinks", {
        method: "POST",
        body: JSON.stringify(tour),
        headers: {
            'Content-Type': 'application/json'
        }
    })

    var array = await promise.json()
    if(array.length){
        return array[0]
    }
    return 'images/paisaje.jpg'
}
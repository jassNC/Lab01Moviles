window.onload = function () {
    updateNavBar()
    loadData()
    document.getElementById("boton-reservar").addEventListener("click", addToCart);
};

let tour
let user

async function loadData() {
    let aaa = await fetch("http://localhost:3000/getTour")
    tour = await aaa.json()
    $("#titleId").html(tour.name)
    $("#descId").html(tour.description)
    $("#detailsId").html(
        "<h2>Información general</h2>"
        + "<p>Categoria: " + tour.category.name + "</p>"
        +"<p>Fecha de salida:</p>"
        + "<p>" + tour.leaveDate + "</p>"
        +"<p>Fecha de regreso:</p>"
        + "<p>" + tour.returnDate + "</p>"
    )
    $("#includesId").html("<h2>Incluye:</h2>"+ getActivities())
    $("#priceId").html("Precio: $"+tour.price)
    $("#ammountId").html(getSeats())
    $("#comments-list").html(getComments())
    if(user.name=="noUser"){
        $("#boton-reservar").prop("disabled",true);
    }
    
}

function getActivities(){
    let array=""
    tour.activities.forEach(element => {
        console.log(element.body)
        array+="<p>"+ element.body + "</p>"
    });
    return array
}

function getSeats(){
    var i = tour.seats +1
    let array = ""
    while(i--){
        array="<option value='"+i+"'>"+i+"</option>"+array
    }
    return array
}

function getComments(){
    let array = ""
    tour.reviews.forEach(element => {
        array+="<li> <div class='comment-main-level'><div class='comment-avatar'><img src='images/avatar1.jpg' alt=''></div>"
        +"<div class='comment-box'> <div class='comment-content'>"+element.body+
        "</div> </div></div></li>"
    });
    console.log(array)
    return array
}

async function updateNavBar() {
    user = await getLoggedUser()
    let options
    if (user.name != "noUser") {
        options = "<ul><li><a href='user.html'>" + user.name + "</a></li><li><a href='carrito.html'>Carrito</a></li><li><a id = 'logoutOpt'>Logout</a></li></ul>"
        $("#navBar").html(options)
        document.getElementById("logoutOpt").addEventListener("click", logout);
    } else {
        options = "<ul><li><a href='http://localhost:3000#openModal'>Iniciar Sesión</a></li><li><a href='registroUsuario.html'>Registrarse</a></li></ul>"
        $("#navBar").html(options)
    }
}
async function getLoggedUser() {
    let promise = await fetch("http://localhost:3000/getLoggedUser")
    return promise.json()
}

async function addToCart() {
    if($("#ammountId").val()>0){
        fetch("http://localhost:3000/addToCart?id="+tour.id+"&price="+tour.price+"&seats="+$("#ammountId").val()+"&name="+tour.name)
    }
}

function logout() {
    fetch("http://localhost:3000/logout")
    window.location = 'http://localhost:3000';
}
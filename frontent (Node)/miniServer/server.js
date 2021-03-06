const express = require('express');
const fetch = require("node-fetch");
var session = require('express-session')
const bodyParser = require('body-parser')
const app = express();
const services = require('./services');
const { response } = require('express');
var path = require('path');

app.use(bodyParser.urlencoded({ extended: true }))

app.use(session({
  secret: '2C44-4D44-WppQ38S',
  resave: true,
  saveUninitialized: true
}));


var auth = function (req, res, next) {
  //if (req.session && req.session.user === "amy" && req.session.admin)
  if (req.session && req.session.user)
    return next();
  else
    return res.sendStatus(401);
};

app.post('/submitRegisterForm', async function (req, res) {
  user = {
    id: req.body.id,
    name: req.body.name,
    email: req.body.email,
    birthdate: req.body.birthdate,
    password: req.body.password,
    country: { name: req.body.countrySelect }
  }
  if (await services.registerUser(user)) {
    res.sendFile(path.join(__dirname + '/web/index.html'))
  } else {
    res.send("<script>alert('Este email ya ha sido registrado'); window.location.href = '/registroUsuario.html'</script>");
  }
})

app.get('/putTour', function (req, res) {
  req.session.tourId = req.query.tourId
  console.log(req.session.tourId)
  res.sendFile(path.join(__dirname + '/web/viaje.html'))
})

app.get('/getTour', async function (req, res) {
  res.send(await services.getTourById(req.session.tourId))
})

app.get('/addFav', function (req, res) {
  var tourId = req.query.tourId
  var userId = req.session.user.id
  services.addFav(userId, tourId)
})

app.get('/removeFav', function (req, res) {
  var tourId = req.query.tourId
  var userId = req.session.user.id
  services.removeFav(userId, tourId)
})

app.get('/getFavs', async function (req, res) {
  if (req.session.user) {
    var userId = req.session.user.id
    res.send(await services.getFavs(userId))
  } else {
    res.send([])
  }
})

app.get('/login', async function (req, res) {
  var user = await services.logUser(req.query.username, req.query.password)

  if (user === "nonExistentUser") {
    res.send('login failed');
  } else {
    req.session.user = user;
    req.session.admin = true;
    res.send("login success!");
  }
});

app.get('/getLoggedUser', function (req, res) {
  if (req.session.user) {
    res.send(req.session.user)
  } else
    res.send({ name: "noUser" })
});

app.get('/logout', function (req, res) {
  req.session.destroy();
  res.send("logout success!");
});


app.get('/content', auth, function (req, res) {
  res.send("You can only see this after you've logged in.");
});

app.get('/cart', function (req, res) {
  res.send(req.session.cart);
});

app.get('/clearCart', function (req, res) {
  req.session.cart = []
  res.send("done");
});

app.get('/buy', function (req, res) {
  var cart = req.session.cart
  cart.forEach(element => {
    var order = {
      seats: element.seats,
      user: { id: req.session.user.id },
      tour: { id: element.id }
    }
    services.addReservation(order)
  });
  res.send("done")
});

app.get('/addToCart', function (req, res) {
  let tour = {
    id: req.query.id,
    nombre: req.query.name,
    precio: req.query.price,
    seats: req.query.seats
  }
  if (req.session.cart) {
  } else
    req.session.cart = []

  req.session.cart.push(tour)
  console.log(req.session.cart)
  res.send("Added")
});

app.use('/', express.static('web/index.html'))

app.use(express.static('web'));

app.listen(3000, () => console.log('listening on port 3000'));


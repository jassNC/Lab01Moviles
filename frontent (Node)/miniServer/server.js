const express = require('express');
const fetch = require("node-fetch");
var session = require('express-session')
const app = express();
const services = require('./services');

app.use(session({
    secret: '2C44-4D44-WppQ38S',
    resave: true,
    saveUninitialized: true
}));


var auth = function(req, res, next) {
    //if (req.session && req.session.user === "amy" && req.session.admin)
    if (req.session && req.session.user)
      return next();
    else
      return res.sendStatus(401);
};

app.get('/login', async function (req, res) {

    var user = await services.logUser(req.query.username, req.query.password)

    if (user==="nonExistentUser") {
        res.send('login failed'); 
    } else{
        req.session.user = user;
        req.session.admin = true;
        res.send("login success!");
    }
  });

  app.get('/getLoggedUser',function (req, res) {
    if(req.session.user){
      res.send(req.session.user)
    }else
      res.send({name:"noUser"})
  });

app.get('/logout', function (req, res) {
    req.session.destroy();
    res.send("logout success!");
  });
  
  
app.get('/content', auth, function (req, res) {
    res.send("You can only see this after you've logged in.");
});

app.use('/',express.static('web/index.html'))

app.use(express.static('web'));

app.listen(3000, () => console.log('listening on port 3000'));

app.get('/addToCart', function (req, res) {
    console.log(req.session.cart)
    if(req.session.cart){
        console.log(req.session.cart)
    }else
        req.session.cart=[]

    req.session.cart.push(req.query)
    res.send("logout success!");
  });
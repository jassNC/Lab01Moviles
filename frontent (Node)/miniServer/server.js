const express = require('express');
const app = express();


app.use('/',express.static('web/index.html'))
app.use(express.static('web'));

app.listen(3000, () => console.log('Gator app listening on port 3000!'));
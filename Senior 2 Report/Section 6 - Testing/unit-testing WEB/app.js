const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const app = express();
const session = require('express-session');

app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json());
app.use(session({
    secret: 'secret',
    resave: false,
    saveUninitialized: false,
}));

app.use((req, res, next) => {
    res.locals.user = req.user;
    next();
})

var index = require('./routes/index');
app.use('/', index);

var port = process.env.PORT || 3000;

module.exports = app.listen(port, () => {
    console.log(`connected on port ${port}`);
});
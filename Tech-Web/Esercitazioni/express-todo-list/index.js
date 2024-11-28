import express from "express";
import morgan from "morgan"; //popular logging middleware (http://expressjs.com/en/resources/middleware/morgan.html)
import flash from "connect-flash"; //simple middleware for flash messages
import session from "express-session";

import { exportAuthenticationStatus } from "./middleware/authorization.js";
import { exportFlashMessagesToViews } from "./middleware/flash-messages.js";

import { homepageRouter } from "./routes/homepageRouter.js";
import { authenticationRouter } from "./routes/authenticationRouter.js";
import { todoRouter } from "./routes/todoRouter.js";
import { resetRouter } from "./routes/resetRouter.js";

const app = express();
const PORT = 3000;

app.set("view engine", "pug"); //use pug as the default template engine

// Register the morgan logging middleware, use the 'dev' format
app.use(morgan('dev'));

// Register the built-in express.static middleware to serve static files.
// All files in the /public directory will be served as static files
app.use(express.static("public"));

// Built-in middleware. Parses url-encoded body (e.g.: those in requests submitted via forms)
// and makes the parameters available in the req.body object.
app.use(express.urlencoded({extended: false}));

// Register session handling middleware
app.use(session({
  secret: 'fu-tzu the great master',
  resave: false,
  saveUninitialized: false,
  cookie: {
    maxAge: 10*60*1000 //10 minutes, in milliseconds
  }
}));

// third-party middleware for flash messages
app.use(flash());

// custom middleware to make flash messages available to views
app.use(exportFlashMessagesToViews);

// custom middleware. Makes authentication status available to templates
app.use(exportAuthenticationStatus);

//routes
app.use(homepageRouter);
app.use(resetRouter);
app.use(authenticationRouter);
app.use(todoRouter);


//catch all, if we get here it's a 404
app.get('*', function(req, res){
  res.status(404).render("errorPage", {code: "404", description: "Page not found."});
});

//error handler
app.use( (err, req, res, next) => {
  console.log(err.stack);
  res.status(err.status || 500).render("errorPage", {
    code: (err.status || 500), 
    description: (err.message || "An error occurred")
  });
});

app.listen(PORT);
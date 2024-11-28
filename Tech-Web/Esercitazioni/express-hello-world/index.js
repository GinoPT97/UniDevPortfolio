import express from "express";
import { logger } from "./logger.js";
import { addTimestamp } from "./addTimestamp.js";
import { router as echoRouter } from "./router.js";

import cookieParser from "cookie-parser";
import session from "express-session";
import flash from "connect-flash";

const app = express(); //
const PORT = 3000;

//app.use(logger);
app.use(addTimestamp);

app.set("view engine", "pug");

app.get('/', (req, res) => {
  res.send(`Request received at ${req.timestamp}`)
});

app.use(express.urlencoded({extended: false}))

app.get("/form", (req, res) => { res.render("form"); });

app.post("/form", (req, res) => {
  res.send(`msg=${req.body.msg}; num=${req.body.num}`);
});

app.use(express.json());
app.post("/json", (req, res) => {
  res.send(`exam=${req.body.exam}; grade=${req.body.grade}`);
})

app.use("/custom", echoRouter);

app.use(cookieParser());
app.get("/cookie-counter", (req, res) => {
  if(! req.cookies?.count){
    res.cookie("count", 1, {maxAge: 10*60*1000});
    res.send("It's the first time you visit this page.");
  } else {
    res.cookie("count", parseInt(req.cookies.count) + 1, {maxAge: 10*60*1000});
    res.send(`You visited this page ${parseInt(req.cookies.count) + 1} times.`);
  }
});

app.use(session({
  secret: "s3cr37", saveUninitialized: false, resave: false
}));
app.get("/session-counter", (req, res) => {
  if(! req.session?.count) {
    req.session.count = 1;
    res.send("It's the first time you visit this page.");
  } else {
    req.session.count = req.session.count + 1;
    res.send(`You visited this page ${req.session.count} times.`);
  }
});

app.use(flash());

app.get("/redirect", (req, res) => {
  req.flash("info", "You have been redirected.");
  req.flash("info", "This is a demo to demonstrate flash messages.");
  req.flash("success", "Everything was fine.");
  req.flash("error", "But we also have an error message.");
  res.redirect("/showflash");
})
app.get("/showflash", (req, res) => {
  let infos = req.flash("info");
  let successes = req.flash("success");
  let errors = req.flash("error");
  res.render("flash", {infos: infos, successes: successes, errors: errors});
})


app.listen(PORT);
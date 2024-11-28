import express from "express";
import { AuthController } from "../controllers/AuthController.js";

export const authenticationRouter = express.Router();

authenticationRouter.get("/auth", (req, res) => {
  res.render("login");
})

authenticationRouter.post("/auth", async (req, res) => {
  let isAuthenticated = await AuthController.checkCredentials(req, res);
  if(isAuthenticated){
    req.flash("success", `Welcome ${req.body.usr}! You successfully logged in.`);
    res.redirect("/");
  } else {
    res.status(401);
    res.render("login", {errors: ["Invalid credentials. Try again."]});
  }
})

authenticationRouter.get("/signup", (req, res) => {
  res.render("signup", {errors: req.flash("error")});
});

authenticationRouter.post("/signup", (req, res) => {
  AuthController.saveUser(req, res).then(() => {
    req.flash("success", `User "${req.body.usr}" successfully created. You can now log in.`);
    res.redirect("/auth");
  }).catch((err) => {
    res.status(500);
    res.render("signup", {errors: [`Could not save user (${err}). Try again.`]});
  })
})

authenticationRouter.all("/logout", (req, res, next) => {
  req.session.isAuthenticated = false;
  req.session.username = undefined;
  req.flash("success", "You have successfully logged out.");
  res.redirect("/"); //redirect to homepage
});
import express from "express";
import { ResetController } from "../controllers/ResetController.js";

export const resetRouter = new express.Router();

resetRouter.all("/reset", (req, res, next) => {
  ResetController.resetApp(req, res).then( () => {
    req.flash("success", "App successfully reset");
    res.redirect("/"); //redirect to homepage
  }).catch(err => {
    next(err);
  })
});
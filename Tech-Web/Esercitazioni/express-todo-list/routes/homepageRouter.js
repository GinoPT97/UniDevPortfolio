import express from "express";

export const homepageRouter = express.Router();

homepageRouter.all("/", (req, res) => {
  res.render("homepage");
})
export function exportFlashMessagesToViews(req, res, next){
  res.locals.errors = req.flash("error");
  res.locals.successes = req.flash("success");
  next();
}

/**
 * This middleware ensures that the user is currently authenticated. If not,
 * redirects to login with an error message.
 * @param {*} req 
 * @param {*} res 
 * @param {*} next 
 */
export function enforceAuthentication(req, res, next){
  if(!req?.session?.isAuthenticated){
    req.flash("error", "You must be logged in to access this feature.");
    res.redirect("/auth");
  } else {
    next();
  }
}

/**
 * This middleware makes the authentication status of the user available to templates
 * by setting the appropriate res.locals properties. These properties are used, for example,
 * in the navbar partial template.
 * @param {*} req 
 * @param {*} res 
 * @param {*} next 
 */
export function exportAuthenticationStatus(req, res, next){
  if(req?.session?.isAuthenticated){
    res.locals.isUserAuthenticated = true;
    res.locals.username = req.session.username;
  }
  next();
}
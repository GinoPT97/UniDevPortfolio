export function logger(req, res, next) {
  console.log("LOGGER HAS BEEN CALLED!");
  next();
}
export function addTimestamp(req, res, next){
  req.timestamp = new Date();
  next();
}
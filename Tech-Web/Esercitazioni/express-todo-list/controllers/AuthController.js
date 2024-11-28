import { User } from "../models/Database.js";

export class AuthController {
  /**
   * Handles post requests on /auth. Checks that the given credentials are valid
   * @param {http.IncomingMessage} request 
   * @param {http.ServerResponse} response 
   */
  static async checkCredentials(req, res){
    let user = new User({ //user data specified in the request
      userName: req.body.usr, 
      password: req.body.pwd
    });

    let found = await User.findOne({
      where: {
        userName: user.userName,
        password: user.password //password was hashed when creating user
      }
    });

    if(found === null) {
      return false;
    } else { //credentials are valid. We create a session
      req.session.isAuthenticated = true;
      req.session.username = found.userName;
      return true;
    }
  }

  /**
   * Attempts to create a new User
   */
  static async saveUser(req, res){
    //save new user
    let user = new User({
      userName: req.body.usr, 
      password: req.body.pwd
    });
    return user.save(); //returns a Promise
  }
}
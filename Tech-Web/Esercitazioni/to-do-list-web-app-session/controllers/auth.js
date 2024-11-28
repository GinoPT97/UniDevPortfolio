import http from "http";
import pug from "pug";
import { handleError, parseCookies, parseRequestBody } from '../utils.js';
import { User } from "../data/User.js";
import { Database } from "../data/Database.js";
import { session } from "../Session.js";


/**
 * Handle requests to /auth 
 * @param {http.IncomingMessage} request  
 * @param {http.ServerResponse} response 
 */
export function handleAuthRequest(request, response){
  switch(request.method){
    case "GET":
      handleAuthRequestGet(request, response); break;
    case "POST":
      handleAuthRequestPost(request, response); break;
    default:
      handleError(request, response, 405, "Unsupported method"); 
  }
}

/**
 * Handle requests to /signup
 * @param {http.IncomingMessage} request  
 * @param {http.ServerResponse} response 
 */
export function handleSignUpRequest(request, response){
  switch(request.method){
    case "GET":
      handleSignUpRequestGet(request, response); break;
    case "POST":
      handleSignUpRequestPost(request, response); break;
    default:
      handleError(request, response, 405, "Unsupported method");
  }
}

/**
 * 
 * @param {http.IncomingMessage} request  
 * @param {http.ServerResponse} response 
 */
function handleAuthRequestGet(request, response){
  let renderedContent = pug.renderFile("./templates/login.pug", {"data": "foo"});
  response.writeHead(200, {"Content-Type": "text/html"});
  response.end(renderedContent);
}

/**
 * Handles post requests on /auth. Checks user credentials.
 * @param {http.IncomingMessage} request  
 * @param {http.ServerResponse} response 
 */
function handleAuthRequestPost(request, response){
  parseRequestBody(request).then((data) => {
    //check user credentials
    let user = new User(data.usr, data.pwd);
    let db = new Database();
    db.checkLogin(user).then(isAuthenticated => {
      console.log(isAuthenticated);
      //redirect to todolist
      if(isAuthenticated){
        //create a new session for the user
        let sessionId = session.createSession();
        session.storeSessionData(sessionId, "username", user.username);
        session.storeSessionData(sessionId, "auth", true);
        response.writeHead(300, {
          "Location": "/",
          "Set-Cookie": [
            `sessionId=${sessionId}; max-age=${60*60}`
          ]
        });
        response.end();
      } else {
        let renderedContent = pug.renderFile("./templates/login.pug", {"error": "Authentication failed. Check your credentials."});
        response.writeHead(401, {"Content-Type": "text/html"});
        response.end(renderedContent);
      }
    });
  });
}


/**
 * Handles get requests on /signup. Renders the signup page.
 * @param {http.IncomingMessage} request  
 * @param {http.ServerResponse} response 
 */
function handleSignUpRequestGet(request, response){
  let renderedContent = pug.renderFile("./templates/signup.pug", {"data": "foo"});
  response.writeHead(200, {"Content-Type": "text/html"});
  response.end(renderedContent);
}

/**
 * Handles post requests on /signup. A new user is created with the data provided
 * in the request body.
 * @param {http.IncomingMessage} request 
 * @param {http.ServerResponse} response 
 */
function handleSignUpRequestPost(request, response){
  parseRequestBody(request).then((data) => {
    //save new user
    let user = new User(data.usr, data.pwd);
    let db = new Database();
    db.saveUser(user).then(function(){
      //redirect to home page
      response.writeHead(300, {"Location": "/"});
      response.end();
    }).catch(function(error){
      let renderedContent = pug.renderFile("./templates/signup.pug", {"error": error});
      response.writeHead(400, {"Content-Type": "text/html"});
      response.end(renderedContent);
    });
    
  });
}

export function handleLogoutRequest(request, response, context){
  let sessionId = parseCookies(request)["sessionId"];
  session.deleteSessionById(sessionId); //delete session
  response.writeHead(300, { //redirect and delete session cookie
    "Location": "/",
    "Set-Cookie": [
      `sessionId=; max-age=-1`
    ]
  });
  response.end();
}
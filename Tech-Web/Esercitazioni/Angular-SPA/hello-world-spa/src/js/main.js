import home from "./views/home.js";
import about from "./views/about.js";
import webtech from "./views/webtech.js";
import renderFooter from "./views/footer.js";

let nav = document.createElement("nav");
nav.id = "nav";
let content = document.createElement("main");
content.id = "content";
let footer = document.createElement("footer");
footer.id = "footer";

app.append(nav, content, footer);

const routes = {
    "/": { title: "Home", render: home },
    "/about": { title: "About", render: about },
    "/webtech": { title: "Web Technologies", render: webtech },
};

function router() {
    let view = routes[location.pathname]; //get route corresponding to current path

    if (view) { //if a route matches the current path
        document.title = view.title; //update the page title to the one of the route
        content.innerHTML = view.render(); //update the content with the result of the render function
    } else { //no route matches the path
        history.replaceState("", "", "/"); //replace current history entry with an emtpy state and change url path to /
        router(); //call router() again
    }
};

// Add Navigation links to the page
for(let route in routes){
    let link = document.createElement("a");
    link.href = route;
    link.innerText = routes[route].title;
    link.setAttribute("data-link","");
    nav.append(link); 
}

// Render footer
footer.innerHTML = renderFooter();

// Handle navigation
window.addEventListener("click", e => {
    if (e.target.matches("[data-link]")) {
        e.preventDefault();
        history.pushState("", "", e.target.href);
        router();
    }
});

// Update router
window.addEventListener("popstate", router);
window.addEventListener("DOMContentLoaded", router);
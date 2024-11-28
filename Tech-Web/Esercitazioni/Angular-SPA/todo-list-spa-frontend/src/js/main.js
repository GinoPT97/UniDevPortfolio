import home from "./views/home.js";
import about from "./views/about.js";
import contact from "./views/contact.js";
import todo_list from "./views/todo-list.js";
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
    "/todo": { title: "To-do List", render: todo_list },
    "/about": { title: "About", render: about },
    "/contact": { title: "Contact", render: contact },
};

function router() {
    let view = routes[location.pathname];

    if (view) {
        document.title = view.title;
        content.innerHTML = view.render();
    } else {
        history.replaceState("", "", "/");
        router();
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
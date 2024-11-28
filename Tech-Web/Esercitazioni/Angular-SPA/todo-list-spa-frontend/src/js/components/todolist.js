const API_URL = "http://localhost:3000"

class TodoList extends HTMLElement {

  list = [];

  constructor() {
    super();
    
    this.innerHTML = /*html*/`
      <input id="todo-input" placeholder="Write your To-dos here"></input>
      <button id="save-todo">Save To-do Item</button>
      <h3>To-do List</h3>
      <ul id="the-list"></ul>
    `;

    this.list = this.fetchTodoItems().then( (res) => {
      this.updateTodoList(res);
    });

    let btn = this.querySelector("#save-todo");
    let input = this.querySelector("#todo-input");

    btn.onclick = () => {
      let toSave = input.value;
      if(!toSave){
        alert("Cannot save a blank to-do item!");
      } else {
        this.saveTodo(toSave).then( () => {
          return this.fetchTodoItems();
        }).then( items => {
          this.updateTodoList(items);
        });
      }
    };
  }

  async fetchTodoItems(){
    let response = await fetch(API_URL+"/todos");
    return response.json(); //parse JSON response into JS Objects
  }

  updateTodoList(list){
    console.log(list);
    let ul = this.querySelector("#the-list");
    ul.innerHTML = ""; //remove all child elements and re-create them. Not the most efficient thing to do.
    for(let item of list){
      let li = document.createElement("li");
      li.innerHTML = `${item.todo} <a href="#" data-delete="${item.id}">(delete)</a>`;
      ul.append(li);
    }
    if(list.length === 0){
      let li = document.createElement("li");
      li.innerHTML= "No to-do item to show.";
      ul.append(li);
    }
    //add handlers for click on the delete links
    ul.querySelectorAll("[data-delete]").forEach(deleteLink => {
      deleteLink.onclick = (event) => {
        event.preventDefault();
        fetch(`${API_URL}/todos/${event.target.getAttribute("data-delete")}`, {
          method: "delete",
          mode: "cors"
        }).then( () => {
          return this.fetchTodoItems();
        }).then( items => {
          this.updateTodoList(items);
        });
      }
    });
  }

  async saveTodo(toSave){
    return fetch(API_URL+"/todos", {
      method: "POST",
      mode: "cors",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({todo: toSave})
    });
  }
}

let count = 0;

//makes the custom element available
customElements.define("todo-list", TodoList);  
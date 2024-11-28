import { Todo} from "../models/Database.js";

export class TodoController {
  
  static async getAllTodos(){
    return Todo.findAll();
  }
  
  static async saveTodo(req){
    let todo = new Todo({
      todo: req.body.todo,
    });
    return todo.save();
  }

  static async findById(req){
    return Todo.findByPk(req.params.id);
  }

  static async update(req){
    let todo = await this.findById(req);
    todo.setDataValue('todo', req.body.todo);
    return todo.save();
  }

  static async delete(req){
    return new Promise( (resolve, reject) => {
      this.findById(req).then( item => {
        item.destroy().then( () => {resolve(item)})
      })
    })
  }
}
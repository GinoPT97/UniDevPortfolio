import express from "express";
import { enforceAuthentication } from "../middleware/authorization.js";
import { TodoController } from "../controllers/TodoController.js";

export const todoRouter = new express.Router();

todoRouter.use(enforceAuthentication);

todoRouter.get("/todo", (req, res, next) => {
  TodoController.getTodosForCurrentUser(req).then(todoItems => {
    res.locals.todoItems = todoItems;
    res.render("todo");
  }).catch(err => {
    next(err);
  });
});

todoRouter.post("/todo", (req, res, next) => {
  TodoController.saveTodo(req).then( result => {
    req.flash("success", "New To-do item saved successfully.");
    res.redirect("/todo");
  }).catch(err => {
    next(err);
  });
});

todoRouter.get("/todo/done/:id", (req, res, next) => {
  TodoController.toggleDoneStatus(req).then( (item) => {
    req.flash("success", `To-do item "${item.todo}" marked as "${item.done ? "done" : "to-do"}".`);
    res.redirect("/todo");
  }).catch( err => {
    next(err);
  })
});


todoRouter.get("/todo/edit/:id", (req, res, next) => {
  TodoController.findById(req).then( (item) => {
    res.locals.item = item;
    res.render("edit-todo");
  }).catch( err => {
    next(err);
  })
});

todoRouter.post("/todo/edit/:id", (req, res, next) => {
  TodoController.update(req).then( (item) => {
    req.flash("success", `To-do item "${item.todo}" has been updated".`);
    res.redirect("/todo");
  }).catch( err => {
    next(err);
  })
});

todoRouter.get("/todo/delete/:id", (req, res, next) => {
  TodoController.delete(req).then( (item) => {
    req.flash("success", `To-do item "${item.todo}" has been deleted".`);
    res.redirect("/todo");
  }).catch( err => {
    next(err);
  })
});
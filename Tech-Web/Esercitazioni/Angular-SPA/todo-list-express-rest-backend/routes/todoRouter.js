import express from "express";
import { TodoController } from "../controllers/TodoController.js";

export const todoRouter = new express.Router();

todoRouter.get("/todos", (req, res, next) => {
  TodoController.getAllTodos().then(todoItems => {
    res.json(todoItems)
  }).catch(err => {
    next(err);
  });
});

todoRouter.post("/todos", (req, res, next) => {
  TodoController.saveTodo(req).then( result => {
    res.json(result);
  }).catch(err => {
    next(err);
  });
});

todoRouter.get("/todos/:id", (req, res, next) => {
  TodoController.findById(req).then( (item) => {
    res.json(item);
  }).catch( err => {
    next(err);
  })
});

todoRouter.delete("/todos/:id", (req, res, next) => {
  TodoController.delete(req).then( (item) => {
    res.json(item);
  }).catch( err => {
    next(err);
  })
});
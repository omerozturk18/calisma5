import { IToDoItems } from 'app/shared/model/to-do-items.model';

export interface IToDoList {
  id?: number;
  todoListName?: string;
  toDoListToDoItems?: IToDoItems[];
}

export class ToDoList implements IToDoList {
  constructor(public id?: number, public todoListName?: string, public toDoListToDoItems?: IToDoItems[]) {}
}

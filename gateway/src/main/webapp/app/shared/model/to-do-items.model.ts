import { Moment } from 'moment';
import { IToDoList } from 'app/shared/model/to-do-list.model';

export const enum Status {
  COMPLETED = 'COMPLETED',
  CONTINUES = 'CONTINUES'
}

export interface IToDoItems {
  id?: number;
  todoItemName?: string;
  description?: string;
  deadline?: Moment;
  status?: Status;
  toDoList?: IToDoList;
}

export class ToDoItems implements IToDoItems {
  constructor(
    public id?: number,
    public todoItemName?: string,
    public description?: string,
    public deadline?: Moment,
    public status?: Status,
    public toDoList?: IToDoList
  ) {}
}

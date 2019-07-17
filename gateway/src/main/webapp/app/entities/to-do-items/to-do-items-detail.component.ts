import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IToDoItems } from 'app/shared/model/to-do-items.model';

@Component({
  selector: 'jhi-to-do-items-detail',
  templateUrl: './to-do-items-detail.component.html'
})
export class ToDoItemsDetailComponent implements OnInit {
  toDoItems: IToDoItems;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ toDoItems }) => {
      this.toDoItems = toDoItems;
    });
  }

  previousState() {
    window.history.back();
  }
}

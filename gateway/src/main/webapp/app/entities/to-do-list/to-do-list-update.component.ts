import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IToDoList, ToDoList } from 'app/shared/model/to-do-list.model';
import { ToDoListService } from './to-do-list.service';

@Component({
  selector: 'jhi-to-do-list-update',
  templateUrl: './to-do-list-update.component.html'
})
export class ToDoListUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    todoListName: [null, [Validators.required]]
  });

  constructor(protected toDoListService: ToDoListService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ toDoList }) => {
      this.updateForm(toDoList);
    });
  }

  updateForm(toDoList: IToDoList) {
    this.editForm.patchValue({
      id: toDoList.id,
      todoListName: toDoList.todoListName
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const toDoList = this.createFromForm();
    if (toDoList.id !== undefined) {
      this.subscribeToSaveResponse(this.toDoListService.update(toDoList));
    } else {
      this.subscribeToSaveResponse(this.toDoListService.create(toDoList));
    }
  }

  private createFromForm(): IToDoList {
    return {
      ...new ToDoList(),
      id: this.editForm.get(['id']).value,
      todoListName: this.editForm.get(['todoListName']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IToDoList>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}

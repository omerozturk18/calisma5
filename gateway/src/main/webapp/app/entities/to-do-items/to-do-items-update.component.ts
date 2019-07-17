import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IToDoItems, ToDoItems } from 'app/shared/model/to-do-items.model';
import { ToDoItemsService } from './to-do-items.service';
import { IToDoList } from 'app/shared/model/to-do-list.model';
import { ToDoListService } from 'app/entities/to-do-list';

@Component({
  selector: 'jhi-to-do-items-update',
  templateUrl: './to-do-items-update.component.html'
})
export class ToDoItemsUpdateComponent implements OnInit {
  isSaving: boolean;

  todolists: IToDoList[];

  editForm = this.fb.group({
    id: [],
    todoItemName: [null, [Validators.required]],
    description: [],
    deadline: [],
    status: [null, [Validators.required]],
    toDoList: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected toDoItemsService: ToDoItemsService,
    protected toDoListService: ToDoListService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ toDoItems }) => {
      this.updateForm(toDoItems);
    });
    this.toDoListService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IToDoList[]>) => mayBeOk.ok),
        map((response: HttpResponse<IToDoList[]>) => response.body)
      )
      .subscribe((res: IToDoList[]) => (this.todolists = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(toDoItems: IToDoItems) {
    this.editForm.patchValue({
      id: toDoItems.id,
      todoItemName: toDoItems.todoItemName,
      description: toDoItems.description,
      deadline: toDoItems.deadline != null ? toDoItems.deadline.format(DATE_TIME_FORMAT) : null,
      status: toDoItems.status,
      toDoList: toDoItems.toDoList
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const toDoItems = this.createFromForm();
    if (toDoItems.id !== undefined) {
      this.subscribeToSaveResponse(this.toDoItemsService.update(toDoItems));
    } else {
      this.subscribeToSaveResponse(this.toDoItemsService.create(toDoItems));
    }
  }

  private createFromForm(): IToDoItems {
    return {
      ...new ToDoItems(),
      id: this.editForm.get(['id']).value,
      todoItemName: this.editForm.get(['todoItemName']).value,
      description: this.editForm.get(['description']).value,
      deadline: this.editForm.get(['deadline']).value != null ? moment(this.editForm.get(['deadline']).value, DATE_TIME_FORMAT) : undefined,
      status: this.editForm.get(['status']).value,
      toDoList: this.editForm.get(['toDoList']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IToDoItems>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackToDoListById(index: number, item: IToDoList) {
    return item.id;
  }
}

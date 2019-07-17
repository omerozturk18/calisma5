import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IToDoList } from 'app/shared/model/to-do-list.model';
import { AccountService } from 'app/core';
import { ToDoListService } from './to-do-list.service';

@Component({
  selector: 'jhi-to-do-list',
  templateUrl: './to-do-list.component.html'
})
export class ToDoListComponent implements OnInit, OnDestroy {
  toDoLists: IToDoList[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected toDoListService: ToDoListService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.toDoListService
      .query()
      .pipe(
        filter((res: HttpResponse<IToDoList[]>) => res.ok),
        map((res: HttpResponse<IToDoList[]>) => res.body)
      )
      .subscribe(
        (res: IToDoList[]) => {
          this.toDoLists = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInToDoLists();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IToDoList) {
    return item.id;
  }

  registerChangeInToDoLists() {
    this.eventSubscriber = this.eventManager.subscribe('toDoListListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}

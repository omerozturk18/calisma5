import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ToDoList } from 'app/shared/model/to-do-list.model';
import { ToDoListService } from './to-do-list.service';
import { ToDoListComponent } from './to-do-list.component';
import { ToDoListDetailComponent } from './to-do-list-detail.component';
import { ToDoListUpdateComponent } from './to-do-list-update.component';
import { ToDoListDeletePopupComponent } from './to-do-list-delete-dialog.component';
import { IToDoList } from 'app/shared/model/to-do-list.model';

@Injectable({ providedIn: 'root' })
export class ToDoListResolve implements Resolve<IToDoList> {
  constructor(private service: ToDoListService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IToDoList> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ToDoList>) => response.ok),
        map((toDoList: HttpResponse<ToDoList>) => toDoList.body)
      );
    }
    return of(new ToDoList());
  }
}

export const toDoListRoute: Routes = [
  {
    path: '',
    component: ToDoListComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.toDoList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ToDoListDetailComponent,
    resolve: {
      toDoList: ToDoListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.toDoList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ToDoListUpdateComponent,
    resolve: {
      toDoList: ToDoListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.toDoList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ToDoListUpdateComponent,
    resolve: {
      toDoList: ToDoListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.toDoList.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const toDoListPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ToDoListDeletePopupComponent,
    resolve: {
      toDoList: ToDoListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.toDoList.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

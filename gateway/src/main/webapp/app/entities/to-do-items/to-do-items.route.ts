import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ToDoItems } from 'app/shared/model/to-do-items.model';
import { ToDoItemsService } from './to-do-items.service';
import { ToDoItemsComponent } from './to-do-items.component';
import { ToDoItemsDetailComponent } from './to-do-items-detail.component';
import { ToDoItemsUpdateComponent } from './to-do-items-update.component';
import { ToDoItemsDeletePopupComponent } from './to-do-items-delete-dialog.component';
import { IToDoItems } from 'app/shared/model/to-do-items.model';

@Injectable({ providedIn: 'root' })
export class ToDoItemsResolve implements Resolve<IToDoItems> {
  constructor(private service: ToDoItemsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IToDoItems> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ToDoItems>) => response.ok),
        map((toDoItems: HttpResponse<ToDoItems>) => toDoItems.body)
      );
    }
    return of(new ToDoItems());
  }
}

export const toDoItemsRoute: Routes = [
  {
    path: '',
    component: ToDoItemsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.toDoItems.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ToDoItemsDetailComponent,
    resolve: {
      toDoItems: ToDoItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.toDoItems.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ToDoItemsUpdateComponent,
    resolve: {
      toDoItems: ToDoItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.toDoItems.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ToDoItemsUpdateComponent,
    resolve: {
      toDoItems: ToDoItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.toDoItems.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const toDoItemsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ToDoItemsDeletePopupComponent,
    resolve: {
      toDoItems: ToDoItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.toDoItems.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

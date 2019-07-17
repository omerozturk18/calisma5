import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IToDoItems } from 'app/shared/model/to-do-items.model';

type EntityResponseType = HttpResponse<IToDoItems>;
type EntityArrayResponseType = HttpResponse<IToDoItems[]>;

@Injectable({ providedIn: 'root' })
export class ToDoItemsService {
  public resourceUrl = SERVER_API_URL + 'api/to-do-items';

  constructor(protected http: HttpClient) {}

  create(toDoItems: IToDoItems): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDoItems);
    return this.http
      .post<IToDoItems>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(toDoItems: IToDoItems): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDoItems);
    return this.http
      .put<IToDoItems>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IToDoItems>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IToDoItems[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(toDoItems: IToDoItems): IToDoItems {
    const copy: IToDoItems = Object.assign({}, toDoItems, {
      deadline: toDoItems.deadline != null && toDoItems.deadline.isValid() ? toDoItems.deadline.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.deadline = res.body.deadline != null ? moment(res.body.deadline) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((toDoItems: IToDoItems) => {
        toDoItems.deadline = toDoItems.deadline != null ? moment(toDoItems.deadline) : null;
      });
    }
    return res;
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IToDoList } from 'app/shared/model/to-do-list.model';

type EntityResponseType = HttpResponse<IToDoList>;
type EntityArrayResponseType = HttpResponse<IToDoList[]>;

@Injectable({ providedIn: 'root' })
export class ToDoListService {
  public resourceUrl = SERVER_API_URL + 'api/to-do-lists';

  constructor(protected http: HttpClient) {}

  create(toDoList: IToDoList): Observable<EntityResponseType> {
    return this.http.post<IToDoList>(this.resourceUrl, toDoList, { observe: 'response' });
  }

  update(toDoList: IToDoList): Observable<EntityResponseType> {
    return this.http.put<IToDoList>(this.resourceUrl, toDoList, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IToDoList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IToDoList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

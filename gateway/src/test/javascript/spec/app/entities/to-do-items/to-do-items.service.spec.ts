/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ToDoItemsService } from 'app/entities/to-do-items/to-do-items.service';
import { IToDoItems, ToDoItems, Status } from 'app/shared/model/to-do-items.model';

describe('Service Tests', () => {
  describe('ToDoItems Service', () => {
    let injector: TestBed;
    let service: ToDoItemsService;
    let httpMock: HttpTestingController;
    let elemDefault: IToDoItems;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(ToDoItemsService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new ToDoItems(0, 'AAAAAAA', 'AAAAAAA', currentDate, Status.COMPLETED);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            deadline: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a ToDoItems', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            deadline: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            deadline: currentDate
          },
          returnedFromService
        );
        service
          .create(new ToDoItems(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a ToDoItems', async () => {
        const returnedFromService = Object.assign(
          {
            todoItemName: 'BBBBBB',
            description: 'BBBBBB',
            deadline: currentDate.format(DATE_TIME_FORMAT),
            status: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            deadline: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of ToDoItems', async () => {
        const returnedFromService = Object.assign(
          {
            todoItemName: 'BBBBBB',
            description: 'BBBBBB',
            deadline: currentDate.format(DATE_TIME_FORMAT),
            status: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            deadline: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ToDoItems', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

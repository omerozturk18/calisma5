/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayTestModule } from '../../../test.module';
import { ToDoListComponent } from 'app/entities/to-do-list/to-do-list.component';
import { ToDoListService } from 'app/entities/to-do-list/to-do-list.service';
import { ToDoList } from 'app/shared/model/to-do-list.model';

describe('Component Tests', () => {
  describe('ToDoList Management Component', () => {
    let comp: ToDoListComponent;
    let fixture: ComponentFixture<ToDoListComponent>;
    let service: ToDoListService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [ToDoListComponent],
        providers: []
      })
        .overrideTemplate(ToDoListComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ToDoListComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ToDoListService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ToDoList(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.toDoLists[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

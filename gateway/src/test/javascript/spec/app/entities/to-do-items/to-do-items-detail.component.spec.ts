/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../test.module';
import { ToDoItemsDetailComponent } from 'app/entities/to-do-items/to-do-items-detail.component';
import { ToDoItems } from 'app/shared/model/to-do-items.model';

describe('Component Tests', () => {
  describe('ToDoItems Management Detail Component', () => {
    let comp: ToDoItemsDetailComponent;
    let fixture: ComponentFixture<ToDoItemsDetailComponent>;
    const route = ({ data: of({ toDoItems: new ToDoItems(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [ToDoItemsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ToDoItemsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ToDoItemsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.toDoItems).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

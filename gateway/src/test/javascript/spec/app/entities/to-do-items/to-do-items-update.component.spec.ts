/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { GatewayTestModule } from '../../../test.module';
import { ToDoItemsUpdateComponent } from 'app/entities/to-do-items/to-do-items-update.component';
import { ToDoItemsService } from 'app/entities/to-do-items/to-do-items.service';
import { ToDoItems } from 'app/shared/model/to-do-items.model';

describe('Component Tests', () => {
  describe('ToDoItems Management Update Component', () => {
    let comp: ToDoItemsUpdateComponent;
    let fixture: ComponentFixture<ToDoItemsUpdateComponent>;
    let service: ToDoItemsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [ToDoItemsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ToDoItemsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ToDoItemsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ToDoItemsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ToDoItems(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ToDoItems();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});

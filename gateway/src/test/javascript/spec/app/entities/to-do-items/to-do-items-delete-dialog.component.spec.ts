/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GatewayTestModule } from '../../../test.module';
import { ToDoItemsDeleteDialogComponent } from 'app/entities/to-do-items/to-do-items-delete-dialog.component';
import { ToDoItemsService } from 'app/entities/to-do-items/to-do-items.service';

describe('Component Tests', () => {
  describe('ToDoItems Management Delete Component', () => {
    let comp: ToDoItemsDeleteDialogComponent;
    let fixture: ComponentFixture<ToDoItemsDeleteDialogComponent>;
    let service: ToDoItemsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [ToDoItemsDeleteDialogComponent]
      })
        .overrideTemplate(ToDoItemsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ToDoItemsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ToDoItemsService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IToDoItems } from 'app/shared/model/to-do-items.model';
import { ToDoItemsService } from './to-do-items.service';

@Component({
  selector: 'jhi-to-do-items-delete-dialog',
  templateUrl: './to-do-items-delete-dialog.component.html'
})
export class ToDoItemsDeleteDialogComponent {
  toDoItems: IToDoItems;

  constructor(protected toDoItemsService: ToDoItemsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.toDoItemsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'toDoItemsListModification',
        content: 'Deleted an toDoItems'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-to-do-items-delete-popup',
  template: ''
})
export class ToDoItemsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ toDoItems }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ToDoItemsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.toDoItems = toDoItems;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/to-do-items', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/to-do-items', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}

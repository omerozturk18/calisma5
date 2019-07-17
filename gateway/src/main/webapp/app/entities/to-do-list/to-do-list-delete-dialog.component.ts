import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IToDoList } from 'app/shared/model/to-do-list.model';
import { ToDoListService } from './to-do-list.service';

@Component({
  selector: 'jhi-to-do-list-delete-dialog',
  templateUrl: './to-do-list-delete-dialog.component.html'
})
export class ToDoListDeleteDialogComponent {
  toDoList: IToDoList;

  constructor(protected toDoListService: ToDoListService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.toDoListService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'toDoListListModification',
        content: 'Deleted an toDoList'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-to-do-list-delete-popup',
  template: ''
})
export class ToDoListDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ toDoList }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ToDoListDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.toDoList = toDoList;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/to-do-list', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/to-do-list', { outlets: { popup: null } }]);
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

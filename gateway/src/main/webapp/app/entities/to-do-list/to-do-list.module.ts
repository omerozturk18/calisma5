import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { GatewaySharedModule } from 'app/shared';
import {
  ToDoListComponent,
  ToDoListDetailComponent,
  ToDoListUpdateComponent,
  ToDoListDeletePopupComponent,
  ToDoListDeleteDialogComponent,
  toDoListRoute,
  toDoListPopupRoute
} from './';

const ENTITY_STATES = [...toDoListRoute, ...toDoListPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ToDoListComponent,
    ToDoListDetailComponent,
    ToDoListUpdateComponent,
    ToDoListDeleteDialogComponent,
    ToDoListDeletePopupComponent
  ],
  entryComponents: [ToDoListComponent, ToDoListUpdateComponent, ToDoListDeleteDialogComponent, ToDoListDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayToDoListModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

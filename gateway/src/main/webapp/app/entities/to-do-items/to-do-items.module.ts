import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { GatewaySharedModule } from 'app/shared';
import {
  ToDoItemsComponent,
  ToDoItemsDetailComponent,
  ToDoItemsUpdateComponent,
  ToDoItemsDeletePopupComponent,
  ToDoItemsDeleteDialogComponent,
  toDoItemsRoute,
  toDoItemsPopupRoute
} from './';

const ENTITY_STATES = [...toDoItemsRoute, ...toDoItemsPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ToDoItemsComponent,
    ToDoItemsDetailComponent,
    ToDoItemsUpdateComponent,
    ToDoItemsDeleteDialogComponent,
    ToDoItemsDeletePopupComponent
  ],
  entryComponents: [ToDoItemsComponent, ToDoItemsUpdateComponent, ToDoItemsDeleteDialogComponent, ToDoItemsDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayToDoItemsModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

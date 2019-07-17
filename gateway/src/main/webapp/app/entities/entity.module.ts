import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        loadChildren: './backend/product/product.module#BackendProductModule'
      },
      {
        path: 'to-do-items',
        loadChildren: './to-do-items/to-do-items.module#GatewayToDoItemsModule'
      },
      {
        path: 'to-do-list',
        loadChildren: './to-do-list/to-do-list.module#GatewayToDoListModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayEntityModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentationComponent } from '../list/documentation.component';
import { DocumentationDetailComponent } from '../detail/documentation-detail.component';
import { DocumentationUpdateComponent } from '../update/documentation-update.component';
import { DocumentationRoutingResolveService } from './documentation-routing-resolve.service';

const documentationRoute: Routes = [
  {
    path: '',
    component: DocumentationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentationDetailComponent,
    resolve: {
      documentation: DocumentationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentationUpdateComponent,
    resolve: {
      documentation: DocumentationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentationUpdateComponent,
    resolve: {
      documentation: DocumentationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentationRoute)],
  exports: [RouterModule],
})
export class DocumentationRoutingModule {}

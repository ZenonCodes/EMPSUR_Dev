import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentationComponent } from './list/documentation.component';
import { DocumentationDetailComponent } from './detail/documentation-detail.component';
import { DocumentationUpdateComponent } from './update/documentation-update.component';
import { DocumentationDeleteDialogComponent } from './delete/documentation-delete-dialog.component';
import { DocumentationRoutingModule } from './route/documentation-routing.module';

@NgModule({
  imports: [SharedModule, DocumentationRoutingModule],
  declarations: [DocumentationComponent, DocumentationDetailComponent, DocumentationUpdateComponent, DocumentationDeleteDialogComponent],
  entryComponents: [DocumentationDeleteDialogComponent],
})
export class DocumentationModule {}

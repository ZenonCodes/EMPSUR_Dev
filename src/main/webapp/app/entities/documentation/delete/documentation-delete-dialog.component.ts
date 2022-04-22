import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentation } from '../documentation.model';
import { DocumentationService } from '../service/documentation.service';

@Component({
  templateUrl: './documentation-delete-dialog.component.html',
})
export class DocumentationDeleteDialogComponent {
  documentation?: IDocumentation;

  constructor(protected documentationService: DocumentationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

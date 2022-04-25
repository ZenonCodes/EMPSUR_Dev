import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumentation, Documentation } from '../documentation.model';
import { DocumentationService } from '../service/documentation.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRecord } from 'app/entities/record/record.model';
import { RecordService } from 'app/entities/record/service/record.service';
import { DocumentationStatus } from 'app/entities/enumerations/documentation-status.model';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';

@Component({
  selector: 'jhi-documentation-update',
  templateUrl: './documentation-update.component.html',
})
export class DocumentationUpdateComponent implements OnInit {
  isSaving = false;
  documentationStatusValues = Object.keys(DocumentationStatus);
  taskStatusValues = Object.keys(TaskStatus);

  tagsSharedCollection: ITag[] = [];
  employeesSharedCollection: IEmployee[] = [];
  recordsSharedCollection: IRecord[] = [];

  editForm = this.fb.group({
    id: [],
    status: [null, [Validators.required]],
    name: [null, [Validators.required]],
    attachment: [],
    attachmentContentType: [],
    description: [],
    issued: [],
    expiration: [],
    approval: [],
    requested: [],
    tags: [],
    employee: [],
    record: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected documentationService: DocumentationService,
    protected tagService: TagService,
    protected employeeService: EmployeeService,
    protected recordService: RecordService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentation }) => {
      this.updateForm(documentation);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('empsurApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentation = this.createFromForm();
    if (documentation.id !== undefined) {
      this.subscribeToSaveResponse(this.documentationService.update(documentation));
    } else {
      this.subscribeToSaveResponse(this.documentationService.create(documentation));
    }
  }

  trackTagById(_index: number, item: ITag): number {
    return item.id!;
  }

  trackEmployeeById(_index: number, item: IEmployee): number {
    return item.id!;
  }

  trackRecordById(_index: number, item: IRecord): number {
    return item.id!;
  }

  getSelectedTag(option: ITag, selectedVals?: ITag[]): ITag {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(documentation: IDocumentation): void {
    this.editForm.patchValue({
      id: documentation.id,
      status: documentation.status,
      name: documentation.name,
      attachment: documentation.attachment,
      attachmentContentType: documentation.attachmentContentType,
      description: documentation.description,
      issued: documentation.issued,
      expiration: documentation.expiration,
      approval: documentation.approval,
      requested: documentation.requested,
      tags: documentation.tags,
      employee: documentation.employee,
      record: documentation.record,
    });

    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing(this.tagsSharedCollection, ...(documentation.tags ?? []));
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      documentation.employee
    );
    this.recordsSharedCollection = this.recordService.addRecordToCollectionIfMissing(this.recordsSharedCollection, documentation.record);
  }

  protected loadRelationshipsOptions(): void {
    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing(tags, ...(this.editForm.get('tags')!.value ?? []))))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employee')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.recordService
      .query()
      .pipe(map((res: HttpResponse<IRecord[]>) => res.body ?? []))
      .pipe(map((records: IRecord[]) => this.recordService.addRecordToCollectionIfMissing(records, this.editForm.get('record')!.value)))
      .subscribe((records: IRecord[]) => (this.recordsSharedCollection = records));
  }

  protected createFromForm(): IDocumentation {
    return {
      ...new Documentation(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      name: this.editForm.get(['name'])!.value,
      attachmentContentType: this.editForm.get(['attachmentContentType'])!.value,
      attachment: this.editForm.get(['attachment'])!.value,
      description: this.editForm.get(['description'])!.value,
      issued: this.editForm.get(['issued'])!.value,
      expiration: this.editForm.get(['expiration'])!.value,
      approval: this.editForm.get(['approval'])!.value,
      requested: this.editForm.get(['requested'])!.value,
      tags: this.editForm.get(['tags'])!.value,
      employee: this.editForm.get(['employee'])!.value,
      record: this.editForm.get(['record'])!.value,
    };
  }
}

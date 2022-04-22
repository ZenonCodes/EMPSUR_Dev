import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRecord, Record } from '../record.model';
import { RecordService } from '../service/record.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-record-update',
  templateUrl: './record-update.component.html',
})
export class RecordUpdateComponent implements OnInit {
  isSaving = false;

  employeesCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    employee: [],
  });

  constructor(
    protected recordService: RecordService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ record }) => {
      this.updateForm(record);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const record = this.createFromForm();
    if (record.id !== undefined) {
      this.subscribeToSaveResponse(this.recordService.update(record));
    } else {
      this.subscribeToSaveResponse(this.recordService.create(record));
    }
  }

  trackEmployeeById(_index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecord>>): void {
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

  protected updateForm(record: IRecord): void {
    this.editForm.patchValue({
      id: record.id,
      name: record.name,
      employee: record.employee,
    });

    this.employeesCollection = this.employeeService.addEmployeeToCollectionIfMissing(this.employeesCollection, record.employee);
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query({ filter: 'record-is-null' })
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employee')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesCollection = employees));
  }

  protected createFromForm(): IRecord {
    return {
      ...new Record(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}

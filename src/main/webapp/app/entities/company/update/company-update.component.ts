import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICompany, Company } from '../company.model';
import { CompanyService } from '../service/company.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-company-update',
  templateUrl: './company-update.component.html',
})
export class CompanyUpdateComponent implements OnInit {
  isSaving = false;
  statusValues = Object.keys(Status);

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    idNumber: [null, [Validators.required]],
    status: [null, [Validators.required]],
    phone: [null, [Validators.required, Validators.pattern('[\\d]+')]],
    adressLine1: [null, [Validators.required]],
    adressLine2: [],
    city: [null, [Validators.required]],
    country: [null, [Validators.required]],
    state: [],
  });

  constructor(protected companyService: CompanyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ company }) => {
      this.updateForm(company);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const company = this.createFromForm();
    if (company.id !== undefined) {
      this.subscribeToSaveResponse(this.companyService.update(company));
    } else {
      this.subscribeToSaveResponse(this.companyService.create(company));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>): void {
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

  protected updateForm(company: ICompany): void {
    this.editForm.patchValue({
      id: company.id,
      name: company.name,
      idNumber: company.idNumber,
      status: company.status,
      phone: company.phone,
      adressLine1: company.adressLine1,
      adressLine2: company.adressLine2,
      city: company.city,
      country: company.country,
      state: company.state,
    });
  }

  protected createFromForm(): ICompany {
    return {
      ...new Company(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      idNumber: this.editForm.get(['idNumber'])!.value,
      status: this.editForm.get(['status'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      adressLine1: this.editForm.get(['adressLine1'])!.value,
      adressLine2: this.editForm.get(['adressLine2'])!.value,
      city: this.editForm.get(['city'])!.value,
      country: this.editForm.get(['country'])!.value,
      state: this.editForm.get(['state'])!.value,
    };
  }
}

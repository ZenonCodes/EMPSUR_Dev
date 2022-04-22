import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DepartmentService } from '../service/department.service';
import { IDepartment, Department } from '../department.model';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

import { DepartmentUpdateComponent } from './department-update.component';

describe('Department Management Update Component', () => {
  let comp: DepartmentUpdateComponent;
  let fixture: ComponentFixture<DepartmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departmentService: DepartmentService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DepartmentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DepartmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departmentService = TestBed.inject(DepartmentService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Company query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const company: ICompany = { id: 13325 };
      department.company = company;

      const companyCollection: ICompany[] = [{ id: 84227 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(companyCollection, ...additionalCompanies);
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const department: IDepartment = { id: 456 };
      const company: ICompany = { id: 58703 };
      department.company = company;

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(department));
      expect(comp.companiesSharedCollection).toContain(company);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Department>>();
      const department = { id: 123 };
      jest.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: department }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(departmentService.update).toHaveBeenCalledWith(department);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Department>>();
      const department = new Department();
      jest.spyOn(departmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: department }));
      saveSubject.complete();

      // THEN
      expect(departmentService.create).toHaveBeenCalledWith(department);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Department>>();
      const department = { id: 123 };
      jest.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departmentService.update).toHaveBeenCalledWith(department);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCompanyById', () => {
      it('Should return tracked Company primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompanyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DocumentationService } from '../service/documentation.service';
import { IDocumentation, Documentation } from '../documentation.model';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRecord } from 'app/entities/record/record.model';
import { RecordService } from 'app/entities/record/service/record.service';

import { DocumentationUpdateComponent } from './documentation-update.component';

describe('Documentation Management Update Component', () => {
  let comp: DocumentationUpdateComponent;
  let fixture: ComponentFixture<DocumentationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentationService: DocumentationService;
  let tagService: TagService;
  let employeeService: EmployeeService;
  let recordService: RecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DocumentationUpdateComponent],
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
      .overrideTemplate(DocumentationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentationService = TestBed.inject(DocumentationService);
    tagService = TestBed.inject(TagService);
    employeeService = TestBed.inject(EmployeeService);
    recordService = TestBed.inject(RecordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tag query and add missing value', () => {
      const documentation: IDocumentation = { id: 456 };
      const tags: ITag[] = [{ id: 71695 }];
      documentation.tags = tags;

      const tagCollection: ITag[] = [{ id: 69682 }];
      jest.spyOn(tagService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollection })));
      const additionalTags = [...tags];
      const expectedCollection: ITag[] = [...additionalTags, ...tagCollection];
      jest.spyOn(tagService, 'addTagToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentation });
      comp.ngOnInit();

      expect(tagService.query).toHaveBeenCalled();
      expect(tagService.addTagToCollectionIfMissing).toHaveBeenCalledWith(tagCollection, ...additionalTags);
      expect(comp.tagsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const documentation: IDocumentation = { id: 456 };
      const employee: IEmployee = { id: 39022 };
      documentation.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 15330 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentation });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Record query and add missing value', () => {
      const documentation: IDocumentation = { id: 456 };
      const record: IRecord = { id: 23911 };
      documentation.record = record;

      const recordCollection: IRecord[] = [{ id: 21347 }];
      jest.spyOn(recordService, 'query').mockReturnValue(of(new HttpResponse({ body: recordCollection })));
      const additionalRecords = [record];
      const expectedCollection: IRecord[] = [...additionalRecords, ...recordCollection];
      jest.spyOn(recordService, 'addRecordToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentation });
      comp.ngOnInit();

      expect(recordService.query).toHaveBeenCalled();
      expect(recordService.addRecordToCollectionIfMissing).toHaveBeenCalledWith(recordCollection, ...additionalRecords);
      expect(comp.recordsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const documentation: IDocumentation = { id: 456 };
      const tags: ITag = { id: 60455 };
      documentation.tags = [tags];
      const employee: IEmployee = { id: 1507 };
      documentation.employee = employee;
      const record: IRecord = { id: 28185 };
      documentation.record = record;

      activatedRoute.data = of({ documentation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(documentation));
      expect(comp.tagsSharedCollection).toContain(tags);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.recordsSharedCollection).toContain(record);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Documentation>>();
      const documentation = { id: 123 };
      jest.spyOn(documentationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentationService.update).toHaveBeenCalledWith(documentation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Documentation>>();
      const documentation = new Documentation();
      jest.spyOn(documentationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentation }));
      saveSubject.complete();

      // THEN
      expect(documentationService.create).toHaveBeenCalledWith(documentation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Documentation>>();
      const documentation = { id: 123 };
      jest.spyOn(documentationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentationService.update).toHaveBeenCalledWith(documentation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTagById', () => {
      it('Should return tracked Tag primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTagById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEmployeeById', () => {
      it('Should return tracked Employee primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmployeeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRecordById', () => {
      it('Should return tracked Record primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRecordById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedTag', () => {
      it('Should return option if no Tag is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTag(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Tag for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTag(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Tag is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTag(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});

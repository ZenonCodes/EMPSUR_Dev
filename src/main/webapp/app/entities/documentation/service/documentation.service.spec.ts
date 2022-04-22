import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { DocumentationStatus } from 'app/entities/enumerations/documentation-status.model';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';
import { IDocumentation, Documentation } from '../documentation.model';

import { DocumentationService } from './documentation.service';

describe('Documentation Service', () => {
  let service: DocumentationService;
  let httpMock: HttpTestingController;
  let elemDefault: IDocumentation;
  let expectedResult: IDocumentation | IDocumentation[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      status: DocumentationStatus.VALID,
      name: 'AAAAAAA',
      attachmentContentType: 'image/png',
      attachment: 'AAAAAAA',
      description: 'AAAAAAA',
      issued: currentDate,
      expiration: currentDate,
      approval: TaskStatus.COMPLETED,
      requested: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          issued: currentDate.format(DATE_FORMAT),
          expiration: currentDate.format(DATE_FORMAT),
          requested: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Documentation', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          issued: currentDate.format(DATE_FORMAT),
          expiration: currentDate.format(DATE_FORMAT),
          requested: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          issued: currentDate,
          expiration: currentDate,
          requested: currentDate,
        },
        returnedFromService
      );

      service.create(new Documentation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Documentation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          status: 'BBBBBB',
          name: 'BBBBBB',
          attachment: 'BBBBBB',
          description: 'BBBBBB',
          issued: currentDate.format(DATE_FORMAT),
          expiration: currentDate.format(DATE_FORMAT),
          approval: 'BBBBBB',
          requested: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          issued: currentDate,
          expiration: currentDate,
          requested: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Documentation', () => {
      const patchObject = Object.assign(
        {
          attachment: 'BBBBBB',
          description: 'BBBBBB',
        },
        new Documentation()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          issued: currentDate,
          expiration: currentDate,
          requested: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Documentation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          status: 'BBBBBB',
          name: 'BBBBBB',
          attachment: 'BBBBBB',
          description: 'BBBBBB',
          issued: currentDate.format(DATE_FORMAT),
          expiration: currentDate.format(DATE_FORMAT),
          approval: 'BBBBBB',
          requested: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          issued: currentDate,
          expiration: currentDate,
          requested: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Documentation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDocumentationToCollectionIfMissing', () => {
      it('should add a Documentation to an empty array', () => {
        const documentation: IDocumentation = { id: 123 };
        expectedResult = service.addDocumentationToCollectionIfMissing([], documentation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentation);
      });

      it('should not add a Documentation to an array that contains it', () => {
        const documentation: IDocumentation = { id: 123 };
        const documentationCollection: IDocumentation[] = [
          {
            ...documentation,
          },
          { id: 456 },
        ];
        expectedResult = service.addDocumentationToCollectionIfMissing(documentationCollection, documentation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Documentation to an array that doesn't contain it", () => {
        const documentation: IDocumentation = { id: 123 };
        const documentationCollection: IDocumentation[] = [{ id: 456 }];
        expectedResult = service.addDocumentationToCollectionIfMissing(documentationCollection, documentation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentation);
      });

      it('should add only unique Documentation to an array', () => {
        const documentationArray: IDocumentation[] = [{ id: 123 }, { id: 456 }, { id: 33623 }];
        const documentationCollection: IDocumentation[] = [{ id: 123 }];
        expectedResult = service.addDocumentationToCollectionIfMissing(documentationCollection, ...documentationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentation: IDocumentation = { id: 123 };
        const documentation2: IDocumentation = { id: 456 };
        expectedResult = service.addDocumentationToCollectionIfMissing([], documentation, documentation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentation);
        expect(expectedResult).toContain(documentation2);
      });

      it('should accept null and undefined values', () => {
        const documentation: IDocumentation = { id: 123 };
        expectedResult = service.addDocumentationToCollectionIfMissing([], null, documentation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentation);
      });

      it('should return initial array if no Documentation is added', () => {
        const documentationCollection: IDocumentation[] = [{ id: 123 }];
        expectedResult = service.addDocumentationToCollectionIfMissing(documentationCollection, undefined, null);
        expect(expectedResult).toEqual(documentationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

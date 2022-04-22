import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRecord, Record } from '../record.model';

import { RecordService } from './record.service';

describe('Record Service', () => {
  let service: RecordService;
  let httpMock: HttpTestingController;
  let elemDefault: IRecord;
  let expectedResult: IRecord | IRecord[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecordService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Record', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Record()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Record', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Record', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Record()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Record', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Record', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRecordToCollectionIfMissing', () => {
      it('should add a Record to an empty array', () => {
        const record: IRecord = { id: 123 };
        expectedResult = service.addRecordToCollectionIfMissing([], record);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(record);
      });

      it('should not add a Record to an array that contains it', () => {
        const record: IRecord = { id: 123 };
        const recordCollection: IRecord[] = [
          {
            ...record,
          },
          { id: 456 },
        ];
        expectedResult = service.addRecordToCollectionIfMissing(recordCollection, record);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Record to an array that doesn't contain it", () => {
        const record: IRecord = { id: 123 };
        const recordCollection: IRecord[] = [{ id: 456 }];
        expectedResult = service.addRecordToCollectionIfMissing(recordCollection, record);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(record);
      });

      it('should add only unique Record to an array', () => {
        const recordArray: IRecord[] = [{ id: 123 }, { id: 456 }, { id: 81402 }];
        const recordCollection: IRecord[] = [{ id: 123 }];
        expectedResult = service.addRecordToCollectionIfMissing(recordCollection, ...recordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const record: IRecord = { id: 123 };
        const record2: IRecord = { id: 456 };
        expectedResult = service.addRecordToCollectionIfMissing([], record, record2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(record);
        expect(expectedResult).toContain(record2);
      });

      it('should accept null and undefined values', () => {
        const record: IRecord = { id: 123 };
        expectedResult = service.addRecordToCollectionIfMissing([], null, record, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(record);
      });

      it('should return initial array if no Record is added', () => {
        const recordCollection: IRecord[] = [{ id: 123 }];
        expectedResult = service.addRecordToCollectionIfMissing(recordCollection, undefined, null);
        expect(expectedResult).toEqual(recordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

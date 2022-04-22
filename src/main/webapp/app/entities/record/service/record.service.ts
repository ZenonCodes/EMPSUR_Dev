import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecord, getRecordIdentifier } from '../record.model';

export type EntityResponseType = HttpResponse<IRecord>;
export type EntityArrayResponseType = HttpResponse<IRecord[]>;

@Injectable({ providedIn: 'root' })
export class RecordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/records');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(record: IRecord): Observable<EntityResponseType> {
    return this.http.post<IRecord>(this.resourceUrl, record, { observe: 'response' });
  }

  update(record: IRecord): Observable<EntityResponseType> {
    return this.http.put<IRecord>(`${this.resourceUrl}/${getRecordIdentifier(record) as number}`, record, { observe: 'response' });
  }

  partialUpdate(record: IRecord): Observable<EntityResponseType> {
    return this.http.patch<IRecord>(`${this.resourceUrl}/${getRecordIdentifier(record) as number}`, record, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRecord[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRecordToCollectionIfMissing(recordCollection: IRecord[], ...recordsToCheck: (IRecord | null | undefined)[]): IRecord[] {
    const records: IRecord[] = recordsToCheck.filter(isPresent);
    if (records.length > 0) {
      const recordCollectionIdentifiers = recordCollection.map(recordItem => getRecordIdentifier(recordItem)!);
      const recordsToAdd = records.filter(recordItem => {
        const recordIdentifier = getRecordIdentifier(recordItem);
        if (recordIdentifier == null || recordCollectionIdentifiers.includes(recordIdentifier)) {
          return false;
        }
        recordCollectionIdentifiers.push(recordIdentifier);
        return true;
      });
      return [...recordsToAdd, ...recordCollection];
    }
    return recordCollection;
  }
}

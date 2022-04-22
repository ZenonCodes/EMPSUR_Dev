import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentation, getDocumentationIdentifier } from '../documentation.model';

export type EntityResponseType = HttpResponse<IDocumentation>;
export type EntityArrayResponseType = HttpResponse<IDocumentation[]>;

@Injectable({ providedIn: 'root' })
export class DocumentationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/documentations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentation: IDocumentation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentation);
    return this.http
      .post<IDocumentation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(documentation: IDocumentation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentation);
    return this.http
      .put<IDocumentation>(`${this.resourceUrl}/${getDocumentationIdentifier(documentation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(documentation: IDocumentation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentation);
    return this.http
      .patch<IDocumentation>(`${this.resourceUrl}/${getDocumentationIdentifier(documentation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDocumentation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDocumentation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentationToCollectionIfMissing(
    documentationCollection: IDocumentation[],
    ...documentationsToCheck: (IDocumentation | null | undefined)[]
  ): IDocumentation[] {
    const documentations: IDocumentation[] = documentationsToCheck.filter(isPresent);
    if (documentations.length > 0) {
      const documentationCollectionIdentifiers = documentationCollection.map(
        documentationItem => getDocumentationIdentifier(documentationItem)!
      );
      const documentationsToAdd = documentations.filter(documentationItem => {
        const documentationIdentifier = getDocumentationIdentifier(documentationItem);
        if (documentationIdentifier == null || documentationCollectionIdentifiers.includes(documentationIdentifier)) {
          return false;
        }
        documentationCollectionIdentifiers.push(documentationIdentifier);
        return true;
      });
      return [...documentationsToAdd, ...documentationCollection];
    }
    return documentationCollection;
  }

  protected convertDateFromClient(documentation: IDocumentation): IDocumentation {
    return Object.assign({}, documentation, {
      issued: documentation.issued?.isValid() ? documentation.issued.format(DATE_FORMAT) : undefined,
      expiration: documentation.expiration?.isValid() ? documentation.expiration.format(DATE_FORMAT) : undefined,
      requested: documentation.requested?.isValid() ? documentation.requested.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.issued = res.body.issued ? dayjs(res.body.issued) : undefined;
      res.body.expiration = res.body.expiration ? dayjs(res.body.expiration) : undefined;
      res.body.requested = res.body.requested ? dayjs(res.body.requested) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((documentation: IDocumentation) => {
        documentation.issued = documentation.issued ? dayjs(documentation.issued) : undefined;
        documentation.expiration = documentation.expiration ? dayjs(documentation.expiration) : undefined;
        documentation.requested = documentation.requested ? dayjs(documentation.requested) : undefined;
      });
    }
    return res;
  }
}

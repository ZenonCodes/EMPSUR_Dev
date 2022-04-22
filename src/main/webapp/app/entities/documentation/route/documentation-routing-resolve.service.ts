import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentation, Documentation } from '../documentation.model';
import { DocumentationService } from '../service/documentation.service';

@Injectable({ providedIn: 'root' })
export class DocumentationRoutingResolveService implements Resolve<IDocumentation> {
  constructor(protected service: DocumentationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentation: HttpResponse<Documentation>) => {
          if (documentation.body) {
            return of(documentation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Documentation());
  }
}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'employee',
        data: { pageTitle: 'empsurApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'company',
        data: { pageTitle: 'empsurApp.company.home.title' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'documentation',
        data: { pageTitle: 'empsurApp.documentation.home.title' },
        loadChildren: () => import('./documentation/documentation.module').then(m => m.DocumentationModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'empsurApp.department.home.title' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'record',
        data: { pageTitle: 'empsurApp.record.home.title' },
        loadChildren: () => import('./record/record.module').then(m => m.RecordModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'empsurApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

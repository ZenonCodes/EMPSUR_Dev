import { IEmployee } from 'app/entities/employee/employee.model';
import { ICompany } from 'app/entities/company/company.model';

export interface IDepartment {
  id?: number;
  name?: string;
  employees?: IEmployee[] | null;
  company?: ICompany | null;
}

export class Department implements IDepartment {
  constructor(public id?: number, public name?: string, public employees?: IEmployee[] | null, public company?: ICompany | null) {}
}

export function getDepartmentIdentifier(department: IDepartment): number | undefined {
  return department.id;
}

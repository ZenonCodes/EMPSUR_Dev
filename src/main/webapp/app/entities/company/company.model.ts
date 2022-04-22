import { IEmployee } from 'app/entities/employee/employee.model';
import { IDepartment } from 'app/entities/department/department.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface ICompany {
  id?: number;
  name?: string;
  idNumber?: string;
  status?: Status;
  phone?: string;
  adressLine1?: string;
  adressLine2?: string | null;
  city?: string;
  country?: string;
  employees?: IEmployee[] | null;
  departments?: IDepartment[] | null;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public name?: string,
    public idNumber?: string,
    public status?: Status,
    public phone?: string,
    public adressLine1?: string,
    public adressLine2?: string | null,
    public city?: string,
    public country?: string,
    public employees?: IEmployee[] | null,
    public departments?: IDepartment[] | null
  ) {}
}

export function getCompanyIdentifier(company: ICompany): number | undefined {
  return company.id;
}

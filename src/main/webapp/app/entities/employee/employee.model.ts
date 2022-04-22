import { IUser } from 'app/entities/user/user.model';
import { IDocumentation } from 'app/entities/documentation/documentation.model';
import { IRecord } from 'app/entities/record/record.model';
import { ICompany } from 'app/entities/company/company.model';
import { IDepartment } from 'app/entities/department/department.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IEmployee {
  id?: number;
  firstName?: string;
  lastName?: string;
  fullName?: string;
  gender?: Gender;
  status?: Status;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string | null;
  city?: string;
  country?: string;
  licenseNumber?: string | null;
  user?: IUser | null;
  documentations?: IDocumentation[] | null;
  record?: IRecord | null;
  company?: ICompany | null;
  department?: IDepartment | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public fullName?: string,
    public gender?: Gender,
    public status?: Status,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string | null,
    public city?: string,
    public country?: string,
    public licenseNumber?: string | null,
    public user?: IUser | null,
    public documentations?: IDocumentation[] | null,
    public record?: IRecord | null,
    public company?: ICompany | null,
    public department?: IDepartment | null
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}

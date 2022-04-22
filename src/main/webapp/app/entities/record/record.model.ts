import { IEmployee } from 'app/entities/employee/employee.model';
import { IDocumentation } from 'app/entities/documentation/documentation.model';

export interface IRecord {
  id?: number;
  name?: string;
  employee?: IEmployee | null;
  documentations?: IDocumentation[] | null;
}

export class Record implements IRecord {
  constructor(
    public id?: number,
    public name?: string,
    public employee?: IEmployee | null,
    public documentations?: IDocumentation[] | null
  ) {}
}

export function getRecordIdentifier(record: IRecord): number | undefined {
  return record.id;
}

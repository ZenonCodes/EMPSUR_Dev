import dayjs from 'dayjs/esm';
import { ITag } from 'app/entities/tag/tag.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IRecord } from 'app/entities/record/record.model';
import { DocumentationStatus } from 'app/entities/enumerations/documentation-status.model';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';

export interface IDocumentation {
  id?: number;
  status?: DocumentationStatus;
  name?: string;
  attachmentContentType?: string;
  attachment?: string;
  description?: string | null;
  issued?: dayjs.Dayjs | null;
  expiration?: dayjs.Dayjs | null;
  approval?: TaskStatus | null;
  requested?: dayjs.Dayjs | null;
  tags?: ITag[] | null;
  employee?: IEmployee | null;
  record?: IRecord | null;
}

export class Documentation implements IDocumentation {
  constructor(
    public id?: number,
    public status?: DocumentationStatus,
    public name?: string,
    public attachmentContentType?: string,
    public attachment?: string,
    public description?: string | null,
    public issued?: dayjs.Dayjs | null,
    public expiration?: dayjs.Dayjs | null,
    public approval?: TaskStatus | null,
    public requested?: dayjs.Dayjs | null,
    public tags?: ITag[] | null,
    public employee?: IEmployee | null,
    public record?: IRecord | null
  ) {}
}

export function getDocumentationIdentifier(documentation: IDocumentation): number | undefined {
  return documentation.id;
}

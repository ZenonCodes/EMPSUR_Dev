import { IDocumentation } from 'app/entities/documentation/documentation.model';

export interface ITag {
  id?: number;
  name?: string;
  documentations?: IDocumentation[] | null;
}

export class Tag implements ITag {
  constructor(public id?: number, public name?: string, public documentations?: IDocumentation[] | null) {}
}

export function getTagIdentifier(tag: ITag): number | undefined {
  return tag.id;
}

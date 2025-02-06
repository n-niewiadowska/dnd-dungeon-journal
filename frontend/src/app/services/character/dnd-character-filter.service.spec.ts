import { TestBed } from '@angular/core/testing';

import { DndCharacterFilterService } from './dnd-character-filter.service';

describe('DndCharacterFilterService', () => {
  let service: DndCharacterFilterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DndCharacterFilterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

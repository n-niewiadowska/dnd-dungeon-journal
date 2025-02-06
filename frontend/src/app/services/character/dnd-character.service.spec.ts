import { TestBed } from '@angular/core/testing';

import { DndCharacterService } from './dnd-character.service';

describe('DndCharacterService', () => {
  let service: DndCharacterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DndCharacterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';

import { SkillFilterService } from './skill-filter.service';

describe('SkillFilterService', () => {
  let service: SkillFilterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SkillFilterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

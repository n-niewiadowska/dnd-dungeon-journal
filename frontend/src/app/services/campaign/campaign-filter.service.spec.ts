import { TestBed } from '@angular/core/testing';

import { CampaignFilterService } from './campaign-filter.service';

describe('CampaignFilterService', () => {
  let service: CampaignFilterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CampaignFilterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

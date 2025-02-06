import { Injectable } from '@angular/core';
import { CampaignDto } from '../../shared/dtos/campaign.dto';
import { BehaviorSubject, combineLatest, map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CampaignFilterService {
  private allCampaigns$ = new BehaviorSubject<CampaignDto[]>([]);
  private textFilter$ = new BehaviorSubject<string>('');
  private statusFilter$ = new BehaviorSubject<string[]>([]);
  private dateFilter$ = new BehaviorSubject<'past' | 'future' | null>(null);
  private sort$ = new BehaviorSubject<'title-asc' | 'title-desc' | 'date-asc' | 'date-desc'>('title-asc');
  private page$ = new BehaviorSubject<number>(1);
  private pageSize$ = new BehaviorSubject<number>(2);

  public campaigns$(): Observable<CampaignDto[]> {
    return combineLatest([
      this.allCampaigns$,
      this.textFilter$,
      this.statusFilter$,
      this.dateFilter$,
      this.sort$,
      this.page$,
      this.pageSize$
    ]).pipe(
      map(([campaigns, text, statuses, dateFilter, sort, page, pageSize]) => {
        let filtered = campaigns;

        if (text) {
          filtered = filtered.filter((campaign) =>
            campaign.title.toLowerCase().includes(text.toLowerCase())
          );
        }

        if (statuses.length > 0) {
          filtered = filtered.filter((campaign) => statuses.includes(campaign.status.toString()));
        }

        if (dateFilter) {
          const now = new Date();
          filtered = filtered.filter((campaign) =>
            dateFilter === 'past'
              ? new Date(campaign.beginningDate) < now
              : new Date(campaign.beginningDate) >= now
          );
        }

        filtered = this.sortCampaigns(filtered, sort);

        const startIndex = (page - 1) * pageSize;
        const paginated = filtered.slice(startIndex, startIndex + pageSize);

        return paginated;
      })
    );
  }

  public setCampaigns(campaigns: CampaignDto[]): void {
    this.allCampaigns$.next(campaigns);
  }

  public applyTextFilter(value: string): void {
    this.textFilter$.next(value);
  }

  public applyStatusFilter(status: string, checked: boolean | null): void {
    const currentStatusFilter = this.statusFilter$.getValue();
    if (checked) {
      this.statusFilter$.next([...currentStatusFilter, status]);
    } else {
      this.statusFilter$.next(currentStatusFilter.filter((s) => s !== status));
    }
  }

  public applyDateFilter(filter: 'past' | 'future' | null): void {
    this.dateFilter$.next(filter);
  }

  public applySort(sort: 'title-asc' | 'title-desc' | 'date-asc' | 'date-desc'): void {
    this.sort$.next(sort);
  }

  public setPage(page: number): void {
    this.page$.next(page);
  }
  
  public setPageSize(pageSize: number): void {
    this.pageSize$.next(pageSize);
    this.page$.next(1);
  }
  
  public getTotalPages(): Observable<number> {
    return combineLatest([this.allCampaigns$, this.pageSize$]).pipe(
      map(([campaigns, pageSize]) => Math.ceil(campaigns.length / pageSize))
    );
  }

  public resetFilters(): void {
    this.textFilter$.next('');
    this.statusFilter$.next([]);
    this.dateFilter$.next(null);
  }

  private sortCampaigns(campaigns: CampaignDto[], sortType: string): CampaignDto[] {
    if (sortType === 'title-asc') {
      campaigns.sort((a, b) => a.title.localeCompare(b.title));
    } else if (sortType === 'title-desc') {
      campaigns.sort((a, b) => b.title.localeCompare(a.title));
    } else if (sortType === 'date-asc') {
      campaigns.sort((a, b) => new Date(a.beginningDate).getTime() - new Date(b.beginningDate).getTime());
    } else if (sortType === 'date-desc') {
      campaigns.sort((a, b) => new Date(b.beginningDate).getTime() - new Date(a.beginningDate).getTime());
    }

    return campaigns;
  }
}

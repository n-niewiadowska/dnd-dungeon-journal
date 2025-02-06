import { Component, OnInit } from '@angular/core';
import { CampaignDto } from '../../../shared/dtos/campaign.dto';
import { CampaignService } from '../../../services/campaign/campaign.service';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { CheckboxModule } from 'primeng/checkbox';
import { RadioButtonModule } from 'primeng/radiobutton';
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { GameStatus } from '../../../shared/enums/game-status.enum';
import { CampaignFilterService } from '../../../services/campaign/campaign-filter.service';
import { AsyncPipe, Location } from '@angular/common';

@Component({
  selector: 'app-campaign-list',
  standalone: true,
  imports: [ TranslatePipe, AsyncPipe, TranslateDirective, ButtonModule, IconFieldModule, InputIconModule, CheckboxModule, RadioButtonModule ],
  templateUrl: './campaign-list.component.html',
  styleUrl: './campaign-list.component.scss'
})
export class CampaignListComponent implements OnInit {
  public campaigns$: Observable<CampaignDto[]>;
  public totalPages$: Observable<number>;
  public currPage$: Observable<number>;
  public readonly statuses: string[] = Object.values(GameStatus).filter((status) => typeof status === 'string');
  public totalPages: number = 0;
  public currPage: number = 0;

  public constructor(
      private campaignService: CampaignService, 
      private campaignFilterService: CampaignFilterService, 
      private router: Router,
      private location: Location
  ) { 
    this.campaigns$ = this.campaignFilterService.campaigns$();
    this.totalPages$ = this.campaignFilterService.getTotalPages();
    this.currPage$ = this.campaignFilterService['page$'];
  }

  public ngOnInit(): void {
    this.campaignService.getAll().subscribe((result) => this.campaignFilterService.setCampaigns(result));
    this.totalPages$.subscribe((result) => this.totalPages = result);
    this.currPage$.subscribe((result) => this.currPage = result);
  }

  public applyTextFilter(value: string): void {
    this.campaignFilterService.applyTextFilter(value);
  }

  public applyStatusFilter(status: string, checked: boolean | null): void {
    this.campaignFilterService.applyStatusFilter(status, checked);
  }

  public applyDateFilter(filter: 'past' | 'future' | null): void {
    this.campaignFilterService.applyDateFilter(filter);
  }

  public applySort(sort: 'title-asc' | 'title-desc' | 'date-asc' | 'date-desc'): void {
    this.campaignFilterService.applySort(sort);
  }

  public clearFilters(): void {
    const form = document.querySelector('form');
    if (form) {
      form.reset();
    }
    this.campaignFilterService.resetFilters();
  }

  public changePage(page: number): void {
    this.campaignFilterService.setPage(page);
  }

  public changePageSize(pageSize: number): void {
    this.campaignFilterService.setPageSize(pageSize);
  }

  public createCampaign(): void {
    this.router.navigate(['/campaign', 'form']);
  }

  public goToDetails(id: number): void {
    this.router.navigate(['/campaign', id, 'details']);
  }

  public goBack(): void {
    this.location.back();
  }
}

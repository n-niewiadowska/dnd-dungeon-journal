import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { CampaignService } from '../../../services/campaign/campaign.service';
import { CampaignDto } from '../../../shared/dtos/campaign.dto';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { MessageService } from 'primeng/api';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-campaign-details',
  standalone: true,
  imports: [ TranslatePipe, ButtonModule, DialogModule ],
  templateUrl: './campaign-details.component.html',
  styleUrl: './campaign-details.component.scss',
})
export class CampaignDetailsComponent implements OnInit {
  public campaign: CampaignDto | null = null;
  public visible: boolean = false;

  public constructor(
    private campaignService: CampaignService, 
    private translateService: TranslateService,
    private messageService: MessageService,
    private router: Router,
    private route: ActivatedRoute, 
    private location: Location,
  ) { }

  public ngOnInit(): void {
    const campaignId = this.route.snapshot.paramMap.get('id');
    if (campaignId) {
      this.campaignService.getById(Number(campaignId)).pipe(
        catchError((_) => {
          this.router.navigate(['/not-found']);
            
          return of(null);
        })
      ).subscribe((result) => {
        this.campaign = result;
      });
    }
  }

  public editCampaign(id: number): void {
    this.router.navigate(['/campaign', id, 'form']);
  }

  public deleteCampaign(): void {
    if (this.campaign && this.campaign.id) {
      this.campaignService.delete(this.campaign.id.toString()).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: this.translateService.instant('toast.summary.delete-success'),
            detail: this.translateService.instant('toast.campaign.delete-success', { title: this.campaign?.title }),
          });
          this.goBack();
        },
        error: () => {
          this.messageService.add({
            severity: 'danger',
            summary: this.translateService.instant('toast.summary.error'),
            detail: this.translateService.instant('toast.summary.delete-error'),
          });
        }
      });
    }
    this.visible = false;
  }

  public showDialog(): void {
    this.visible = true;
  }

  public goBack(): void {
    this.location.back();
  }
}

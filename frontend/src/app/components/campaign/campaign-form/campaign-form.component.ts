import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CampaignService } from '../../../services/campaign/campaign.service';
import { Location } from '@angular/common';
import { CampaignForm } from '../../../shared/forms/campaign-form';
import { CampaignDto } from '../../../shared/dtos/campaign.dto';
import { GameStatus } from '../../../shared/enums/game-status.enum';
import { MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DatePickerModule } from 'primeng/datepicker';
import { TextareaModule } from 'primeng/textarea';
import { ValidationMessageComponent } from '../../validation-message/validation-message.component';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-campaign-form',
  standalone: true,
  imports: [ ReactiveFormsModule, ValidationMessageComponent, TranslatePipe, DialogModule, ButtonModule, InputTextModule, DatePickerModule, TextareaModule ],
  templateUrl: './campaign-form.component.html',
  styleUrl: './campaign-form.component.scss'
})
export class CampaignFormComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();
  public gameStatuses = Object.values(GameStatus).filter((status) => typeof status === 'string');
  public visible: boolean = false;
  public isEdit: boolean = false;
  public campaignId: number | null = null;

  public campaignForm = new FormGroup<CampaignForm>({
    id: new FormControl<number | null>(this.campaignId || null),
    title: new FormControl<string | null>('', [
      Validators.required,
    ]),
    description: new FormControl<string | null>(''),
    beginningDate: new FormControl<Date | null>(new Date(), [
      Validators.required,
    ]),
    status: new FormControl<GameStatus | null>(null),
  },
  );


  public constructor(
    private campaignService: CampaignService,
    private translateService: TranslateService,
    private messageService: MessageService,
    private router: Router,
    private route: ActivatedRoute, 
    private location: Location,
  ) {}

  public ngOnInit(): void {
    this.campaignId = Number(this.route.snapshot.paramMap.get('id'));
    this.isEdit = !!this.campaignId;

    if (this.isEdit && this.campaignId) {
      this.subscription.add(
        this.campaignService.getById(this.campaignId).subscribe({
          next: (result) => {
            this.campaignForm.setValue(result);
          },
          error: () => this.router.navigate(['/not-found'])
        })
      );
    }
  }

  public saveCampaign(): void {
    if (this.campaignForm.invalid) {
      this.campaignForm.markAllAsTouched();
      
      return;
    }

    const campaignData: CampaignDto = this.mapFormToDto();

    if (this.isEdit && this.campaignId) {
      this.subscription.add(
        this.campaignService.edit(this.campaignId, campaignData).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: this.translateService.instant('toast.summary.edit-success'),
              detail: this.translateService.instant('toast.campaign.edit-success'),
            });
            this.goBack();
          },
          error: () => {
            this.messageService.add({
              severity: 'danger',
              summary: this.translateService.instant('toast.summary.error'),
              detail: this.translateService.instant('toast.campaign.edit-error'),
            });
          }
        })
      );
    } else {
      this.subscription.add(
        this.campaignService.create(campaignData).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: this.translateService.instant('toast.summary.create-success'),
              detail: this.translateService.instant('toast.campaign.create-success'),
            });
            this.goBack();
          },
          error: () => {
            this.messageService.add({
              severity: 'danger',
              summary: this.translateService.instant('toast.summary.error'),
              detail: this.translateService.instant('toast.campaign.create-error'),
            });
            this.goBack();
          }
        })
      );
    }

    this.campaignForm.reset();
    this.campaignForm.markAsPristine();
    this.campaignForm.markAsUntouched();
  }

  public showDialog(): void {
    if (this.isEdit) {
      this.visible = true;
    } else {
      this.saveCampaign();
    }
  }

  public goBack(): void {
    this.location.back();
  }

  public ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private mapFormToDto(): CampaignDto {
    const formValue = this.campaignForm.value;

    return {
      id: formValue.id!,
      title: formValue.title!,
      description: formValue.description || '',
      beginningDate: formValue.beginningDate!,
      status: formValue.status || GameStatus.PLANNED,
    };
  }
}

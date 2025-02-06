import { Routes } from "@angular/router";
import { CampaignListComponent } from "./campaign-list/campaign-list.component";
import { CampaignDetailsComponent } from "./campaign-details/campaign-details.component";
import { CampaignFormComponent } from "./campaign-form/campaign-form.component";

export const CAMPAIGN_ROUTES: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'list',
  },
  {
    path: 'list',
    component: CampaignListComponent,
    title: 'DnD Campaigns',
  },
  {
    path: ':id/details',
    component: CampaignDetailsComponent,
    title: 'DnD Campaign Details',
  },
  {
    path: 'form',
    component: CampaignFormComponent,
    title: 'DnD Campaign Form'
  },
  {
    path: ':id/form',
    component: CampaignFormComponent,
    title: 'DnD Campaign Edit Form'
  }
];
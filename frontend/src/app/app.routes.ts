import { Routes } from '@angular/router';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { HomeComponent } from './components/home/home.component';

export const routes: Routes = [
  {
    path: '',
    title: 'Dungeon Journal',
    component: HomeComponent,
  },
  {
    path: 'campaign',
    loadChildren: () =>
      import('./components/campaign/campaign.routes').then((r) => r.CAMPAIGN_ROUTES),
  },
  {
    path: 'character',
    loadChildren: () =>
      import('./components/character/character.routes').then((r) => r.CHARACTER_ROUTES),
  },
  {
    path: 'skill',
    loadChildren: () =>
      import('./components/skill/skill.routes').then((r) => r.SKILL_ROUTES),
  },
  {
    path: 'not-found',
    title: '404',
    component: PageNotFoundComponent,
  },
  {
    path: '**',
    redirectTo: 'not-found',
  }
];

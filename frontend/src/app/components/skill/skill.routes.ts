import { Routes } from "@angular/router";
import { SkillListComponent } from "./skill-list/skill-list.component";
import { SkillDetailsComponent } from "./skill-details/skill-details.component";

export const SKILL_ROUTES: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'list',
  },
  {
    path: 'list',
    component: SkillListComponent,
    title: 'DnD Skills',
  },
  {
    path: ':id/details',
    component: SkillDetailsComponent,
    title: 'DnD Skill Details',
  },
];
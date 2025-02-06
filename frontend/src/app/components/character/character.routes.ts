import { Routes } from "@angular/router";
import { CharacterListComponent } from "./character-list/character-list.component";
import { CharacterDetailsComponent } from "./character-details/character-details.component";
import { CharacterFormComponent } from "./character-form/character-form.component";

export const CHARACTER_ROUTES: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'list',
  },
  {
    path: 'list',
    component: CharacterListComponent,
    title: 'DnD Characters',
  },
  {
    path: ':id/details',
    component: CharacterDetailsComponent,
    title: 'DnD Character Details',
  },
  {
    path: 'form',
    component: CharacterFormComponent,
    title: 'DnD Character Form'
  },
  {
    path: ':id/form',
    component: CharacterFormComponent,
    title: 'DnD Character Edit Form'
  }
];
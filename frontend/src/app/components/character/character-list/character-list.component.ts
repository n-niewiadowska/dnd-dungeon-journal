import { Component, OnInit } from '@angular/core';
import { DndCharacterDto } from '../../../shared/dtos/dnd-character.dto';
import { DndCharacterService } from '../../../services/character/dnd-character.service';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { DndCharacterFilterService } from '../../../services/character/dnd-character-filter.service';
import { Race } from '../../../shared/enums/race.enum';
import { DndClass } from '../../../shared/enums/dnd-class.enum';
import { AsyncPipe, Location } from '@angular/common';

@Component({
  selector: 'app-character-list',
  standalone: true,
  imports: [ TranslatePipe, AsyncPipe, TranslateDirective, ButtonModule, ToggleButtonModule ],
  templateUrl: './character-list.component.html',
  styleUrl: './character-list.component.scss'
})
export class CharacterListComponent implements OnInit {
  public characters$: Observable<DndCharacterDto[]>;
  public totalPages$: Observable<number>;
  public currPage$: Observable<number>;
  public readonly races: string[] = Object.values(Race).filter((race) => typeof race === 'string');
  public readonly dndClasses: string[] = Object.values(DndClass).filter((dndClass) => typeof dndClass === 'string');
  public totalPages: number = 0;
  public currPage: number = 0;
  
  public constructor(
      private characterService: DndCharacterService, 
      private characterFilterService: DndCharacterFilterService, 
      private router: Router,
      private location: Location
  ) { 
    this.characters$ = this.characterFilterService.characters$();
    this.totalPages$ = this.characterFilterService.getTotalPages();
    this.currPage$ = this.characterFilterService['page$'];
  }

  public ngOnInit(): void {
    this.characterService.getAll().subscribe((result) => this.characterFilterService.setCharacters(result));
    this.totalPages$.subscribe((result) => this.totalPages = result);
    this.currPage$.subscribe((result) => this.currPage = result);
  }

  public applyRaceFilter(race: string): void {
    this.characterFilterService.applyRaceFilter(race);
  }

  public applyClassFilter(dndClass: string, checked: boolean | null): void {
    this.characterFilterService.applyClassFilter(dndClass, checked);
  }

  public applyMagicFilter(isMagic: boolean | null): void {
    this.characterFilterService.applyMagicFilter(isMagic);
  }

  public applySort(sort: 'firstname-asc' | 'firstname-desc' | 'age-asc' | 'age-desc'): void {
    this.characterFilterService.applySort(sort);
  }

  public clearFilters(): void {
    const form = document.querySelector('form');
    if (form) {
      form.reset();
    }
    this.characterFilterService.resetFilters();
  }

  public changePage(page: number): void {
    this.characterFilterService.setPage(page);
  }

  public changePageSize(pageSize: number): void {
    this.characterFilterService.setPageSize(pageSize);
  }

  public createCharacter(): void {
    this.router.navigate(['/character', 'form']);
  }

  public goToDetails(id: number): void {
    this.router.navigate(['/character', id, 'details']);
  }

  public goBack(): void {
    this.location.back();
  }
}

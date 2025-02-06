import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Ability } from '../../../shared/enums/ability.enum';
import { SkillDto } from '../../../shared/dtos/skill.dto';
import { SkillService } from '../../../services/skill/skill.service';
import { SkillFilterService } from '../../../services/skill/skill-filter.service';
import { Router } from '@angular/router';
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';
import { AsyncPipe, Location } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { SliderModule } from 'primeng/slider';

@Component({
  selector: 'app-skill-list',
  standalone: true,
  imports: [ TranslatePipe, AsyncPipe, TranslateDirective, ButtonModule, IconFieldModule, InputIconModule, SliderModule ],
  templateUrl: './skill-list.component.html',
  styleUrl: './skill-list.component.scss'
})
export class SkillListComponent implements OnInit {
  public skills$: Observable<SkillDto[]>;
  public totalPages$: Observable<number>;
  public currPage$: Observable<number>;
  public readonly abilities: string[] = Object.values(Ability).filter((ability) => typeof ability === 'string');
  public totalPages: number = 0;
  public currPage: number = 0;
  
  public constructor(
    private skillService: SkillService, 
    private skillFilterService: SkillFilterService, 
    private router: Router,
    private location: Location
  ) { 
    this.skills$ = this.skillFilterService.skills$();
    this.totalPages$ = this.skillFilterService.getTotalPages();
    this.currPage$ = this.skillFilterService['page$'];
  }

  public ngOnInit(): void {
    this.skillService.getAll().subscribe((result) => this.skillFilterService.setSkills(result));
    this.totalPages$.subscribe((result) => this.totalPages = result);
    this.currPage$.subscribe((result) => this.currPage = result);
  }

  public applyTextFilter(value: string): void {
    this.skillFilterService.applyTextFilter(value);
  }

  public applyAbilityFilter(ability: string): void {
    this.skillFilterService.applyAbilityFilter(ability);
  }

  public applyLevelFilter(levelRange: { min: number, max: number } | null): void {
    this.skillFilterService.applyLevelFilter(levelRange);
  }

  public applySort(sort: 'name-asc' | 'name-desc' | 'level-asc' | 'level-desc'): void {
    this.skillFilterService.applySort(sort);
  }

  public clearFilters(): void {
    const form = document.querySelector('form');
    if (form) {
      form.reset();
    }
    this.skillFilterService.resetFilters();
  }

  public changePage(page: number): void {
    this.skillFilterService.setPage(page);
  }

  public changePageSize(pageSize: number): void {
    this.skillFilterService.setPageSize(pageSize);
  }

  public goToDetails(id: number): void {
    this.router.navigate(['/skill', id, 'details']);
  }

  public goBack(): void {
    this.location.back();
  }
}

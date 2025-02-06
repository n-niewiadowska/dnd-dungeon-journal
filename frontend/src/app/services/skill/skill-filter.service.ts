import { Injectable } from '@angular/core';
import { BehaviorSubject, combineLatest, map, Observable } from 'rxjs';
import { SkillDto } from '../../shared/dtos/skill.dto';

@Injectable({
  providedIn: 'root'
})
export class SkillFilterService {
  private allSkills$ = new BehaviorSubject<SkillDto[]>([]);
  private textFilter$ = new BehaviorSubject<string>('');
  private abilityFilter$ = new BehaviorSubject<string>('');
  private levelFilter$ = new BehaviorSubject<{min: number, max: number} | null>(null);
  private sort$ = new BehaviorSubject<'name-asc' | 'name-desc' | 'level-asc' | 'level-desc'>('name-asc');
  private page$ = new BehaviorSubject<number>(1);
  private pageSize$ = new BehaviorSubject<number>(2);

  public skills$(): Observable<SkillDto[]> {
    return combineLatest([
      this.allSkills$,
      this.textFilter$,
      this.abilityFilter$,
      this.levelFilter$,
      this.sort$,
      this.page$,
      this.pageSize$
    ]).pipe(
      map(([skills, text, ability, level, sort, page, pageSize]) => {
        let filtered = skills;

        if (text) {
          filtered = filtered.filter((skill) =>
            skill.name.toLowerCase().includes(text.toLowerCase())
          );
        }

        if (ability) {
          filtered = filtered.filter((skill) =>
            skill.relatedAbility.toString() === ability);
        }

        if (level) {
          filtered = filtered.filter((skill) =>
            skill.level >= level.min && skill.level <= level.max
          );
        }

        filtered = this.sortCharacters(filtered, sort);

        const startIndex = (page - 1) * pageSize;
        const paginated = filtered.slice(startIndex, startIndex + pageSize);

        return paginated;
      })
    );
  }

  public setSkills(characters: SkillDto[]): void {
    this.allSkills$.next(characters);
  }

  public applyTextFilter(text: string): void {
    this.textFilter$.next(text);
  }

  public applyAbilityFilter(ability: string): void {
    this.abilityFilter$.next(ability);
  }

  public applyLevelFilter(levelRange: { min: number, max: number} | null): void {
    this.levelFilter$.next(levelRange);
  }

  public applySort(sort: 'name-asc' | 'name-desc' | 'level-asc' | 'level-desc'): void {
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
    return combineLatest([this.allSkills$, this.pageSize$]).pipe(
      map(([campaigns, pageSize]) => Math.ceil(campaigns.length / pageSize))
    );
  }

  public resetFilters(): void {
    this.textFilter$.next('');
    this.abilityFilter$.next('');
    this.levelFilter$.next(null);
  }

  private sortCharacters(skills: SkillDto[], sortType: string): SkillDto[] {
    if (sortType === 'name-asc') {
      skills.sort((a, b) => a.name.localeCompare(b.name));
    } else if (sortType === 'name-desc') {
      skills.sort((a, b) => b.name.localeCompare(a.name));
    } else if (sortType === 'level-asc') {
      skills.sort((a, b) => a.level - b.level);
    } else if (sortType === 'level-desc') {
      skills.sort((a, b) => b.level - a.level);
    }

    return skills;
  }
}

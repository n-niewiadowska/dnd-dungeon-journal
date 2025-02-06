import { Injectable } from '@angular/core';
import { BehaviorSubject, combineLatest, map, Observable } from 'rxjs';
import { DndCharacterDto } from '../../shared/dtos/dnd-character.dto';

@Injectable({
  providedIn: 'root'
})
export class DndCharacterFilterService {
  private allCharacters$ = new BehaviorSubject<DndCharacterDto[]>([]);
  private raceFilter$ = new BehaviorSubject<string>('');
  private classFilter$ = new BehaviorSubject<string[]>([]);
  private magicFilter$ = new BehaviorSubject<boolean | null>(null);
  private sort$ = new BehaviorSubject<'firstname-asc' | 'firstname-desc' | 'age-asc' | 'age-desc'>('firstname-asc');
  private page$ = new BehaviorSubject<number>(1);
  private pageSize$ = new BehaviorSubject<number>(2);

  public characters$(): Observable<DndCharacterDto[]> {
    return combineLatest([
      this.allCharacters$,
      this.raceFilter$,
      this.classFilter$,
      this.magicFilter$,
      this.sort$,
      this.page$,
      this.pageSize$
    ]).pipe(
      map(([characters, dndRace, dndClasses, isMagic, sort, page, pageSize]) => {
        let filtered = characters;

        if (dndRace) {
          filtered = filtered.filter((character) =>
            character.race.toString() === dndRace);
        }

        if (dndClasses.length > 0) {
          filtered = filtered.filter((character) => 
            dndClasses.includes(character.dndClass.toString()));
        }

        if (isMagic != null) {
          filtered = filtered.filter((character) =>
            isMagic === character.canPerformMagic);
        }

        filtered = this.sortCharacters(filtered, sort);

        const startIndex = (page - 1) * pageSize;
        const paginated = filtered.slice(startIndex, startIndex + pageSize);

        return paginated;
      })
    );
  }

  public setCharacters(characters: DndCharacterDto[]): void {
    this.allCharacters$.next(characters);
  }

  public applyRaceFilter(race: string): void {
    this.raceFilter$.next(race);
  }

  public applyClassFilter(dndClass: string, checked: boolean | null): void {
    const currentClassFilter = this.classFilter$.getValue();
    if (checked) {
      this.classFilter$.next([...currentClassFilter, dndClass]);
    } else {
      this.classFilter$.next(currentClassFilter.filter((c) => c !== dndClass));
    }
  }

  public applyMagicFilter(isMagic: boolean | null): void {
    this.magicFilter$.next(isMagic);
  }

  public applySort(sort: 'firstname-asc' | 'firstname-desc' | 'age-asc' | 'age-desc'): void {
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
    return combineLatest([this.allCharacters$, this.pageSize$]).pipe(
      map(([campaigns, pageSize]) => Math.ceil(campaigns.length / pageSize))
    );
  }

  public resetFilters(): void {
    this.raceFilter$.next('');
    this.classFilter$.next([]);
    this.magicFilter$.next(null);
  }

  private sortCharacters(characters: DndCharacterDto[], sortType: string): DndCharacterDto[] {
    if (sortType === 'firstname-asc') {
      characters.sort((a, b) => a.firstName.localeCompare(b.firstName));
    } else if (sortType === 'firstname-desc') {
      characters.sort((a, b) => b.firstName.localeCompare(a.firstName));
    } else if (sortType === 'age-asc') {
      characters.sort((a, b) => a.age - b.age);
    } else if (sortType === 'age-desc') {
      characters.sort((a, b) => b.age - a.age);
    }

    return characters;
  }
}

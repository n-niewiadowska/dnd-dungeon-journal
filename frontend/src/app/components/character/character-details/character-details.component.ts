import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { DndCharacterService } from '../../../services/character/dnd-character.service';
import { DndCharacterDto } from '../../../shared/dtos/dnd-character.dto';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { TranslateDirective, TranslatePipe, TranslateService } from '@ngx-translate/core';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-character-details',
  standalone: true,
  imports: [ TranslatePipe, TranslateDirective, DialogModule, ButtonModule ],
  templateUrl: './character-details.component.html',
  styleUrl: './character-details.component.scss'
})
export class CharacterDetailsComponent implements OnInit {
  public character: DndCharacterDto | null = null;
  public visible: boolean = false;

  public constructor(
    private characterService: DndCharacterService, 
    private translateService: TranslateService,
    private messageService: MessageService,
    private router: Router,
    private route: ActivatedRoute, 
    private location: Location,
  ) { }

  public ngOnInit(): void {
    const characterId = this.route.snapshot.paramMap.get('id');
    if (characterId) {
      this.characterService.getById(Number(characterId)).pipe(
        catchError((_) => {
          this.router.navigate(['/not-found']);
          
          return of(null);
        })
      ).subscribe((result) => {
        this.character = result;
      });
    }
  }

  public editCharacter(id: number): void {
    this.router.navigate(['/character', id, 'form']);
  }

  public deleteCharacter(): void {
    if (this.character && this.character.id) {
      this.characterService.delete(this.character.id.toString()).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: this.translateService.instant('toast.summary.delete-success'),
            detail: this.translateService.instant(
              'toast.character.delete-success', 
              { firstName: this.character?.firstName, lastName: this.character?.lastName}),
          });
          this.goBack();
        },
        error: () => {
          this.messageService.add({
            severity: 'danger',
            summary: this.translateService.instant('toast.summary.error'),
            detail: this.translateService.instant('toast.character.delete-error'),
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

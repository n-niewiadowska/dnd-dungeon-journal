import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { DndClass } from '../../../shared/enums/dnd-class.enum';
import { Race } from '../../../shared/enums/race.enum';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DndCharacterForm } from '../../../shared/forms/dnd-character-form';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { DndCharacterService } from '../../../services/character/dnd-character.service';
import { MessageService } from 'primeng/api';
import { DndCharacterDto } from '../../../shared/dtos/dnd-character.dto';
import { SkillForm } from '../../../shared/forms/skill-form';
import { Ability } from '../../../shared/enums/ability.enum';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ValidationMessageComponent } from '../../validation-message/validation-message.component';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-character-form',
  standalone: true,
  imports: [ ReactiveFormsModule, TranslatePipe, ValidationMessageComponent, ButtonModule, DialogModule, InputTextModule, TextareaModule, InputNumberModule, RadioButtonModule ],
  templateUrl: './character-form.component.html',
  styleUrl: './character-form.component.scss'
})
export class CharacterFormComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();
  public dndClasses = Object.values(DndClass).filter((dndClass) => typeof dndClass === 'string');
  public races = Object.values(Race).filter((race) => typeof race === 'string');
  public abilities = Object.values(Ability).filter((ability) => typeof ability === 'string');
  public visible: boolean = false;
  public isEdit: boolean = false;
  public characterId: number | null = null;
  public skillId: number | null = null;

  public skillForm: FormGroup<SkillForm> = new FormGroup<SkillForm>({
    id: new FormControl<number | null>(this.skillId || null),
    name: new FormControl<string | null>('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(30),
    ]),
    relatedAbility: new FormControl<Ability | null>(null),
    description: new FormControl<string | null>(''),
    level: new FormControl<number | null>(0, [
      Validators.min(0)
    ]),
    effect: new FormControl<string | null>(''),
  });

  public characterForm = new FormGroup<DndCharacterForm>({
    id: new FormControl<number | null>(this.characterId || null),
    firstName: new FormControl<string | null>('', [
      Validators.required,
    ]),
    lastName: new FormControl<string | null>('', [
      Validators.required,
    ]),
    dndClass: new FormControl<DndClass | null>(null),
    race: new FormControl<Race | null>(null),
    age: new FormControl<number | null>(15, [
      Validators.min(15),
    ]),
    canPerformMagic: new FormControl<boolean | null>(false),
    skill: this.skillForm,
  },
  );

  public constructor(
    private characterService: DndCharacterService,
    private translateService: TranslateService,
    private messageService: MessageService,
    private router: Router,
    private route: ActivatedRoute, 
    private location: Location,
  ) {}

  public ngOnInit(): void {
    this.characterId = Number(this.route.snapshot.paramMap.get('id'));
    this.isEdit = !!this.characterId;

    if (this.isEdit && this.characterId) {
      this.subscription.add(
        this.characterService.getById(this.characterId).subscribe({
          next: (result) => {
            this.skillId = result.skill?.id || null;

            const characterData = {
              ...result,
              skill: result.skill || {
                id: 0,
                name: '',
                relatedAbility: null,
                description: '',
                level: 0,
                effect: '',
              },
            };

            this.characterForm.setValue(characterData);
            
          },
          error: () => this.router.navigate(['/not-found'])
        })
      );
    }
  }

  public saveCharacter(): void {
    if (this.characterForm.invalid) {
      this.characterForm.markAllAsTouched();
      
      return;
    }

    const characterData: DndCharacterDto = this.mapFormToDto();

    if (this.isEdit && this.characterId) {
      this.subscription.add(
        this.characterService.edit(this.characterId, characterData).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: this.translateService.instant('toast.summary.edit-success'),
              detail: this.translateService.instant('toast.character.edit-success')
            });
            this.goBack();
          },
          error: () => {
            this.messageService.add({
              severity: 'danger',
              summary: this.translateService.instant('toast.summary.error'),
              detail: this.translateService.instant('toast.character.edit-error')
            });
          }
        })
      );
    } else {
      this.subscription.add(
        this.characterService.create(characterData).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: this.translateService.instant('toast.summary.create-success'),
              detail: this.translateService.instant('toast.character.create-success')
            });
            this.goBack();
          },
          error: () => {
            this.messageService.add({
              severity: 'danger',
              summary: this.translateService.instant('toast.summary.error'),
              detail: this.translateService.instant('toast.character.create-error')
            });
            this.goBack();
          }
        })
      );
    }

    this.characterForm.reset();
    this.characterForm.markAsPristine();
    this.characterForm.markAsUntouched();
  }

  public showDialog(): void {
    if (this.isEdit) {
      this.visible = true;
    } else {
      this.saveCharacter();
    }
  }

  public goBack(): void {
    this.location.back();
  }

  public ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private mapFormToDto(): DndCharacterDto {
    const formValue = this.characterForm.value;

    return {
      id: formValue.id!,
      firstName: formValue.firstName!,
      lastName: formValue.lastName!,
      dndClass: formValue.dndClass!,
      race: formValue.race!,
      age: formValue.age!,
      canPerformMagic: formValue.canPerformMagic!,
      skill: formValue.skill
        ? {
          id: formValue.skill.id!,
          name: formValue.skill.name!,
          relatedAbility: formValue.skill.relatedAbility!,
          description: formValue.skill.description!,
          level: formValue.skill.level ?? 0,
          effect: formValue.skill.effect!,
        }
        : null,
    };
  }
}

import { FormControl } from "@angular/forms";
import { Ability } from "../enums/ability.enum";

export interface SkillForm {
    id: FormControl<number | null>,
    name: FormControl<string | null>,
    relatedAbility: FormControl<Ability | null>,
    description: FormControl<string | null>,
    level: FormControl<number | null>,
    effect: FormControl<string | null>,
};
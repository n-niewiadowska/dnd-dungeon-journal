import { FormControl, FormGroup } from "@angular/forms";
import { DndClass } from "../enums/dnd-class.enum";
import { Race } from "../enums/race.enum";
import { SkillForm } from "./skill-form";

export interface DndCharacterForm {
    id: FormControl<number | null>,
    firstName: FormControl<string | null>,
    lastName: FormControl<string | null>,
    dndClass: FormControl<DndClass | null>,
    race: FormControl<Race | null>,
    age: FormControl<number | null>,
    canPerformMagic: FormControl<boolean | null>,
    skill: FormGroup<SkillForm>,
};
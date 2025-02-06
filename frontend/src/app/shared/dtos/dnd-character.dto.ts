import { DndClass } from "../enums/dnd-class.enum";
import { Race } from "../enums/race.enum";
import { SkillDto } from "./skill.dto";

export interface DndCharacterDto {
    readonly id: number | null,
    readonly firstName: string,
    readonly lastName: string,
    readonly dndClass: DndClass,
    readonly race: Race,
    readonly age: number,
    readonly canPerformMagic: boolean,
    readonly skill: SkillDto | null,
};
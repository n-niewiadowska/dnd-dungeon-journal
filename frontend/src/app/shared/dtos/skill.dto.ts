import { Ability } from "../enums/ability.enum";

export interface SkillDto {
    readonly id: number | null,
    readonly name: string,
    readonly relatedAbility: Ability,
    readonly description: string,
    readonly level: number,
    readonly effect: string,
};
import { GameStatus } from "../enums/game-status.enum";

export interface CampaignDto {
    readonly id: number | null,
    readonly title: string,
    readonly description: string,
    readonly beginningDate: Date,
    readonly status: GameStatus,
};
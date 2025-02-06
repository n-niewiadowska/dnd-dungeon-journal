import { FormControl } from "@angular/forms";
import { GameStatus } from "../enums/game-status.enum";

export interface CampaignForm {
    id: FormControl<number | null>,
    title: FormControl<string | null>,
    description: FormControl<string | null>,
    beginningDate: FormControl<Date | null>,
    status: FormControl<GameStatus | null>,
};
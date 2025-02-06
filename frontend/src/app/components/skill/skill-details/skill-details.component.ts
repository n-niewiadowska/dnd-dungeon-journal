import { Component, OnInit } from '@angular/core';
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { SkillService } from '../../../services/skill/skill.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { SkillDto } from '../../../shared/dtos/skill.dto';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-skill-details',
  standalone: true,
  imports: [ TranslatePipe, TranslateDirective, ButtonModule ],
  templateUrl: './skill-details.component.html',
  styleUrl: './skill-details.component.scss'
})
export class SkillDetailsComponent implements OnInit {
  public skill: SkillDto | null = null;

  public constructor(
    private skillService: SkillService,
    private router: Router,
    private route: ActivatedRoute, 
    private location: Location,
  ) { }

  public ngOnInit(): void {
    const campaignId = this.route.snapshot.paramMap.get('id');
    if (campaignId) {
      this.skillService.getById(Number(campaignId)).pipe(
        catchError((_) => {
          this.router.navigate(['/not-found']);
          
          return of(null);
        })
      ).subscribe((result) => {
        this.skill = result;
      });
    }
  }

  public goBack(): void {
    this.location.back();
  }
}

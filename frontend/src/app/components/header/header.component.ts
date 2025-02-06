import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { SelectButtonModule } from 'primeng/selectbutton';
import { ToggleSwitchModule } from 'primeng/toggleswitch';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [ TranslatePipe, ButtonModule, SelectButtonModule, ToggleSwitchModule ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  public isDarkMode: boolean = false;
  public langOptions = [
    { label: "EN", value: "en" },
    { label: "PL", value: "pl" }
  ];

  public constructor(private router: Router, private translateService: TranslateService) {}
  
  public goToHome(): void {
    this.router.navigate(['/']);
  }

  public goToCampaigns(): void {
    this.router.navigate(['/campaign']);
  }

  public goToCharacters(): void {
    this.router.navigate(['/character']);
  }

  public goToSkills(): void {
    this.router.navigate(['/skill']);
  }

  public toggleTheme(): void {
    this.isDarkMode = !this.isDarkMode;

    document.body.setAttribute(
      'data-theme',
      this.isDarkMode ? 'dark' : 'light'
    );
  }

  public changeLanguage(lang: string): void {
    this.translateService.use(lang);
  }
}

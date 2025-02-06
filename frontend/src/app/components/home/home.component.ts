import { Component } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { GalleriaModule } from 'primeng/galleria';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ TranslatePipe, GalleriaModule ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  public images: { previewImageSrc: string, alt: string }[] = [ 
    { 
      previewImageSrc: 'https://gamesguru.pl/wp-content/uploads/2024/08/dragon-age-the-veilguard-z-data-premiery-do-thedas-wrocimy-w-pazdzierniku-1024x576.jpg', 
      alt: 'Dragon Age: Veilguard',
    }, 
    { 
      previewImageSrc: 'https://ocdn.eu/pulscms-transforms/1/01Bk9kpTURBXy8zYmY2NWY3YWNjZjhkN2U4YTMwOGNlZGMwMmExOTVmOS5qcGeSlQMAAM0G380D3ZMFzQcwzQOY3gACoTAGoTEB', 
      alt: 'Baldur\'s Gate 3',
    }, 
    { 
      previewImageSrc: 'https://livecards.net/pl/elden-ring-shadow-of-the-erdtree-deluxe-edition-pc-eu-74019.jpg', 
      alt: 'Elden Ring',
    }, 
  ]; 
}

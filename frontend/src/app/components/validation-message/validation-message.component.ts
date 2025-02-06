import { Component, Input } from '@angular/core';
import { FormControl } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-validation-message',
  standalone: true,
  imports: [ TranslatePipe ],
  templateUrl: './validation-message.component.html',
  styleUrl: './validation-message.component.scss'
})
export class ValidationMessageComponent {
  @Input({ required: true }) public control!: FormControl;
}

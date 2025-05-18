import { Component } from '@angular/core';
import { NavbarComponent } from "./components/navbar/navbar.component";
import { FooterComponent } from './components/footer/footer.component';
import { TypesComponent } from './sections/types/types.component';
import { StepsComponent } from './sections/steps/steps.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ NavbarComponent, TypesComponent, StepsComponent, FooterComponent ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})

export class AppComponent {
  title = 'VanGo';
}
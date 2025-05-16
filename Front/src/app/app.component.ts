import { Component } from '@angular/core';
import { NavbarComponent } from "./components/navbar/navbar.component";
import { FooterComponent } from './components/footer/footer.component';
import { TypesComponent } from './sections/types/types.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ NavbarComponent, TypesComponent, FooterComponent ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})

export class AppComponent {
  title = 'VanGo';
}
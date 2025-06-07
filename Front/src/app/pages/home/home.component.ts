import { Component } from '@angular/core';
import { NavbarComponent } from "../../components/navbar/navbar.component";
import { FooterComponent } from '../../components/footer/footer.component';
import { TypesComponent } from '../../sections/types/types.component';
import { StepsComponent } from '../../sections/steps/steps.component';
import { ApplicationComponent } from '../../sections/application/application.component';
import { HeroComponent } from '../../sections/hero/hero.component';
import { ReviewComponent } from '../../sections/review/review.component';
import { VideoComponent } from '../../sections/video/video.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    HeroComponent,
    TypesComponent,
    StepsComponent,
    ReviewComponent,
    ApplicationComponent,
    FooterComponent,
    VideoComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  title = 'VanGo';
}

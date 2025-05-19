import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CatalogComponent } from '../../pages/catalog/catalog.component';
import { SelectusComponent } from '../../pages/selectus/selectus.component';
import { RoutesComponent } from '../../pages/routes/routes.component';
import { ContactComponent } from '../../pages/contact/contact.component';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterLink, CatalogComponent, SelectusComponent, RoutesComponent, ContactComponent],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
export class FooterComponent {

}

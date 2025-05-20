import { Routes } from '@angular/router';
import { CatalogComponent } from './pages/catalog/catalog.component';
import { SelectusComponent } from './pages/selectus/selectus.component';
import { RoutesComponent } from './pages/routes/routes.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { ContactComponent } from './pages/contact/contact.component';
import { LocationComponent } from './pages/location/location.component';

export const routes: Routes = [

    { path: '',              component: HomeComponent },
    { path: 'catalogo',      component: CatalogComponent },
    { path: 'eligenos',      component: SelectusComponent },
    { path: 'rutas',         component: RoutesComponent },
    { path: 'contacto',      component: ContactComponent },
    { path: 'ubicacion',     component: LocationComponent },
    { path: 'registro',      component: RegisterComponent },
    { path: 'login',         component: LoginComponent },

];

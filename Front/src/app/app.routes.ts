import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { CatalogComponent } from './pages/catalog/catalog.component';
import { SelectusComponent } from './pages/selectus/selectus.component';
import { RoutesComponent } from './pages/routes/routes.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { ContactComponent } from './pages/contact/contact.component';

export const routes: Routes = [

    { path: '',             component: HomeComponent },
    { path: 'catalogo',     component: CatalogComponent },
    { path: 'eligenos',     component: SelectusComponent },
    { path: 'rutas',        component: RoutesComponent },
    { path: 'contacto',     component: ContactComponent },
    { path: 'registro',     component: RegisterComponent },
    { path: 'login',        component: LoginComponent },

];

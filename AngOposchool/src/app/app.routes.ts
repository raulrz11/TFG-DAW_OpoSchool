import { Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import {PaymentComponent} from "./components/payment/payment.component";
import {RenewSubscripcionComponent} from "./components/renew-subscripcion/renew-subscripcion.component";
import {PerfilComponent} from "./components/perfil/perfil.component";
import {AlumnosComponent} from "./components/alumnos/alumnos.component";
import {GruposComponent} from "./components/grupos/grupos.component";
import {SubscripcionesComponent} from "./components/subscripciones/subscripciones.component";
import {ProfesoresComponent} from "./components/profesores/profesores.component";
import {UsuariosComponent} from "./components/usuarios/usuarios.component";
import {TareasComponent} from "./components/tareas/tareas.component";
import {TareaEntregaComponent} from "./components/tarea-entrega/tarea-entrega.component";
import {EntregasComponent} from "./components/entregas/entregas.component";
import {authGuard} from "./guards/auth.guard";
import {noAuthGuard} from "./guards/no-auth.guard";

export const routes: Routes = [
  {
    path: '', component: LoginComponent, canActivate: [noAuthGuard], data: { animation: 'login' }
  },
  {
    path: 'register', component: RegisterComponent, canActivate: [noAuthGuard], data: { animation: 'register' }
  },
  {
    path: 'payment', component: PaymentComponent, canActivate: [noAuthGuard], data: { animation: 'payment' }
  },
  {
    path: 'renew-subscripcion', canActivate: [authGuard], component: RenewSubscripcionComponent
  },
  {
    path: 'perfil', canActivate: [authGuard], component: PerfilComponent
  },
  //USUARIOS
  {
    path: 'tareas', canActivate: [authGuard], component: TareasComponent
  },
  {
    path: 'entregas', canActivate: [authGuard], component: EntregasComponent
  },
  {
    path: 'tarea-entrega/:id', canActivate: [authGuard], component: TareaEntregaComponent
  },
  //ADMINISTRACION
  {
    path: 'alumnos', canActivate: [authGuard], component: AlumnosComponent
  },
  {
    path: 'profesores', canActivate: [authGuard], component: ProfesoresComponent
  },
  {
    path: 'grupos', canActivate: [authGuard], component: GruposComponent
  },
  {
    path: 'subscripciones', canActivate: [authGuard], component: SubscripcionesComponent
  },
  {
    path: 'usuarios', canActivate: [authGuard], component: UsuariosComponent
  },
];

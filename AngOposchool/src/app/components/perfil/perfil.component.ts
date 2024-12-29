import {Component, OnInit} from '@angular/core';
import {HeaderComponent} from "../header/header.component";
import {LoginService} from "../../services/auth/login.service";
import {AlumnosService} from "../../services/alumnos/alumnos.service";
import {ProfesoresService} from "../../services/profesores/profesores.service";
import {UsuariosService} from "../../services/usuarios/usuarios.service";
import {GruposService} from "../../services/grupos/grupos.service";
import {SubscripcionesService} from "../../services/subscripciones/subscripciones.service";
import {jwtDecode} from "jwt-decode";
import {DatePipe, NgIf} from "@angular/common";
import {ButtonDirective} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {ConfirmationService, MessageService, PrimeTemplate} from "primeng/api";
import {Ripple} from "primeng/ripple";
import {FormsModule} from "@angular/forms";
import {ToastModule} from "primeng/toast";
import {ConfirmDialogModule} from "primeng/confirmdialog";

interface DecodedToken {
  sub: string
  idAlumno: number
  idProfesor: number
  email: string
  nombre: string
  rol: string
  exp: number
}

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [
    HeaderComponent,
    NgIf,
    DatePipe,
    ButtonDirective,
    DialogModule,
    InputTextModule,
    PrimeTemplate,
    Ripple,
    FormsModule,
    ToastModule,
    ConfirmDialogModule
  ],
  providers: [
    ConfirmationService,
    MessageService
  ],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit{
  usuario: any
  alumno: any
  profesor: any
  profesorAlumno: any
  grupo: any

  emailDialog: boolean = false
  passwordDialog: boolean = false
  formDataEmail = {
    "email" : ''
  }

  formDataPassword = {
    "password" : ''
  }

  errors: any = {}

  constructor(
      private loginService: LoginService,
      private usuariosService: UsuariosService,
      private alumnosService: AlumnosService,
      private profesoresService: ProfesoresService,
      private gruposService: GruposService,
      private subscripcionesService: SubscripcionesService,
      private messageService: MessageService,
      private confirmationService: ConfirmationService
  ) {
  }

  ngOnInit(): void {
    this.usuariosService.getCurrentUsuario().subscribe((data) => {
      this.usuario = data
      console.log(this.usuario)
    })

    const token = this.loginService.getToken()
    if (!token || token.split('.').length !== 3) {
      return
    }
    const decoded: DecodedToken = jwtDecode(token)
    const idProfesor = decoded.idProfesor
    const idAlumno = decoded.idAlumno

    if (idAlumno){
      this.alumnosService.getAlumno(idAlumno).subscribe((data) => {
        this.alumno = data
        this.gruposService.getGrupoByAlumno(this.alumno.id).subscribe((data) => {
          this.grupo = data
          this.profesoresService.getProfesorByNombre(this.grupo.profesor).subscribe((data) => {
            this.profesorAlumno = data
          })
        })
      })
    }

    if (idProfesor){
      this.profesoresService.getProfesor(idProfesor).subscribe((data) => {
        this.profesor = data
      })
    }
  }

  editEmail(){
    this.emailDialog = true
  }

  editPassword(){
    this.passwordDialog = true
  }

  updateEmail(){
    this.usuariosService.updateEmail(this.formDataEmail, this.usuario.id).subscribe({
      next: (updatedUsuario) => {
        this.emailDialog = false
        this.ngOnInit()
        this.messageService.add({severity: 'success', summary: 'Actualizado', detail: 'Email actualizado'})
      },
      error: (err) => {
        this.errors = err.error
      }
    })
  }

  updatePassword(){
    this.usuariosService.updatePassword(this.formDataPassword, this.usuario.id).subscribe({
      next: (updatedUsuario) => {
        this.passwordDialog = false
        this.ngOnInit()
        this.messageService.add({severity: 'success', summary: 'Atualizada', detail: 'Contrasena actualizada'})
      },
      error: (err) => {
        this.errors = err.error
      }
    })
  }

  cancelSubscription(){
    this.confirmationService.confirm({
      message: 'Estas seguro que quieres cancelar tu subscripcion?',
      header: 'Mensaje de confirmacion',
      acceptLabel: 'Si',
      rejectLabel: 'No',
      acceptIcon: 'none',
      rejectIcon: 'none',
      acceptButtonStyleClass: 'p-button-success m-1',
      rejectButtonStyleClass: 'p-button-danger m-1',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        const token = this.loginService.getToken()
        if (!token || token.split('.').length !== 3) {
          return
        }
        const decoded: DecodedToken = jwtDecode(token)
        const idAlumno = decoded.idAlumno

        this.subscripcionesService.getSubscripcionesByAlumno(idAlumno).subscribe((data: any) => {
          const subscripciones = data.content
          const subscripcionActiva = subscripciones.find((subscripcion: any) => subscripcion.estado == 'ACTIVA')
          this.subscripcionesService.cancelSubscripcion(subscripcionActiva.id).subscribe((data: any) => {
            this.loginService.logout()
          })
        })
      }
    })
  }
}

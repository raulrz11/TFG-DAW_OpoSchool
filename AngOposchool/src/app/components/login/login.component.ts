import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {LoginService} from "../../services/auth/login.service";
import {NgIf} from "@angular/common";
import {Router} from "@angular/router";
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent{
  loginData = {
    "username" : '',
    "password" : ''
  }
  errors: any = {}

  constructor(public loginService:LoginService, private router: Router) {}

  formSubmit() {
    this.loginService.login(this.loginData).subscribe({
      next: (response: any) => {
        this.loginService.setToken(response.token)

        const rol = this.loginService.getUserRol()
        if (rol == 'ADMIN'){
          this.router.navigate(['/perfil'])
        }else if (rol == 'PROFESOR'){
          this.router.navigate(['/perfil'])
        }else if (rol == 'ALUMNO'){
          let data = this.loginService.getUserData()
          if (data){
            this.loginService.getSubscripcionesByAlumno(data.idAlumno).subscribe({
              next: (response: any) => {
                const subscripciones = response.content
                if (Array.isArray(subscripciones)){
                  const subscripcionActiva = subscripciones.find((sub) => sub.estado == 'ACTIVA')
                  const subscripcionPendiente = subscripciones.find((sub) => sub.estado == 'PENDIENTE')

                  if (subscripcionActiva){
                    this.router.navigate(['/perfil'])
                  }else if (subscripcionPendiente){
                    Swal.fire({
                      title: "<strong>Subscripcion inactiva</strong>",
                      icon: "info",
                      html: `La subscripcion se activara el <strong>${subscripcionPendiente.fechaInicio}</strong> y podras acceder al aula virtual a partir de ese dia`,
                      confirmButtonText: 'Entendido',
                      customClass: {
                        confirmButton: 'custom-confirm-button'
                      }
                    }).then(() => {
                      window.location.reload()
                    })
                    this.loginService.logout()
                  }else {
                    this.router.navigate(['/renew-subscripcion'])
                  }
                }
              },
              error: (err) => {
                this.errors = err.error
                console.error('Error al gestionar el estado de la subscripcion:', err.error);
              },
            })
          }
        }else {
          this.errors = 'Rol no definido'
          this.router.navigate([''])
        }
      },
      error: (err) => {
        this.errors = err.error
        console.error('Error en el login:', err.error);
      },
    });
  }

  logout(){
    this.loginService.logout()
  }

  goRegister(){
    this.router.navigate(["/register"])
  }

  //Cambio entre login y registro
  isActive = false

  toggleRegister(): void {
    this.isActive = true
  }

  toggleLogin(): void {
    this.isActive = false
  }
}

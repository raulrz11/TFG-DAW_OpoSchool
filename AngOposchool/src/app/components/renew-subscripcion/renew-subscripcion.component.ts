import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {SubscripcionesService} from "../../services/subscripciones/subscripciones.service";
import {LoginService} from "../../services/auth/login.service";
import Swal from "sweetalert2";
import {jwtDecode} from "jwt-decode";

interface DecodedToken {
  sub: string
  idAlumno: number
  email: string
  nombre: string
  rol: string
  exp: number
}

@Component({
  selector: 'app-renew-subscripcion',
  standalone: true,
    imports: [
        ReactiveFormsModule
    ],
  templateUrl: './renew-subscripcion.component.html',
  styleUrl: './renew-subscripcion.component.css'
})
export class RenewSubscripcionComponent {
  paymentForm: FormGroup
  isVisible: boolean = false

  constructor(private fb: FormBuilder, private router: Router, private loginService: LoginService, private subscripcionesService: SubscripcionesService) {
    this.paymentForm = this.fb.group({
      numTarjeta: ['', Validators.required],
      fechaExpiracion: ['', Validators.required],
      codigoSeguridad: ['', Validators.required],
    })
  }

  formSubmit(){
    if (this.paymentForm.valid){
      const paymentData = {
        tarjeta: {
          numTarjeta: this.paymentForm.value.numTarjeta.replace(/\s+/g, ''),
          fechaExpiracion: this.paymentForm.value.fechaExpiracion,
          codigoSeguridad: this.paymentForm.value.codigoSeguridad,
        }
      }

      const token = this.loginService.getToken()
      if (!token || token.split('.').length !== 3) {
        return
      }
      const decoded: DecodedToken = jwtDecode(token)
      const idAlumno = decoded.idAlumno

      this.subscripcionesService.renewSubscripcion(paymentData, idAlumno).subscribe({
        next: (response: any) => {
          Swal.fire({
            title: "<strong>Subscripcion renovada!</strong>",
            icon: "success",
            html: `La subscripcion se activara el <strong>${response.fechaInicio}</strong> y podras acceder al aula virtual a partir de ese dia`,
            confirmButtonText: 'Entendido',
            customClass: {
              confirmButton: 'custom-confirm-button'
            }
          }).then(() => {
            this.loginService.logout()
          })
        },
        error: (err) => {
          console.error('Error al reactivar la subscripcion:', err);
        },
      })
    }
  }

  onNumTarjetaInput(event: Event){
    const inputElement = event.target as HTMLInputElement;

    const value = inputElement.value.replace(/\s+/g, '');
    const formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;

    inputElement.value = formattedValue;

    this.paymentForm.get('numTarjeta')?.setValue(formattedValue, { emitEvent: false });
  }

  toggleForm(){
    this.isVisible = true
  }
}

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {RegisterService} from "../../services/auth/register.service";
import {NgIf} from "@angular/common";
import {animate, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent implements OnInit{
  paymentForm: FormGroup
  registerData: any
  errors: any = {}

  constructor(private fb: FormBuilder, private router: Router, private registerService: RegisterService) {
    this.paymentForm = this.fb.group({
      numTarjeta: [''],
      fechaExpiracion: [''],
      codigoSeguridad: [''],
    })

    const navigation = this.router.getCurrentNavigation()
    this.registerData = navigation?.extras.state?.['registerData']
    console.log('Datos del registro:', this.registerData)
    if (!this.registerData){
      this.router.navigate(["/register"])
    }

  }

  public formSubmit(){
    const paymentData = {
      tarjeta: {
        numTarjeta: this.paymentForm.value.numTarjeta.replace(/\s+/g, ''),
        fechaExpiracion: this.paymentForm.value.fechaExpiracion,
        codigoSeguridad: this.paymentForm.value.codigoSeguridad,
      }
    }

    const completeData = {...this.registerData, ...paymentData}
    console.log(completeData)
    this.registerService.register(completeData).subscribe({
      next: (response: any) => {
        const username = response[0]
        const password = response[1]
        console.log(username)
        console.log(password)
        this.router.navigate([""])
      },
      error: (err) => {
        this.errors = err.error
        console.error('Error al registrarse:', err);
      },
    })
  }

  onNumTarjetaInput(event: Event){
    const inputElement = event.target as HTMLInputElement;

    const value = inputElement.value.replace(/\s+/g, '');
    const formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;

    inputElement.value = formattedValue;

    this.paymentForm.get('numTarjeta')?.setValue(formattedValue, { emitEvent: false });
  }

  ngOnInit(): void {

  }
}

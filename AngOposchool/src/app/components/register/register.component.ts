import { Component } from '@angular/core';
import {animate, style, transition, trigger} from "@angular/animations";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {RegisterService} from "../../services/auth/register.service";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerForm: FormGroup
  errors: any = {}

  constructor(private fb: FormBuilder, private router: Router, private registerService: RegisterService) {
    this.registerForm = this.fb.group({
      nombre: [''],
      apellidos: [''],
      email: [''],
      telefono: [''],
      dni: [''],
    })
  }

  public formSubmit(){
    this.registerService.validateRegisterForm(this.registerForm.value).subscribe({
      next: (response: any) => {
        this.router.navigate(["/payment"], {
          state: {registerData: this.registerForm.value}
        })
      },
      error: (err) => {
        this.errors = err.error
        console.error('Error al registrarse:', err);
      }
    })
  }
}

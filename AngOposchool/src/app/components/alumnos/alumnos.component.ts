import {Component, OnInit, ViewChild} from '@angular/core';
import {HeaderComponent} from "../header/header.component";
import {Table, TableModule} from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToolbarModule } from 'primeng/toolbar';
import { DropdownModule } from 'primeng/dropdown';
import {AlumnosService} from "../../services/alumnos/alumnos.service";
import {Ripple} from "primeng/ripple";
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";
import {ToastModule} from "primeng/toast";
import {ConfirmationService, MessageService} from "primeng/api";

@Component({
  selector: 'app-alumnos',
  standalone: true,
  imports: [
    HeaderComponent,
    TableModule,
    ButtonModule,
    DialogModule,
    InputTextModule,
    InputNumberModule,
    ConfirmDialogModule,
    ToolbarModule,
    DropdownModule,
    Ripple,
    FormsModule,
    NgIf,
    ToastModule,
  ],
  providers: [
    ConfirmationService,
    MessageService
  ],
  templateUrl: './alumnos.component.html',
  styleUrl: './alumnos.component.css'
})
export class AlumnosComponent implements OnInit{
  @ViewChild('dt') dt: Table | undefined;
  alumnos: any[] = []
  alumno: any = {
    id: null,
    nombre: '',
    apellidos: '',
    email: '',
    telefono: '',
    dni: '',
    tarjeta: null
  }
  alumnoDialog: boolean = false
  submitted: boolean = false
  isCreating: boolean = false

  errors: any = {}

  constructor(private alumnosService: AlumnosService, private messageService: MessageService, private confirmationService: ConfirmationService) {
  }

  ngOnInit(): void {
    this.alumnosService.getAlumnos().subscribe((data: any) => {
      this.alumnos = data.content
    })
  }

  newAlumno(){
    this.alumno = {
      nombre: '',
      apellidos: '',
      email: '',
      telefono: '',
      dni: '',
      tarjeta: { numTarjeta: '', fechaExpiracion: '', codigoSeguridad: '' }
    }
    this.alumnoDialog = true
    this.submitted = false
    this.isCreating = true
  }

  editAlumno(alumno: any){
    this.alumno = {...alumno}
    delete this.alumno.tarjeta
    this.alumnoDialog = true
    this.isCreating = false
  }

  saveAlumno(){
    this.submitted = true
    if (this.alumno.nombre && this.alumno.apellidos && this.alumno.telefono && this.alumno.dni){
      if (this.alumno.id){
        this.alumnosService.updateAlumno(this.alumno).subscribe({
          next: (data: any) => {
            this.alumnoDialog = false
            this.ngOnInit()
            this.messageService.add({severity: 'success', summary: 'Actualizado', detail: 'Alumno actualizado'})
          },
          error: (err) => {
            this.errors = err.error
          }
        })
      }else {
        this.alumnosService.createAlumno(this.alumno).subscribe({
          next: (data: any) => {
            this.alumnoDialog = false
            this.ngOnInit()
            this.messageService.add({severity: 'success', summary: 'Creado', detail: 'Alumno creado'})
          },
          error: (err) => {
            this.errors = err.error
          }
        })
      }
    }
  }

  deleteAlumno(alumno: any){
    this.confirmationService.confirm({
      message: 'Estas seguro que quieres eliminar a ' + alumno.nombre + '?',
      header: 'Mensaje de confirmacion',
      acceptLabel: 'Si',
      rejectLabel: 'No',
      acceptIcon: 'none',
      rejectIcon: 'none',
      acceptButtonStyleClass: 'p-button-success m-1',
      rejectButtonStyleClass: 'p-button-danger m-1',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.alumnosService.deleteAlumno(alumno.id).subscribe(() => {
          this.ngOnInit()
        })
        this.messageService.add({severity: 'success', summary: 'Eliminado', detail: 'Alumno eliminado'})
      }
    })
  }

  hideDialog(){
    this.alumnoDialog = false
    this.submitted = false
  }

  onFilter(event: Event): void {
    const inputValue = (event.target as HTMLInputElement).value;
    this.dt?.filterGlobal(inputValue, 'contains');
  }
}

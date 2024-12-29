import {Component, OnInit, ViewChild} from '@angular/core';
import {HeaderComponent} from "../header/header.component";
import {Table, TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {InputNumberModule} from "primeng/inputnumber";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToolbarModule} from "primeng/toolbar";
import {DropdownModule} from "primeng/dropdown";
import {Ripple} from "primeng/ripple";
import {FormsModule} from "@angular/forms";
import {DatePipe, NgIf} from "@angular/common";
import {ToastModule} from "primeng/toast";
import {ConfirmationService, MessageService} from "primeng/api";
import {GruposService} from "../../services/grupos/grupos.service";
import {CalendarModule} from "primeng/calendar";
import {ProfesoresService} from "../../services/profesores/profesores.service";
import {AlumnosService} from "../../services/alumnos/alumnos.service";

@Component({
  selector: 'app-grupos',
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
    CalendarModule,
    DatePipe,
  ],
  providers: [
    ConfirmationService,
    MessageService,
    DatePipe
  ],
  templateUrl: './grupos.component.html',
  styleUrl: './grupos.component.css'
})
export class GruposComponent implements OnInit{
  @ViewChild('dt') dt: Table | undefined;
  @ViewChild('dtAlumnos') dtAlumnos: Table | undefined;
  grupos: any[] = []
  grupo: any
  grupoDialog: boolean = false
  grupoNombre: any
  alumnosDialog: boolean = false
  alumnosToAddDialog: boolean = false
  submitted: boolean = false

  alumnos: any[] = []
  alumnosToAdd: any[] = []
  profesores: any[] = []
  duraciones: any[] = [
    { label: '30 minutos', value: 'PT30M' },
    { label: '1 hora', value: 'PT1H' },
    { label: '1 hora 30 minutos', value: 'PT1H30M' },
    { label: '2 horas', value: 'PT2H' },
    { label: '2 horas 30 minutos', value: 'PT2H30M' },
    { label: '3 horas', value: 'PT3H' },
  ]

  errors: any = {}

  constructor(private gruposService: GruposService, private alumnosService: AlumnosService, private profesoresService: ProfesoresService, private messageService: MessageService, private confirmationService: ConfirmationService, private datePipe: DatePipe) {
  }

  ngOnInit(): void {
    this.gruposService.getGrupos().subscribe((data: any) => {
      this.grupos = data.content
    })
    this.profesoresService.getProfesores().subscribe((data: any) => {
      this.profesores = data.content
    })
  }

  newGrupo(){
    this.grupo = {}
    this.grupoDialog = true
    this.submitted = false
  }

  editGrupo(grupo: any){
    this.grupo = {...grupo}
    this.grupo.fechaClase = new Date(grupo.fechaClase)
    this.grupo.profesor = this.profesores.find((profesor) => profesor.nombre === grupo.profesor)
    this.grupoDialog = true
  }

  saveGrupo(){
    this.submitted = true
    if (this.grupo.nombre && this.grupo.fechaClase && this.grupo.horaClase && this.grupo.duracionClase){
      this.grupo.horaClase = this.datePipe.transform(this.grupo.horaClase, 'HH:mm:ss')
      this.grupo.duracionClase = this.grupo.duracionClase.value
      if (this.grupo.profesor){
        this.grupo.profesor = { id: this.grupo.profesor.id }
      }
      if (this.grupo.id){
        this.gruposService.updateGrupo(this.grupo).subscribe({
          next: (data: any) => {
            this.grupoDialog = false
            this.ngOnInit()
            this.messageService.add({severity: 'success', summary: 'Actualizado', detail: 'Grupo actualizado'})
          },
          error: (err) => {
            this.errors = err.error
          }
        })
      }else {
        this.gruposService.createGrupo(this.grupo).subscribe({
          next: (data: any) => {
            this.grupoDialog = false
            this.ngOnInit()
            this.messageService.add({severity: 'success', summary: 'Creado', detail: 'Grupo creado'})
          },
          error: (err) => {
            this.errors = err.error
          }
        })
      }
    }
  }

  deleteGrupo(grupo: any){
    this.confirmationService.confirm({
      message: 'Estas seguro que quieres eliminar el grupo ' + grupo.nombre + '?',
      header: 'Mensaje de confirmacion',
      acceptLabel: 'Si',
      rejectLabel: 'No',
      acceptIcon: 'none',
      rejectIcon: 'none',
      acceptButtonStyleClass: 'p-button-success m-1',
      rejectButtonStyleClass: 'p-button-danger m-1',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.gruposService.deleteGrupo(grupo.id).subscribe(() => {
          this.ngOnInit()
        })
        this.messageService.add({severity: 'success', summary: 'Eliminado', detail: 'Grupo eliminado'})
      }
    })
  }

  showAlumnos(grupo: any){
    this.gruposService.getAlumnosInGrupo(grupo.id).subscribe((data: any) => {
      this.alumnos = data
    })
    this.grupoNombre = grupo.nombre
    console.log(this.alumnos)
    this.alumnosDialog = true
  }

  showAlumnosToAdd(grupo: any){
    this.gruposService.getAlumnosInNoGrupo(grupo.id).subscribe((data: any) => {
      this.alumnosToAdd = data
    })
    console.log(this.alumnosToAdd)
    this.grupo = grupo
    this.alumnosToAddDialog = true
  }

  addAlumno(alumno: any, grupo: any){
    this.alumnosService.getAlumno(alumno.id).subscribe((alumno: any) => {
      console.log(alumno)
      if (alumno.grupo != 'SIN GRUPO'){
        this.confirmationService.confirm({
          message: 'Este alumno ya pertenece a un grupo, seguro que quieres cambiarlo de grupo?',
          header: 'Mensaje de confirmacion',
          acceptLabel: 'Si',
          rejectLabel: 'No',
          acceptIcon: 'none',
          rejectIcon: 'none',
          acceptButtonStyleClass: 'p-button-success m-1',
          rejectButtonStyleClass: 'p-button-danger m-1',
          icon: 'pi pi-exclamation-triangle',
          accept: () => {
            this.gruposService.addAlumno(alumno, grupo).subscribe(() => {
              this.gruposService.getAlumnosInNoGrupo(grupo.id).subscribe((data: any) => {
                this.alumnosToAdd = data
              })
              this.ngOnInit()
            })
            this.messageService.add({severity: 'success', summary: 'Cambiado de grupo', detail: 'Alumno cambiado de grupo'})
          }
        })
      }else{
        this.gruposService.addAlumno(alumno, grupo).subscribe(() => {
          this.gruposService.getAlumnosInNoGrupo(grupo.id).subscribe((data: any) => {
            this.alumnosToAdd = data
          })
          this.ngOnInit()
        })
        this.messageService.add({severity: 'success', summary: 'Agregado al grupo', detail: 'Alumno agregado al grupo'})
      }
    })
  }

  removeAlumno(alumno: any){
    this.confirmationService.confirm({
      message: 'Estas seguro que quieres eliminar a este alumno del grupo?',
      header: 'Mensaje de confirmacion',
      acceptLabel: 'Si',
      rejectLabel: 'No',
      acceptIcon: 'none',
      rejectIcon: 'none',
      acceptButtonStyleClass: 'p-button-success m-1',
      rejectButtonStyleClass: 'p-button-danger m-1',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.gruposService.removeAlumno(alumno).subscribe(() => {
          this.gruposService.getAlumnosInGrupo(this.grupo.id).subscribe((data: any) => {
            this.alumnos = data
          })
          this.ngOnInit()
        })
        this.messageService.add({severity: 'success', summary: 'Eliminado', detail: 'Alumno eliminado del grupo'})
      }
    })
  }

  onFilter(event: Event): void {
    const inputValue = (event.target as HTMLInputElement).value;
    this.dt?.filterGlobal(inputValue, 'contains');
  }

  onFilterAlumnos(event: Event): void {
    const inputValue = (event.target as HTMLInputElement).value;
    this.dtAlumnos?.filterGlobal(inputValue, 'contains');
  }
}

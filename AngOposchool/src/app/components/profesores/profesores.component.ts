import {Component, OnInit, ViewChild} from '@angular/core';
import {Table, TableModule} from "primeng/table";
import {ProfesoresService} from "../../services/profesores/profesores.service";
import {ConfirmationService, MessageService} from "primeng/api";
import {HeaderComponent} from "../header/header.component";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {InputNumberModule} from "primeng/inputnumber";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToolbarModule} from "primeng/toolbar";
import {DropdownModule} from "primeng/dropdown";
import {Ripple} from "primeng/ripple";
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'app-profesores',
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
  templateUrl: './profesores.component.html',
  styleUrl: './profesores.component.css'
})
export class ProfesoresComponent implements OnInit{
  @ViewChild('dt') dt: Table | undefined;
  profesores: any[] = []
  profesor: any = {}
  profesorDialog: boolean = false
  submitted: boolean = false

  errors: any = {}

  constructor(private profesoresService: ProfesoresService, private messageService: MessageService, private confirmationService: ConfirmationService) {
  }

  ngOnInit(): void {
    this.profesoresService.getProfesores().subscribe((data: any) => {
      this.profesores = data.content
    })
  }

  newProfesor(){
    this.profesor = {}
    this.profesorDialog = true
    this.submitted = false
  }

  editProfesor(profesor: any){
    this.profesor = {...profesor}
    this.profesorDialog = true
  }

  saveProfesor(){
    this.submitted = true
    if (this.profesor.nombre && this.profesor.apellidos && this.profesor.telefono && this.profesor.dni){
      if (this.profesor.id){
        this.profesoresService.updateProfesor(this.profesor).subscribe({
          next: (data: any) => {
            this.profesorDialog = false
            this.ngOnInit()
            this.messageService.add({severity: 'success', summary: 'Actualizado', detail: 'Profesor actualizado'})
          },
          error: (err) => {
            this.errors = err.error
          }
        })
      }else {
        this.profesoresService.createProfesor(this.profesor).subscribe({
          next: (data: any) => {
            this.profesorDialog = false
            this.ngOnInit()
            this.messageService.add({severity: 'success', summary: 'Creado', detail: 'Profesor creado'})
          },
          error: (err) => {
            this.errors = err.error
          }
        })
      }
    }
  }

  deleteProfesor(profesor: any){
    this.confirmationService.confirm({
      message: 'Estas seguro que quieres eliminar a ' + profesor.nombre + '?',
      header: 'Mensaje de confirmacion',
      acceptLabel: 'Si',
      rejectLabel: 'No',
      acceptIcon: 'none',
      rejectIcon: 'none',
      acceptButtonStyleClass: 'p-button-success m-1',
      rejectButtonStyleClass: 'p-button-danger m-1',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.profesoresService.deleteProfesor(profesor.id).subscribe(() => {
          this.ngOnInit()
        })
        this.messageService.add({severity: 'success', summary: 'Eliminado', detail: 'Profesor eliminado'})
      }
    })
  }

  onFilter(event: Event): void {
    const inputValue = (event.target as HTMLInputElement).value;
    this.dt?.filterGlobal(inputValue, 'contains');
  }

}

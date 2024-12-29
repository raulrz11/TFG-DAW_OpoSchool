import {Component, OnInit, ViewChild} from '@angular/core';
import {HeaderComponent} from "../header/header.component";
import {Table, TableModule} from "primeng/table";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToolbarModule} from "primeng/toolbar";
import {DropdownModule} from "primeng/dropdown";
import {Ripple} from "primeng/ripple";
import {DatePipe, NgIf} from "@angular/common";
import {ToastModule} from "primeng/toast";
import {SubscripcionesService} from "../../services/subscripciones/subscripciones.service";

@Component({
  selector: 'app-subscripciones',
  standalone: true,
  imports: [
    HeaderComponent,
    TableModule,
    DialogModule,
    InputTextModule,
    ConfirmDialogModule,
    ToolbarModule,
    DropdownModule,
    Ripple,
    NgIf,
    ToastModule,
    DatePipe,
  ],
  templateUrl: './subscripciones.component.html',
  styleUrl: './subscripciones.component.css'
})
export class SubscripcionesComponent implements OnInit{
  @ViewChild('dt') dt: Table | undefined;
  subscripciones: any[] = []
  filteredSubscripciones: any[] = []
  selectedSubscripcion: any

  errors: any = {}

  constructor(private subscripcionesService: SubscripcionesService) {
  }

  ngOnInit(): void {
    this.subscripcionesService.getSubscripciones().subscribe((data: any) => {
      this.subscripciones = data.content
      this.filteredSubscripciones = [...this.subscripciones]
    })
  }

  mostrarSubsAlumno(){
    if (this.selectedSubscripcion){
      const alumnoId = this.selectedSubscripcion.alumnoId
      console.log(this.selectedSubscripcion)
      console.log(alumnoId)

      this.subscripcionesService.getSubscripcionesByAlumno(alumnoId).subscribe({
        next: (subscripcionesAlumno: any) => {
          this.filteredSubscripciones = subscripcionesAlumno.content
        },
        error: (err) => {
          this.errors = err.error
        }
      })
    }
  }

  borrarFiltro(){
    this.filteredSubscripciones = [...this.subscripciones]
    this.selectedSubscripcion = null
  }

  onFilter(event: Event): void {
    const inputValue = (event.target as HTMLInputElement).value;
    this.dt?.filterGlobal(inputValue, 'contains');
  }
}

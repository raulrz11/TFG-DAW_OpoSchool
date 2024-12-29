import {Component, OnDestroy, OnInit} from '@angular/core';
import {DatePipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {HeaderComponent} from "../header/header.component";
import {FormsModule} from "@angular/forms";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {Ripple} from "primeng/ripple";
import {jwtDecode} from "jwt-decode";
import {TareasService} from "../../services/tareas/tareas.service";
import {LoginService} from "../../services/auth/login.service";
import {GruposService} from "../../services/grupos/grupos.service";
import {ConfirmationService, MessageService} from "primeng/api";
import {ActivatedRoute, Router} from "@angular/router";
import {EntregasService} from "../../services/entregas/entregas.service";

interface DecodedToken {
  sub: string
  idAlumno: number
  email: string
  nombre: string
  rol: string
  exp: number
}

@Component({
  selector: 'app-tarea-entrega',
  standalone: true,
  imports: [
    HeaderComponent,
    NgIf,
    FormsModule,
    ConfirmDialogModule,
    ToastModule,
    DialogModule,
    InputTextModule,
    Ripple,
    NgForOf,
    NgClass,
    DatePipe,
  ],
  providers: [
    ConfirmationService,
    MessageService
  ],
  templateUrl: './tarea-entrega.component.html',
  styleUrl: './tarea-entrega.component.css'
})
export class TareaEntregaComponent implements OnInit, OnDestroy{
  tareas: any[] = []
  tarea: any
  tareaId: any
  selectedTarea: any
  entrega: any
  archivoSeleccionado!: File | null
  entregaDialog: boolean = false
  intervalId: any
  routeSub: any

  errors: any = {}

  constructor(
    private tareasService: TareasService,
    private entregasService: EntregasService,
    private loginService: LoginService,
    private gruposService: GruposService,
    private messageService: MessageService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.routeSub = this.activatedRoute.paramMap.subscribe((params) => {
      this.tareaId = Number(params.get('id'));

      this.tareasService.getTarea(this.tareaId).subscribe((data) => {
        this.tarea = data
      })

      const token = this.loginService.getToken()
      if (!token || token.split('.').length !== 3) {
        return
      }
      const decoded: DecodedToken = jwtDecode(token)
      const idAlumno = decoded.idAlumno

      this.entregasService.getEntregasByTareaAndAlumno(this.tareaId, idAlumno).subscribe({
        next: (data) => {
          this.entrega = data
        },
        error: (err) => {
          this.entrega = null
        }
      })
    })

    this.intervalId = setInterval(() => {
      this.tareas = [...this.tareas]
    }, 1000)

    const token = this.loginService.getToken()
    if (!token || token.split('.').length !== 3) {
      return
    }
    const decoded: DecodedToken = jwtDecode(token)
    const idAlumno = decoded.idAlumno

    if (!idAlumno) {
      this.tareasService.getTareas().subscribe((data: any) => {
        this.tareas = data.content
        this.selectedTarea = this.tareas.find(t => t.id === this.tareaId)
      })
    }else {
      this.gruposService.getGrupoByAlumno(idAlumno).subscribe((data: any) => {
        const grupoId = data.id
        this.tareasService.getTareasByGrupo(grupoId).subscribe((data: any) => {
          this.tareas = data.content
          this.selectedTarea = this.tareas.find(t => t.id === this.tareaId)
        })
      })
    }
  }

  ngOnDestroy(): void {
    if (this.routeSub) {
      this.routeSub.unsubscribe()
    }
    clearInterval(this.intervalId)
  }

  editEntrega(entrega: any){
    this.entrega = {...entrega}

    if (this.entrega.archivoUrl && !this.archivoSeleccionado) {
      fetch(this.entrega.archivoUrl)
        .then((response) => response.blob())
        .then((blob) => {
          const filename = this.entrega.archivoUrl.split('/').pop() || 'archivo';
          this.archivoSeleccionado = new File([blob], filename, { type: blob.type });
        });
    }

    this.entregaDialog = true
  }

  onFileSelected(event: any) {
      this.archivoSeleccionado = event.target.files[0];
  }

  updateEntrega(){
      const formData = new FormData()
      if (this.archivoSeleccionado && this.archivoSeleccionado instanceof File) {
          formData.append('archivo', this.archivoSeleccionado);
      }
      this.entregasService.updateEntrega(this.entrega.id, formData).subscribe({
          next: (data: any) => {
              this.entregaDialog = false
              this.ngOnInit()
              this.messageService.add({severity: 'success', summary: 'Actualizada', detail: 'Entrega actualizada'})
          },
          error: (err) => {
              this.errors = err.error
          }
      })
  }

  selectTarea(tarea: any){
    this.selectedTarea = tarea
    this.router.navigate(['/tarea-entrega', tarea.id])
  }

  getTiempoRestante(fechaEntrega: string): { texto: string, finalizada: boolean } {
    const ahora = new Date();
    const fechaFin = new Date(fechaEntrega);
    const tiempoRestante = fechaFin.getTime() - ahora.getTime();

    if (tiempoRestante <= 0) {
      return { texto: 'Finalizada', finalizada: true };
    }

    const dias = Math.floor(tiempoRestante / (1000 * 60 * 60 * 24));
    const horas = Math.floor((tiempoRestante % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutos = Math.floor((tiempoRestante % (1000 * 60 * 60)) / (1000 * 60));
    const segundos = Math.floor((tiempoRestante % (1000 * 60)) / 1000);

    return {
      texto: `${dias}d ${horas}h ${minutos}m ${segundos}s`,
      finalizada: false,
    };
  }

}

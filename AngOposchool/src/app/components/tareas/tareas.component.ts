import {Component, OnInit} from '@angular/core';
import {TareasService} from "../../services/tareas/tareas.service";
import {ConfirmationService, MessageService} from "primeng/api";
import {LoginService} from "../../services/auth/login.service";
import {jwtDecode} from "jwt-decode";
import {GruposService} from "../../services/grupos/grupos.service";
import {HeaderComponent} from "../header/header.component";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {Ripple} from "primeng/ripple";
import {CalendarModule} from "primeng/calendar";
import {DomSanitizer, SafeResourceUrl} from "@angular/platform-browser";
import {Router, RouterLink} from "@angular/router";
import {EntregasService} from "../../services/entregas/entregas.service";

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
  selector: 'app-tareas',
  standalone: true,
  imports: [
    HeaderComponent,
    NgForOf,
    NgIf,
    FormsModule,
    ConfirmDialogModule,
    ToastModule,
    DialogModule,
    InputTextModule,
    Ripple,
    CalendarModule,
    RouterLink
  ],
  providers: [
    ConfirmationService,
    MessageService
  ],
  templateUrl: './tareas.component.html',
  styleUrl: './tareas.component.css'
})
export class TareasComponent implements OnInit{
  tareas: any[] = []
  tareasFiltradas: any[] = []
  tareasFiltradasPayload: any[] = []
  tarea: any
  tareaDialog: boolean = false
  entrega: any
  entregaDialog: boolean = false
  tareaModal: boolean = false
  archivoVistaPrevia: SafeResourceUrl | null = null
  submitted: boolean = false
  busqueda: any
  rol: any
  archivoSeleccionado!: File | null
  grupos: any[] = []
  grupoSeleccionado: any
  grupoFormSeleccionado: any
  isFiltroGrupo: boolean = false
  isCreating: boolean = false

  errors: any = {}

  constructor(
    private tareasService: TareasService,
    private entregasService: EntregasService,
    private loginService: LoginService,
    private gruposService: GruposService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private sanitizer: DomSanitizer,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.rol = this.loginService.getUserRol()
    const token = this.loginService.getToken()
    if (!token || token.split('.').length !== 3) {
      return
    }
    const decoded: DecodedToken = jwtDecode(token)
    const idProfesor = decoded.idProfesor
    const idAlumno = decoded.idAlumno

    if (idProfesor){
      this.gruposService.getGruposByProfesor(idProfesor).subscribe((data: any) => {
        this.grupos = data.content
      })
    }

    if (!idAlumno) {
      this.tareasService.getTareas().subscribe((data: any) => {
        this.tareas = data.content
        this.tareasFiltradas = data.content
      })
    }else {
      this.gruposService.getGrupoByAlumno(idAlumno).subscribe((data: any) => {
        const grupoId = data.id
        this.tareasService.getTareasByGrupo(grupoId).subscribe((data: any) => {
          this.tareas = data.content
          this.tareasFiltradas = data.content
        })
      })
    }
  }

  newTarea(){
    this.tarea = {}
    this.tareaDialog = true
    this.submitted = false
    this.isCreating = true
  }

  editTarea(tarea: any){
    this.tarea = {...tarea}
    this.tarea.fechaEntrega = new Date(tarea.fechaEntrega);

    if (this.tarea.archivoUrl && !this.archivoSeleccionado) {
      fetch(this.tarea.archivoUrl)
        .then((response) => response.blob())
        .then((blob) => {
          const filename = this.tarea.archivoUrl.split('/').pop() || 'archivo';
          this.archivoSeleccionado = new File([blob], filename, { type: blob.type });
        });
    }

    this.tareaDialog = true
    this.isCreating = false
  }

  newEntrega(tarea: any){
    this.tarea = {...tarea}
    this.entrega = {}
    this.entregaDialog = true
    this.submitted = false
  }

  onFileSelected(event: any) {
    this.archivoSeleccionado = event.target.files[0];
  }

  saveTarea(){
    this.submitted = true
    if (this.tarea.titulo && this.tarea.descripcion && this.tarea.fechaEntrega){
      const formData = new FormData()
      formData.append('dto', new Blob([JSON.stringify(this.tarea)], { type: 'application/json' }))
      if (this.archivoSeleccionado && this.archivoSeleccionado instanceof File) {
        formData.append('archivo', this.archivoSeleccionado);
      }
      if (this.tarea.id){
        this.tareasService.updateTarea(this.tarea.id, formData).subscribe({
          next: (data: any) => {
            this.tareaDialog = false
            this.ngOnInit()
            this.messageService.add({severity: 'success', summary: 'Actualizada', detail: 'Tarea actualizada'})
          },
          error: (err) => {
            this.errors = err.error
          }
        })
      }else {
        this.tareasService.saveTarea(this.grupoFormSeleccionado, formData).subscribe({
          next: (data: any) => {
            this.tareaDialog = false
            this.ngOnInit()
            this.messageService.add({severity: 'success', summary: 'Creada', detail: 'Tarea creada'})
          },
          error: (err) => {
            this.errors = err.error
          }
        })
      }
    }
  }

  saveEntrega(){
    const token = this.loginService.getToken()
    if (!token || token.split('.').length !== 3) {
        return
    }
    const decoded: DecodedToken = jwtDecode(token)
    const idAlumno = decoded.idAlumno
    const formData = new FormData()
    if (this.archivoSeleccionado && this.archivoSeleccionado instanceof File) {
        formData.append('archivo', this.archivoSeleccionado);
    }
    this.entregasService.saveEntrega(this.tarea.id, idAlumno, formData).subscribe({
        next: (data: any) => {
            this.entregaDialog = false
            this.ngOnInit()
            this.messageService.add({severity: 'success', summary: 'Entregada', detail: 'Entrega realizada'})
        },
        error: (err) => {
            this.errors = err.error
        }
    })
  }

  deleteTarea(tarea: any){
    this.confirmationService.confirm({
      message: 'Estas seguro que quieres eliminar esta tarea?',
      header: 'Mensaje de confirmacion',
      acceptLabel: 'Si',
      rejectLabel: 'No',
      acceptIcon: 'none',
      rejectIcon: 'none',
      acceptButtonStyleClass: 'p-button-success m-1',
      rejectButtonStyleClass: 'p-button-danger m-1',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.tareasService.deleteTarea(tarea.id).subscribe(() => {
          this.ngOnInit()
        })
        this.messageService.add({severity: 'success', summary: 'Eliminado', detail: 'Tarea eliminada'})
      }
    })
  }

  filtrarTareasPorGrupo(): void {
    if (this.grupoSeleccionado != 0) {
      this.tareasService.getTareasByGrupo(this.grupoSeleccionado).subscribe((data: any) => {
        this.tareasFiltradas = data.content;
        this.tareasFiltradasPayload = data.content
        this.isFiltroGrupo = true
      });
    } else {
      this.tareasFiltradas = this.tareas;
      this.isFiltroGrupo = false
    }
  }

  buscarTareas(): void {
    if (this.isFiltroGrupo){
      if (this.busqueda) {
        this.tareasFiltradas = this.tareasFiltradasPayload.filter((tarea) =>
          tarea.titulo.toLowerCase().includes(this.busqueda.toLowerCase())
        )
      } else {
        this.tareasFiltradas = this.tareasFiltradasPayload;
      }
    }else {
      if (this.busqueda) {
        this.tareasFiltradas = this.tareas.filter((tarea) =>
          tarea.titulo.toLowerCase().includes(this.busqueda.toLowerCase())
        )
      } else {
        this.tareasFiltradas = this.tareas;
      }
    }
  }

  //MODAL
  vistaPrevia(tarea: any){
    if (tarea.archivoUrl){
      this.tareaModal = true
      this.archivoVistaPrevia = this.sanitizer.bypassSecurityTrustResourceUrl(tarea.archivoUrl)
    }
  }

  cerrarModal(){
    this.tareaModal = false
    this.archivoVistaPrevia = null
  }

  goTarea(tarea: any){
    this.router.navigate(['/tarea-entrega', tarea.id])
  }
}

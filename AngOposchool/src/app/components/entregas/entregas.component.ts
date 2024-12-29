import {Component, OnInit, ViewChild} from '@angular/core';
import {ButtonDirective, ButtonModule} from "primeng/button";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {DialogModule} from "primeng/dialog";
import {HeaderComponent} from "../header/header.component";
import {InputTextModule} from "primeng/inputtext";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {ConfirmationService, MessageService, PrimeTemplate} from "primeng/api";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Ripple} from "primeng/ripple";
import {Table, TableModule} from "primeng/table";
import {ToastModule} from "primeng/toast";
import {ToolbarModule} from "primeng/toolbar";
import {InputNumberModule} from "primeng/inputnumber";
import {DropdownModule} from "primeng/dropdown";
import {EntregasService} from "../../services/entregas/entregas.service";
import {GruposService} from "../../services/grupos/grupos.service";
import {TareasService} from "../../services/tareas/tareas.service";
import {jwtDecode} from "jwt-decode";
import {LoginService} from "../../services/auth/login.service";

interface DecodedToken {
    sub: string
    idProfesor: number
    email: string
    nombre: string
    rol: string
    exp: number
}

@Component({
  selector: 'app-entregas',
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
        DatePipe,
        NgForOf,
    ],
    providers: [
        ConfirmationService,
        MessageService
    ],
  templateUrl: './entregas.component.html',
  styleUrl: './entregas.component.css'
})
export class EntregasComponent implements OnInit{
    @ViewChild('dt') dt: Table | undefined;
    entregas: any[] = []
    entrega: any
    entregaDialog: boolean = false
    grupos: any[] = []
    grupoSeleccionado: any
    tareas: any[] = []
    tareaSeleccionada: any


    errors: any = {}

    constructor(
        private entregasService: EntregasService,
        private gruposService: GruposService,
        private tareasService: TareasService,
        private loginService: LoginService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService) {
    }

    ngOnInit(): void {
        const token = this.loginService.getToken()
        if (!token || token.split('.').length !== 3) {
            return
        }
        const decoded: DecodedToken = jwtDecode(token)
        const idProfesor = decoded.idProfesor
        this.gruposService.getGruposByProfesor(idProfesor).subscribe((data: any) => {
            this.grupos = data.content
        })
    }

    editEntrega(entrega: any){
        this.entrega = {...entrega}
        this.entregaDialog = true
    }

    correctEntrega(){
        if (this.entrega.calificacion){
            this.entregasService.correctEntrega(this.entrega).subscribe({
                next: (data: any) => {
                    this.entregaDialog = false
                    this.loadEntregas()
                    this.messageService.add({severity: 'success', summary: 'Corregida', detail: 'Entrega corregida'})
                },
                error: (err) => {
                    this.errors = err.error
                }
            })
        }
    }

    deleteEntrega(entrega: any){
        this.confirmationService.confirm({
            message: 'Estas seguro que quieres eliminar esta entrega?',
            header: 'Mensaje de confirmacion',
            acceptLabel: 'Si',
            rejectLabel: 'No',
            acceptIcon: 'none',
            rejectIcon: 'none',
            acceptButtonStyleClass: 'p-button-success m-1',
            rejectButtonStyleClass: 'p-button-danger m-1',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.entregasService.deleteEntrega(entrega.id).subscribe(() => {
                    this.loadEntregas()
                })
                this.messageService.add({severity: 'success', summary: 'Eliminada', detail: 'Entrega eliminada'})
            }
        })
    }

    selectTarea(){
        this.tareasService.getTareasByGrupo(this.grupoSeleccionado).subscribe((data: any) => {
            this.tareas = data.content
        })
    }

    loadEntregas(){
        this.entregasService.getEntregas(this.tareaSeleccionada).subscribe((data: any) => {
            this.entregas = data.content
        })
    }

    onFilter(event: Event): void {
        const inputValue = (event.target as HTMLInputElement).value;
        this.dt?.filterGlobal(inputValue, 'contains');
    }

}

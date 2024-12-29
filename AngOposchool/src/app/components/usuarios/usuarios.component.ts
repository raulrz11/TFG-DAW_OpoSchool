import {Component, OnInit, ViewChild} from '@angular/core';
import {HeaderComponent} from "../header/header.component";
import {Table, TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {ToolbarModule} from "primeng/toolbar";
import {Ripple} from "primeng/ripple";
import {NgIf} from "@angular/common";
import {UsuariosService} from "../../services/usuarios/usuarios.service";
import {InputTextModule} from "primeng/inputtext";
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [
    HeaderComponent,
    TableModule,
    ButtonModule,
    ToolbarModule,
    Ripple,
    NgIf,
    InputTextModule,
    ToastModule,
  ],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.css'
})
export class UsuariosComponent implements OnInit{
  @ViewChild('dt') dt: Table | undefined;
  usuarios: any[] = []

  constructor(private usuariosService: UsuariosService) {
  }

  ngOnInit(): void {
    this.usuariosService.getUsuarios().subscribe((data: any) => {
      this.usuarios = data.content
    })
  }

  onFilter(event: Event): void {
    const inputValue = (event.target as HTMLInputElement).value;
    this.dt?.filterGlobal(inputValue, 'contains');
  }
}

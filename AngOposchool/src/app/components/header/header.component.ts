import {Component, OnInit} from '@angular/core';
import {LoginService} from "../../services/auth/login.service";
import {NgIf} from "@angular/common";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {jwtDecode} from "jwt-decode";

interface DecodedToken {
  sub: string
  idAlumno: number
  email: string
  nombre: string
  rol: string
  exp: number
}

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    NgIf,
    RouterLink,
    RouterLinkActive,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{
  nombreUsuario: any
  rol: any

  constructor(private loginService: LoginService) {
  }

  public logOut(){
    this.loginService.logout()
  }

  public getNombrePerfil(){
    let token = this.loginService.getToken()
    if (!token || token.split('.').length !== 3) {
      this.nombreUsuario = 'invitado'.charAt(0).toUpperCase()
      return null
    }
    const decoded: DecodedToken = jwtDecode(token)
    this.nombreUsuario = decoded.nombre.charAt(0).toUpperCase()
    return this.nombreUsuario
  }

  ngOnInit(): void {
    this.rol = this.loginService.getUserRol()
    this.getNombrePerfil()
  }


}

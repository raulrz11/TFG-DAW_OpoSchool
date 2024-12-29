import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {jwtDecode} from "jwt-decode";
import {catchError, Observable, throwError} from "rxjs";

interface DecodedToken {
  sub: string
  idAlumno: number
  email: string
  nombre: string
  rol: string
  exp: number
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private baseUrl = 'http://localhost:3000/auth'

  constructor(private http: HttpClient, private router: Router) { }

  public login(loginData:any){
    return this.http.post(`${this.baseUrl}/signin`, loginData).pipe(
        catchError((error: HttpErrorResponse) => {
          return throwError(() => error)
        })
    )
  }

  public setToken(token:any){
    localStorage.setItem('token', token)
  }

  public getToken(){
    return localStorage.getItem('token')
  }

  public isLoggedIn(){
    let token = localStorage.getItem('token')
    if(token == undefined || token == '' || token == null){
      return false
    }
    try {
      const decoded: DecodedToken = jwtDecode(token)
      return decoded.exp > Date.now() / 1000
    } catch (error) {
      console.error('Error al decodificar el token o token expirado:', error)
      return false
    }
  }

  public logout(){
    localStorage.removeItem('token')
    this.router.navigate([''])
  }

  public getUserRol(){
    const token = this.getToken()
    if (!token || token.split('.').length !== 3) {
      return null;
    }
    const decoded: DecodedToken = jwtDecode(token)
    return decoded.rol
  }

  public getUserData(){
    const token = this.getToken()
    if (!token || token.split('.').length !== 3) {
      return null;
    }
    const decoded: DecodedToken = jwtDecode(token)
    return {
      username: decoded.sub,
      idAlumno: decoded.idAlumno,
      email: decoded.email,
      nombre: decoded.nombre,
      rol: decoded.rol
    }
  }

  public getSubscripcionesByAlumno(idAlumno: number): Observable<any>{
    return this.http.get<any>(`http://localhost:3000/subscripciones/alumno/${idAlumno}`)
  }
}

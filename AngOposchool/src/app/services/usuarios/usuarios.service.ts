import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {
  private baseUrl = 'http://localhost:3000/usuarios/'

  constructor(private http: HttpClient) { }

  public getUsuarios(){
    return this.http.get(this.baseUrl)
  }

  public getCurrentUsuario(){
    return this.http.get(`${this.baseUrl}current-user`)
  }

  public updateEmail(formData: any, id: number){
    return this.http.put(`${this.baseUrl}email/${id}`, formData).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }

  public updatePassword(formData: any, id: number){
    return this.http.put(`${this.baseUrl}password/${id}`, formData).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }
}

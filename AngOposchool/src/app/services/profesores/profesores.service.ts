import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProfesoresService {
  private baseUrl = 'http://localhost:3000/profesores/'

  constructor(private http: HttpClient) { }

  public getProfesores(){
    return this.http.get(this.baseUrl)
  }

  public getProfesor(id: number){
    return this.http.get(`${this.baseUrl}${id}`)
  }

  public getProfesorByNombre(nombre: any){
    return this.http.get(`${this.baseUrl}profesor/${nombre}`)
  }

  public createProfesor(profesor: any){
    return this.http.post(this.baseUrl, profesor).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }

  public updateProfesor(profesor: any){
    return this.http.put(`${this.baseUrl}${profesor.id}`, profesor).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }

  public deleteProfesor(id: number){
    return this.http.delete(`${this.baseUrl}${id}`)
  }
}

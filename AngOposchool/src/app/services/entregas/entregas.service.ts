import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EntregasService {
  private baseUrl = 'http://localhost:3000/entregas/'

  constructor(private http: HttpClient) { }

  public getEntregasByTareaAndAlumno(tareaId: number, alumnoId: number){
    return this.http.get(`${this.baseUrl}tarea/${tareaId}/alumno/${alumnoId}`)
  }

  public getEntregas(tareaId: number){
      return this.http.get(`${this.baseUrl}tarea/${tareaId}`)
  }

  public saveEntrega(tareaId: any, alumnoId: any, formData: FormData){
    return this.http.post(`${this.baseUrl}${tareaId}/${alumnoId}`, formData).pipe(
        catchError((error: HttpErrorResponse) => {
          return throwError(() => error)
        })
    )
  }

  public updateEntrega(id: any, formData: FormData){
    return this.http.patch(`${this.baseUrl}${id}`, formData).pipe(
        catchError((error: HttpErrorResponse) => {
          return throwError(() => error)
        })
    )
  }

  public correctEntrega(entrega: any){
      return this.http.put(`${this.baseUrl}${entrega.id}`, entrega).pipe(
          catchError((error: HttpErrorResponse) => {
              return throwError(() => error)
          })
      )
  }

  public deleteEntrega(id: number){
    return this.http.delete(`${this.baseUrl}${id}`)
  }
}

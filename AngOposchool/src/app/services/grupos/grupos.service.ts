import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class GruposService {
  private baseUrl = 'http://localhost:3000/grupos/'

  constructor(private http: HttpClient) { }

  public getGrupos(){
    return this.http.get(this.baseUrl)
  }

  public getAlumnosInGrupo(id: number ){
    return this.http.get(`${this.baseUrl}alumnosInGrupo/${id}`)
  }

  public getAlumnosInNoGrupo(id: number ){
    return this.http.get(`${this.baseUrl}alumnosInNoGrupo/${id}`)
  }

  public getGruposByProfesor(id: number){
    return this.http.get(`${this.baseUrl}profesor/${id}`)
  }

  public getGrupoByAlumno(id: number){
    return this.http.get(`${this.baseUrl}alumno/${id}`)
  }

  public createGrupo(grupo: any){
    return this.http.post(this.baseUrl, grupo).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }

  public updateGrupo(grupo: any){
    return this.http.put(`${this.baseUrl}${grupo.id}`, grupo).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }

  public deleteGrupo(id: number){
    return this.http.delete(`${this.baseUrl}${id}`)
  }

  public addAlumno(alumno: any, grupo: any){
    return this.http.put(`${this.baseUrl}addAlumno/${alumno.id}/${grupo.id}`, {}).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }

  public removeAlumno(alumno: any){
    return this.http.put(`${this.baseUrl}removeAlumno/${alumno.id}`, {}).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }
}

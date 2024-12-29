import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AlumnosService {
  private baseUrl = 'http://localhost:3000/alumnos/'

  constructor(private http: HttpClient) { }

  public getAlumnos(){
    return this.http.get(this.baseUrl)
  }

  public getAlumno(id: number){
    return this.http.get(`${this.baseUrl}${id}`)
  }

  public createAlumno(alumno: any){
    return this.http.post(this.baseUrl, alumno).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }

  public updateAlumno(alumno: any){
    return this.http.put(`${this.baseUrl}${alumno.id}`, alumno).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }

  public deleteAlumno(id: number){
    return this.http.delete(`${this.baseUrl}${id}`)
  }
}

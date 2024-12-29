import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TareasService {
  private baseUrl = 'http://localhost:3000/tareas/'

  constructor(private http: HttpClient) { }

  public getTareas(){
    return this.http.get(this.baseUrl)
  }

  public getTareasByGrupo(id: number){
    return this.http.get(`${this.baseUrl}grupo/${id}`)
  }

  public getTarea(id: number){
    return this.http.get(`${this.baseUrl}${id}`)
  }

  saveTarea(grupoId: number, formData: FormData){
    return this.http.post(`${this.baseUrl}${grupoId}`, formData).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }

  updateTarea(tareaId: number, formData: FormData){
    return this.http.put(`${this.baseUrl}${tareaId}`, formData).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    )
  }

  deleteTarea(id: number){
    return this.http.delete(`${this.baseUrl}${id}`)
  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SubscripcionesService {
  private baseUrl = 'http://localhost:3000/subscripciones/'

  constructor(private http: HttpClient) { }

  public getSubscripciones(){
    return this.http.get(this.baseUrl)
  }

  public getSubscripcionesByAlumno(idAlumno: number){
    return this.http.get(`${this.baseUrl}alumno/${idAlumno}`)
  }

  public renewSubscripcion(paymentData: any, idAlumno: number){
    return this.http.post(`${this.baseUrl}${idAlumno}`, paymentData)
  }

  public cancelSubscripcion(id: number){
    return this.http.delete(`${this.baseUrl}${id}`)
  }
}

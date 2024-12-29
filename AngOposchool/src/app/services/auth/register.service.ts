import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {catchError, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private baseUrl = 'http://localhost:3000/auth'

  constructor(private http: HttpClient, private router: Router) { }

  public register(registerData: any){
    return this.http.post(`${this.baseUrl}/signup`, registerData).pipe(
        catchError((error: HttpErrorResponse) => {
          return throwError(() => error)
        })
    )
  }

    public validateRegisterForm(registerData: any){
        return this.http.post(`${this.baseUrl}/validateForm`, registerData).pipe(
            catchError((error: HttpErrorResponse) => {
                return throwError(() => error)
            })
        )
    }
}

import {HttpInterceptorFn} from "@angular/common/http";
import {inject} from "@angular/core";
import {LoginService} from "../services/auth/login.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const loginService = inject(LoginService)

  const token = loginService.getToken()

  const authReq = token
  ? req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      },
    })
  : req

  return next(authReq)
}

import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {LoginService} from "../services/auth/login.service";

export const noAuthGuard: CanActivateFn = (route, state) => {
  const loginService = inject(LoginService);
  const router = inject(Router);

  if (!loginService.isLoggedIn()) {
    return true;
  } else {
    router.navigate(['']);
    return false;
  }
};

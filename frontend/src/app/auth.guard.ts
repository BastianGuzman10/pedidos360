import { Injectable, inject } from '@angular/core';
import { CanActivate } from '@angular/router';
import { MsalGuard } from '@azure/msal-angular';
import { environment } from '../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  private msalGuard = inject(MsalGuard);

  canActivate(...args: Parameters<CanActivate['canActivate']>) {
    if (!environment.authEnabled) {
      return true;
    }

    return this.msalGuard.canActivate(...args);
  }
}
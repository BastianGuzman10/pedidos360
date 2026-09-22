import { Injectable, Injector, inject } from '@angular/core';
import { CanActivate } from '@angular/router';
import { MsalGuard } from '@azure/msal-angular';
import { environment } from '../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  private injector = inject(Injector);

  canActivate(...args: Parameters<CanActivate['canActivate']>) {
    if (!environment.authEnabled) {
      return true;
    }

    return this.injector.get(MsalGuard).canActivate(...args);
  }
}

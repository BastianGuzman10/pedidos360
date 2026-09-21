import { Injectable } from '@angular/core';
import { MsalInterceptor } from '@azure/msal-angular';

@Injectable()
export class AuthInterceptor extends MsalInterceptor {}

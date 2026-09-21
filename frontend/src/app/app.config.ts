import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { MsalModule, MsalService, MSAL_GUARD_CONFIG, MSAL_INSTANCE, MSAL_INTERCEPTOR_CONFIG, MsalGuard, MsalInterceptorConfiguration } from '@azure/msal-angular';
import { InteractionType, PublicClientApplication } from '@azure/msal-browser';
import { routes } from './app.routes';
import { environment } from '../environments/environment';
import { AuthInterceptor } from './auth.interceptor';
function msalInstanceFactory() { return new PublicClientApplication({ auth: { clientId: environment.msal.clientId, authority: environment.msal.authority, knownAuthorities: environment.msal.knownAuthorities, redirectUri: environment.msal.redirectUri, postLogoutRedirectUri: environment.msal.redirectUri }, cache: { cacheLocation: 'sessionStorage' } }); }
function guardConfigFactory() { return { interactionType: InteractionType.Redirect, authRequest: { scopes: [environment.msal.apiScope] } }; }
function interceptorConfigFactory(): MsalInterceptorConfiguration { return { interactionType: InteractionType.Redirect, protectedResourceMap: environment.authEnabled ? new Map([[`${environment.apiUrl}/*`, [environment.msal.apiScope]]]) : new Map() }; }
export const appConfig: ApplicationConfig = { providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes), provideHttpClient(withInterceptorsFromDi()), importProvidersFrom(MsalModule), { provide: MSAL_INSTANCE, useFactory: msalInstanceFactory }, { provide: MSAL_GUARD_CONFIG, useFactory: guardConfigFactory }, { provide: MSAL_INTERCEPTOR_CONFIG, useFactory: interceptorConfigFactory }, { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }, MsalGuard, MsalService] };

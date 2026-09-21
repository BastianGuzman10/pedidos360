import { Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { AppComponent } from './app.component';
export const routes: Routes = [{ path: '', component: AppComponent, canActivate: [AuthGuard] }, { path: '**', redirectTo: '' }];

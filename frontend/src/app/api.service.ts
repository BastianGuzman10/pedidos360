import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Pedido, Producto } from './models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);
  private baseUrl = environment.apiUrl;
  productos(): Observable<Producto[]> { return this.http.get<Producto[]>(`${this.baseUrl}/productos`); }
  crearProducto(producto: Producto): Observable<Producto> { return this.http.post<Producto>(`${this.baseUrl}/productos`, producto); }
  actualizarProducto(id: number, producto: Producto): Observable<Producto> { return this.http.put<Producto>(`${this.baseUrl}/productos/${id}`, producto); }
  eliminarProducto(id: number): Observable<void> { return this.http.delete<void>(`${this.baseUrl}/productos/${id}`); }
  pedidos(): Observable<Pedido[]> { return this.http.get<Pedido[]>(`${this.baseUrl}/pedidos`); }
  pedido(id: number): Observable<Pedido> { return this.http.get<Pedido>(`${this.baseUrl}/pedidos/${id}`); }
  crearPedido(pedido: Pedido): Observable<Pedido> { return this.http.post<Pedido>(`${this.baseUrl}/pedidos`, pedido); }
  eliminarPedido(id: number): Observable<void> { return this.http.delete<void>(`${this.baseUrl}/pedidos/${id}`); }
}

import { Component, Injector, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MsalService } from '@azure/msal-angular';
import { AccountInfo } from '@azure/msal-browser';
import { ApiService } from './api.service';
import { Pedido, Producto } from './models';
import { environment } from '../environments/environment';

@Component({ selector: 'app-root', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './app.component.html', styleUrl: './app.component.scss' })
export class AppComponent implements OnInit {
  private api = inject(ApiService);
  private injector = inject(Injector);
  readonly authEnabled = environment.authEnabled;
  account?: AccountInfo;
  tab = 'inicio'; loading = false; error = ''; success = '';
  productos: Producto[] = []; pedidos: Pedido[] = []; selectedPedido?: Pedido; editingId?: number;
  productForm: Producto = { nombre: '', descripcion: '', precio: 0, stock: 0 };
  orderForm: Pedido = { estado: 'PENDIENTE', total: 0, correoCliente: '', detalles: [] };

  ngOnInit() {
    if (!this.authEnabled) { this.loadProducts(); this.loadOrders(); return; }
    const auth = this.injector.get(MsalService);
    auth.instance.initialize()
      .then(() => auth.instance.handleRedirectPromise())
      .then(result => {
        this.account = result?.account ?? auth.instance.getActiveAccount() ?? auth.instance.getAllAccounts()[0];
        if (this.account) { auth.instance.setActiveAccount(this.account); this.loadProducts(); this.loadOrders(); }
      })
      .catch(() => this.error = 'No fue posible inicializar Microsoft Entra ID. Revisa la configuracion.');
  }

  login() { if (this.authEnabled) this.injector.get(MsalService).loginRedirect({ scopes: ['openid', 'profile', environment.msal.apiScope] }); }
  logout() { if (this.authEnabled) this.injector.get(MsalService).logoutRedirect(); }
  setTab(tab: string) { this.tab = tab; this.error = ''; this.success = ''; }
  loadProducts() { this.api.productos().subscribe({ next: data => this.productos = data, error: e => this.showError(e) }); }
  loadOrders() { this.api.pedidos().subscribe({ next: data => this.pedidos = data, error: e => this.showError(e) }); }
  saveProduct() { this.loading = true; const request = this.editingId ? this.api.actualizarProducto(this.editingId, this.productForm) : this.api.crearProducto(this.productForm); request.subscribe({ next: () => { this.success = 'Producto guardado correctamente.'; this.resetProduct(); this.loadProducts(); }, error: e => this.showError(e), complete: () => this.loading = false }); }
  editProduct(producto: Producto) { this.editingId = producto.id; this.productForm = { ...producto }; this.setTab('productos'); }
  deleteProduct(id: number) { if (!confirm('¿Eliminar este producto?')) return; this.api.eliminarProducto(id).subscribe({ next: () => this.loadProducts(), error: e => this.showError(e) }); }
  resetProduct() { this.editingId = undefined; this.productForm = { nombre: '', descripcion: '', precio: 0, stock: 0 }; }
  addDetail() { this.orderForm.detalles.push({ productoId: 0, cantidad: 1, precioUnitario: 0 }); }
  createOrder() { if (!this.orderForm.detalles.length) { this.error = 'Agrega al menos un producto al pedido.'; return; } this.orderForm.total = this.orderForm.detalles.reduce((sum, d) => sum + d.cantidad * d.precioUnitario, 0); this.api.crearPedido(this.orderForm).subscribe({ next: () => { this.success = 'Pedido creado correctamente.'; this.orderForm = { estado: 'PENDIENTE', total: 0, correoCliente: '', detalles: [] }; this.loadOrders(); }, error: e => this.showError(e) }); }
  viewOrder(order: Pedido) { this.selectedPedido = order; this.setTab('detalle'); }
  private showError(error: any) { this.loading = false; this.error = error?.status === 401 ? 'Debes iniciar sesion nuevamente.' : error?.status === 403 ? 'Tu cuenta no tiene el permiso requerido.' : 'No fue posible completar la operacion.'; }
}

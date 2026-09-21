export interface Producto { id?: number; nombre: string; descripcion: string; precio: number; stock: number; }
export interface DetallePedido { id?: number; productoId: number; cantidad: number; precioUnitario: number; }
export interface Pedido { id?: number; fecha?: string; estado: string; total: number; correoCliente: string; detalles: DetallePedido[]; }

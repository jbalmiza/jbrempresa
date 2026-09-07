import { HttpErrorResponse } from '@angular/common/http';

export interface ApiError {
  fecha?: string;
  estado?: number;
  codigo?: string;
  mensaje?: string;
  ruta?: string;
  referencia?: string | null;
  campos?: Record<string, string>;
  message?: string;
  detail?: string;
}

export function mensajeErrorApi(error: unknown, fallback = 'No se pudo completar la operación.'): string {
  if (!(error instanceof HttpErrorResponse)) return fallback;
  const api = error.error as ApiError | string | null;
  if (typeof api === 'string' && api.trim()) return api;
  if (!api || typeof api !== 'object') return fallback;
  const campos = api.campos ? Object.values(api.campos).filter(Boolean) : [];
  const mensaje = api.mensaje || api.detail || api.message || fallback;
  return campos.length ? `${mensaje} ${campos.join(' ')}` : mensaje;
}

export function normalizarErrorApi(error: HttpErrorResponse): void {
  const api = error.error as ApiError | null;
  if (!api || typeof api !== 'object') return;
  const mensaje = mensajeErrorApi(error);
  // Compatibilidad temporal con pantallas anteriores mientras todas usan mensajeErrorApi.
  api.message = mensaje;
  api.detail = mensaje;
}

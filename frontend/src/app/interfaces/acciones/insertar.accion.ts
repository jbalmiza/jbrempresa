import { Accion } from "../metadata/accion.model";
import { }

export const INSERTAR: Accion = {

    id: "insertar",

    titulo: "Insertar",

    descripcion: "Insertar un nuevo registro",

    icono: "add",

    tooltip: "Insertar",

    requiereSeleccion: false,

    permiteMultiple: false,

    confirmar: false,

    orden: 20
	
	
	// Ejecuta una inserción de un registro
	async ejecutar(entidad: Entidad, datos: any) {

	       try {

	           // URL dinámica según entidad
	           const url = `/api/${entidad.tabla}`;

	           // Enviar datos al backend
	           const response = await fetch(url, {
	               method: 'POST',
	               headers: {
	                   'Content-Type': 'application/json'
	               },
	               body: JSON.stringify(datos)
	           });

	           // Si el backend falla
	           if (!response.ok) {

	               const error = await response.text();

	               return {
	                   correcta: false,
	                   mensaje: error || 'Error al insertar'
	               };

	           }

	           // Respuesta OK del backend
	           const data = await response.json();

	           return {
	               correcta: true,
	               mensaje: 'Registro insertado correctamente',
	               datos: data
	           };

	       } catch (e: any) {

	           // Error de red o inesperado
	           return {
	               correcta: false,
	               mensaje: e?.message || 'Error inesperado'
	           };

	       }

	   }

};
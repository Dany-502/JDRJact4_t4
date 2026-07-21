# Act4. API REST con Spring Boot, Spring Security y JWT : API REST de Mascotas

* **Institución:** Instituto Tecnológico de Oaxaca
* **Carrera:** Ingeniería en Sistemas Computacionales
* **Materia:** Programación Web
* **Estudiante:** Rodriguez Juarez Jose Daniel
* **No.Control:** 22161222
* **Grupo:** 7SC
* **Profesor:** Adelina Martinez Nieto

**Enlaces de prueba en vivo (VPS):**

* **Autenticación:**
  * **Registro:** POST `http://66.179.80.246:8087/api/auth/register`
  * **Login:** POST `http://66.179.80.246:8087/api/auth/login`
* **Mascotas:**
  * **Crear Mascota:** POST `http://66.179.80.246:8087/api/mascotas`
  * **Listar Mascotas:** GET `http://66.179.80.246:8087/api/mascotas`
  * **Obtener Mascota por ID:** GET `http://66.179.80.246:8087/api/mascotas/{id}`
  * **Actualizar Mascota:** PUT `http://66.179.80.246:8087/api/mascotas/{id}`
  * **Eliminar Mascota:** DELETE `http://66.179.80.246:8087/api/mascotas/{id}`

---

## Descripción del Proyecto
Este proyecto consistio en construir una API REST  con Spring Boot para la gestión de mascotas, incluyendo autenticación y autorización mediante JWT.

## Endpoints y Pruebas (Realizadas en Bruno)

### 1. Registro de usuario
Permite registrar un nuevo usuario en la plataforma.
![Registro de usuario](capturas/RegistrarUsuario.png)

### 2. Inicio de sesión (Login)
Permite a un usuario autenticarse y obtener su token JWT.
![Login](capturas/Login.png)

### 3. Crear mascota
Endpoint protegido que permite al usuario autenticado registrar una nueva mascota.
![Crear Mascota](capturas/CrearMascota.png)

### 4. Listar mascotas
Obtiene la lista paginada de mascotas pertenecientes al usuario autenticado.
![Listar Mascotas](capturas/ListarMascotas.png)

### 5. Actualizar mascota
Permite modificar los datos de una mascota existente perteneciente al usuario.
![Actualizar Mascota](capturas/ActualizarMascota.png)

### 6. Eliminar mascota
Elimina una mascota especificada por su ID.
![Mascota eliminada](capturas/MascotaEliminada.png)

### 7. Verificación post-eliminación
Comprobación de que la mascota eliminada ya no aparece en el listado.
![Eliminar mascota despues](capturas/EliminarMascotaDespues.png)

### 8. Prueba de seguridad (Sin token)
Verificación de que los endpoints protegidos rechazan peticiones sin un token válido, devolviendo un error 403 Forbidden o 401 Unauthorized.
![Prueba sin token](capturas/PruebaSinToken.png)



# 📌 Crud-BooksUsers


## 📄 Descripción del Proyecto
Este proyecto es una solución integral para gestionar el inventario de una librería y la administración de préstamos de libros a usuarios. Se implementan funcionalidades CRUD para la gestión de libros, registro de usuarios para solicitar préstamos, control de fechas de préstamo y entrega, y generación de estadísticas que muestran la cantidad de libros disponibles y la cantidad de préstamos realizados.


## Características

- **CRUD para Libros:** Agregar, consultar, actualizar y eliminar libros del inventario.
- **Registro de Usuarios:** Gestión y registro de usuarios para solicitar préstamos.
- **Control de Préstamos:** Registro y seguimiento de fechas de inicio y entrega de préstamos.
- **Estadísticas:** Visualización de la cantidad de libros disponibles y la cantidad de préstamos realizados.
- **Documentación Interactiva:** Swagger UI para explorar y probar la API.
- **Colección de Postman:** Archivo incluido para facilitar pruebas de los endpoints de la API.


## 🚀 Requisitos previos
Antes de comenzar, asegúrate de tener instaladas las siguientes herramientas en tu máquina:

- [Node.js](https://nodejs.org/) (versión 18 o superior)
- [Angular CLI](https://angular.io/cli) (versión 18)
- [Java 17](https://www.oracle.com/java/technologies/downloads/#java21) o superior
- [Maven](https://maven.apache.org/download.cgi) (para la gestión de dependencias)
- [Git](https://git-scm.com/) (para clonar el repositorio)

## 📥 Instalación y ejecución

### 1. Clonar el repositorio
```sh
git clone https://github.com/CrisAcosMoya/Crud-BooksUsers.git
cd Proj-Employees
cd employees-front
```

### 2. Instalar dependencias
```sh
npm install -g @angular/cli
```

### 3. Ejecutar el proyecto en desarrollo
```sh
ng serve
```
```sh
npm run json-server
```
Luego, abre en el navegador: [http://localhost:4200/](http://localhost:4200/)

### 4. Ejecucion del Backend

Ejecuta el servidor Spring Boot con Maven:

```bash
mvn spring-boot:run
```

El servidor estará disponible en: http://localhost:8080.

### 5. **Acceder a la Documentación Swagger**

Para explorar los endpoints y realizar pruebas, accede a la documentación Swagger disponible en el siguiente enlace:

[🔗 Swagger UI - Localhost](http://localhost:8080/swagger-ui.html)

🔧 Notas Adicionales

Se ha incluido una Colección de Postman en el repositorio para facilitar la prueba de los endpoints de la API. Importa la colección desde el archivo **postman_collection.json** que se encuentra en el directorio raíz del proyecto Backend.

## 🛠️ Tecnologías y Arquitectura

Este proyecto consta de dos partes:

### 🔹 Frontend
- Desarrollado con **TypeScript y Angular**.
- Uso de buenas prácticas en desarrollo web.
- Modularización de componentes y servicios.
- Implementación de estilos con Angular Material.

### 🔹 Backend
- Desarrollado con **Java y Spring Boot**.
- Implementación de **arquitectura limpia**.
- Aplicación de **principios SOLID**.
- **Pruebas unitarias** para garantizar la calidad del código.
- Contenedorización con **Docker** para una mejor gestión de despliegue.

## 📝 Licencia
Este proyecto está bajo la licencia [MIT](LICENSE).

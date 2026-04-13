# Sistema de Gestión Integral - Ferretería Yomara

![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

## 📖 Resumen Ejecutivo
Plataforma empresarial (B2B/B2C) diseñada para la orquestación de operaciones críticas en el sector retail de construcción. El sistema centraliza la facturación POS, el control de inventarios multisede (Kardex) y la trazabilidad logística de última milla mediante una arquitectura Cloud-First.

## Características Principales (Core Features)
* **Punto de Venta (POS) Reactivo:** Interfaz web en Angular para procesamiento ágil de ventas y emisión de comprobantes.
* **Control de Inventario Centralizado:** Motor de reglas en Spring Boot para validación de stock en tiempo real y prevención de quiebres.
* **Logística Mobile-First:** Aplicación nativa en Kotlin para transportistas, permitiendo la carga de evidencias de entrega (fotos/firmas) directo a la nube.
* **Seguridad y Auditoría:** Autenticación mediante JWT con Control de Acceso Basado en Roles (RBAC) para proteger datos financieros.

## Arquitectura de Software
El proyecto sigue principios de **Clean Architecture** y separación de responsabilidades (SoC):
- **Backend:** API RESTful construida con Java 17 y Spring Boot 3.
- **Frontend (Web):** Single Page Application (SPA) en Angular.
- **Frontend (Mobile):** App Android nativa en Kotlin.
- **Persistencia:** PostgreSQL alojado en Supabase.
- **Infraestructura:** Despliegue del servidor en AWS EC2 y almacenamiento de archivos/evidencias en AWS S3.

## ⚙️ Puesta en Marcha

### Prerrequisitos
- Java JDK 17+
- Node.js v18+ y Angular CLI
- Maven
- Credenciales configuradas para Supabase y AWS EC2.

### Instalación Backend 
```bash
git clone [https://github.com/Darkzz10/SOLUCION-WEB-PARA-UN-SISTEMA-DE-GESTI-N-INTEGRADA-FERRETERIA-YOMARA.git](https://github.com/Darkzz10/SOLUCION-WEB-PARA-UN-SISTEMA-DE-GESTI-N-INTEGRADA-FERRETERIA-YOMARA.git)
cd backend-yomara
mvn clean install
mvn spring-boot:run

###  Instalación Fronted 

cd frontend-yomara
npm install
ng serve
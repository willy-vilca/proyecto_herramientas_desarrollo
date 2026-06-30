# Sistema Contador

Sistema web desarrollado con **Spring Boot**, **Thymeleaf** y **PostgreSQL** para la gestión de clientes, categorías e ingresos/gastos.

El proyecto implementa un pipeline completo de **Integración Continua (CI)** y **Despliegue Continuo (CD)** utilizando **GitHub Actions**, **Microsoft Azure App Service** y **Azure Database for PostgreSQL**.

---

# Estado del Proyecto

| Workflow | Estado |
|----------|--------|
| Build | ![Build](https://github.com/willy-vilca/proyecto_herramientas_desarrollo/actions/workflows/01-build.yml/badge.svg) |
| Tests | ![Tests](https://github.com/willy-vilca/proyecto_herramientas_desarrollo/actions/workflows/02-test.yml/badge.svg) |
| Package | ![Package](https://github.com/willy-vilca/proyecto_herramientas_desarrollo/actions/workflows/03-package.yml/badge.svg) |
| Deploy | ![Deploy](https://github.com/willy-vilca/proyecto_herramientas_desarrollo/actions/workflows/04-deploy.yml/badge.svg) |

---

# Tecnologías utilizadas

- Java 17
- Spring Boot
- Thymeleaf
- PostgreSQL
- Maven
- GitHub Actions
- Microsoft Azure App Service
- Azure Database for PostgreSQL
- CodeQL

---

# Arquitectura del Sistema

```
Usuario
    │
    ▼
Spring Boot + Thymeleaf
    │
    ▼
Azure Database for PostgreSQL
    │
    ▼
Azure App Service
```

---

# Pipeline CI/CD

```
Developer

    │

git push

    │

▼ Workflow 01
Build

    │

▼ Workflow 02
Tests

    │

▼ Workflow 03
Package (.jar)

    │

Artifact

    │

▼ Workflow 04
Deploy Azure

    │

Azure App Service

    │

▼ CodeQL
Análisis de Seguridad
```

---

# Despliegue

La aplicación se encuentra desplegada en Microsoft Azure mediante **Azure App Service**.

URL:

```
https://sistema-contador-euhnhuhfefhmgxaj.eastus-01.azurewebsites.net 
```

---

# Características implementadas

- Gestión de clientes
- Gestión de categorías
- Registro de ingresos
- Registro de gastos
- Reportes
- Persistencia en PostgreSQL
- Variables de entorno en Azure
- Despliegue automático mediante GitHub Actions
- Empaquetado automático del proyecto
- Publicación automática en Azure
- Escaneo automático de seguridad mediante CodeQL

---

## Autores

-Leonardo Buleje Barbarán
-Christian Castro Gutierrez
-Roberto Huamán Chacaliaza
-Javier Trejo Vargas
-Willy Vilca Huaytalla

Universidad Tecnológica del Perú
Ingeniería de Sistemas

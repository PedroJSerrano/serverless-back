# Database MySQL

Módulo de gestión de base de datos que proporciona scripts de migración y versionado de esquemas usando Liquibase.

## Funcionalidades

- **Versionado de base de datos** - Control de versiones de esquemas con Liquibase
- **Scripts de migración** - Creación y actualización de estructuras de BD
- **Conector MySQL** - Driver optimizado para conexiones MySQL

## Tecnologías Utilizadas

- **Liquibase 4.33.0** - Herramienta de versionado de base de datos
- **MySQL Connector 9.3.0** - Driver JDBC para MySQL
- **Maven** - Gestión de dependencias

## Estructura del Módulo

```
database-mysql/
├── src/main/resources/
│   └── db/changelog/        # Scripts de migración Liquibase
│       ├── db.changelog-master.xml
│       └── migrations/      # Archivos de migración individuales
└── pom.xml                 # Dependencias Liquibase y MySQL
```

## Uso

Este módulo contiene únicamente scripts y dependencias para la gestión de base de datos. No requiere despliegue como los otros módulos serverless.

### Comandos de Liquibase

```bash
# Aplicar migraciones
mvn liquibase:update

# Generar SQL de migración
mvn liquibase:updateSQL

# Rollback a versión anterior
mvn liquibase:rollback -Dliquibase.rollbackCount=1

# Validar changelog
mvn liquibase:validate
```

## Configuración

Las propiedades de conexión a la base de datos se configuran en:
- `src/main/resources/liquibase.properties`
- Variables de entorno del sistema
- Parámetros Maven

### Variables de Entorno

```bash
DB_URL=jdbc:mysql://localhost:3306/myapp
DB_USERNAME=usuario
DB_PASSWORD=contraseña
```

## Desarrollo

### Prerrequisitos
- **Java 21**
- **Maven 3.8+**
- **MySQL Server** (local o remoto)

### Crear Nueva Migración

1. Crear archivo en `src/main/resources/db/changelog/migrations/`
2. Seguir convención de nombres: `YYYY-MM-DD-descripcion.xml`
3. Agregar referencia en `db.changelog-master.xml`

### Ejemplo de Migración

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                   http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.3.xsd">

    <changeSet id="create-users-table" author="developer">
        <createTable tableName="users">
            <column name="id" type="BIGINT" autoIncrement="true">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="username" type="VARCHAR(50)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="email" type="VARCHAR(100)">
                <constraints nullable="false"/>
            </column>
            <column name="created_at" type="TIMESTAMP" defaultValueComputed="CURRENT_TIMESTAMP">
                <constraints nullable="false"/>
            </column>
        </createTable>
    </changeSet>

</databaseChangeLog>
```

## Integración con Otros Módulos

Este módulo proporciona la estructura de base de datos que pueden usar:
- **Auth Manager** - Para almacenar credenciales de usuario
- **User Manager** - Para gestión completa de usuarios
- Futuros módulos que requieran persistencia MySQL

## Buenas Prácticas

- **Nunca modificar** migraciones ya aplicadas
- **Usar rollback** solo en desarrollo
- **Validar scripts** antes de aplicar en producción
- **Hacer backup** antes de migraciones importantes
- **Documentar cambios** en cada migración
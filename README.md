## Requisitos

* Docker
* JDK 8
* Mvn (maven)
* Postman

## Instrucciones


Descargar el proyecto Cuentahabientes
### `git clone https://github.com/antoniosprza/Cuentahabientes.git`


Para generar la red donde se contendrán la base de datos y el servicio de Cuentahabientes
### `docker network create clientes`


Para generar la imagen de la base de datos ejecutar el Docker file en /src/main/BD de Cuentahabientes (desde el directorio donde se descargaron los repositorios)
### `cd /src/main/BD`
### `docker build -t postgres .`

Y ejecutar la imagen para PostgreSQL
### `docker run --volume aLocalPath:/home/gonet/gpkg --rm -P -p 55432:5432 -d --name postgres12 --network clientes postgres`

Descargar la imagen para RabbitMQ
### `docker pull rabbitmq:3.8-management-alpine`

Ejecutar la imagen para RabbitMQ
### `docker run -p 15672:15672 -p 5672:5672 --name rabbitmq --network clientes -d rabbitmq:3.8-management-alpine`

Crear el archivo jar del micro servicio Cuentahabientes con maven (desde el directorio donde se descargo el repositorio)
### `mvn clean package -DskipTests=true`

Ejecutar el Dockerfile para generar la imagen para el micro servicio de /src
### `docker build -t cuentahabientes .`

Ejecutar la imagen
### `docker run -p 8080:8080 -d --name cuentahabientes --network clientes cuentahabientes`

Verificar que el servicio envia o recibe los mensajes correctamente. Enviar desde postman el id: 1 o 2 o 3
### `localhost:8080/api/cuentahabientes/id`
Para recibir 
### `localhost:8080/api/recibirMensaje`

Por último para validar los mensajes enviados o recibidos ejecutar los logs de docker, desde una terminal
### `docker logs -f cuentahabientes`
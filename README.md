# Appointment Service
- This service is responsible for appointments:
  - Customers
    - Book or cancel their appointments
  - Administrator/Teller
    - Manage appointments
    - Confirm appointments
    - Cancel appointment with reason
- The appointment service act as the client([gRPC](https://grpc.io/) protocol) in receiving data from the following services:
  - Auth Service - To request user details for sending notifications about the appointments.
  - Branch Service - To add branch details to email notification about booking.
- For testing scheduling an appointment the following Province and cities are loaded the admin just need to create one branch for testing:
```json
[
  [
    {
      "name": "Western Cape",
      "cities": [
        {
          "name": "Cape Town"
        },
        {
          "name": "Stellenbosch"
        },
        {
          "name": "George"
        },
        {
          "name": "Paarl"
        }
      ]
    },
    {
      "name": "KwaZulu-Natal",
      "cities": [
        {
          "name": "Durban"
        },
        {
          "name": "Pietermaritzburg"
        }
      ]
    },
    {
      "name": "Eastern Cape",
      "cities": [
        {
          "name": "Bhisho"
        },
        {
          "name": "Gqeberha"
        },
        {
          "name": "East London"
        }
      ]
    },
    {
      "name": "Free State",
      "cities": [
        {
          "name": "Bloemfontein"
        }
      ]
    },
    {
      "name": "Gauteng",
      "cities": [
        {
          "name": "Johannesburg"
        }
      ]
    },
    {
      "name": "Limpopo",
      "cities": [
        {
          "name": "Polokwane"
        }
      ]
    },
    {
      "name": "Mpumalanga",
      "cities": [
        {
          "name": "Mbombela"
        }
      ]
    },
    {
      "name": "Northern Cape",
      "cities": [
        {
          "name": "Kimberley"
        }
      ]
    },
    {
      "name": "North West",
      "cities": [
        {
          "name": "Mahikeng"
        }
      ]
    }
  ]
]
```
## Existing public Docker Image
- There is an already existing public image you can use without building the new one if you not making code changes:
  - Image - ```docker.io/menelismthembu12/appointment-service```
  - Tag - ```1.1.2```
- The service allows config to be externalized using config-server.

Config
```yaml
infrastructure:
  env: local
  DB:
    USERNAME: {db_user}
    PASSWORD: ${secret.db_password}

spring:

  cloud:
    config:
      enabled: false # If not running config server

  datasource:
    url: jdbc:postgresql://localhost:5432/{appointment_db}
    username: ${infrastructure.DB.USERNAME}
    password: ${infrastructure.DB.PASSWORD}

  kafka:
    bootstrap-servers:
      - localhost:9092
      - .........
    producer:
      acks: -1
      batch-size: 100
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      compression-type: zstd

  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: http://auth-service:9000/oauth2/jwks
  
  # Service intercommunication
  grpc:
    client:
      channel:
        auth-service:
          target: "static://localhost:9090"
        branch-service:
          target: "static://localhost:9091"

springdoc:
  api-docs:
    path: /appointment-service/v3/api-docs
  swagger-ui:
    path: /appointment-service/swagger-ui.html
    operations-sorter: method
    doc-expansion: none

app:
  open-api:
    info:
      title: Appointment Branch Service
      description: Appointment Branch Service
      version: 1.0.0
  white-list:
    - "/appointment-service/swagger-ui/**"
    - "/appointment-service/v3/api-docs/**"
  admin-routes:
    - "/api/v1/appointment/admin/**"
  kafka:
    outgoing:
      notification: ${infrastructure.env}-appointment-notifications
```
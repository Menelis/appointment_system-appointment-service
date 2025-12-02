# Appointment Service
- This service is responsible for appointments:
  - Customers
    - Book or cancel their appointments
  - Administrator/User
    - Manage appointments
    - Confirm appointments
    - Cancel appointment with reason
- The appointment service act as the client([gRPC](https://grpc.io/) protocol) in receiving data from the following services:
  - Auth Service - To request user details for sending notifications about the appointments.
  - Branch Service - To add branch details to email notification about booking.
    
## Existing public Docker Image
- There is an already existing public image you can use without building the new one if you not making code changes:
  - Image - ```docker.io/menelismthembu12/appointment-service```
  - Tag - ```1.1.1``
- The service allows config to be externalized using config-server.

Config
```yaml
infrastructure:
  env: local
  DB:
    USERNAME: {db_user}
    PASSWORD: {db_password}
spring:
  cloud:
    config:
      enabled: false
  datasource:
    url: jdbc:postgresql://localhost:5432/{appointment_db}
    username: ${infrastructure.DB.USERNAME}
    password: ${infrastructure.DB.PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true
    properties:
      hibernate:
        format_sql: true
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
    notification-topic: ${infrastructure.env}-appointment-notifications
  email-template:
    pending-confirmation-email-template: "Dear %s,<br/><br/> Thank you for making an appointment at branch: %s for date:%s at slot: %s.<br/> Your reference number is: %s <br/> Booking status is:<b>%s</b>.<br/> You will get a notification with the progress of your appointment.<br/></br/><br/> %s<br/><br/>%s"
    confirmation-email-template: "Dear %s,<br/><br/> Your appointment at branch: %s for date:%s at slot: %s.<br/> Your reference number is: %s <br/> Booking status is: <b>%s</b>. <br/>We looking forward in seeing you.<br/></br/><br/>%s<br/><br/>%s"
    cancellation-email-template: "Dear %s,<br/><br/> Your appointment at branch: %s for date:%s at slot: %s has been <b>CANCELLED</b> due to reason: %s.<br/>Thank You for your effort in making the booking.<br/></br/><br/>%s<br/><br/>%s"

# Service intercommunication
grpc:
  client:
    auth-service: # This key is used by @GrpcClient
      address: static://auth-service:9090
      negotiation-type: plaintext
    branch-service: # This key is used by @GrpcClient
      address: static://branch-service:9091
      negotiation-type: plaintext

```
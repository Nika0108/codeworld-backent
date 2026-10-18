# Code World – Təcrübə və Təqaüd Proqramına Qeydiyyat Sistemi

Bu layihə Code World Education Center üçün hazırlanmış Java Backend sistemidir.

Sistem vasitəsilə istifadəçilər aktiv təcrübə və təqaüd proqramlarına müraciət edə, administrator isə müraciətləri idarə edə bilər.

## Texnologiyalar

- Java 17
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- PostgreSQL
- Hibernate
- Maven
- Lombok
- Apache POI

## Əsas imkanlar

### Program idarəetməsi

- Yeni proqram yaratmaq
- Proqramı yeniləmək
- Proqramları siyahılamaq
- Proqramı silmək
- Aktiv proqramı müəyyən etmək
- Eyni anda yalnız bir aktiv proqramın olması

### Proqram tipləri

- EXPERIENCE – Təcrübə proqramı
- SCHOLARSHIP – Təqaüd proqramı

## Müraciət sistemi

İstifadəçi aşağıdakı məlumatlarla müraciət edə bilər:

- Ad
- Soyad
- Telefon
- Email
- Motivasiya
- Təhsil forması
- İxtisas
- CV

### Təhsil formaları

- ONLINE
- OFFLINE

### İxtisaslar

- JAVA_BACKEND
- FRONTEND
- UI_UX

## Müraciət statusları

- PENDING
- ACCEPTED
- REJECTED
- INTERVIEW_INVITED

Status dəyişiklikləri tarixçə şəklində saxlanılır.

## Müraciət kodu

Hər müraciət üçün avtomatik unikal kod yaradılır.

Məsələn:

CW-INT-2026-0001

Təqaüd proqramı üçün:

CW-SCH-2026-0001

## Təkrar müraciətin qarşısının alınması

Eyni aktiv proqram üçün eyni email və telefon nömrəsi ilə ikinci dəfə müraciət etmək mümkün deyil.

## WhatsApp

Hər müraciət üçün telefon nömrəsinə əsasən WhatsApp keçidi yaradılır.

## Excel Export

Sistem müraciətləri Excel formatında ixrac etməyə imkan verir:

- Bütün müraciətlər
- Yalnız qəbul edilmiş müraciətlər

## Word Export

Müraciət edən şəxslərin məlumatları Word .docx formatında ixrac edilə bilər.

## API Endpoint-ləri

### Program API

```text
POST   /api/programs
GET    /api/programs
GET    /api/programs/{id}
PUT    /api/programs/{id}
DELETE /api/programs/{id}

POST   /api/applications
GET    /api/applications
GET    /api/applications/{id}
GET    /api/applications/{id}/history
PUT    /api/applications/{id}/status
POST   /api/applications/{id}/cv
DELETE /api/applications/{id}

GET /api/export/applications/excel
GET /api/export/applications/accepted/excel
GET /api/export/applications/word

GET /api/admin/statistics/total-applications
GET /api/admin/statistics/active-program-applications
GET /api/admin/statistics/active-program
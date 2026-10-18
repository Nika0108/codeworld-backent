-- Code World database tables
-- Bu fayl təqdimat üçün SQL sxemidir.
-- Tətbiq işləyərkən cədvəlləri Hibernate özü yaradır.

CREATE TABLE IF NOT EXISTS programs (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    program_number INTEGER NOT NULL,
    start_month INTEGER NOT NULL,
    start_year INTEGER NOT NULL,
    end_date DATE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS applications (
    id BIGSERIAL PRIMARY KEY,
    application_code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    email VARCHAR(255) NOT NULL,
    motivation VARCHAR(2000) NOT NULL,
    education_type VARCHAR(50) NOT NULL,
    field VARCHAR(50) NOT NULL,
    feedback VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    admin_note VARCHAR(255),
    program_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    cv VARCHAR(500),

    CONSTRAINT fk_application_program
        FOREIGN KEY (program_id)
        REFERENCES programs(id),

    CONSTRAINT uk_application_program_email_phone
        UNIQUE (program_id, email, phone)
);

CREATE TABLE IF NOT EXISTS application_status_history (
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    comment VARCHAR(255),
    changed_at TIMESTAMP,

    CONSTRAINT fk_status_history_application
        FOREIGN KEY (application_id)
        REFERENCES applications(id)
);
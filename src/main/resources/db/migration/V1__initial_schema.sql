-- ============================================================
-- users
-- ============================================================
CREATE TABLE users (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    username        VARCHAR(255)    NOT NULL,
    nickname        VARCHAR(15)     NOT NULL,
    email           VARCHAR(255),
    password        VARCHAR(255),
    login_type      VARCHAR(255),
    provider_id     VARCHAR(255),
    role            VARCHAR(255),
    status          VARCHAR(255),
    deleted_version BIGINT          NOT NULL,
    created_at      DATETIME(6)     NOT NULL,
    updated_at      DATETIME(6)     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_username_deleted_version UNIQUE (username, deleted_version)
);

-- ============================================================
-- stores
-- ============================================================
CREATE TABLE stores (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(50)     NOT NULL,
    category        VARCHAR(255),
    address         VARCHAR(100)    NOT NULL,
    detail_address  VARCHAR(50),
    zip_code        VARCHAR(100)    NOT NULL,
    sigungu_code    VARCHAR(5)      NOT NULL,
    latitude        DOUBLE,
    longitude       DOUBLE,
    phone           VARCHAR(11)     NOT NULL,
    user_id         BIGINT,
    owner_name      VARCHAR(50),
    business_number VARCHAR(10)     NOT NULL,
    favorites       INT             NOT NULL DEFAULT 0,
    status          VARCHAR(255),
    created_at      DATETIME(6)     NOT NULL,
    updated_at      DATETIME(6)     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_stores_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- ============================================================
-- store_tables  (@Table(name="storeTables") → store_tables)
-- ============================================================
CREATE TABLE store_tables (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    store_id      BIGINT,
    table_name    VARCHAR(15)  NOT NULL,
    min_capacity  INT          NOT NULL,
    max_capacity  INT          NOT NULL,
    status        VARCHAR(255),
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_store_tables_store FOREIGN KEY (store_id) REFERENCES stores (id)
);

-- ============================================================
-- schedules
-- ============================================================
CREATE TABLE schedules (
    id               BIGINT      NOT NULL AUTO_INCREMENT,
    store_id         BIGINT,
    day_of_week      VARCHAR(255),
    start_time       TIME(6)     NOT NULL,
    end_time         TIME(6)     NOT NULL,
    interval_minute  INT         NOT NULL,
    status           VARCHAR(255),
    created_at       DATETIME(6) NOT NULL,
    updated_at       DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_schedules_store FOREIGN KEY (store_id) REFERENCES stores (id)
);

-- ============================================================
-- reservations
-- ============================================================
CREATE TABLE reservations (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    user_id           BIGINT,
    name              VARCHAR(30)  NOT NULL,
    store_id          BIGINT,
    target_date_time  DATETIME(6)  NOT NULL,
    day_of_week       VARCHAR(255),
    head_count        INT          NOT NULL,
    table_id          BIGINT,
    status            VARCHAR(255),
    created_at        DATETIME(6)  NOT NULL,
    updated_at        DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_reservations_user  FOREIGN KEY (user_id)  REFERENCES users (id),
    CONSTRAINT fk_reservations_store FOREIGN KEY (store_id) REFERENCES stores (id),
    CONSTRAINT fk_reservations_table FOREIGN KEY (table_id) REFERENCES store_tables (id)
);

-- ============================================================
-- favorites
-- ============================================================
CREATE TABLE favorites (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    user_id    BIGINT,
    store_id   BIGINT,
    status     VARCHAR(255),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_favorites_user  FOREIGN KEY (user_id)  REFERENCES users (id),
    CONSTRAINT fk_favorites_store FOREIGN KEY (store_id) REFERENCES stores (id)
);
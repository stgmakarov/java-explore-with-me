DROP TABLE IF EXISTS STATISTICS, APPS;

CREATE TABLE APPS
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE STATISTICS
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app_id    BIGINT REFERENCES APPS (id) ON DELETE CASCADE,
    uri       VARCHAR(255)                            NOT NULL,
    ip        VARCHAR(15)                             NOT NULL,
    timestamp TIMESTAMP                               NOT NULL,
    CONSTRAINT pk_stat PRIMARY KEY (id)
);
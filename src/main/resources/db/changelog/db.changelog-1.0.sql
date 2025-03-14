--changeset FrolovIV:1
CREATE TABLE IF NOT EXISTS document_groups
(
    id                     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name                   VARCHAR(255)                            NOT NULL,
    color                  VARCHAR(255)                            NOT NULL,
    document_group_user_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_document_groups PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS document_types
(
    id                                  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name                                VARCHAR(255)                            NOT NULL,
    days_before_expiration_to_warn_user INTEGER                                 NOT NULL,
    CONSTRAINT pk_document_types PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS documents
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title             VARCHAR(255)                            NOT NULL,
    date_of_issue     TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    expiration_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    user_id           BIGINT                                  NOT NULL,
    document_type_id  BIGINT                                  NOT NULL,
    document_group_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_documents PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username VARCHAR(255)                            NOT NULL,
    password VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS document_types
    ADD CONSTRAINT uc_document_types_name UNIQUE (name);

ALTER TABLE IF EXISTS users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

CREATE INDEX IF NOT EXISTS idx_documents_document_group_id ON documents (document_group_id);
CREATE INDEX IF NOT EXISTS idx_documents_date_of_issue ON documents (date_of_issue);
CREATE INDEX IF NOT EXISTS idx_documents_expiration_date ON documents (expiration_date);
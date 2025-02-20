--changeset FrolovIV:2
INSERT INTO document_types (name, days_before_expiration_to_warn_user)
VALUES ('Паспорт РФ', 14),
       ('Паспорт заграничный', 60),
       ('Прибор учета', 30),
       ('Пароль', 2);
-- Insert sample customers
INSERT INTO customers (name, phone_number)
VALUES ('Piet Pieters', '0612345678'),
       ('Klaas Klaassen', '0687654321'),
       ('Gert Verhulst', '0611223344'),
       ('Willemien Willemsen', '0688776655'),
       ('Jantien Zelf', '0611115555')
;

-- Insert sample dogs
INSERT INTO dogs (name, breed, age, customer_id)
VALUES ('Buddy', 'Labrador', 3, 1),
       ('Max', 'Golden Retriever', 5, 2),
       ('Lassie', 'Husky', 1, 1),
       ('Senna', 'Teckel', 6, 4),
       ('Samson', 'Bearded collie', 5, 3),
       ('Molly', 'Maltezer', 9, 5);

-- Insert sample appointments
INSERT INTO appointments (date, customer_id, dog_id, status)
VALUES ('2024-05-10 09:00:00', 1, 1, 'Scheduled'),
       ('2024-05-12 11:00:00', 2, 2, 'Confirmed'),
       ('2024-12-23 10:00:00', 5, 6, 'Scheduled');

-- Insert sample treatments
INSERT INTO treatments (name, price)
VALUES ('Vacht wassen', 30.0),
       ('Ontvlooien', 15.0),
       ('Vacht vlechten', 20.0),
       ('Nagels knippen', 12.0),
       ('Tanden poetsen', 10.0);

-- Insert sample products
INSERT INTO products (name, price, stock)
VALUES ('Honden shampoo', 12.99, 50),
       ('Honden tandpasta', 8.50, 30),
       ('Ontvlooiingsmiddel', 30.99, 10),
       ('Hondensnoepjes', 5.00, 120);

INSERT INTO users (username, password)
VALUES ('admin', '$2a$10$H0xLFvvBYM.NtMbTlsYe4emMWIo/2DGrohgTG8PmwIQoThv3S9zjW');

INSERT INTO roles (username, role)
VALUES ('admin', 'ADMIN');

INSERT INTO users (username, password)
VALUES ('cashier', '$2a$10$5SP253Ev3YlFL/h/TdSP5eAgPxiEx8VyMSbrWhvxMggxzzYoZKGy.');

INSERT INTO roles (username, role)
VALUES ('cashier', 'CASHIER');

INSERT INTO users (username, password)
VALUES ('doggroomer', '$2a$10$Wtpz7aVnAJdbvgppY4YaQ.GlL5yV697ZkdTq29VnMp7CP0ZKl5pM2');

INSERT INTO roles (username, role)
VALUES ('doggroomer', 'DOGGROOMER');



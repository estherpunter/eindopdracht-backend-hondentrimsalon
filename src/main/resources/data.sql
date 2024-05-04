-- Insert sample customers
INSERT INTO customers (name, phone_number)
VALUES ('John Doe', '123-456-7890'),
       ('Jane Smith', '987-654-3210');

-- Insert sample dogs
INSERT INTO dogs (name, breed, age, customer_id)
VALUES ('Buddy', 'Labrador Retriever', 3, 1),
       ('Max', 'Golden Retriever', 5, 2);

-- Insert sample appointments
INSERT INTO appointments (date, customer_id, dog_id, status)
VALUES ('2024-05-10 09:00:00', 1, 1, 'Scheduled'),
       ('2024-05-12 11:00:00', 2, 2, 'Confirmed');

-- Insert sample treatments
INSERT INTO treatments (name, price)
VALUES ('Basic Grooming', 30.0),
       ('Flea Treatment', 15.0);

-- Insert sample products
INSERT INTO products (name, price, stock)
VALUES ('Dog Shampoo', 12.99, 50),
       ('Brush', 8.50, 30);


INSERT INTO users (username, password, enabled)
VALUES ('admin', '$2a$10$H0xLFvvBYM.NtMbTlsYe4emMWIo/2DGrohgTG8PmwIQoThv3S9zjW', true);

INSERT INTO roles (username, role)
VALUES ('admin', 'ADMIN');

INSERT INTO users (username, password, enabled)
VALUES ('cashier', '$2a$10$5SP253Ev3YlFL/h/TdSP5eAgPxiEx8VyMSbrWhvxMggxzzYoZKGy.', true);

INSERT INTO roles (username, role)
VALUES ('cashier', 'CASHIER');

INSERT INTO users (username, password, enabled)
VALUES ('doggroomer', '$2a$10$Wtpz7aVnAJdbvgppY4YaQ.GlL5yV697ZkdTq29VnMp7CP0ZKl5pM2', true);

INSERT INTO roles (username, role)
VALUES ('doggroomer', 'DOGGROOMER');



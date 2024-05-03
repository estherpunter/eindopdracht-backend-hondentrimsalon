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


-- Create Users table
DROP TABLE IF EXISTS Users;
CREATE TABLE Users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled  BOOLEAN DEFAULT true
);

INSERT INTO users (username, password, enabled) VALUES ('admin', 'admin_password', true);
INSERT INTO users (username, password, enabled) VALUES ('cashier', 'cashier_password', true);
INSERT INTO users (username, password, enabled) VALUES ('doggroomer', 'doggroomer_password', true);

INSERT INTO roles (username, role) VALUES ('admin', 'ADMIN');
INSERT INTO roles (username, role) VALUES ('cashier', 'CASHIER');
INSERT INTO roles (username, role) VALUES ('DOGGROOMER', 'DOGGROOMER');
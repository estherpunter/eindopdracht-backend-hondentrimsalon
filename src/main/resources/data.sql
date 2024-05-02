-- Insert sample customers
INSERT INTO customers (name, phonenumber)
VALUES ('Alice', '1234567890');

INSERT INTO customers (name, phonenumber)
VALUES ('Bob', '9876543210');

-- Insert sample dogs
INSERT INTO dogs (name, breed, age, customer_id)
VALUES ('Buddy', 'Labrador', 3, 1); -- Assign Buddy to Alice (customer_id = 1)

INSERT INTO dogs (name, breed, age, customer_id)
VALUES ('Max', 'Golden Retriever', 5, 2); -- Assign Max to Bob (customer_id = 2)

-- Insert sample appointments
INSERT INTO appointments (date, customer_id, dog_id, status)
VALUES ('2024-05-10 09:00:00', 1, 1, 'Scheduled'); -- Appointment for Alice's dog (Buddy)

INSERT INTO appointments (date, customer_id, dog_id, status)
VALUES ('2024-05-12 14:00:00', 2, 2, 'Scheduled'); -- Appointment for Bob's dog (Max)

-- Insert sample products
INSERT INTO products (name, price, stock)
VALUES ('Shampoo', 15.99, 50);

INSERT INTO products (name, price, stock)
VALUES ('Brush', 12.50, 30);

-- Insert sample treatments
INSERT INTO treatments (name, price)
VALUES ('Trimming', 25.00);

INSERT INTO treatments (name, price)
VALUES ('Nail Clipping', 10.00);

-- Insert sample receipt (linked to appointment and containing products/treatments)
INSERT INTO receipts (appointment_id)
VALUES (1); -- Receipt for Alice's appointment

-- Link products with appointment
INSERT INTO appointment_products (appointment_id, product_id)
VALUES (1, 1); -- Shampoo used in Alice's appointment

-- Link treatments with appointment
INSERT INTO appointment_treatments (appointment_id, treatment_id)
VALUES (1, 1); -- Trimming performed in Alice's appointment

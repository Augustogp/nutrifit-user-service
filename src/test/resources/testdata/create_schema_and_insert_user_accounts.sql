CREATE TABLE user_account (
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO user_account (id, username, password, first_name, last_name, email) VALUES
(UUID_TO_BIN('f3c6a214-7d7e-4d29-a9a9-58d6e1b623e0'), 'jdoe',  '$2a$10$kKiJ0eF0bYJl.7yQGdZFeO6/8L9rh5XUVrkMNymb5MfDhIxhGEoQO', 'John',  'Doe',    'jdoe@example.com'), -- password: password123
(UUID_TO_BIN(UUID()), 'asmith','$2a$10$7O9cKY2qkC1AVXiKjVnnleJz4TydwI7fXO6cLZZY5vSOrTlLQel2C', 'Alice', 'Smith',  'asmith@example.com'), -- password: secure456
(UUID_TO_BIN(UUID()), 'bwayne','$2a$10$zC1H2J9nU1m1Jg0gJZMNx.jGaqz1N93BzK5Uf7XMi9ymnN/LQOeWe', 'Bruce', 'Wayne',  'bwayne@waynecorp.com'); -- password: batman

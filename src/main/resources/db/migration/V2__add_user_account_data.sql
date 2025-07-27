INSERT INTO user_account (id, username, password, first_name, last_name, email) VALUES
(UUID_TO_BIN(UUID()), 'jdoe',  '$2a$10$kKiJ0eF0bYJl.7yQGdZFeO6/8L9rh5XUVrkMNymb5MfDhIxhGEoQO', 'John',  'Doe',    'jdoe@example.com'), -- password: password123
(UUID_TO_BIN(UUID()), 'asmith','$2a$10$7O9cKY2qkC1AVXiKjVnnleJz4TydwI7fXO6cLZZY5vSOrTlLQel2C', 'Alice', 'Smith',  'asmith@example.com'), -- password: secure456
(UUID_TO_BIN(UUID()), 'bwayne','$2a$10$zC1H2J9nU1m1Jg0gJZMNx.jGaqz1N93BzK5Uf7XMi9ymnN/LQOeWe', 'Bruce', 'Wayne',  'bwayne@waynecorp.com'); -- password: batman

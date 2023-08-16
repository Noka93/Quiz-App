CREATE TABLE authorities (
                             username VARCHAR(255) NOT NULL,
                             authority VARCHAR(255) NOT NULL,
                             PRIMARY KEY (username, authority)
);

INSERT INTO authorities (username, authority) VALUES ('remi', 'ROLE_USER');

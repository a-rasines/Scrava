DROP SCHEMA IF EXISTS Scrava;
DROP USER IF EXISTS 'snJzZHH0mNgTRhrB61ZZQfqjBAA61LOU'@'localhost';
CREATE SCHEMA IF NOT EXISTS Scrava;
--# DO NOT USE THIS CREDENTIALS FOR THE DATABASE AS THIS REPO IS PUBLIC #--
CREATE USER IF NOT EXISTS 'snJzZHH0mNgTRhrB61ZZQfqjBAA61LOU'@'localhost' IDENTIFIED BY '226cfaffa340c20bb2d76359b6f86f02effdbaf99b52905bdfb03a94224a6a3335dbdf2951650f86ebfbb809ae298a0914b073b3633d1e18cfe023c1f0c3485b';
GRANT ALL ON Scrava.* TO 'snJzZHH0mNgTRhrB61ZZQfqjBAA61LOU'@'localhost';

USE Scrava;

CREATE TABLE User (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name char(20),
    password TEXT(256),
    UNIQUE (name)
);

CREATE TABLE Project (
    id BIGINT AUTO_INCREMENT,
    author BIGINT,
    name TEXT,
    content TEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (author) REFERENCES User(id)
);

CREATE TABLE Tutorial (
    id BIGINT AUTO_INCREMENT,
    author BIGINT,
    name TEXT,
    content TEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (author) REFERENCES User(id)
)
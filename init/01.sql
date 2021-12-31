CREATE DATABASE IF NOT EXISTS titlebot;
USE titlebot;
CREATE TABLE IF NOT EXISTS sites(
    id INT NOT NULL AUTO_INCREMENT,
    url TEXT NOT NULL,
    iconUrl TEXT,
    title TEXT,
    PRIMARY KEY ( id )
);

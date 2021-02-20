CREATE TABLE IF NOT EXISTS post (
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    created TIMESTAMP
);

CREATE TABLE IF NOT EXISTS candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    photoId INTEGER REFERENCES photo(id),
    cityId INTEGER
);

CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  name TEXT,
  email TEXT UNIQUE,
  password TEXT
);

CREATE TABLE IF NOT EXISTS city (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE IF NOT EXISTS photo (
    id SERIAL PRIMARY KEY,
    picture BYTEA
);
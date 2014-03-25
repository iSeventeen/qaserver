# qaserver schema

# ---!Ups

CREATE TABLE student(
  id BIGSERIAL NOT NULL PRIMARY KEY,
  card_id BIGINT,
  name VARCHAR(50) NOT NULL,
  age SMALLINT,
  gender SMALLINT,
  avatar VARCHAR(255)
);

CREATE TABLE parent(
  id BIGSERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  role VARCHAR(50) NOT NULL,
  avatar VARCHAR(255) NOT NULL,
  student bigint,
  FOREIGN KEY(student) REFERENCES student(id) on delete cascade
);

CREATE TABLE hot_words(
  id bigserial NOT NULL PRIMARY KEY,
  word VARCHAR(255) NOT NULL
);

# ---!Downs
DROP TABLE if exists student;
DROP TABLE if exists parent;
DROP TABLE if exists hot_words;


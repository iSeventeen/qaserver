CREATE TABLE users(
  id bigserial NOT NULL primary key,
  email VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE hot_words(
  id bigserial NOT NULL PRIMARY KEY,
  word VARCHAR(255) NOT NULL
);

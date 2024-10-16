CREATE TABLE IF NOT EXISTS users (
   id       SERIAL             NOT NULL,
   nickname VARCHAR(128)    NOT NULL,
   email VARCHAR(128)    NOT NULL UNIQUE,
   password VARCHAR(512)    NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tweets (
    id SERIAL NOT NULL,
    name VARCHAR(256),
    text VARCHAR(512),
    image VARCHAR(256),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

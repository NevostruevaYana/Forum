CREATE TABLE IF NOT EXISTS "user" (
            id serial PRIMARY KEY,
            name text NOT NULL UNIQUE,
            psw text NOT NULL,
            active bool NOT NULL,
            last_time_of_activity timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS main_theme (
            id serial PRIMARY KEY,
            theme_name text NOT NULL
);

CREATE TABLE IF NOT EXISTS sub_theme (
            id serial PRIMARY KEY,
            theme_name text NOT NULL,
            main_theme_id int NOT NULL
);

CREATE TABLE IF NOT EXISTS message (
            id serial PRIMARY KEY,
            text text NOT NULL,
            user_name text NOT NULL,
            time timestamp NOT NULL,
            sub_theme text NOT NULL
);
-- Таблица mpa_rating
CREATE TABLE IF NOT EXISTS mpa_rating (
  mpa_rating_id INTEGER PRIMARY KEY, --INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  rating varchar(50) UNIQUE
);

-- Таблица films
CREATE TABLE IF NOT EXISTS films (
  film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(100) NOT NULL,
  description text,
  release_date date CHECK (release_date >= '1895-12-28'),
  duration int,
  mpa_rating_id INTEGER REFERENCES mpa_rating(mpa_rating_id) NULL
);


-- Таблица genre
CREATE TABLE IF NOT EXISTS genre (
  genre_id INTEGER PRIMARY KEY, --GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  genre varchar(50) NOT NULL
);

-- Таблица film_genre
CREATE TABLE IF NOT EXISTS film_genre (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  film_id INTEGER REFERENCES films(film_id),
  genre_id INTEGER REFERENCES genre(genre_id),
  UNIQUE (film_id, genre_id)
);

-- Таблица users
CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email varchar UNIQUE CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
  login varchar(50) NOT NULL,
  name varchar(50),
  birthday date
);

-- Таблица film_likes
CREATE TABLE IF NOT EXISTS film_likes (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  film_id int REFERENCES films(film_id),
  user_id int REFERENCES users(user_id),
  UNIQUE (film_id, user_id)
);



-- Таблица user_friends
CREATE TABLE IF NOT EXISTS user_friends (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  user_id int REFERENCES users(user_id),
  friend_id int REFERENCES users(user_id),
  status ENUM ('pending', 'confirmed'),
  UNIQUE (user_id, friend_id)
);

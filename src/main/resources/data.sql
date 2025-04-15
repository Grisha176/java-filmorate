MERGE INTO genre (genre_id,genre) VALUES
(1,'Комедия'),
(2,'Драма'),
(3,'Мультфильм'),
(4,'Триллер'),
(5,'Документальный'),
(6,'Боевик');
--(7,'Триллер'),
--(8,'Детектив'),
--(9,'Приключения'),
--(10,'Фэнтези');

MERGE INTO mpa_rating (mpa_rating_id,rating) VALUES
(1,'G'),       -- General Audiences (Для всех возрастов)
(2,'PG'),      -- Parental Guidance Suggested (Рекомендуется присмотр родителей)
(3,'PG-13'),   -- Parents Strongly Cautioned (Родители должны быть особенно осторожны)
(4,'R'),       -- Restricted (Ограничено для лиц младше 17 лет без сопровождения взрослых)
(5,'NC-17');   -- No One 17 and Under Admitted (Запрещено для лиц младше 17 лет)
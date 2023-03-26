DELETE FROM likes_by_users;
DELETE FROM film_genres;
DELETE FROM friends;
DELETE FROM film_directors;
DELETE FROM feed;
DELETE FROM reviews;
DELETE FROM films;
DELETE FROM users;
DELETE FROM directors;
DELETE FROM genres;
DELETE FROM mpa;

ALTER TABLE films ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reviews ALTER COLUMN id RESTART WITH 1;

INSERT INTO genres(id, name) VALUES (1, 'Комедия');
INSERT INTO genres(id, name) VALUES (2, 'Драма');
INSERT INTO genres(id, name) VALUES (3, 'Мультфильм');
INSERT INTO genres(id, name) VALUES (4, 'Триллер');
INSERT INTO genres(id, name) VALUES (5, 'Документальный');
INSERT INTO genres(id, name) VALUES (6, 'Боевик');

INSERT INTO mpa(id, name, description) VALUES (1, 'G', 'У фильма нет возрастных ограничений');
INSERT INTO mpa(id, name, description) VALUES (2, 'PG', 'Детям рекомендуется смотреть фильм с родителями');
INSERT INTO mpa(id, name, description) VALUES (3, 'PG-13', 'Детям до 13 лет просмотр не желателен');
INSERT INTO mpa(id, name, description) VALUES (4, 'R', 'Лицам до 17 лет просматривать фильм можно только' ||
                                                         ' в присутствии взрослого');
INSERT INTO mpa(id, name, description) VALUES (5, 'NC-17', 'Лицам до 18 лет просмотр запрещён');
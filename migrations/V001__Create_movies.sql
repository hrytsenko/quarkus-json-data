create table movies
(
    id    varchar(20) primary key,
    movie jsonb not null
);

create unique index movies_imdb on movies ((movie->>'imdb'));

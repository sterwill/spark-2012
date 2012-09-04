drop table if exists codes cascade;
drop table if exists checkins cascade;
drop table if exists locations cascade;
drop table if exists users cascade;

create table users (
  id text primary key,
  email text unique not null,
  full_name text not null
);

create table locations (
  id text primary key,
  name text not null
);

create table checkins (
  id text primary key,
  time timestamp without time zone not null,
  location_id text not null references locations(id) on delete cascade on update cascade,
  user_id text not null references users(id) on delete cascade on update cascade
);

create table codes (
  id text primary key,
  code text not null,
  time timestamp without time zone not null,
  user_id text not null references users(id) on delete cascade on update cascade
);

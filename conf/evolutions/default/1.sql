# User schema

# --- !Ups

create table users (
  id integer not null primary key,
  email varchar(255) not null,
  name varchar(255) not null,
  password varchar(255) not null
);

create sequence user_seq;

# --- !Downs

drop table if exists users;
drop sequence if exists user_seq;

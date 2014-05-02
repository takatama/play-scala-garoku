# --- !Ups

insert into user (id, email, name, password) values (user_seq.nextval, 'a@a', 'a', 'a');

# --- !Downs

delete from user where id = 1

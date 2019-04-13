insert into burger (id,total_price) values (1L,5.0);
INSERT INTO ingredient (label,name,quantity,burger_id) VALUES ('Salad','salad',0L,1L);
INSERT INTO ingredient (label,name,quantity,burger_id) VALUES ('Bacon','bacon',0L,1L);
INSERT INTO ingredient (label,name,quantity,burger_id) VALUES ('Cheese','cheese',0L,1L);
INSERT INTO ingredient (label,name,quantity,burger_id) VALUES ('Meat','meat',0L,1L);
INSERT INTO users (id, created_at, updated_at, email, name, password, username) values (1L,NOW(),NOW(),'foo@foobar.com','Foo Bar','$2a$10$7qIfo4TtCFopae/m7saycenm8tdhSlEqmGtUz.CEDkyzk2BHgX0cu	','foobar90');
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

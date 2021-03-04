
create TABLE gift_certificates (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(45) NOT NULL unique,
  description VARCHAR(300) NOT NULL ,
  price INT NOT NULL,
  duration INT NOT NULL,
  create_date TIMESTAMP not null ,
  last_update_date timestamp not null,
  deleted boolean default false
  );


-- -----------------------------------------------------
-- Table `mydb`.`tags`
-- -----------------------------------------------------
create TABLE tags (
  tag_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL unique );


-- -----------------------------------------------------
-- Table `mydb`.`gift_sertificates_has_tags`
-- -----------------------------------------------------
create TABLE gift_certificates_has_tags (
  gift_certificates_id INT NOT NULL,
  tags_id INT NOT NULL,
  PRIMARY KEY (gift_certificates_id, tags_id));


create TABLE users (
  user_id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(30) NOT NULL unique ,
  password VARCHAR(70) NOT NULL,
  first_name VARCHAR(30) NOT NULL ,
  last_name VARCHAR(30) NOT NULL,
  PRIMARY KEY (`user_id`));


CREATE TABLE roles (
 id int not null auto_increment,
 name varchar(30) not null unique,
 primary key (id));


 CREATE TABLE users_has_roles (
  users_id INT NOT NULL,
  roles_id INT NOT NULL,
  PRIMARY KEY (users_id, roles_id));


create TABLE orders (
  id INT NOT NULL AUTO_INCREMENT,
  users_id INT NOT NULL,
  cost INT NOT NULL,
  buy_date timestamp NOT NULL,
  PRIMARY KEY (id));

create TABLE gift_certificates_has_orders (
  gift_certificates_id INT NOT NULL,
  orders_id INT NOT NULL,
  PRIMARY KEY (gift_certificates_id, orders_id));
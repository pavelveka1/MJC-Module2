
CREATE TABLE gift_certificates (
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
CREATE TABLE tags (
  tag_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL unique );


-- -----------------------------------------------------
-- Table `mydb`.`gift_sertificates_has_tags`
-- -----------------------------------------------------
CREATE TABLE gift_certificates_has_tags (
  gift_certificates_id INT NOT NULL,
  tags_id INT NOT NULL,
  PRIMARY KEY (gift_certificates_id, tags_id));


CREATE TABLE users (
  users_id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(30) NOT NULL ,
  last_name VARCHAR(30) NOT NULL,
  PRIMARY KEY (users_id));


CREATE TABLE orders (
  id INT NOT NULL AUTO_INCREMENT,
  users_id INT NOT NULL,
  cost INT NOT NULL,
  buy_date timestamp NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE gift_certificates_has_orders (
 gift_certificates_id INT NOT NULL,
  orders_id INT NOT NULL,
  PRIMARY KEY (gift_certificates_id, orders_id));
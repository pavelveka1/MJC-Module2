
CREATE TABLE gift_certificates (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(45) NOT NULL unique,
  description VARCHAR(300) NOT NULL unique,
  price INT NOT NULL,
  duration INT NOT NULL,
  create_date TIMESTAMP not null ,
  last_update_date timestamp not null
  );


-- -----------------------------------------------------
-- Table `mydb`.`tags`
-- -----------------------------------------------------
CREATE TABLE tags (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL unique );


-- -----------------------------------------------------
-- Table `mydb`.`gift_sertificates_has_tags`
-- -----------------------------------------------------
CREATE TABLE gift_certificates_has_tags (
  gift_certificates_id INT NOT NULL,
  tags_id INT NOT NULL,
  PRIMARY KEY (gift_certificates_id, tags_id));



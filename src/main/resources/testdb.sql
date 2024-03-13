SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;  
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;  
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';  
   
-- -----------------------------------------------------  
-- Schema mydb  
-- -----------------------------------------------------  
-- -----------------------------------------------------  
-- Schema TestDb
-- -----------------------------------------------------  

DROP SCHEMA IF EXISTS `TestDb` ; 

CREATE SCHEMA IF NOT EXISTS `TestDb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;  
USE `TestDb` ;

  CREATE TABLE IF NOT EXISTS `TestDb`.`users` (  
  `user_id` INT NOT NULL AUTO_INCREMENT,  
  `username` VARCHAR(255) NOT NULL UNIQUE,  
  
  PRIMARY KEY (`user_id`))  
ENGINE = InnoDB  
DEFAULT CHARACTER SET = utf8mb4  
COLLATE = utf8mb4_0900_ai_ci;

  CREATE TABLE IF NOT EXISTS `TestDb`.`blogs` (  
  `blog_id` INT NOT NULL AUTO_INCREMENT,  
  `title` VARCHAR(255) NOT NULL UNIQUE,
  `user_id` INT,
  
  PRIMARY KEY (`blog_id`),
  CONSTRAINT FOREIGN KEY(`user_id`)
  REFERENCES users(user_id))  
ENGINE = InnoDB  
DEFAULT CHARACTER SET = utf8mb4  
COLLATE = utf8mb4_0900_ai_ci; 

  CREATE TABLE IF NOT EXISTS `TestDb`.`replies` (  
  `reply_id` INT NOT NULL AUTO_INCREMENT,  
  `title` VARCHAR(255) NOT NULL UNIQUE,
  `user_id` INT,
  `blog_id` INT,
  
  PRIMARY KEY (`reply_id`),
  CONSTRAINT FOREIGN KEY(`user_id`)
  REFERENCES users(user_id),
  CONSTRAINT foreign key(`blog_id`)
  REFERENCES blogs(blog_id))
ENGINE = InnoDB  
DEFAULT CHARACTER SET = utf8mb4  
COLLATE = utf8mb4_0900_ai_ci; 

-- insert statements
INSERT INTO users (user_id, username) VALUES (1, 'user1');
INSERT INTO users (user_id, username) VALUES (2, 'user2');
INSERT INTO users (user_id, username) VALUES (3, 'user3');

INSERT INTO blogs (blog_id, title, user_id) VALUES (1, 'blog1', 1);
INSERT INTO blogs (blog_id, title, user_id) VALUES (2, 'blog2', 2);
INSERT INTO blogs (blog_id, title, user_id) VALUES (3, 'blog3', 3);
INSERT INTO blogs (blog_id, title, user_id) VALUES (4, 'blog4', 3);

INSERT INTO replies (reply_id, title, user_id, blog_id) VALUES (1, 'comment1', 1, 2);
INSERT INTO replies (reply_id, title, user_id, blog_id) VALUES (2, 'comment2', 2, 1);
INSERT INTO replies (reply_id, title, user_id, blog_id) VALUES (3, 'comment3', 2, 3);
INSERT INTO replies (reply_id, title, user_id, blog_id) VALUES (4, 'comment4', 2, 4);



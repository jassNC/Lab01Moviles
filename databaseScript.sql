-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema tourdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tourdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tourdb` DEFAULT CHARACTER SET utf8 ;
USE `tourdb` ;

-- -----------------------------------------------------
-- Table `tourdb`.`ACTIVITY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tourdb`.`ACTIVITY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `BODY` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `BODY_UNIQUE` (`BODY` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tourdb`.`CATEGORY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tourdb`.`CATEGORY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `NAME_UNIQUE` (`NAME` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tourdb`.`COUNTRY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tourdb`.`COUNTRY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `NAME_UNIQUE` (`NAME` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tourdb`.`TOUR`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tourdb`.`TOUR` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NOT NULL,
  `DESCRIPTION` VARCHAR(300) NOT NULL,
  `RATING` FLOAT NOT NULL,
  `LEAVE_DATE` DATETIME NOT NULL,
  `RETURN_DATE` DATETIME NOT NULL,
  `PRICE` DOUBLE NOT NULL,
  `SEATS` INT NOT NULL,
  `CATEGORY_FK` INT NOT NULL,
  `COUNTRY_FK` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_TOUR_CATEGORY1_idx` (`CATEGORY_FK` ASC) VISIBLE,
  INDEX `fk_TOUR_COUNTRY1_idx` (`COUNTRY_FK` ASC) VISIBLE,
  CONSTRAINT `fk_TOUR_CATEGORY1`
    FOREIGN KEY (`CATEGORY_FK`)
    REFERENCES `tourdb`.`CATEGORY` (`ID`),
  CONSTRAINT `fk_TOUR_COUNTRY1`
    FOREIGN KEY (`COUNTRY_FK`)
    REFERENCES `tourdb`.`COUNTRY` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tourdb`.`FEATURE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tourdb`.`FEATURE` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `TOUR_FK` INT NOT NULL,
  `ACTIVITY_FK` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_INCLUDE_TOUR1_idx` (`TOUR_FK` ASC) VISIBLE,
  INDEX `fk_FEATURE_ACTIVITY1_idx` (`ACTIVITY_FK` ASC) VISIBLE,
  CONSTRAINT `fk_FEATURE_ACTIVITY1`
    FOREIGN KEY (`ACTIVITY_FK`)
    REFERENCES `tourdb`.`ACTIVITY` (`ID`),
  CONSTRAINT `fk_INCLUDE_TOUR1`
    FOREIGN KEY (`TOUR_FK`)
    REFERENCES `tourdb`.`TOUR` (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tourdb`.`USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tourdb`.`USER` (
  `ID` INT NOT NULL,
  `NAME` VARCHAR(45) NOT NULL,
  `EMAIL` VARCHAR(45) NOT NULL,
  `BIRTH_DATE` DATE NOT NULL,
  `PASSWORD` VARCHAR(45) NOT NULL,
  `ADMIN` TINYINT NOT NULL DEFAULT 0,
  `COUNTRY_FK` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `EMAIL_UNIQUE` (`EMAIL` ASC) VISIBLE,
  INDEX `fk_USER_COUNTRY1_idx` (`COUNTRY_FK` ASC) VISIBLE,
  CONSTRAINT `fk_USER_COUNTRY1`
    FOREIGN KEY (`COUNTRY_FK`)
    REFERENCES `tourdb`.`COUNTRY` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tourdb`.`RESERVATION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tourdb`.`RESERVATION` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `SEATS` INT NOT NULL,
  `USER_FK` INT NOT NULL,
  `TOUR_FK` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_RESERVATION_USER1_idx` (`USER_FK` ASC) VISIBLE,
  INDEX `fk_RESERVATION_TOUR1_idx` (`TOUR_FK` ASC) VISIBLE,
  CONSTRAINT `fk_RESERVATION_TOUR1`
    FOREIGN KEY (`TOUR_FK`)
    REFERENCES `tourdb`.`TOUR` (`ID`),
  CONSTRAINT `fk_RESERVATION_USER1`
    FOREIGN KEY (`USER_FK`)
    REFERENCES `tourdb`.`USER` (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tourdb`.`REVIEW`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tourdb`.`REVIEW` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `BODY` VARCHAR(45) NOT NULL,
  `TOUR_FK` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_OPINION_TOUR1_idx` (`TOUR_FK` ASC) VISIBLE,
  CONSTRAINT `fk_OPINION_TOUR1`
    FOREIGN KEY (`TOUR_FK`)
    REFERENCES `tourdb`.`TOUR` (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `tourdb`.`IMAGE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tourdb`.`IMAGE` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `LINK` VARCHAR(500) NOT NULL,
  `TOUR_FK` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_IMAGE_TOUR1_idx` (`TOUR_FK` ASC) VISIBLE,
  CONSTRAINT `fk_IMAGE_TOUR1`
    FOREIGN KEY (`TOUR_FK`)
    REFERENCES `tourdb`.`TOUR` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourdb`.`FAVORITE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tourdb`.`FAVORITE` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `TOUR_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  INDEX `fk_FAVORITES_TOUR1_idx` (`TOUR_ID` ASC) VISIBLE,
  INDEX `fk_FAVORITES_USER1_idx` (`USER_ID` ASC) VISIBLE,
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk_FAVORITES_TOUR1`
    FOREIGN KEY (`TOUR_ID`)
    REFERENCES `tourdb`.`TOUR` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FAVORITES_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `tourdb`.`USER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

CREATE DATABASE  IF NOT EXISTS `gamejam` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gamejam`;
-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: gamejam
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account_achievements`
--

DROP TABLE IF EXISTS `account_achievements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_achievements` (
  `accountid` int(11) NOT NULL,
  `achieveid` int(11) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`accountid`,`achieveid`),
  KEY `aa_achieveid_FK` (`achieveid`),
  CONSTRAINT `aa_accountid_FK` FOREIGN KEY (`accountid`) REFERENCES `accounts` (`accountid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `aa_achieveid_FK` FOREIGN KEY (`achieveid`) REFERENCES `achievements` (`achieveid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_achievements`
--

LOCK TABLES `account_achievements` WRITE;
/*!40000 ALTER TABLE `account_achievements` DISABLE KEYS */;
INSERT INTO `account_achievements` VALUES (3,1,'2019-11-18 18:09:33'),(3,3,'2019-11-18 18:10:57');
/*!40000 ALTER TABLE `account_achievements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `accountid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `exp` int(11) DEFAULT '0',
  `level` int(11) DEFAULT '1',
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `guest` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`accountid`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  CONSTRAINT `accounts_chk_1` CHECK ((`exp` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,'admin','admin',0,1,1,0),(2,'guest','guest',0,1,0,1),(3,'test','test',358,2,0,0);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `achievements`
--

DROP TABLE IF EXISTS `achievements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `achievements` (
  `achieveid` int(11) NOT NULL AUTO_INCREMENT,
  `gameid` int(11) DEFAULT NULL,
  `description` varchar(256) NOT NULL,
  `condition` varchar(5) NOT NULL,
  `amount` int(11) NOT NULL,
  `exp` int(11) DEFAULT '0',
  `iconpath` varchar(128) NOT NULL,
  PRIMARY KEY (`achieveid`),
  KEY `gameid_idx` (`gameid`),
  CONSTRAINT `achievements_gameid_FK` FOREIGN KEY (`gameid`) REFERENCES `games` (`gameid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `achievements_chk_1` CHECK ((`condition` in (_utf8mb4'play',_utf8mb4'win',_utf8mb4'score'))),
  CONSTRAINT `achievements_chk_2` CHECK ((`exp` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `achievements`
--

LOCK TABLES `achievements` WRITE;
/*!40000 ALTER TABLE `achievements` DISABLE KEYS */;
INSERT INTO `achievements` VALUES (1,1,'Play 5 games of Tic-Tac-Toe','play',5,0,'/TTTPlay5.png'),(2,1,'Win 5 games of Tic-Tac-Toe','win',5,0,'/TTTWin5.png'),(3,1,'Play 10 games of Tic-Tac-Toe','play',10,0,'/TTTPlay10.png'),(4,1,'Win 10 games of Tic-Tac-Toe','win',10,0,'/TTTWin10.png');
/*!40000 ALTER TABLE `achievements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gamelog`
--

DROP TABLE IF EXISTS `gamelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gamelog` (
  `logid` int(11) NOT NULL AUTO_INCREMENT,
  `statsid` int(11) NOT NULL,
  `win` tinyint(1) NOT NULL DEFAULT '0',
  `loss` tinyint(1) NOT NULL DEFAULT '0',
  `tie` tinyint(1) NOT NULL DEFAULT '0',
  `incomplete` tinyint(1) NOT NULL DEFAULT '0',
  `timeplayed` time NOT NULL DEFAULT '00:00:00',
  `score` int(11) NOT NULL DEFAULT '0',
  `date` datetime NOT NULL,
  PRIMARY KEY (`logid`),
  KEY `accountid_idx` (`statsid`),
  CONSTRAINT `statsid` FOREIGN KEY (`statsid`) REFERENCES `statistics` (`statsid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gamelog`
--

LOCK TABLES `gamelog` WRITE;
/*!40000 ALTER TABLE `gamelog` DISABLE KEYS */;
INSERT INTO `gamelog` VALUES (80,1,1,0,0,0,'00:00:01',108,'2019-11-14 10:27:10'),(81,1,0,0,1,0,'00:00:00',52,'2019-11-15 11:02:13'),(82,1,1,0,0,0,'00:00:01',108,'2019-11-15 11:06:41'),(83,2,0,1,0,0,'00:00:00',104,'2019-11-15 11:14:56'),(86,1,0,0,1,0,'00:00:00',52,'2019-11-18 17:40:10'),(87,1,1,0,0,0,'00:00:01',108,'2019-11-18 17:40:23'),(88,1,0,0,1,0,'00:00:00',52,'2019-11-18 18:10:05'),(89,1,0,0,1,0,'00:00:00',52,'2019-11-18 18:10:21'),(90,1,1,0,0,0,'00:00:01',108,'2019-11-18 18:10:34'),(91,1,0,0,1,0,'00:00:00',52,'2019-11-18 18:10:49'),(92,1,0,0,1,0,'00:00:00',52,'2019-11-18 18:10:57');
/*!40000 ALTER TABLE `gamelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games` (
  `gameid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `iconpath` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`gameid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
INSERT INTO `games` VALUES (1,'Tic-Tac-Toe','/tictactoeicon.png'),(2,'Connect-Four','/connectFourIcon.png'),(3,'Battleship','/battleshipIcon.png'),(4,'Space Shooter','/spaceShooterIcon.png');
/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statistics`
--

DROP TABLE IF EXISTS `statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statistics` (
  `statsid` int(11) NOT NULL AUTO_INCREMENT,
  `accountid` int(11) NOT NULL,
  `gameid` int(11) NOT NULL,
  `wins` int(11) DEFAULT '0',
  `losses` int(11) DEFAULT '0',
  `ties` int(11) DEFAULT '0',
  `incomplete` int(11) DEFAULT '0',
  `timeplayed` time DEFAULT '00:00:00',
  PRIMARY KEY (`statsid`),
  KEY `accountid_idx` (`accountid`),
  KEY `gameid_idx` (`gameid`),
  CONSTRAINT `accountid` FOREIGN KEY (`accountid`) REFERENCES `accounts` (`accountid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `gameid` FOREIGN KEY (`gameid`) REFERENCES `games` (`gameid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=290 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statistics`
--

LOCK TABLES `statistics` WRITE;
/*!40000 ALTER TABLE `statistics` DISABLE KEYS */;
INSERT INTO `statistics` VALUES (1,3,1,4,0,6,0,'00:00:00'),(2,3,2,0,1,0,0,'00:00:00'),(276,3,3,0,0,0,0,'00:00:00'),(289,3,4,0,0,0,0,'00:00:00');
/*!40000 ALTER TABLE `statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `themes`
--

DROP TABLE IF EXISTS `themes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `themes` (
  `themeid` int(11) NOT NULL AUTO_INCREMENT,
  `accountid` int(11) NOT NULL,
  `themename` varchar(256) NOT NULL,
  `themepath` varchar(128) NOT NULL,
  PRIMARY KEY (`themeid`),
  UNIQUE KEY `themename_UNIQUE` (`themename`),
  UNIQUE KEY `themepath_UNIQUE` (`themepath`),
  KEY `accountid_themes_FK_idx` (`accountid`),
  CONSTRAINT `accountid_themes_FK` FOREIGN KEY (`accountid`) REFERENCES `accounts` (`accountid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `themes`
--

LOCK TABLES `themes` WRITE;
/*!40000 ALTER TABLE `themes` DISABLE KEYS */;
/*!40000 ALTER TABLE `themes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-12-03 12:50:50

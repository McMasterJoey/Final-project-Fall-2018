-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: gamejam
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `accounts` (
  `accountid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `exp` int(11) DEFAULT '0',
  `level` int(11) DEFAULT '1',
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `guest` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`accountid`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (7,'guest','guest',0,1,0,1),(10,'admin','admin',0,1,1,0),(26,'test','test',0,1,0,0),(51,'john','john',0,1,0,0),(52,'adam','adam',0,1,0,0);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gamelog`
--

DROP TABLE IF EXISTS `gamelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gamelog` (
  `logid` int(11) NOT NULL AUTO_INCREMENT,
  `statsid` int(11) NOT NULL,
  `win` tinyint(1) NOT NULL DEFAULT '0',
  `loss` tinyint(1) NOT NULL DEFAULT '0',
  `tie` tinyint(1) NOT NULL DEFAULT '0',
  `incomplete` tinyint(1) NOT NULL DEFAULT '0',
  `timeplayed` time NOT NULL DEFAULT '00:00:00',
  `score` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`logid`),
  KEY `accountid_idx` (`statsid`),
  CONSTRAINT `statsid` FOREIGN KEY (`statsid`) REFERENCES `statistics` (`statsid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gamelog`
--

LOCK TABLES `gamelog` WRITE;
/*!40000 ALTER TABLE `gamelog` DISABLE KEYS */;
INSERT INTO `gamelog` VALUES (5,14,1,0,0,0,'00:00:01',1),(6,14,1,0,0,0,'00:00:01',2),(7,15,1,0,0,0,'00:00:00',0),(8,14,1,0,0,0,'00:00:01',3),(9,15,0,0,1,0,'00:00:00',4),(10,15,0,1,0,0,'00:00:00',5),(11,14,0,0,0,1,'00:00:01',6),(12,52,1,0,0,0,'00:00:01',7),(13,52,1,0,0,0,'00:00:01',8),(14,52,0,0,1,0,'00:00:00',9),(15,53,1,0,0,0,'00:00:00',10),(16,54,1,0,0,0,'00:00:01',11),(17,54,0,0,1,0,'00:00:00',12),(18,54,0,1,0,0,'00:00:01',13),(19,55,0,1,0,0,'00:00:00',14),(20,55,0,1,0,0,'00:00:00',15),(21,52,1,0,0,0,'00:00:01',0),(22,52,1,0,0,0,'00:00:01',0),(23,54,1,0,0,0,'00:00:01',0),(24,54,1,0,0,0,'00:00:01',0),(25,14,0,0,1,0,'00:00:00',0),(26,14,0,0,0,1,'00:00:01',0),(27,14,0,0,1,0,'00:00:00',52),(28,14,1,0,0,0,'00:00:01',108),(29,15,0,0,1,0,'00:00:00',220),(30,15,1,0,0,0,'00:00:00',200),(31,14,1,0,0,0,'00:00:01',108),(32,52,1,0,0,0,'00:00:01',108);
/*!40000 ALTER TABLE `gamelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `games` (
  `gameid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `iconpath` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`gameid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
INSERT INTO `games` VALUES (1,'Tic-Tac-Toe','/tictactoeicon.png'),(2,'Connect-Four','/connectFourIcon.png'),(3,'Battleship','/battleshipIcon.png');
/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statistics`
--

DROP TABLE IF EXISTS `statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statistics`
--

LOCK TABLES `statistics` WRITE;
/*!40000 ALTER TABLE `statistics` DISABLE KEYS */;
INSERT INTO `statistics` VALUES (14,26,1,5,0,2,2,'00:00:00'),(15,26,2,2,1,2,0,'00:00:00'),(52,51,1,5,0,1,0,'00:00:00'),(53,51,2,1,0,0,0,'00:00:00'),(54,52,1,3,1,1,0,'00:00:00'),(55,52,2,0,2,0,0,'00:00:00');
/*!40000 ALTER TABLE `statistics` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-11-04 15:45:24

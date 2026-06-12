/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.8.6-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: staffdb
-- ------------------------------------------------------
-- Server version	11.8.6-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `attendanceId` int(11) NOT NULL AUTO_INCREMENT,
  `staffId` int(11) NOT NULL,
  `workDate` date NOT NULL,
  `checkInTime` timestamp NULL DEFAULT NULL,
  `checkOutTime` timestamp NULL DEFAULT NULL,
  `status` varchar(50) DEFAULT 'PRESENT',
  PRIMARY KEY (`attendanceId`),
  KEY `staffId` (`staffId`),
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`staffId`) REFERENCES `staff` (`staffId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES
(1,24,'2026-06-12','2026-06-12 09:59:24','2026-06-12 09:59:34','PRESENT'),
(2,23,'2026-06-12','2026-06-12 10:13:50','2026-06-12 11:36:28','PRESENT'),
(3,21,'2026-06-12','2026-06-12 11:50:10','2026-06-12 11:50:40','PRESENT');
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `departmentId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  PRIMARY KEY (`departmentId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES
(1,'ict_dpt','ict'),
(2,'hr','hr'),
(3,'agri','specializes with agriculture issues'),
(4,'the goat','gat drunk'),
(5,'safety','make workers safe at work'),
(6,'hotel','management');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `roleId` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES
(1,'db-admin','database adminstration'),
(2,'ict-surpport','server-side-operations'),
(3,'cuban','cuban people'),
(4,'dj','disk jockey'),
(5,'sales manager','sales'),
(6,'ux/ui','website designing');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `staffId` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(100) NOT NULL,
  `lastName` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `hireDate` date DEFAULT NULL,
  `departmentId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  `status` varchar(100) DEFAULT 'Active',
  PRIMARY KEY (`staffId`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_department` (`departmentId`),
  KEY `fk_role` (`roleId`),
  CONSTRAINT `fk_department` FOREIGN KEY (`departmentId`) REFERENCES `department` (`departmentId`),
  CONSTRAINT `fk_role` FOREIGN KEY (`roleId`) REFERENCES `roles` (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES
(15,'caleb','mutinda','mutidac996@gmail.com','0793303747','2026-06-02',1,2,'ACTIVE'),
(16,'admin','admin','admin@mspace.co.ke','0734444455','2026-06-01',1,1,'ACTIVE'),
(17,'newton','mwangi','mwangi@mspace.co.ke','0734452345','2026-06-01',1,1,'ACTIVE'),
(19,'Daniel','Kamau','dankamau@yahoo.com','0756346522','2026-05-01',2,1,'ACTIVE'),
(20,'Jabali','Owen','owenjabal@mspace.co.ke','0799454534','2026-05-04',1,1,'INACTIVE'),
(21,'Purity','Mutuku','purity@gmail.com','0793456783','2026-04-06',1,1,'ACTIVE'),
(22,'Judas','Iscariot','betrayal@lucifer.hell','0766666666','2026-06-02',2,1,'INACTIVE'),
(23,'Museo','Joshua','mutseoc996@gmail.com','0793303748','2026-06-02',1,3,'INACTIVE'),
(24,'Anne','Gitau','anne@yahoo.com','0712345687','2026-05-05',5,5,'ACTIVE'),
(25,'caleb','joshua','mutindac996@gmail.com','0793303745','2026-06-05',1,2,'INACTIVE'),
(27,'mutinda','marj','mutind@gmail.com','0795556554','2026-06-29',1,1,'ACTIVE'),
(28,'vivi','nyambura','vivin@gmail.com','0793333321','2026-06-17',1,1,'ACTIVE'),
(29,'nick','njenga','nick@gmail.com','011456654','2026-06-01',1,1,'ACTIVE'),
(30,'nicki','minaj','nickiminaj@gmail.com','0734343434','2026-06-02',1,1,'ACTIVE');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `useraccount`
--

DROP TABLE IF EXISTS `useraccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `useraccount` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `staffId` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `passwordHash` varchar(255) NOT NULL,
  `role` varchar(100) NOT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `staffId` (`staffId`),
  UNIQUE KEY `username` (`username`),
  CONSTRAINT `fk_staff` FOREIGN KEY (`staffId`) REFERENCES `staff` (`staffId`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `useraccount` WRITE;
/*!40000 ALTER TABLE `useraccount` DISABLE KEYS */;
INSERT INTO `useraccount` VALUES
(1,16,'admin','@Server','Admin'),
(2,24,'anee','anee','Staff'),
(4,23,'museo','joshua','Staff'),
(5,21,'purity','12345','Staff'),
(6,15,'caleb','caleb','Admin'),
(7,22,'juda','devil','Staff'),
(8,28,'vivi','vivi','Staff'),
(9,29,'nick','nick','Staff'),
(10,30,'minaji','minaji','Staff');
/*!40000 ALTER TABLE `useraccount` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-06-12 15:16:51

-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 07, 2011 at 09:13 
-- Server version: 5.1.41
-- PHP Version: 5.3.1


SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `monitor`
--

-- --------------------------------------------------------

--
-- Table structure for table `actions`
--
DROP TABLE IF EXISTS `actions`;
CREATE TABLE IF NOT EXISTS `actions` (
  `ID_ACTION` int(10) unsigned NOT NULL,
  `ID_JOB` int(10) unsigned NOT NULL,
  `ID_ACTION_TYPE` int(10) unsigned NOT NULL,
  `TARGET` varchar(255) DEFAULT NULL,
  `LANGUAGE` char(2) DEFAULT NULL,
  PRIMARY KEY (`ID_ACTION`),
  KEY `FK_ACTION_JOB` (`ID_JOB`),
  KEY `FK_ACTION_TYPE` (`ID_ACTION_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

--
-- Table structure for table `query_test_result`
--
DROP TABLE IF EXISTS `query_test_result`;
CREATE TABLE `query_test_result` (
  `ID_QUERY` int(11) unsigned NOT NULL,
  `DATA` mediumblob,
  `CONTENT_TYPE` varchar(45) DEFAULT NULL,
  `XPATH_RESULT` varchar(200) DEFAULT NULL,
  `RESPONSE_DELAY` float DEFAULT '0',
  `RESPONSE_SIZE` INTEGER DEFAULT 0,
  PRIMARY KEY (`ID_QUERY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `action_types`
--
DROP TABLE IF EXISTS `action_types`;
CREATE TABLE IF NOT EXISTS `action_types` (
  `ID_ACTION_TYPE` int(10) unsigned NOT NULL,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_ACTION_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

INSERT INTO `action_types` VALUES ('1', 'E-MAIL');
INSERT INTO `action_types` VALUES ('2', 'RSS');
-- --------------------------------------------------------

--
-- Table structure for table `alerts`
--
DROP TABLE IF EXISTS `alerts`;
CREATE TABLE IF NOT EXISTS `alerts` (
  `ID_ALERT` int(10) unsigned NOT NULL,
  `ID_JOB` int(10) unsigned NOT NULL,
  `ID_OLD_STATUS` int(10) unsigned NOT NULL,
  `ID_NEW_STATUS` int(10) unsigned NOT NULL,
  `CAUSE` text NOT NULL,
  `ALERT_DATE_TIME` datetime NOT NULL,
  `EXPOSE_RSS` tinyint(1) NOT NULL,
  `RESPONSE_DELAY` float NOT NULL,
  `HTTP_CODE` int(10) unsigned DEFAULT NULL,
  `IMAGE` mediumblob,
  `CONTENT_TYPE` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID_ALERT`),
  KEY `FK_ALERTS_JOB` (`ID_JOB`),
  KEY `FK_ALERTS_OLD_STATUS` (`ID_OLD_STATUS`),
  KEY `FK_ALERTS_NEW_STATUS` (`ID_NEW_STATUS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Table structure for table `holidays`
--
DROP TABLE IF EXISTS `holidays`;
CREATE TABLE IF NOT EXISTS `holidays` (
  `ID_HOLIDAYS` int(10) unsigned NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `DATE` datetime NOT NULL,
  PRIMARY KEY (`ID_HOLIDAYS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `http_methods`
--
DROP TABLE IF EXISTS `http_methods`;
CREATE TABLE IF NOT EXISTS `http_methods` (
  `ID_HTTP_METHOD` int(10) unsigned NOT NULL,
  `NAME` varchar(10) NOT NULL,
  PRIMARY KEY (`ID_HTTP_METHOD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

INSERT INTO `http_methods` VALUES ('1', 'GET');
INSERT INTO `http_methods` VALUES ('2', 'POST');

-- --------------------------------------------------------

--
-- Table structure for table `jobs`
--
DROP TABLE IF EXISTS `jobs`;
CREATE TABLE IF NOT EXISTS `jobs` (
  `ID_JOB` int(10) unsigned NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `ID_SERVICE_TYPE` int(10) unsigned NOT NULL,
  `SERVICE_URL` varchar(255),
  `ID_HTTP_METHOD` int(10) unsigned NOT NULL,
  `TEST_INTERVAL` int(10) unsigned NOT NULL,
  `TIMEOUT` int(10) unsigned NOT NULL,
  `BUSINESS_ERRORS` tinyint(1) NOT NULL DEFAULT '0',
  `SLA_START_TIME` datetime,
  `LOGIN` varchar(45) DEFAULT NULL,
  `PASSWORD` varchar(45) DEFAULT NULL,
  `IS_PUBLIC` tinyint(1) NOT NULL DEFAULT '0',
  `IS_AUTOMATIC` tinyint(1) NOT NULL DEFAULT '0',
  `ALLOWS_REALTIME` tinyint(1) NOT NULL DEFAULT '0',
  `TRIGGERS_ALERTS` tinyint(1) NOT NULL DEFAULT '0',
  `ID_STATUS` int(10) unsigned NOT NULL DEFAULT '4',
  `HTTP_ERRORS` tinyint(1) NOT NULL DEFAULT '0',
  `SLA_END_TIME` datetime,
  `STATUS_UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `SAVE_RESPONSE` tinyint(1) NOT NULL DEFAULT '0',
  `RUN_SIMULTANEOUS` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID_JOB`),
  UNIQUE KEY `UNIQUE_NAME` (`NAME`) USING BTREE,
  KEY `FK_JOBS_SERVICE_TYPE` (`ID_SERVICE_TYPE`),
  KEY `FK_JOBS_HTTP_METHOD` (`ID_HTTP_METHOD`),
  KEY `FK_JOBS_STATUS` (`ID_STATUS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `job_agg_hour_log_entries`
--
DROP TABLE IF EXISTS `job_agg_hour_log_entries`;
CREATE TABLE IF NOT EXISTS `job_agg_hour_log_entries` (
  `DATE_LOG` datetime NOT NULL,
  `ID_JOB` int(10) unsigned NOT NULL,
  `H1_MEAN_RESP_TIME` float NOT NULL,
  `H1_MEAN_RESP_TIME_INSPIRE` float NOT NULL,
  `H1_AVAILABILITY` float NOT NULL,
  `H1_AVAILABILITY_INSPIRE` float NOT NULL,
  `H1_NB_BIZ_ERRORS` int(10) unsigned NOT NULL,
  `H1_NB_BIZ_ERRORS_INSPIRE` int(10) unsigned NOT NULL,
  `H1_NB_CONN_ERRORS` int(10) unsigned NOT NULL,
  `H1_NB_CONN_ERRORS_INSPIRE` int(10) unsigned NOT NULL,
  `H1_MAX_RESP_TIME` float NOT NULL DEFAULT '0',
  `H1_MIN_RESP_TIME` float NOT NULL DEFAULT '0',
  `H1_MAX_RESP_TIME_INSPIRE` float NOT NULL DEFAULT '0',
  `H1_MIN_RESP_TIME_INSPIRE` float NOT NULL DEFAULT '0',
  `H1_UNAVAILABILITY` float NOT NULL DEFAULT '0',
  `H1_UNAVAILABILITY_INSPIRE` float NOT NULL DEFAULT '0',
  `H1_FAILURE` float NOT NULL DEFAULT '0',
  `H1_FAILURE_INSPIRE` float NOT NULL DEFAULT '0',
  `H1_UNTESTED` float NOT NULL DEFAULT '0',
  `H1_UNTESTED_INSPIRE` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`DATE_LOG`,`ID_JOB`),
  KEY `FK_JOB_AGG_HOUR_LOG_ENTRIES_JOB` (`ID_JOB`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `job_agg_log_entries`
--
DROP TABLE IF EXISTS `job_agg_log_entries`;
CREATE TABLE IF NOT EXISTS `job_agg_log_entries` (
  `DATE_LOG` datetime NOT NULL,
  `ID_JOB` int(10) unsigned NOT NULL,
  `SLA_MEAN_RESP_TIME` float NOT NULL,
  `H24_MEAN_RESP_TIME` float NOT NULL,
  `SLA_AVAILABILITY` float NOT NULL,
  `H24_AVAILABILITY` float NOT NULL,
  `SLA_NB_BIZ_ERRORS` int(10) unsigned NOT NULL,
  `H24_NB_BIZ_ERRORS` int(10) unsigned NOT NULL,
  `SLA_NB_CONN_ERRORS` int(10) unsigned NOT NULL,
  `H24_NB_CONN_ERRORS` int(10) unsigned NOT NULL,
  `H24_MAX_RESP_TIME` float NOT NULL DEFAULT '0',
  `H24_MIN_RESP_TIME` float NOT NULL DEFAULT '0',
  `SLA_MAX_RESP_TIME` float NOT NULL DEFAULT '0',
  `SLA_MIN_RESP_TIME` float NOT NULL DEFAULT '0',
  `SLA_UNAVAILABILITY` float NOT NULL DEFAULT '0',
  `H24_UNAVAILABILITY` float NOT NULL DEFAULT '0',
  `SLA_FAILURE` float NOT NULL DEFAULT '0',
  `H24_FAILURE` float NOT NULL DEFAULT '0',
  `SLA_UNTESTED` float NOT NULL DEFAULT '0',
  `H24_UNTESTED` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`DATE_LOG`,`ID_JOB`),
  KEY `FK_JOB_AGG_LOG_ENTRIES_JOB` (`ID_JOB`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `job_defaults`
--
DROP TABLE IF EXISTS `job_defaults`;
CREATE TABLE IF NOT EXISTS `job_defaults` (
  `ID_PARAM` int(10) unsigned NOT NULL,
  `COLUMN_NAME` varchar(45) NOT NULL,
  `STRING_VALUE` varchar(45) DEFAULT NULL,
  `VALUE_TYPE` varchar(20) NOT NULL,
  PRIMARY KEY (`ID_PARAM`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `job_defaults` VALUES ('1', 'IS_PUBLIC', 'false', 'bool');
INSERT INTO `job_defaults` VALUES ('2', 'IS_AUTOMATIC', 'false', 'bool');
INSERT INTO `job_defaults` VALUES ('3', 'ALLOWS_REALTIME', 'true', 'bool');
INSERT INTO `job_defaults` VALUES ('4', 'TRIGGERS_ALERTS', 'false', 'bool');
INSERT INTO `job_defaults` VALUES ('5', 'TEST_INTERVAL', '3600', 'int');
INSERT INTO `job_defaults` VALUES ('6', 'TIMEOUT', '30', 'int');
INSERT INTO `job_defaults` VALUES ('7', 'BUSINESS_ERRORS', 'true', 'bool');
INSERT INTO `job_defaults` VALUES ('8', 'HTTP_ERRORS', 'true', 'bool');
INSERT INTO `job_defaults` VALUES ('9', 'SLA_START_TIME', '08:00:00', 'time');
INSERT INTO `job_defaults` VALUES ('10', 'SLA_END_TIME', '18:00:00', 'time');
INSERT INTO `job_defaults` VALUES ('11', 'RUN_SIMULATANEOUS', 'false', 'bool');
INSERT INTO `job_defaults` VALUES ('12', 'SAVE_RESPONSE', 'false', 'bool');

-- --------------------------------------------------------

--
-- Table structure for table `last_ids`
--
DROP TABLE IF EXISTS `last_ids`;
CREATE TABLE IF NOT EXISTS `last_ids` (
  `TABLE_NAME` varchar(255) NOT NULL,
  `LAST_ID` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`TABLE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

			
-- --------------------------------------------------------

--
-- Table structure for table `last_query_results`
--
DROP TABLE IF EXISTS `last_query_results`;
CREATE TABLE IF NOT EXISTS `last_query_results` (
  `ID_LAST_QUERY_RESULT` int(10) unsigned NOT NULL,
  `ID_QUERY` int(10) unsigned NOT NULL,
  `DATA` mediumblob,
  `XML_RESULT` mediumtext,
  `TEXT_RESULT` mediumtext,
  `PICTURE_URL` varchar(1000) DEFAULT NULL,
  `CONTENT_TYPE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID_LAST_QUERY_RESULT`),
  KEY `FK_LAST_QUERY_QUERY` (`ID_QUERY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `log_entries`
--
DROP TABLE IF EXISTS `log_entries`;
CREATE TABLE IF NOT EXISTS `log_entries` (
  `ID_LOG_ENTRY` int(10) unsigned NOT NULL,
  `ID_QUERY` int(10) unsigned NOT NULL,
  `REQUEST_TIME` datetime NOT NULL,
  `RESPONSE_DELAY` float NOT NULL,
  `MESSAGE` text NOT NULL,
  `ID_STATUS` int(10) unsigned NOT NULL,
  `HTTP_CODE` int(10) unsigned DEFAULT NULL,
  `EXCEPTION_CODE` varchar(100) DEFAULT NULL,
  `RESPONSE_SIZE` INTEGER DEFAULT 0,
  PRIMARY KEY (`ID_LOG_ENTRY`),
  KEY `fk_log_entries_statuses_STATUS` (`ID_STATUS`),
  KEY `FK_LOG_ENTRIES_QUERY` (`ID_QUERY`),
  KEY `IX_LOG_ENTRIES_ID_QUERY_REQUEST_TIME` (`ID_QUERY`,`REQUEST_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Table structure for table `overview_page`
--
DROP TABLE IF EXISTS `overview_page`;
CREATE TABLE IF NOT EXISTS `overview_page` (
  `ID_OVERVIEW_PAGE` int(10) unsigned NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `IS_PUBLIC` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID_OVERVIEW_PAGE`),
  UNIQUE KEY `URL_UNIQUE` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `overview_queries`
--
DROP TABLE IF EXISTS `overview_queries`;
CREATE TABLE IF NOT EXISTS `overview_queries` (
  `ID_OVERVIEW_QUERY` int(10) unsigned NOT NULL,
  `ID_OVERVIEW_PAGE` int(10) unsigned NOT NULL,
  `ID_QUERY` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_OVERVIEW_QUERY`),
  KEY `FK_OVERVIEW_REQ_PAGE` (`ID_OVERVIEW_PAGE`),
  KEY `FK_OW_QUERY_PAGE` (`ID_OVERVIEW_PAGE`),
  KEY `FK_OVERVIEWQUERY_LASTRESULT` (`ID_QUERY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------


--
-- Table structure for table `periods`
--
DROP TABLE IF EXISTS `periods`;
CREATE TABLE IF NOT EXISTS `periods` (
  `ID_PERIODS` int(10) unsigned NOT NULL,
  `ID_SLA` int(10) unsigned NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `MONDAY` tinyint(1) DEFAULT '0',
  `TUESDAY` tinyint(1) DEFAULT '0',
  `WEDNESDAY` tinyint(1) DEFAULT '0',
  `THURSDAY` tinyint(1) DEFAULT '0',
  `FRIDAY` tinyint(1) DEFAULT '0',
  `SATURDAY` tinyint(1) DEFAULT '0',
  `SUNDAY` tinyint(1) DEFAULT '0',
  `HOLIDAYS` tinyint(1) DEFAULT '0',
  `SLA_START_TIME` time NOT NULL,
  `SLA_END_TIME` time NOT NULL,
  `INCLUDE` tinyint(1) DEFAULT '0',
  `DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID_PERIODS`),
  KEY `FK_PERIODS_SLA` (`ID_SLA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `queries`
--
DROP TABLE IF EXISTS `queries`;
CREATE TABLE IF NOT EXISTS `queries` (
  `ID_QUERY` int(10) unsigned NOT NULL,
  `ID_JOB` int(10) unsigned NOT NULL,
  `ID_SERVICE_METHOD` int(10) unsigned NOT NULL,
  `ID_STATUS` int(10) unsigned NOT NULL DEFAULT '4',
  `NAME` varchar(45) NOT NULL,
  `SOAP_URL` varchar(250) DEFAULT NULL,
  `QUERY_METHOD` varchar(20) DEFAULT '',
  `QUERY_SERVICE_TYPE` varchar(20) DEFAULT '',
  `QUERY_URL` VARCHAR(255) NOT NULL,
  `LOGIN` VARCHAR(45) NULL DEFAULT '',
  `PASSWORD` VARCHAR(45) NULL DEFAULT '',
  PRIMARY KEY (`ID_QUERY`),
  KEY `FK_QUERIES_METHOD` (`ID_SERVICE_METHOD`),
  KEY `FK_QUERIES_JOB` (`ID_JOB`),
  KEY `FK_QUERIES_STATUS` (`ID_STATUS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Table structure for table `query_agg_hour_log_entries`
--
DROP TABLE IF EXISTS `query_agg_hour_log_entries`;
CREATE TABLE IF NOT EXISTS `query_agg_hour_log_entries` (
  `DATE_LOG` datetime NOT NULL,
  `ID_QUERY` int(10) unsigned NOT NULL,
  `H1_MEAN_RESP_TIME` float NOT NULL,
  `H1_MEAN_RESP_TIME_INSPIRE` float NOT NULL,
  `H1_AVAILABILITY` float NOT NULL,
  `H1_AVAILABILITY_INSPIRE` float NOT NULL,
  `H1_NB_BIZ_ERRORS` int(10) unsigned NOT NULL,
  `H1_NB_BIZ_ERRORS_INSPIRE` int(10) unsigned NOT NULL,
  `H1_NB_CONN_ERRORS` int(10) unsigned NOT NULL,
  `H1_NB_CONN_ERRORS_INSPIRE` int(10) unsigned NOT NULL,
  `H1_MAX_RESP_TIME` float NOT NULL DEFAULT '0',
  `H1_MIN_RESP_TIME` float NOT NULL DEFAULT '0',
  `H1_MAX_RESP_TIME_INSPIRE` float NOT NULL DEFAULT '0',
  `H1_MIN_RESP_TIME_INSPIRE` float NOT NULL DEFAULT '0',
  `H1_UNAVAILABILITY` float NOT NULL DEFAULT '0',
  `H1_UNAVAILABILITY_INSPIRE` float NOT NULL DEFAULT '0',
  `H1_FAILURE` float NOT NULL DEFAULT '0',
  `H1_FAILURE_INSPIRE` float NOT NULL DEFAULT '0',
  `H1_UNTESTED` float NOT NULL DEFAULT '0',
  `H1_UNTESTED_INSPIRE` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`DATE_LOG`,`ID_QUERY`),
  KEY `FK_QUERY_AGG_HOUR_LOG_ENTRIES_QUERY` (`ID_QUERY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `query_agg_log_entries`
--
DROP TABLE IF EXISTS `query_agg_log_entries`;
CREATE TABLE IF NOT EXISTS `query_agg_log_entries` (
  `DATE_LOG` datetime NOT NULL,
  `ID_QUERY` int(10) unsigned NOT NULL,
  `SLA_MEAN_RESP_TIME` float NOT NULL,
  `H24_MEAN_RESP_TIME` float NOT NULL,
  `SLA_AVAILABILITY` float NOT NULL,
  `H24_AVAILABILITY` float NOT NULL,
  `SLA_NB_BIZ_ERRORS` int(10) unsigned NOT NULL,
  `H24_NB_BIZ_ERRORS` int(10) unsigned NOT NULL,
  `SLA_NB_CONN_ERRORS` int(10) unsigned NOT NULL,
  `H24_NB_CONN_ERRORS` int(10) unsigned NOT NULL,
  `H24_MAX_RESP_TIME` float NOT NULL DEFAULT '0',
  `H24_MIN_RESP_TIME` float NOT NULL DEFAULT '0',
  `SLA_MAX_RESP_TIME` float NOT NULL DEFAULT '0',
  `SLA_MIN_RESP_TIME` float NOT NULL DEFAULT '0',
  `SLA_UNAVAILABILITY` float NOT NULL DEFAULT '0',
  `H24_UNAVAILABILITY` float NOT NULL DEFAULT '0',
  `SLA_FAILURE` float NOT NULL DEFAULT '0',
  `H24_FAILURE` float NOT NULL DEFAULT '0',
  `SLA_UNTESTED` float NOT NULL DEFAULT '0',
  `H24_UNTESTED` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`DATE_LOG`,`ID_QUERY`),
  KEY `FK_QUERY_AGG_LOG_ENTRIES_QUERY` (`ID_QUERY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `query_params`
--
DROP TABLE IF EXISTS `query_params`;
CREATE TABLE IF NOT EXISTS `query_params` (
  `ID_QUERY` int(10) unsigned NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `VALUE` text,
  PRIMARY KEY (`ID_QUERY`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `query_validation_results`
--
DROP TABLE IF EXISTS `query_validation_results`;
CREATE TABLE IF NOT EXISTS `query_validation_results` (
  `ID_QUERY_VALIDATION_RESULT` int(11) NOT NULL,
  `ID_QUERY` int(10) unsigned NOT NULL,
  `SIZE_VALIDATION_RESULT` tinyint(1) DEFAULT NULL,
  `RESPONSE_SIZE` INTEGER DEFAULT 0,
  `TIME_VALIDATION_RESULT` tinyint(1) DEFAULT NULL,
  `DELIVERY_TIME` float DEFAULT NULL,
  `XPATH_VALIDATION_RESULT` tinyint(1) DEFAULT NULL,
  `XPATH_VALIDATION_OUTPUT` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID_QUERY_VALIDATION_RESULT`,`ID_QUERY`),
  KEY `fk_query_validation_results_queries1` (`ID_QUERY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `query_validation_settings`
--
DROP TABLE IF EXISTS `query_validation_settings`;
CREATE TABLE IF NOT EXISTS `query_validation_settings` (
  `ID_QUERY_VALIDATION_SETTINGS` int(10) NOT NULL,
  `ID_QUERY` int(10) unsigned NOT NULL,
  `USE_SIZE_VALIDATION` tinyint(1) NOT NULL DEFAULT '0',
  `NORM_SIZE` INTEGER DEFAULT 0,
  `NORM_SIZE_TOLERANCE` float DEFAULT NULL,
  `USE_TIME_VALIDATION` tinyint(1) NOT NULL DEFAULT '0',
  `NORM_TIME` float DEFAULT NULL,
  `USE_XPATH_VALIDATION` tinyint(1) NOT NULL DEFAULT '0',
  `XPATH_EXPRESSION` varchar(1000) DEFAULT NULL,
  `XPATH_EXPECTED_OUTPUT` varchar(1000) DEFAULT NULL,
  `USE_TEXT_VALIDATION` tinyint(1) NOT NULL DEFAULT '0',
  `TEXT_EXPECTED_MATCH` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID_QUERY_VALIDATION_SETTINGS`,`ID_QUERY`),
  KEY `fk_query_validation_settings_queries1` (`ID_QUERY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--
DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `ID_ROLE` int(10) unsigned NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `RANK` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_ROLE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

	INSERT INTO `roles` VALUES ('1', 'ROLE_ADMIN', '1');
	INSERT INTO `roles` VALUES ('2', 'ROLE_USER', '3');

-- --------------------------------------------------------

--
-- Table structure for table `service_methods`
--
DROP TABLE IF EXISTS `service_methods`;
CREATE TABLE IF NOT EXISTS `service_methods` (
  `ID_SERVICE_METHOD` int(10) unsigned NOT NULL,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_SERVICE_METHOD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

INSERT INTO `service_methods` VALUES ('1', 'GetCapabilities');
INSERT INTO `service_methods` VALUES ('2', 'GetMap');
INSERT INTO `service_methods` VALUES ('3', 'GetFeature');
INSERT INTO `service_methods` VALUES ('4', 'GetRecordById');
INSERT INTO `service_methods` VALUES ('5', 'GetTile');
INSERT INTO `service_methods` VALUES ('6', 'GetRecords');
INSERT INTO `service_methods` VALUES ('7', 'GetCoverage');
INSERT INTO `service_methods` VALUES ('8', 'DescribeSensor');
INSERT INTO `service_methods` VALUES ('9', 'SOAP 1.1');
INSERT INTO `service_methods` VALUES ('10', 'SOAP 1.2');
INSERT INTO `service_methods` VALUES ('11', 'HTTP POST');
INSERT INTO `service_methods` VALUES ('12', 'HTTP GET');
INSERT INTO `service_methods` VALUES ('13', 'FTP');
			
-- --------------------------------------------------------

--
-- Table structure for table `service_types`
--
DROP TABLE IF EXISTS `service_types`;
CREATE TABLE IF NOT EXISTS `service_types` (
  `ID_SERVICE_TYPE` int(10) unsigned NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `VERSION` varchar(10) NOT NULL,
  PRIMARY KEY (`ID_SERVICE_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

INSERT INTO `service_types` VALUES ('1', 'WMS', '1.1.1');
INSERT INTO `service_types` VALUES ('2', 'WFS', '1.1.0');
INSERT INTO `service_types` VALUES ('4', 'WMTS', '1.0.0');
INSERT INTO `service_types` VALUES ('5', 'CSW', '2.0.2');
INSERT INTO `service_types` VALUES ('6', 'SOS', '1.0.0');
INSERT INTO `service_types` VALUES ('7', 'WCS', '1.0.0');
INSERT INTO `service_types` VALUES ('8', 'ALL', '0');

-- --------------------------------------------------------

--
-- Table structure for table `service_types_methods`
--
DROP TABLE IF EXISTS `service_types_methods`;
CREATE TABLE IF NOT EXISTS `service_types_methods` (
  `ID_SERVICE_TYPE` int(10) unsigned NOT NULL,
  `ID_SERVICE_METHOD` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID_SERVICE_TYPE`,`ID_SERVICE_METHOD`),
  KEY `FK_SERVICE_TYPES_METHODS_METHOD` (`ID_SERVICE_METHOD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `service_types_methods` VALUES ('1', '1');
INSERT INTO `service_types_methods` VALUES ('2', '1');
INSERT INTO `service_types_methods` VALUES ('4', '1');
INSERT INTO `service_types_methods` VALUES ('5', '1');
INSERT INTO `service_types_methods` VALUES ('6', '1');
INSERT INTO `service_types_methods` VALUES ('7', '1');
INSERT INTO `service_types_methods` VALUES ('8', '1');
INSERT INTO `service_types_methods` VALUES ('1', '2');
INSERT INTO `service_types_methods` VALUES ('8', '2');
INSERT INTO `service_types_methods` VALUES ('2', '3');
INSERT INTO `service_types_methods` VALUES ('8', '3');
INSERT INTO `service_types_methods` VALUES ('5', '4');
INSERT INTO `service_types_methods` VALUES ('8', '4');
INSERT INTO `service_types_methods` VALUES ('4', '5');
INSERT INTO `service_types_methods` VALUES ('8', '5');
INSERT INTO `service_types_methods` VALUES ('5', '6');
INSERT INTO `service_types_methods` VALUES ('8', '6');
INSERT INTO `service_types_methods` VALUES ('7', '7');
INSERT INTO `service_types_methods` VALUES ('8', '7');
INSERT INTO `service_types_methods` VALUES ('6', '8');
INSERT INTO `service_types_methods` VALUES ('8', '8');
INSERT INTO `service_types_methods` VALUES ('8', '9');
INSERT INTO `service_types_methods` VALUES ('8', '10');
INSERT INTO `service_types_methods` VALUES ('8', '11');
INSERT INTO `service_types_methods` VALUES ('8', '12');
INSERT INTO `service_types_methods` VALUES ('8', '13');

-- --------------------------------------------------------

--
-- Table structure for table `sla`
--
DROP TABLE IF EXISTS `sla`;
CREATE TABLE IF NOT EXISTS `sla` (
  `ID_SLA` int(10) unsigned NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `EXCLUDE_WORST` tinyint(1) DEFAULT '0',
  `MEASURE_TIME_TO_FIRST` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ID_SLA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `statuses`
--
DROP TABLE IF EXISTS `statuses`;
CREATE TABLE IF NOT EXISTS `statuses` (
  `ID_STATUS` int(10) unsigned NOT NULL,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_STATUS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

INSERT INTO `statuses` VALUES ('1', 'AVAILABLE');
INSERT INTO `statuses` VALUES ('2', 'OUT_OF_ORDER');
INSERT INTO `statuses` VALUES ('3', 'UNAVAILABLE');
INSERT INTO `statuses` VALUES ('4', 'NOT_TESTED');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `LOGIN` varchar(45) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `ID_ROLE` int(10) unsigned DEFAULT NULL,
  `EXPIRATION` date DEFAULT NULL,
  `ENABLED` tinyint(1) NOT NULL DEFAULT '1',
  `LOCKED` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`LOGIN`),
  KEY `FK_USERS_ROLE` (`ID_ROLE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `overview_query_view`
--
DROP VIEW IF EXISTS `overview_query_view`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `overview_query_view` AS select (select count(0) AS `count(0)` from `overview_queries` where ((`overview_queries`.`ID_QUERY` = `q`.`ID_QUERY`) and (`overview_queries`.`ID_OVERVIEW_PAGE` = `p`.`ID_OVERVIEW_PAGE`))) AS `QUERY_IS_PUBLIC`,`p`.`ID_OVERVIEW_PAGE` AS `ID_OVERVIEW_PAGE`,`p`.`NAME` AS `NAME_OVERVIEW_PAGE`,`q`.`ID_QUERY` AS `ID_QUERY`,`q`.`NAME` AS `NAME_QUERY`,`l`.`ID_LAST_QUERY_RESULT` AS `ID_LAST_QUERY_RESULT` from ((`queries` `q` left join `last_query_results` `l` on((`q`.`ID_QUERY` = `l`.`ID_QUERY`))) join `overview_page` `p`) where `q`.`ID_JOB` in (select `jobs`.`ID_JOB` AS `ID_JOB` from `jobs` where (`jobs`.`SAVE_RESPONSE` = 1));

--
-- Constraints for dumped tables
--
INSERT INTO `users` VALUES ('Admin', 'adm', '1', null, '1', '0');
INSERT INTO `users` VALUES ('user', 'usr', '2', null, '1', '0');
--
-- Constraints for table `actions`
--
ALTER TABLE `actions`
  ADD CONSTRAINT `FK_ACTION_JOB` FOREIGN KEY (`ID_JOB`) REFERENCES `jobs` (`ID_JOB`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_ACTION_TYPE` FOREIGN KEY (`ID_ACTION_TYPE`) REFERENCES `action_types` (`ID_ACTION_TYPE`);

--
-- Constraints for table `alerts`
--
ALTER TABLE `alerts`
  ADD CONSTRAINT `FK_ALERTS_JOB` FOREIGN KEY (`ID_JOB`) REFERENCES `jobs` (`ID_JOB`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_ALERTS_NEW_STATUS` FOREIGN KEY (`ID_NEW_STATUS`) REFERENCES `statuses` (`ID_STATUS`),
  ADD CONSTRAINT `FK_ALERTS_OLD_STATUS` FOREIGN KEY (`ID_OLD_STATUS`) REFERENCES `statuses` (`ID_STATUS`);

--
-- Constraints for table `jobs`
--
ALTER TABLE `jobs`
  ADD CONSTRAINT `FK_JOBS_HTTP_METHOD` FOREIGN KEY (`ID_HTTP_METHOD`) REFERENCES `http_methods` (`ID_HTTP_METHOD`),
  ADD CONSTRAINT `FK_JOBS_SERVICE_TYPE` FOREIGN KEY (`ID_SERVICE_TYPE`) REFERENCES `service_types` (`ID_SERVICE_TYPE`),
  ADD CONSTRAINT `FK_JOBS_STATUS` FOREIGN KEY (`ID_STATUS`) REFERENCES `statuses` (`ID_STATUS`);

--
-- Constraints for table `job_agg_hour_log_entries`
--
ALTER TABLE `job_agg_hour_log_entries`
  ADD CONSTRAINT `FK_JOB_AGG_HOUR_LOG_ENTRIES_JOB` FOREIGN KEY (`ID_JOB`) REFERENCES `jobs` (`ID_JOB`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `job_agg_log_entries`
--
ALTER TABLE `job_agg_log_entries`
  ADD CONSTRAINT `FK_JOB_AGG_LOG_ENTRIES_JOB` FOREIGN KEY (`ID_JOB`) REFERENCES `jobs` (`ID_JOB`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `last_query_results`
--
ALTER TABLE `last_query_results`
  ADD CONSTRAINT `FK_LAST_QUERY_QUERY` FOREIGN KEY (`ID_QUERY`) REFERENCES `queries` (`ID_QUERY`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `log_entries`
--
ALTER TABLE `log_entries`
  ADD CONSTRAINT `FK_LOG_ENTRIES_QUERY` FOREIGN KEY (`ID_QUERY`) REFERENCES `queries` (`ID_QUERY`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_log_entries_statuses_STATUS` FOREIGN KEY (`ID_STATUS`) REFERENCES `statuses` (`ID_STATUS`);

--
-- Constraints for table `overview_queries`
--
ALTER TABLE `overview_queries`
  ADD CONSTRAINT `FK_OVERVIEWQUERY_QUERY` FOREIGN KEY (`ID_QUERY`) REFERENCES `queries` (`ID_QUERY`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_OW_QUERY_PAGE` FOREIGN KEY (`ID_OVERVIEW_PAGE`) REFERENCES `overview_page` (`ID_OVERVIEW_PAGE`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `periods`
--
ALTER TABLE `periods`
  ADD CONSTRAINT `FK_PERIODS_SLA` FOREIGN KEY (`ID_SLA`) REFERENCES `sla` (`ID_SLA`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `queries`
--
ALTER TABLE `queries`
  ADD CONSTRAINT `FK_QUERIES_JOB` FOREIGN KEY (`ID_JOB`) REFERENCES `jobs` (`ID_JOB`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_QUERIES_METHOD` FOREIGN KEY (`ID_SERVICE_METHOD`) REFERENCES `service_methods` (`ID_SERVICE_METHOD`),
  ADD CONSTRAINT `FK_QUERIES_STATUS` FOREIGN KEY (`ID_STATUS`) REFERENCES `statuses` (`ID_STATUS`);

--
-- Constraints for table `query_agg_hour_log_entries`
--
ALTER TABLE `query_agg_hour_log_entries`
  ADD CONSTRAINT `FK_QUERY_AGG_HOUR_LOG_ENTRIES_QUERY` FOREIGN KEY (`ID_QUERY`) REFERENCES `queries` (`ID_QUERY`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `query_agg_log_entries`
--
ALTER TABLE `query_agg_log_entries`
  ADD CONSTRAINT `FK_QUERY_AGG_LOG_ENTRIES_QUERY` FOREIGN KEY (`ID_QUERY`) REFERENCES `queries` (`ID_QUERY`) ON DELETE CASCADE;

--
-- Constraints for table `query_params`
--
ALTER TABLE `query_params`
  ADD CONSTRAINT `FK_QUERY_PARAMS_QUERY` FOREIGN KEY (`ID_QUERY`) REFERENCES `queries` (`ID_QUERY`) ON DELETE CASCADE;

--
-- Constraints for table `query_validation_results`
--
ALTER TABLE `query_validation_results`
  ADD CONSTRAINT `fk_query_validation_results_queries1` FOREIGN KEY (`ID_QUERY`) REFERENCES `queries` (`ID_QUERY`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `query_validation_settings`
--
ALTER TABLE `query_validation_settings`
  ADD CONSTRAINT `fk_query_validation_settings_queries1` FOREIGN KEY (`ID_QUERY`) REFERENCES `queries` (`ID_QUERY`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `service_types_methods`
--
ALTER TABLE `service_types_methods`
  ADD CONSTRAINT `FK_SERVICE_TYPES_METHODS_METHOD` FOREIGN KEY (`ID_SERVICE_METHOD`) REFERENCES `service_methods` (`ID_SERVICE_METHOD`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_SERVICE_TYPES_METHODS_TYPE` FOREIGN KEY (`ID_SERVICE_TYPE`) REFERENCES `service_types` (`ID_SERVICE_TYPE`) ON DELETE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FK_USERS_ROLE` FOREIGN KEY (`ID_ROLE`) REFERENCES `roles` (`ID_ROLE`) ON DELETE SET NULL;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

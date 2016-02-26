

DROP TABLE IF EXISTS volume;
CREATE TABLE `volume` (
  `volume_id` int(11) NOT NULL AUTO_INCREMENT,
  `volume_identifier` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `startDate` smallint(6) DEFAULT NULL,
  `endDate` smallint(6) DEFAULT NULL,
  `given_name` varchar(255) DEFAULT NULL,
  `family_name` varchar(255) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `filename` varchar(255) DEFAULT NULL,
  UNIQUE KEY `volume_volume_idx` (`volume_id`),
  UNIQUE KEY `volume_identifier` (`volume_identifier`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS section;
CREATE TABLE `section` (
  `section_id` int(11) NOT NULL AUTO_INCREMENT,
  `volume_id` int(11) DEFAULT NULL,
  `section_identifier` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `geographic` varchar(255) DEFAULT NULL,
  `dateCreated` smallint(6) DEFAULT NULL,
  `sectionNumberAsString` varchar(16) DEFAULT NULL,
  `given_name` varchar(255) DEFAULT NULL,
  `family_name` varchar(255) DEFAULT NULL,
  UNIQUE KEY `section_section_idx` (`section_id`),
  UNIQUE KEY `section_identifier` (`section_identifier`),
  KEY `volume_id` (`volume_id`),
  CONSTRAINT `section_ibfk_1` FOREIGN KEY (`volume_id`) REFERENCES `volume` (`volume_id`) ON DELETE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS page;
CREATE TABLE `page` (
  `page_id` int(11) NOT NULL AUTO_INCREMENT,
  `section_id` int(11) DEFAULT NULL,
  `page_number` int(11) DEFAULT NULL,
  `page_identifier` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  UNIQUE KEY `page_page_idx` (`page_id`),
  UNIQUE KEY `page_identifier` (`page_identifier`),
  KEY `FK_page_section_id` (`section_id`),
  CONSTRAINT `FK_page_section_id` FOREIGN KEY (`section_id`) REFERENCES `section` (`section_id`) ON DELETE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


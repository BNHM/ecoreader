

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
  `dateCreated` smallint(6) DEFAULT NULL,
  `sectionNumberAsString` varchar(16) DEFAULT NULL,
  `given_name` varchar(255) DEFAULT NULL,
  `family_name` varchar(255) DEFAULT NULL,
  UNIQUE KEY `section_section_idx` (`section_id`),
  UNIQUE KEY `section_identifier` (`section_identifier`),
  KEY `volume_id` (`volume_id`),
  CONSTRAINT `section_ibfk_1` FOREIGN KEY (`volume_id`) REFERENCES `volume` (`volume_id`) ON DELETE CASCADE
  CONSTRAINT `section_geographic_id` FOREIGN KEY (`geographic_id`) REFERENCES `geographic` (`geographic_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS geographic;
CREATE TABLE `geographic` (
  `geographic_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `geographic` varchar(2083) NOT NULL,
  UNIQUE KEY `geographic_geographic` (`geographic`),
)DROP TABLE IF EXISTS geographic;

CREATE TABLE `sectionGeographies` (
  `geographic_id` int(11) UNSIGNED NOT NULL,
  `section_id` int(11) NOT NULL,
  CONSTRAINT `sectionGeographies_geographic_id` FOREIGN KEY (`geographic_id`) REFERENCES `geographic` (`geographic_id`),
  CONSTRAINT `sectionGeographies_section_id` FOREIGN KEY (`section_id`) REFERENCES `section` (`section_id`) ON DELETE CASCADE,
) ENGINE=InnoDB  DEFAULT CHARSET=utf8String ENGINE=InnoDB  DEFAULT CHARSET=utf8String

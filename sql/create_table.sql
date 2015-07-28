## basic structure of Mysql database to start with
create table `volume` (
`volume_id` int(11) auto_increment,
`volume_identifier` varchar (256),
`type` varchar(256),
`title` varchar(256),
`startDate` smallint,
`endDate` smallint,
`name` varchar(256),
`updated` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
`filename` varchar(256),
UNIQUE KEY `volume_volume_idx` (`volume_id`),
## TODO should this be unique?
UNIQUE KEY `volume_filename` (`filename`)
);

create table `section` (
`section_id` int(11) auto_increment,
 `volume_id` int(11),
  section_identifier varchar(256),
  type varchar(256),
  title varchar(256),
  geographic varchar(256),
  dateCreated smallint,
  sectionNumberAsString varchar(16),
  UNIQUE KEY `section_section_idx` (`section_id`),
  CONSTRAINT `FK_section_volume_id` FOREIGN KEY (`volume_id`) REFERENCES `volume` (`volume_id`) ON DELETE CASCADE
);

create table page (
  page_id int(11) auto_increment,
  section_id int(11),
  page_number int,
  page_identifier varchar(256),
  type varchar(256),
  UNIQUE KEY `page_page_idx` (`page_id`),
  CONSTRAINT `FK_page_section_id` FOREIGN KEY (`section_id`) REFERENCES `section` (`section_id`) ON DELETE CASCADE
);

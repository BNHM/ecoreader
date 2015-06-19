## basic structure of Mysql database to start with
create table volume      {
volume_id  auto increment
volume_identifier           varchar (256)
type varchar(256)
title varchar(256)
startDate datetime,
enddDate datetime,
name varchar(256)
}

create table section {
section_id auto increment
 volume_id (foreign key to volume.volume_id),
  section_identifier varchar(256),
  type varchar(256).
  title varchar(256),
  geographic varchar(256),
  dateCreated datetime,
  sectionNumberAsString varchar(16),
}

create table page {
  page_id auto increment,
  section_id (foreign key to section.section_id),
  page_number int,
  page_identifier varchar(256),
  type varchar(256)
}

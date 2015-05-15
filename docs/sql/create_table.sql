# A proposed Table Structure for ecoReader

DROP TABLE IF EXISTS volumes;
CREATE TABLE volumes (
  volume_id  auto increment int,
  volume_language VARCHAR(255),
  title VARCHAR(255).
  identifier VARCHAR(255),
  date_start datetime,
  date_end datetime,
  author_name  VARCHAR(255)
);

DROP TABLE IF EXISTS sections;
CREATE TABLE sections (
  volume_id auto increment int,
  title VARCHAR(255),
  identifier VARCHAR(255),
  location  TEXT,
  date_created datetime,
  section_number int
);

DROP TABLE IF EXISTS pages;
CREATE TABLE pages (
  page_id auto increment int,
  section_id int,
  page_number int,
  image_file_input_name VARCHAR(255),
  image_file_input_path VARCHAR(255),
  image_location VARCHAR(255)
);
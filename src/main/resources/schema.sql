CREATE TABLE IF NOT EXISTS staff_member (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  last_service DATE
);

CREATE TABLE IF NOT EXISTS day_of_service (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  calendar_day DATE,
  staff_member_id INT, FOREIGN KEY (staff_member_id) REFERENCES staff_member(id)
);
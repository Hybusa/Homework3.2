-- liquibase formatted sql

-- changeset akuznetsov:1

CREATE INDEX student_name_index ON student(name);
CREATE INDEX faculty_color_index ON faculty(color);

--changeset akuznetsov:2

DROP INDEX faculty_color_index;
CREATE INDEX faculty_color_name ON faculty(color, name);

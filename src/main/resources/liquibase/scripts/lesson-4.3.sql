-- liquibase formatted sql

-- changeset akuznetsov:1

CREATE INDEX student_name_index ON student(name);
CREATE INDEX faculty_color_index ON faculty(name,color)
CREATE TABLE Cars (
                      id SERIAL PRIMARY KEY,
                      brand VARCHAR(255) NOT NULL,
                      model VARCHAR(255) NOT NULL,
                      price NUMERIC NOT NULL
);
CREATE TABLE Users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       age INT NOT NULL,
                       has_license BOOLEAN NOT NULL,
                       cars_id INT REFERENCES Cars(id)
);

SELECT student.name, faculty.name
FROM student JOIN faculty ON student.faculty_id = faculty.id;

SELECT s.name
FROM student s
         JOIN avatar a ON a.student_id = s.id
WHERE a.student_id IS NOT NULL;
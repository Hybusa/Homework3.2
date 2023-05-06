ALTER TABLE student ADD CONSTRAINT limit_age CHECK (age > 16);
ALTER TABLE student ADD CONSTRAINT unique_name UNIQUE (name);
ALTER TABLE student ADD CONSTRAINT name_not_null CHECK (name IS NOT NULL);
ALTER TABLE student ALTER COLUMN age SET DEFAULT 20;

ALTER TABLE faculty ADD CONSTRAINT unique_pair_name_color UNIQUE (name, color);

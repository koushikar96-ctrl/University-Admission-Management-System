-- Create database
CREATE DATABASE IF NOT EXISTS admissionsystem;
USE admissionsystem;

-- Table for seat availability
CREATE TABLE IF NOT EXISTS seat_availability (
    department VARCHAR(50) PRIMARY KEY,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL
);

INSERT INTO seat_availability (department, total_seats, available_seats) VALUES
('BE CSE', 60, 60),
('BE MECH', 60, 60),
('BE ECE', 60, 60),
('BE EEE', 60, 60),
('BE CIVIL', 60, 60),
('BTECH IT', 60, 60),
('BTECH AIDS', 60, 60),
('BE BME', 60, 60);

-- Table for personal details
CREATE TABLE IF NOT EXISTS studentpersonaldetails (
    name VARCHAR(50),
    age INT,
    dob VARCHAR(20),
    gender VARCHAR(10),
    father_name VARCHAR(50),
    mother_name VARCHAR(50),
    father_occupation VARCHAR(50),
    mother_occupation VARCHAR(50),
    blood_group VARCHAR(10),
    city_of_residence VARCHAR(50),
    address VARCHAR(200),
    aadhar VARCHAR(12),
    contact VARCHAR(10),
    email VARCHAR(100),
    religion VARCHAR(30),
    caste VARCHAR(30),
    community VARCHAR(30),
    nationality VARCHAR(30),
    rollno VARCHAR(20) PRIMARY KEY,
    department VARCHAR(30)
);

-- Table for educational details
CREATE TABLE IF NOT EXISTS studenteducationaldetails (
    sslc_institute VARCHAR(100),
    hsc_institute VARCHAR(100),
    sslc_year INT,
    hsc_year INT,
    sslc_marks INT,
    hsc_marks INT,
    cutoff INT,
    accomodation_type VARCHAR(20),
    graduation_year INT,
    rollno VARCHAR(20),
    FOREIGN KEY (rollno) REFERENCES studentpersonaldetails(rollno) ON DELETE CASCADE
);

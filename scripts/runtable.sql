show tables;
CREATE TABLE wr_member (
                           user_id VARCHAR(20),
                           user_name VARCHAR(20),
                           phonenumber INT,
                           email_address VARCHAR(200),
                           password VARCHAR(20),
                           recommender_id VARCHAR(20),
                           registration_date DATE,
                           last_certification_date DATE
);

CREATE TABLE Course (
    id               bigint(20)   NOT NULL AUTO_INCREMENT,
    createdAt        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name             varchar(100) NOT NULL,
    code             varchar(50)  NOT NULL,
    instructorEmail  varchar(100) NOT NULL,
    categoryId       bigint(20)   NOT NULL,
    description      text,
    status           enum('ACTIVE', 'INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
    inactivatedAt    datetime     NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_Course_Code UNIQUE (code),
    CONSTRAINT FK_Course_Category FOREIGN KEY (categoryId) REFERENCES Category(id),
    CONSTRAINT FK_Course_Instructor FOREIGN KEY (instructorEmail) REFERENCES User(email)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

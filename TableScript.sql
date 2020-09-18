use seamos_amigos_db;

CREATE TABLE users (
    userID INT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(30),
    lastName VARCHAR(30),
    password VARCHAR(32), -- should be encrypted, CHAR is better if the field is always the same length
    email VARCHAR(64) NOT NULL, -- not null if this is what you will use as a "username"
    PRIMARY KEY (userID)
);

CREATE TABLE personalInfo (
    userID INT NOT NULL,
    gender ENUM ('MALE', 'FEMALE'),
    dateOfBirth DATE,
    phoneNumber VARCHAR(15),
    personalEmail VARCHAR(64), -- may or may not be the same as the email field in the "users" table
    workEmail VARCHAR(64),
    bio TEXT,
    FOREIGN KEY (userID) REFERENCES users (userID)
);

CREATE TABLE relationships (
    userID INT NOT NULL,
    userID2 INT, -- allowed to be null if the user is single or does not specify who they are in a relationship with
    status ENUM ('SINGLE', 'IN A RELATIONSHIP', 'MARRIED', 'IT''S COMPLICATED' /* etc */),
    FOREIGN KEY (userID) REFERENCES users (userID)
);

CREATE TABLE connections (
    userId INT NOT NULL,
    userId2 INT NOT NULL, 
    FOREIGN KEY (userId) REFERENCES users (userId),
    FOREIGN KEY (userId2) REFERENCES users (userId)
);

/* each photo is created here. This way, when a user wants to share a photo,
   we don't have to duplicate each column. We just create another row in
   the "userPhotos" table below that) REFERENCES the same photoID. */
CREATE TABLE photos (
    photoID INT NOT NULL AUTO_INCREMENT,
    url VARCHAR(200),
    caption VARCHAR(200),
    dateOfUpload TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (photoID)
);

CREATE TABLE userPhotos (
    userID INT NOT NULL,
    photoID INT NOT NULL,
    FOREIGN KEY (userID) REFERENCES users (userID),
    FOREIGN KEY (photoID) REFERENCES photos (photoID)
);

/* vidoes, handled exactly the same as photos */
CREATE TABLE videos (
    videoID INT NOT NULL AUTO_INCREMENT,
    url VARCHAR(200),
    caption VARCHAR(200),
    dateOfUpload TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (videoID)
);

CREATE TABLE userVideos (
    userID INT NOT NULL,
    videoID INT NOT NULL,
    FOREIGN KEY (userID) REFERENCES users (userID),
    FOREIGN KEY (videoID) REFERENCES videos (videoID)
);

CREATE TABLE status (
    userID INT NOT NULL,
    status TEXT,
    FOREIGN KEY (userID) REFERENCES users (userID)
);
DROP DATABASE IF EXISTS chat;
CREATE DATABASE chat;
USE chat;
CREATE TABLE user (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);
CREATE TABLE message (
    id INT PRIMARY KEY AUTO_INCREMENT,
    sender VARCHAR(255) NOT NULL,
    receiver VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender) REFERENCES user(username),
    FOREIGN KEY (receiver) REFERENCES user(username)
);

INSERT INTO user (username, password)
VALUES ('Zjuer', '123456');
INSERT INTO user (username, password)
VALUES ('Sender1', '123456');
INSERT INTO user (username, password)
VALUES ('Sender2', '123456');

INSERT INTO message (sender, receiver, content)
VALUES ('Sender1', 'Zjuer', '这是一条测试消息1');
INSERT INTO message (sender, receiver, content)
VALUES ('Sender2', 'Zjuer', '这是一条测试消息2');
INSERT INTO message (sender, receiver, content)
VALUES ('Sender1', 'Zjuer', '这是一条测试消息3');
INSERT INTO message (sender, receiver, content)
VALUES ('Sender2', 'Zjuer', '这是一条测试消息4');
INSERT INTO message (sender, receiver, content)
VALUES ('Zjuer', 'Sender1', '这是一条测试消息5');
INSERT INTO message (sender, receiver, content)
VALUES ('Zjuer', 'Sender1', '这是一条测试消息6');


CREATE TABLE user (
                      user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50) NOT NULL UNIQUE,
                      email VARCHAR(100) NOT NULL UNIQUE,
                      password VARCHAR(100) NOT NULL,
                      school VARCHAR(100),
                      date_of_birth DATE,
                      avatar VARCHAR(255),
                      bio TEXT,
                      address TEXT,
                      phone_number VARCHAR(20),
                      role VARCHAR(50),
                      create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE post (
                      post_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      user_id BIGINT NOT NULL,
                      group_id BIGINT,
                      content TEXT,
                      image VARCHAR(255),
                      video VARCHAR(255),
                      create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (user_id) REFERENCES user(user_id),
                      FOREIGN KEY (group_id) REFERENCES `group`(group_id)
);

CREATE TABLE comment (
                         comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         post_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         content TEXT,
                         create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (post_id) REFERENCES post(post_id),
                         FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE `like` (
                        like_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        post_id BIGINT NOT NULL,
                        user_id BIGINT NOT NULL,
                        FOREIGN KEY (post_id) REFERENCES post(post_id),
                        FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE follower (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          follower_id BIGINT NOT NULL,
                          following_id BIGINT NOT NULL,
                          FOREIGN KEY (follower_id) REFERENCES user(user_id),
                          FOREIGN KEY (following_id) REFERENCES user(user_id)
);

CREATE TABLE block (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       blocker_id BIGINT NOT NULL,
                       blocked_id BIGINT NOT NULL,
                       FOREIGN KEY (blocker_id) REFERENCES user(user_id),
                       FOREIGN KEY (blocked_id) REFERENCES user(user_id)
);

CREATE TABLE `group` (
                         group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         owner_id BIGINT NOT NULL,
                         groupname VARCHAR(100),
                         create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (owner_id) REFERENCES user(user_id)
);

CREATE TABLE groupmember (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             group_id BIGINT NOT NULL,
                             member_id BIGINT NOT NULL,
                             FOREIGN KEY (group_id) REFERENCES `group`(group_id),
                             FOREIGN KEY (member_id) REFERENCES user(user_id)
);

CREATE TABLE notification (
                              notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              content TEXT,
                              type VARCHAR(50),
                              FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE groupchat (
                           groupchat_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           owner_id BIGINT NOT NULL,
                           groupname VARCHAR(100),
                           image VARCHAR(255),
                           create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (owner_id) REFERENCES user(user_id)
);

CREATE TABLE groupchatmember (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 group_id BIGINT NOT NULL,
                                 member_id BIGINT NOT NULL,
                                 FOREIGN KEY (group_id) REFERENCES groupchat(groupchat_id),
                                 FOREIGN KEY (member_id) REFERENCES user(user_id)
);

CREATE TABLE message (
                         message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         sender_id BIGINT NOT NULL,
                         receiver_id BIGINT,
                         groupchat_id BIGINT,
                         content TEXT,
                         image VARCHAR(255),
                         video VARCHAR(255),
                         create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (sender_id) REFERENCES user(user_id),
                         FOREIGN KEY (receiver_id) REFERENCES user(user_id),
                         FOREIGN KEY (groupchat_id) REFERENCES groupchat(groupchat_id)
);

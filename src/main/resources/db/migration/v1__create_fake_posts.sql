
INSERT INTO post (user_id, group_id, content, image, video, create_at) VALUES
                                                                           (1, NULL, 'First post content. This is a simple post to test the system.', 'image1.jpg', 'video1.mp4', CURRENT_TIMESTAMP),
                                                                           (2, NULL, 'Another post content. A post from another user.', 'image2.jpg', 'video2.mp4', CURRENT_TIMESTAMP),
                                                                           (1, 1, 'A post in group 1. This one belongs to a specific group.', 'image3.jpg', 'video3.mp4', CURRENT_TIMESTAMP),
                                                                           (1, 2, 'User 1 posts again in group 2. Test data for group posts.', 'image4.jpg', 'video4.mp4', CURRENT_TIMESTAMP),
                                                                           (2, 1, 'This is a test post in group 3, made by user 2.', 'image5.jpg', 'video5.mp4', CURRENT_TIMESTAMP),
                                                                           (1, NULL, 'This post has no group, just content to check the normal posts.', 'image6.jpg', 'video6.mp4', CURRENT_TIMESTAMP);

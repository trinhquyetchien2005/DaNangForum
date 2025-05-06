-- V3__create_fake_groups.sql

-- Insert giả dữ liệu cho bảng Group
INSERT INTO `group` (owner_id, groupname, create_at) VALUES
                                                         (1, 'Group A', CURRENT_TIMESTAMP),
                                                         (2, 'Group B', CURRENT_TIMESTAMP);


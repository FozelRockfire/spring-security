INSERT INTO t_users (name, username, password)
VALUES ('John Doe', 'johndoe@gmail.com', '$2a$12$Ah9Ang1eCe8Tj8XDrUZuyu/hp0uq5.bP3NjEDdD/6F4TI/VkdBqaq'),
       ('Mike Smith', 'mikesmith@yahoo.com', '$2a$12$PRt1w2a/whY2fuvtw6TQX.AHntlteKjNyRwZN5Ad8kX7baDglL/z2');
-- пароли admin и user соответственно

INSERT INTO t_roles (user_id, role)
VALUES (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');
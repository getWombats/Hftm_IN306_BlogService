CREATE TABLE BlogPost
(
    blog_id      BIGINT AUTO_INCREMENT NOT NULL,
    title        VARCHAR(255) NULL,
    content      TEXT NULL,
    author       VARCHAR(255) NULL,
    createdAt    datetime NULL,
    lastEditedAt datetime NULL,
    CONSTRAINT pk_blogpost PRIMARY KEY (blog_id)
);

CREATE TABLE Comment
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    commentNumber INT NOT NULL,
    content       VARCHAR(255) NULL,
    author        VARCHAR(255) NULL,
    createdAt     datetime NULL,
    lastEditedAt  datetime NULL,
    blog_id       BIGINT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);

ALTER TABLE Comment
    ADD CONSTRAINT FK_COMMENT_ON_BLOG FOREIGN KEY (blog_id) REFERENCES BlogPost (blog_id);
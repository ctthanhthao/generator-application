create table word
(
    word_id      BIGINT PRIMARY KEY auto_increment,
    word         VARCHAR(1000),
    category     VARCHAR(20),
    UNIQUE KEY word_unique (word)
);

CREATE TABLE sentence
(
    sentence_id  BIGINT PRIMARY KEY auto_increment,
    text VARCHAR(16000),
    generated_date TIMESTAMP,
    show_display_count INT,
    by_rule varchar(50),
    UNIQUE KEY sentence_unique (text)
);
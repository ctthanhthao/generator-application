
CREATE DATABASE IF NOT EXISTS generator_app;
use generator_app;

create table word
(
    word_id      BIGINT PRIMARY KEY auto_increment,
    word         VARCHAR(200) NOT NULL,
    category     VARCHAR(20),
    UNIQUE KEY uniq_word (word)
)ENGINE=InnoDB  CHARACTER SET utf8;

CREATE TABLE sentence
(
    sentence_id  BIGINT PRIMARY KEY auto_increment,
    text VARCHAR(1000) NOT NULL,
    generated_date TIMESTAMP,
    show_display_count INT,
    by_rule varchar(50),
    UNIQUE KEY uniq_sen (text)
)ENGINE=InnoDB  CHARACTER SET utf8;
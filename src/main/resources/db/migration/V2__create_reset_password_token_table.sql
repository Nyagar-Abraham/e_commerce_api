create table password_reset_tokens
(
    id          BIGINT auto_increment
        primary key,
    token       varchar(255) not null,
    expiry_date datetime     not null,
    user_id     bigint       not null,
    constraint password_reset_tokens_users_id_fk
        foreign key (user_id) references users (id)
);


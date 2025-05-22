-- CATEGORIES TABLE
create table categories
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);



-- CARTS TABLE
create table carts
(
    id         binary(16) default (uuid_to_bin(uuid())) not null
        primary key,
    cart_total decimal(10, 2)                           null,
    user_id    bigint                                   not null
);

-- CUSTOMERS TABLE
create table users
(
    id         bigint auto_increment
        primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    mobile_no  varchar(20)       null,
    email      varchar(255) not null,
    password   varchar(255) not null,
    created_on datetime  default (CURRENT_TIMESTAMP()) not null,
    card_no    varchar(19)  null,
    cart_id    binary(16)   null,
    role       varchar(255) not null default 'CUSTOMER',
    constraint user_cart_id_fk
        foreign key (cart_id) references carts (id)
);


-- PRODUCTS TABLE
create table products
(
    id           bigint auto_increment
        primary key,
    name         varchar(255)   not null,
    price        decimal(10, 2) not null,
    description  text           not null,
    manufacturer varchar(255)   not null,
    quantity     int default 1  not null,
    category_id  bigint         not null,
    status       varchar(255)   not null,
    seller_id    bigint         null,
    constraint products_categories_id_fk
        foreign key (category_id) references categories (id)
            on delete cascade,
    constraint products_sellers_id_fk
        foreign key (seller_id) references users (id)
            on delete set null
);

-- Now that customers table exists, add the foreign key on carts table
alter table carts
    add constraint cart_user_id_fk
        foreign key (user_id) references users (id)
            on delete cascade;

-- CART ITEMS TABLE
create table cart_items
(
    id         bigint auto_increment
        primary key,
    cart_id    binary(16)    not null,
    product_id bigint        not null,
    quantity   int default 1 not null,
    constraint cart_items_cart_product_unique
        unique (cart_id, product_id),
    constraint cart_item_cart_id_fk
        foreign key (cart_id) references carts (id)
            on delete cascade,
    constraint cart_item_product_id_fk
        foreign key (product_id) references products (id)
            on delete cascade
);

-- ADDRESSES TABLE
create table addresses
(
    id            bigint auto_increment
        primary key,
    street_no     varchar(255) not null,
    building_name varchar(255) not null,
    locality      varchar(255) null,
    city          varchar(255) not null,
    state         varchar(255) not null,
    pincode       varchar(255) null,
    user_id       bigint       not null,
    constraint addresses_customer_id_fk
        foreign key (user_id) references users (id)
            on delete cascade
);

-- ORDERS TABLE
create table orders
(
    id         bigint auto_increment
        primary key,
    order_date date           default (current_date()) not null,
    status     varchar(255)                            not null,
    total      decimal(10, 2) default 0                null,
    card_no    varchar(19)                             not null,
    user_id    bigint                                  not null,
    address_id bigint                                  not null,
    constraint orders_addresses_id_fk
        foreign key (address_id) references addresses (id)
            on delete cascade,
    constraint orders_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade
);

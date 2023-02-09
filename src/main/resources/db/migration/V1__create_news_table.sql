create table new
(
    title        text,
    author       varchar(10),
    link     varchar(100) primary key ,
    source_media varchar(100),
    content      text,
    tag          varchar(10),
    updated_at timestamp,
    created_at timestamp
);


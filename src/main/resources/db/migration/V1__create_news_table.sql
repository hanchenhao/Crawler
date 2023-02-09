create table new
(
    id           bigint primary key,
    title        text,
    author       varchar(10),
    base_url     varchar(100),
    source_media varchar(100),
    content      text,
    tag          varchar(10),
    updated_time timestamp,
    created_time timestamp
)

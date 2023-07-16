CREATE TABLE IF NOT EXISTS posts (
    id serial PRIMARY KEY,
    user_id INTEGER ,
	caption TEXT,
	image VARCHAR(250),
    jml_like INTEGER DEFAULT 0,
    jml_comment INTEGER DEFAULT 0,
	created_at TIMESTAMP,
    updated_at TIMESTAMP 
);


CREATE TABLE IF NOT EXISTS comment_post (
    id serial PRIMARY KEY,
    user_id INTEGER ,
    post_id INTEGER ,
	komentar TEXT,
	created_at TIMESTAMP,
    updated_at TIMESTAMP 
);

CREATE TABLE IF NOT EXISTS like_post (
    id serial PRIMARY KEY,
    user_id INTEGER ,
    post_id INTEGER ,
	created_at TIMESTAMP,
    updated_at TIMESTAMP 
);

CREATE TABLE IF NOT EXISTS stories (
    id serial PRIMARY KEY,
    user_id INTEGER ,
	judul VARCHAR(250),
	deskripsi VARCHAR(250),
	image VARCHAR(250),
    time INTEGER DEFAULT 3500,
	created_at TIMESTAMP,
    updated_at TIMESTAMP 
);

CREATE TABLE IF NOT EXISTS master_users (
    id serial PRIMARY KEY,
	username VARCHAR ( 50 ) UNIQUE NOT NULL,
	password VARCHAR ( 50 ) NOT NULL,
	email VARCHAR ( 255 ) UNIQUE NOT NULL,
	image VARCHAR(250),
    time INTEGER DEFAULT 3500,
    jml_pengikut INTEGER DEFAULT 0,
    jml_mengikuti INTEGER DEFAULT 0,
	created_at TIMESTAMP,
    updated_at TIMESTAMP 
);

CREATE TABLE IF NOT EXISTS followers (
    id serial PRIMARY KEY,
    user_id INTEGER ,
    follower_user_id INTEGER ,
	created_at TIMESTAMP,
    updated_at TIMESTAMP 
);

CREATE SCHEMA paymybuddy AUTHORIZATION "PMB";

-- DROP SEQUENCE paymybuddy.address_address_id_seq;

CREATE SEQUENCE paymybuddy.address_address_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
	
-- DROP SEQUENCE paymybuddy.bank_account_bank_account_id_seq;

CREATE SEQUENCE paymybuddy.bank_account_bank_account_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
	
-- DROP SEQUENCE paymybuddy.money_transfer_money_transfer_id_seq;

CREATE SEQUENCE paymybuddy.money_transfer_money_transfer_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;

-- DROP SEQUENCE paymybuddy.phone_number_phone_number_id_seq;

CREATE SEQUENCE paymybuddy.phone_number_phone_number_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;

-- DROP SEQUENCE paymybuddy.type_of_transfer_type_of_transfer_id_seq;

CREATE SEQUENCE paymybuddy.type_of_transfer_type_of_transfer_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;

-- DROP SEQUENCE paymybuddy.user_user_id_seq;

CREATE SEQUENCE paymybuddy.user_user_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;-- paymybuddy.bank_account definition

-- Drop table

-- DROP TABLE paymybuddy.bank_account;

CREATE TABLE paymybuddy.bank_account (
	bank_account_id bigserial NOT NULL,
	account_number varchar(30) NULL,
	bank_name varchar(50) NULL,
	response_from_bank_api bool NOT NULL,
	CONSTRAINT bank_account_pkey PRIMARY KEY (bank_account_id)
);


-- paymybuddy.type_of_transfer definition

-- Drop table

-- DROP TABLE paymybuddy.type_of_transfer;

CREATE TABLE paymybuddy.type_of_transfer (
	type_of_transfer_id serial NOT NULL,
	"name" varchar(255) NULL,
	percent_to_collect float8 NOT NULL,
	CONSTRAINT type_of_transfer_pkey PRIMARY KEY (type_of_transfer_id)
);


-- paymybuddy."user" definition

-- Drop table

-- DROP TABLE paymybuddy."user";

CREATE TABLE paymybuddy."user" (
	user_id bigserial NOT NULL,
	app_account float8 NOT NULL,
	birthdate timestamp NULL,
	email varchar(100) NULL,
	enabled bool NOT NULL,
	firstname varchar(50) NULL,
	lastname varchar(50) NULL,
	"password" varchar(60) NULL,
	"role" varchar(50) NULL,
	bank_account_id int8 NULL,
	CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email),
	CONSTRAINT user_pkey PRIMARY KEY (user_id),
	CONSTRAINT fkelf1n7mrytfidb2gijv2gqmsv FOREIGN KEY (bank_account_id) REFERENCES paymybuddy.bank_account(bank_account_id)
);


-- paymybuddy.user_money_friends definition

-- Drop table

-- DROP TABLE paymybuddy.user_money_friends;

CREATE TABLE paymybuddy.user_money_friends (
	money_sender int8 NOT NULL,
	money_friend int8 NOT NULL,
	CONSTRAINT fkh4vbwkvn8k5hd9ew28gtwox7u FOREIGN KEY (money_sender) REFERENCES paymybuddy."user"(user_id),
	CONSTRAINT fkt7we5ksklmtwe3hnqqvtklvix FOREIGN KEY (money_friend) REFERENCES paymybuddy."user"(user_id)
);


-- paymybuddy.address definition

-- Drop table

-- DROP TABLE paymybuddy.address;

CREATE TABLE paymybuddy.address (
	address_id bigserial NOT NULL,
	city varchar(50) NULL,
	"number" varchar(10) NULL,
	street varchar(50) NULL,
	zip varchar(10) NULL,
	user_id int8 NULL,
	CONSTRAINT address_pkey PRIMARY KEY (address_id),
	CONSTRAINT fkda8tuywtf0gb6sedwk7la1pgi FOREIGN KEY (user_id) REFERENCES paymybuddy."user"(user_id)
);


-- paymybuddy.money_transfer definition

-- Drop table

-- DROP TABLE paymybuddy.money_transfer;

CREATE TABLE paymybuddy.money_transfer (
	money_transfer_id bigserial NOT NULL,
	amount float8 NOT NULL,
	"date" timestamp NULL,
	description varchar(255) NULL,
	money_friend_id int8 NULL,
	money_sender_id int8 NOT NULL,
	type_of_transfer_id int4 NOT NULL,
	CONSTRAINT money_transfer_pkey PRIMARY KEY (money_transfer_id),
	CONSTRAINT fk7bwusq4o0eyud3pgtknj3582a FOREIGN KEY (money_friend_id) REFERENCES paymybuddy."user"(user_id),
	CONSTRAINT fkhktgews5qfl51thxhes28m0rw FOREIGN KEY (type_of_transfer_id) REFERENCES paymybuddy.type_of_transfer(type_of_transfer_id),
	CONSTRAINT fkt26h13edy3593ldsnyqr2mpgh FOREIGN KEY (money_sender_id) REFERENCES paymybuddy."user"(user_id)
);


-- paymybuddy.phone_number definition

-- Drop table

-- DROP TABLE paymybuddy.phone_number;

CREATE TABLE paymybuddy.phone_number (
	phone_number_id bigserial NOT NULL,
	"name" varchar(20) NULL,
	"number" varchar(10) NULL,
	user_id int8 NOT NULL,
	CONSTRAINT phone_number_pkey PRIMARY KEY (phone_number_id),
	CONSTRAINT fkb609grkur7fch5if2c0nrcujh FOREIGN KEY (user_id) REFERENCES paymybuddy."user"(user_id)
);

CREATE TABLE IF NOT EXISTS account
(
    id uuid UNIQUE NOT NULL,
    name varchar(30)  NOT NULL,
    pin varchar(4) NOT NULL,
    number varchar(30) NOT NULL,
    balance numeric NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
)
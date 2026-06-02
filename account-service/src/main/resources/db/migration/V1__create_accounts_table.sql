CREATE TABLE accounts(
    id uuid PRIMARY KEY,
    owner_name TEXT NOT NULL,
    balance NUMERIC(15, 2) NOT NULL CHECK(balance >= 0),
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX fx_owner_name ON accounts(owner_name, currency);
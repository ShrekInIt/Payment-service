CREATE TABLE ledger_transactions (
    id UUID PRIMARY KEY,
    reference_id UUID NOT NULL UNIQUE,
    type VARCHAR(30) NOT NULL CHECK (type IN ('PAYMENT', 'REFUND', 'MANUAL_ADJUSTMENT')),
    status VARCHAR(6) NOT NULL CHECK (status IN ('POSTED', 'FAILED')),
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_ledger_transactions_reference_id ON ledger_transactions (reference_id);
CREATE INDEX idx_ledger_type ON ledger_transactions (type);
CREATE INDEX idx_ledger_status ON ledger_transactions (status);


CREATE TABLE ledger_entries (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL,
    account_id UUID NOT NULL,
    direction varchar(10) NOT NULL CHECK (direction IN ('DEBIT', 'CREDIT')),
    amount DECIMAL(19, 4) NOT NULL CHECK (amount > 0),
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP NOT NULL,

    FOREIGN KEY (transaction_id) REFERENCES ledger_transactions(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE INDEX idx_ledger_entries_transaction_id ON ledger_entries (transaction_id);
CREATE INDEX idx_ledger_entries_account_id ON ledger_entries (account_id);






















CREATE TABLE payments(
    id UUID PRIMARY KEY,
    from_account_id UUID NOT NULL,
    to_account_id UUID NOT NULL,
    amount NUMERIC(15, 2) NOT NULL CHECK(amount > 0),
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PROCESSING', 'SUCCEEDED', 'FAILED')),
    failure_reason TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_payments_from_account_id ON payments(from_account_id);
CREATE INDEX idx_payments_to_account_id ON payments(to_account_id);
CREATE INDEX idx_payments_status ON payments(status);
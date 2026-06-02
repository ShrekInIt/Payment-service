SELECT 'CREATE DATABASE payment_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'payment_db')\gexec

SELECT 'CREATE DATABASE account_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'account_db')\gexec
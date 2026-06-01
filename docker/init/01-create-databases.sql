SELECT 'CREATE DATABASE payment_service'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'payment_service')\gexec
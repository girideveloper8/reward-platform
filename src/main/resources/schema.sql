-- Create Customer table
CREATE TABLE customer (
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL
);

-- Create Transaction table
CREATE TABLE transaction (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DOUBLE NOT NULL,
    transaction_month VARCHAR(255),
    customer_id BIGINT,
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id)
        REFERENCES Customer(customer_id)
);

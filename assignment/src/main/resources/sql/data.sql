-- 고객 데이터
INSERT INTO users (user_id, name, password, type, create_date) VALUES ('jisue', '김지수', '$2a$10$mPjjEYXsH9mFJ7rOc4Q8K.Zmth3HGZeCsb7MoxgsNfczPwpl6Y2/u', 'ORDER',current_date);
INSERT INTO users (user_id, name, password, type, create_date) VALUES ('jisue2', '김지수2', '$2a$10$mPjjEYXsH9mFJ7rOc4Q8K.Zmth3HGZeCsb7MoxgsNfczPwpl6Y2/u', 'DELIVERY',current_date);

-- 주문 데이터
INSERT INTO orders (order_id, order_user_id, payment_type, receiver, address, order_amount, payment_amount, delivery_status, create_date, update_date) VALUES ('000001', 'jisue', 'CARD', '김지수', '서울특별시', 20000, 20000, 'PENDING', current_date, null);
INSERT INTO orders (order_id, order_user_id, payment_type, receiver, address, order_amount, payment_amount, delivery_status, create_date, update_date) VALUES ('000002', 'jisue', 'CASH', '김지수', '경기도', 30000, 30000, 'COMPLETED', CURRENT_DATE - INTERVAL '2' DAY, CURRENT_DATE);
INSERT INTO orders (order_id, order_user_id, payment_type, receiver, address, order_amount, payment_amount, delivery_status, create_date, update_date) VALUES ('000003', 'jisue', 'CASH', '김지수', '경기도', 30000, 30000, 'STARTED', CURRENT_DATE - INTERVAL '5' DAY, CURRENT_DATE - INTERVAL '4' DAY);
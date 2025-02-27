-- 회원 테이블
CREATE TABLE users (
    user_id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    type VARCHAR(10) NOT NULL,
    create_date TIMESTAMP(3) NOT NULL,
    update_date TIMESTAMP(3)
);

COMMENT ON COLUMN users.user_id IS '고객 ID';
COMMENT ON COLUMN users.name IS '고객 이름';
COMMENT ON COLUMN users.password IS '비밀번호';
COMMENT ON COLUMN users.type IS '고객 타입';
COMMENT ON COLUMN users.create_date IS '가입 날짜';
COMMENT ON COLUMN users.update_date IS '갱신 날짜';

-- 주문 테이블
CREATE TABLE orders (
    order_id VARCHAR(100) PRIMARY KEY,
    order_user_id VARCHAR(100) NOT NULL,
    payment_type VARCHAR(4) NOT NULL,
    receiver VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    order_amount BIGINT NOT NULL,
    payment_amount BIGINT,
    delivery_status VARCHAR(10) NOT NULL,
    create_date TIMESTAMP(3) NOT NULL,
    update_date TIMESTAMP(3)
);

COMMENT ON COLUMN orders.order_id IS '주문 고유 식별자';
COMMENT ON COLUMN orders.order_user_id IS '주문 고객 ID';
COMMENT ON COLUMN orders.payment_type IS '결제 유형';
COMMENT ON COLUMN orders.receiver IS '받는 사람';
COMMENT ON COLUMN orders.address IS '도착지 주소';
COMMENT ON COLUMN orders.order_amount IS '주문 금액';
COMMENT ON COLUMN orders.payment_amount IS '결제 금액';
COMMENT ON COLUMN orders.delivery_status IS '배달 상태';
COMMENT ON COLUMN orders.create_date IS '주문 날짜';
COMMENT ON COLUMN orders.update_date IS '갱신 날짜';
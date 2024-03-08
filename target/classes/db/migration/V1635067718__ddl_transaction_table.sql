CREATE TABLE IF NOT EXISTS PHR_TRANSACTION(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  transaction_id uuid NOT NULL DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
  reference_id VARCHAR(50),
  health_id_number VARCHAR(17),
  registration_method VARCHAR(20),
  phr_address VARCHAR(50),
  phr_provider VARCHAR(50) DEFAULT 'abdm',
  status VARCHAR(50),
  kyc_type VARCHAR(50),
  full_name VARCHAR(150),
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  middle_name VARCHAR(50),
  gender VARCHAR(10),
  email VARCHAR(320),
  mobile VARCHAR(10),
  mobile_country_code VARCHAR(8) DEFAULT '+91',
  day_of_birth VARCHAR(2),
  month_of_birth VARCHAR(2),
  year_of_birth VARCHAR(4),
  date_of_birth VARCHAR(10),
  profile_photo TEXT,
  kyc_status VARCHAR(20) DEFAULT 'PENDING',
  address_line VARCHAR(1000),
  country_name VARCHAR(30) DEFAULT 'INDIA',
  country_code VARCHAR(30) DEFAULT 1,
  state_name VARCHAR(50),
  state_code VARCHAR(30),
  district_name VARCHAR(50),
  district_code VARCHAR(30),
  sub_district_name VARCHAR(50),
  sub_district_code VARCHAR(30),
  town_name VARCHAR(50),
  town_code VARCHAR(30),
  village_name VARCHAR(50),
  village_code VARCHAR(30),
  ward_name VARCHAR(50),
  ward_code VARCHAR(30),
  address_type VARCHAR(20) DEFAULT 'PERMANENT',
  otp VARCHAR(255),
  retry_method VARCHAR(20),
  retry_count INTEGER DEFAULT 0,
  mobile_verified BOOLEAN DEFAULT FALSE,
  email_verified BOOLEAN DEFAULT FALSE,
  password VARCHAR(255),
  pincode VARCHAR(6),
  created_date TIMESTAMP DEFAULT NOW() NOT NULL,
  created_by VARCHAR(50) DEFAULT 'PHR_WEB_APP' NOT NULL,
  updated_date TIMESTAMP DEFAULT NOW() NOT NULL,
  updated_by VARCHAR(50) DEFAULT 'PHR_WEB_APP' NOT NULL
);

CREATE INDEX PHR_TRANSACTION_ID_IDX ON PHR_TRANSACTION (id);
CREATE INDEX PHR_TRANSACTION_TXNID_IDX ON PHR_TRANSACTION (transaction_id);
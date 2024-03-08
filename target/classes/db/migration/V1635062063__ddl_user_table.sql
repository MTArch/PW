CREATE TABLE IF NOT EXISTS PHR_USERS(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  health_id_number VARCHAR(17),
  phr_address VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(1000) NOT NULL,
  phr_provider VARCHAR(50) NOT NULL DEFAULT 'abdm',
  full_name VARCHAR(150) NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  middle_name VARCHAR(50),
  gender VARCHAR(10) NOT NULL,
  email VARCHAR(320),
  mobile VARCHAR(10) NOT NULL,
  mobile_country_code VARCHAR(8) DEFAULT '+91' NOT NULL,
  day_of_birth VARCHAR(2),
  month_of_birth VARCHAR(2),
  year_of_birth VARCHAR(4) NOT NULL,
  date_of_birth VARCHAR(10) NOT NULL,
  profile_photo TEXT,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  kyc_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  mobile_verified BOOLEAN DEFAULT FALSE,
  email_verified BOOLEAN DEFAULT FALSE,
  created_date TIMESTAMP DEFAULT NOW() NOT NULL,
  created_by VARCHAR(50) DEFAULT 'PHR_WEB_APP' NOT NULL,
  updated_date TIMESTAMP DEFAULT NOW() NOT NULL,
  updated_by VARCHAR(50) DEFAULT 'PHR_WEB_APP' NOT NULL
);

-- Indexes
CREATE INDEX PHR_USERS_ID_IDX ON PHR_USERS (id);
CREATE INDEX PHR_USERS_PHR_ADDRESS_IDX ON PHR_USERS (phr_address);
CREATE INDEX PHR_USERS_MOBILE_IDX ON PHR_USERS (mobile);
CREATE INDEX PHR_USERS_EMAIL_IDX ON PHR_USERS (email);
CREATE INDEX PHR_USERS_HID_IDX ON PHR_USERS (health_id_number);

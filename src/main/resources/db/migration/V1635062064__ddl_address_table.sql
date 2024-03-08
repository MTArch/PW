CREATE TABLE IF NOT EXISTS PHR_ADDRESS(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  user_id BIGINT,
  address_line VARCHAR(250),
  country_name VARCHAR(30) NOT NULL DEFAULT 'INDIA',
  country_code VARCHAR(30) NOT NULL DEFAULT 1,
  state_name VARCHAR(50) NOT NULL,
  state_code VARCHAR(30) NOT NULL,
  district_name VARCHAR(50) NOT NULL,
  district_code VARCHAR(30) NOT NULL,
  sub_district_name VARCHAR(50),
  sub_district_code VARCHAR(30),
  town_name VARCHAR(50),
  town_code VARCHAR(30),
  village_name VARCHAR(50),
  village_code VARCHAR(30),
  ward_name VARCHAR(50),
  ward_code VARCHAR(30),
  address_type VARCHAR(20) DEFAULT 'PERMANENT',
  pincode VARCHAR(6),
  status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL,
  created_date TIMESTAMP DEFAULT NOW() NOT NULL,
  created_by VARCHAR(50) DEFAULT 'PHR_WEB_APP' NOT NULL,
  updated_date TIMESTAMP DEFAULT NOW() NOT NULL,
  updated_by VARCHAR(50) DEFAULT 'PHR_WEB_APP' NOT NULL,
  CONSTRAINT PHR_ADDRESS_UID_FK FOREIGN KEY(user_id) REFERENCES PHR_USERS(id) ON DELETE CASCADE
);

CREATE INDEX PHR_ADDRESS_ID_IDX ON PHR_ADDRESS (id);
ALTER TABLE phr_address ALTER COLUMN state_code drop not null;
ALTER TABLE phr_address ALTER COLUMN district_code drop not null;
ALTER TABLE phr_address ALTER COLUMN state_name drop not null;
ALTER TABLE phr_address ALTER COLUMN district_name drop not null;
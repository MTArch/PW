-- DROP TABLE IF EXISTS public.security_captcha;

CREATE TABLE IF NOT EXISTS SECURITY_CAPTCHA
(
id BIGSERIAL PRIMARY KEY NOT NULL,
user_action VARCHAR(10) ,
captcha_answer VARCHAR(10) ,
captcha_id VARCHAR(255) ,
captcha_text VARCHAR(20) ,
captcha_valid boolean DEFAULT false,
client_id VARCHAR(50) ,
client_ip VARCHAR(50) ,
created_date timestamp without time zone NOT NULL,
lst_updated_by VARCHAR(255) ,
latitude VARCHAR(10) ,
longitude VARCHAR(10) ,
origin VARCHAR(50) ,
retry_count integer DEFAULT 0,
status VARCHAR(20) ,
update_date timestamp without time zone
);

-- Indexes
CREATE INDEX captcha_id_idx ON SECURITY_CAPTCHA(captcha_id);




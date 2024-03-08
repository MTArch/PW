CREATE TABLE IF NOT EXISTS public.phr_error
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    created_date timestamp without time zone NOT NULL,
    health_id_number character varying(255),
    req_payload character varying(1000),
    res_payload character varying(1000),
    update_date timestamp without time zone
);
-- Indexes
CREATE INDEX PHR_ERROR_IDX ON PHR_ERROR (health_id_number);
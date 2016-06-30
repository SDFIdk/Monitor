ALTER TABLE jobs modify service_url varchar(255);
ALTER TABLE jobs modify SLA_START_TIME datetime;
ALTER TABLE jobs modify SLA_END_TIME datetime;
ALTER TABLE queries ADD QUERY_URL VARCHAR(255) NOT NULL;
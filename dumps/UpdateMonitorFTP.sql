INSERT INTO `service_methods` VALUES ('13', 'FTP');
INSERT INTO `service_types_methods` VALUES ('8', '13');
ALTER TABLE queries ADD LOGIN VARCHAR(45) NULL DEFAULT '';
ALTER TABLE queries ADD PASSWORD VARCHAR(45) NULL DEFAULT '';

-- Changes for size
ALTER TABLE monitor.log_entries MODIFY RESPONSE_SIZE INTEGER DEFAULT 0;
ALTER TABLE monitor.query_test_result MODIFY RESPONSE_SIZE INTEGER DEFAULT 0;
ALTER TABLE monitor.query_validation_results MODIFY RESPONSE_SIZE INTEGER DEFAULT 0;
ALTER TABLE monitor.query_validation_settings MODIFY NORM_SIZE INTEGER DEFAULT 0;

ALTER TABLE monitor.log_entries MODIFY RESPONSE_SIZE INTEGER UNSIGNED DEFAULT 0;

ALTER TABLE monitor.query_test_result MODIFY RESPONSE_SIZE INTEGER UNSIGNED DEFAULT 0;

ALTER TABLE monitor.query_validation_results MODIFY RESPONSE_SIZE INTEGER UNSIGNED DEFAULT 0;

ALTER TABLE monitor.query_validation_settings MODIFY NORM_SIZE INTEGER UNSIGNED DEFAULT 0;

-- Disable safe mode
SET SQL_SAFE_UPDATES=0;

update monitor.query_validation_settings set norm_size = 0 where norm_size is null

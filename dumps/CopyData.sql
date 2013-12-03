 DROP PROCEDURE IF EXISTS copyJobData;
delimiter $$
CREATE PROCEDURE copyJobData()
BEGIN
   DECLARE done INT DEFAULT false;
   declare serviceurlTemp varchar(255);
   declare ID_JOBTemp int(10);

   declare jobcursor CURSOR
   FOR 
   SELECT ID_JOB,service_url FROM jobs;
   DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;
   -- Open the cursor
   OPEN jobcursor;
   
	read_loop: LOOP
		FETCH jobcursor INTO ID_JOBTemp,serviceurlTemp;
		IF done THEN
			LEAVE read_loop;
		END IF;
		UPDATE queries SET QUERY_URL = serviceurlTemp where ID_JOB = ID_JOBTemp;
	END LOOP;

   -- Close the cursor
   CLOSE jobcursor;

end$$

call copyJobData;
/* Formatted on 10/30/2018 2:11:23 PM (QP5 v5.139.911.3011) */
DECLARE
   IF_EXISTS_FLAG   NUMBER;
   PART_SUFFIX      VARCHAR2 (50);
   TRIG_PREFIX      VARCHAR2 (50);
BEGIN
   PART_SUFFIX := '_PART';
   TRIG_PREFIX := 'GTR_';

   FOR REC
      IN (SELECT *
            FROM OUTPUT_TABLE_INFO
           WHERE TABLE_NAME IS NOT NULL
                 AND UPPER (TABLE_NAME) LIKE 'CPMON_CPU%')
   LOOP
      BEGIN
         SELECT COUNT (1)
           INTO IF_EXISTS_FLAG
           FROM ALL_TABLES
          WHERE TABLE_NAME IN
                   (UPPER (REC.TABLE_NAME),
                    UPPER (REC.TABLE_NAME) || PART_SUFFIX);

         IF IF_EXISTS_FLAG = 2
         THEN
            EXECUTE IMMEDIATE 'DROP TABLE ' || UPPER (REC.TABLE_NAME);

            EXECUTE IMMEDIATE   'ALTER TABLE '
                             || UPPER (REC.TABLE_NAME)
                             || PART_SUFFIX
                             || ' RENAME TO '
                             || UPPER (REC.TABLE_NAME);

            EXECUTE IMMEDIATE   ' ALTER TRIGGER '
                             || TRIG_PREFIX
                             || UPPER (REC.TABLE_NAME)
                             || PART_SUFFIX
                             || ' RENAME TO '
                             || TRIG_PREFIX
                             || UPPER (REC.TABLE_NAME);

            DBMS_OUTPUT.
            put_line (
               'ROLLBACK SUCCESS FOR ' || UPPER (REC.TABLE_NAME) || ' :) ');
         ELSE
            DBMS_OUTPUT.
            put_line (
                  'ROLLBACK FAILED FOR '
               || UPPER (REC.TABLE_NAME)
               || ' !!!  ->  '
               || IF_EXISTS_FLAG);
         END IF;
      END;
   END LOOP;
END;


/*
flashback TABLE CPMON_CPU TO BEFORE DROP;
flashback TABLE CPMON_CPU_LINUX TO BEFORE DROP;
flashback TABLE CPMON_RAM_UNIX TO BEFORE DROP;
flashback TABLE CPMON_RAM TO BEFORE DROP;
*/
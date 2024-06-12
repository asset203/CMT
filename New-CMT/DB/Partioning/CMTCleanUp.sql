/* Formatted on 10/30/2018 1:32:39 PM (QP5 v5.139.911.3011) */
DECLARE
   IF_EXISTS_FLAG        NUMBER;
   PART_SUFFIX   VARCHAR2 (50);
BEGIN
   PART_SUFFIX := '_PART';
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
            EXECUTE IMMEDIATE 'DROP TABLE ' || UPPER (REC.TABLE_NAME) || PART_SUFFIX ;

            DBMS_OUTPUT.
            put_line (
               'CLEANUP SUCCESS FOR ' || UPPER (REC.TABLE_NAME) || ' :) ');
         ELSE
            DBMS_OUTPUT.
            put_line (
                  'CLEANUP FAILED FOR '
               || UPPER (REC.TABLE_NAME)
               || ' !!!  ->  '
               || IF_EXISTS_FLAG);
         END IF;
      END;
   END LOOP;
END;
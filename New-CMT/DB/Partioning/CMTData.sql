/* Formatted on 10/30/2018 2:11:18 PM (QP5 v5.139.911.3011) */
         ALTER SESSION SET skip_unusable_indexes=TRUE;
SET SERVEROUTPUT ON

DECLARE
   SCRIPT_1      VARCHAR2 (32767);
   SCRIPT_2      VARCHAR2 (32767);
   SCRIPT_3      VARCHAR2 (32767);
   PART_SUFFIX   VARCHAR2 (50);
   INDEX_N       VARCHAR2 (500);
   START_DATE    VARCHAR2 (500);
   SCHEMA_NAME   VARCHAR2 (50);
BEGIN
   /*Get Tables  creation scripts*/
   DBMS_OUTPUT.put_line ('Lets start , Get Data tables');
   SCHEMA_NAME := 'CMT_USER_CR01';
   PART_SUFFIX := '_PART';

   START_DATE := 'TO_DATE(''01/01/2017'', ''DD/MM/YYYY'')';

   FOR REC
      IN (SELECT *
            FROM OUTPUT_TABLE_INFO
           WHERE TABLE_NAME IS NOT NULL
                 AND UPPER (TABLE_NAME) LIKE 'CPMON_CPU%')
   LOOP
      BEGIN
         DBMS_OUTPUT.put_line ('Insert Data into ' || REC.TABLE_NAME);


         FOR REC1 IN (SELECT CONSTRAINT_NAME, TABLE_NAME
                        FROM all_constraints
                       WHERE TABLE_NAME = REC.TABLE_NAME)
         LOOP
            BEGIN
               DBMS_OUTPUT.
               put_line ('DISABLE CONSTRAINT FOR ' || REC.TABLE_NAME);

               SCRIPT_1 :=
                     'ALTER TABLE '
                  || REC.TABLE_NAME
                  || ' DISABLE CONSTRAINT '
                  || REC1.CONSTRAINT_NAME;

               EXECUTE IMMEDIATE SCRIPT_1;

               SCRIPT_1 := '';
            END;
         END LOOP;

         EXECUTE IMMEDIATE   'INSERT /*+ APPEND */ INTO '
                          || REC.TABLE_NAME
                          || ' SELECT * FROM '
                          || REC.TABLE_NAME
                          || PART_SUFFIX
                          || ' WHERE  '
                          || REC.DELETE_DATE_COL
                          || ' >= '
                          || START_DATE;

         COMMIT;
         DBMS_OUTPUT.
               put_line ('DATA INSERTED FOR ' || REC.TABLE_NAME);

         FOR REC2 IN (SELECT CONSTRAINT_NAME, TABLE_NAME
                        FROM all_constraints
                       WHERE TABLE_NAME = REC.TABLE_NAME)
         LOOP
            BEGIN
               DBMS_OUTPUT.
               put_line ('ENABLE CONSTRAINT FOR ' || REC.TABLE_NAME);

               SCRIPT_1 :=
                     'ALTER TABLE '
                  || REC.TABLE_NAME
                  || ' ENABLE CONSTRAINT '
                  || REC2.CONSTRAINT_NAME;

               EXECUTE IMMEDIATE SCRIPT_1;

               SCRIPT_1 := '';
            END;
         END LOOP;
      END;
   END LOOP;
END;
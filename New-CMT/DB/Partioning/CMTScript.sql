/* Formatted on 10/30/2018 2:11:29 PM (QP5 v5.139.911.3011) */
SET SERVEROUTPUT ON

DECLARE
   SCRIPT_1      VARCHAR2 (32767);
   SCRIPT_2      VARCHAR2 (32767);
   SCRIPT_3      VARCHAR2 (32767);
   PART_SUFFIX   VARCHAR2 (50);
   TRIG_PREFIX   VARCHAR2 (50);
   START_DATE    VARCHAR2 (500);
   SCHEMA_NAME   VARCHAR2 (50);
BEGIN
                     /*Get Tables  creation scripts*/
               DBMS_OUTPUT.
                   put_line ('Lets start , Get Tables creation scripts');
               SCHEMA_NAME := 'CMT_USER_CR01';
               PART_SUFFIX := '_PART';
               TRIG_PREFIX := 'GTR_';
               START_DATE := 'TO_DATE(''01/01/2017'', ''DD/MM/YYYY'')';
     
     FOR REC IN (SELECT *
                             FROM OUTPUT_TABLE_INFO
                            WHERE TABLE_NAME IS NOT NULL AND UPPER(TABLE_NAME) LIKE  'CPMON_CPU%')

               LOOP
                  BEGIN

                        DBMS_OUTPUT.
                                    put_line ('get creation script for Table : '|| REC.TABLE_NAME);
                         SELECT DBMS_LOB.
                                SUBSTR (
                                   DBMS_METADATA.
                                   get_ddl ('TABLE', UPPER (REC.TABLE_NAME), SCHEMA_NAME),
                                   32767,
                                   1)
                           INTO SCRIPT_1
                           FROM DUAL;

                        DBMS_OUTPUT.
                                    put_line ('get creation script for Trigger : '|| REC.TABLE_NAME);
                         SELECT DBMS_LOB.
                                SUBSTR (
                                   DBMS_METADATA.
                                   get_ddl ('TRIGGER',
                                            UPPER (TRIG_PREFIX || REC.TABLE_NAME),
                                            SCHEMA_NAME),
                                   32767,
                                   1)
                           INTO SCRIPT_2
                           FROM DUAL;

                        DBMS_OUTPUT.
                                    put_line ('Add partition script for table : '|| REC.TABLE_NAME);
                         SCRIPT_1 :=
                               SCRIPT_1
                            || CHR (10)
                            || 'PARTITION BY RANGE ('
                            || REC.DELETE_DATE_COL
                            || ')'
                            || CHR (10)
                            || 'INTERVAL(NUMTOYMINTERVAL(1,''MONTH''))';
                            
                         SCRIPT_1 :=
                               SCRIPT_1
                            || CHR (10)
                            || '( PARTITION '
                            || REC.TABLE_NAME
                            || '_START'
                            || ' VALUES LESS THAN (TO_DATE(''01/02/2017'', ''DD/MM/YYYY'')))';

                             
                        DBMS_OUTPUT.
                            put_line ('Rename table : '|| REC.TABLE_NAME || ' to ' || REC.TABLE_NAME
                                          || PART_SUFFIX);
                                          
                         EXECUTE IMMEDIATE   'ALTER TABLE  '
                                          || REC.TABLE_NAME
                                          || ' RENAME TO '
                                          || REC.TABLE_NAME
                                          || PART_SUFFIX;

                                 --TODO: Drop Triggers
                                 
                          DBMS_OUTPUT.
                            put_line ('Rename trigger : '|| TRIG_PREFIX  || REC.TABLE_NAME || ' to ' || REC.TABLE_NAME 
                                          || PART_SUFFIX);
                          EXECUTE IMMEDIATE   'ALTER TRIGGER  '
                                                  || UPPER (TRIG_PREFIX  || REC.TABLE_NAME)
                                                  || ' RENAME TO '
                                                  || UPPER (TRIG_PREFIX || REC.TABLE_NAME)
                                                  || PART_SUFFIX;
                                                  

                            DBMS_OUTPUT.
                                        put_line ('Creat new Table : '|| REC.TABLE_NAME);
                            
                            EXECUTE IMMEDIATE SCRIPT_1;

                           DBMS_OUTPUT.
                                put_line ('Preparing script for Trigger : '|| TRIG_PREFIX  || REC.TABLE_NAME);
                                    
                             SELECT SUBSTR (SCRIPT_2, INSTR(SCRIPT_2, 'ALTER TRIGGER', 1),INSTR(SCRIPT_2,'ENABLE' , -1)) INTO SCRIPT_3 FROM DUAL;
                             SELECT REPLACE (SCRIPT_2, SCRIPT_3, '') INTO SCRIPT_2 FROM DUAL;
                             SELECT REPLACE (SCRIPT_2, 'EDITIONABLE', '') INTO SCRIPT_2 FROM DUAL;

                        /*
                                  DBMS_OUTPUT.
                                    put_line (SCRIPT_2);
                                DBMS_OUTPUT.
                                    put_line (SCRIPT_3);*/

                        DBMS_OUTPUT.
                                    put_line ('Creat new Trigger : '|| TRIG_PREFIX  || REC.TABLE_NAME);
                                 EXECUTE IMMEDIATE SCRIPT_2;
                                 EXECUTE IMMEDIATE SCRIPT_3;

                                 SCRIPT_1 := '';
                                 SCRIPT_2 := '';
                                 SCRIPT_3 := '';

      END;
   END LOOP;
END;
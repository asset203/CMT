

CREATE TABLE DC_SMPP_LOGS
(
  ID  NUMBER(19) NOT NULL,
  SMPP_SITE VARCHAR2(100 BYTE) NOT NULL,
  SMPP_APP  VARCHAR2(10 BYTE) NOT NULL,
  IP  VARCHAR2(100 BYTE) NOT NULL,
  SMSC_SITE VARCHAR2(100 BYTE)  NOT NULL,
  SYSID VARCHAR2(40 BYTE) NOT NULL,
  THRESHOLD NUMBER(4) NOT NULL,
  CONCATENATION NUMBER(4) NOT NULL,
  NO_OF_SESSIONS NUMBER(10) ,
  SMS_QUEUE_SIZE NUMBER(10) ,
  TIME_WINDOW_FROM_HOUR NUMBER(2) ,
  TIME_WINDOW_FROM_MIN NUMBER(2) ,
  TIME_WINDOW_TO_HOUR NUMBER(2) ,
  TIME_WINDOW_TO_MIN  NUMBER(2) ,
  CONSTRAINT smpp_logs_pk PRIMARY KEY (ID)
);


CREATE SEQUENCE GSQ_DC_SMPP_LOGS
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
  

CREATE OR REPLACE TRIGGER GTR_DC_SMPP_LOGS BEFORE INSERT ON DC_SMPP_LOGS FOR EACH ROW
WHEN (
NEW.ID IS NULL
      )
BEGIN SELECT GSQ_DC_SMPP_LOGS.NEXTVAL INTO :NEW.ID FROM DUAL; END;
/






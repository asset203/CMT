package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SMSLGPData extends PersistenceObject{
	public Date rec_time;
	public double id_file;
	public double rec_no;
	public String inboundMessageType;
	public String timeStamp;
	public String routingAction;
	public String rejectInfo_rejectCause;
	public String rejectInfo_moRoutingRule;
	public String rejectInfo_moExtConditionRule;
	public String rejectInfo_mtRoutingRule;
	public String rejectInfo_mtExtConditionRule;
	public String rejectInfo_aoRoutingRule;
	public String rejectInfo_aoExtConditionRule;
	public String rejectInfo_atRoutingRule;
	public String rejectInfo_atExtConditionRule;
	public String rejectInfo_mnpViolation;
	public String responseInfo_submissionResult;
	public String responseInfo_moRoutingRule;
	public String responseInfo_mtRoutingRule;
	public String responseInfo_aoRoutingRule;
	public String responseInfo_atRoutingRule;
	public String responseInfo_queryResult;
	public String responseInfo_mapImsi_country;
	public String responseInfo_mapImsi_network;
	public String responseInfo_mapImsi_imsi;
	public String responseInfo_scrambledImsi_country;
	public String responseInfo_scrambledImsi_network;
	public String responseInfo_scrambledImsi_imsi;
	public String responseInfo_mapLmsi;
	public String responseInfo_mapMsc_country;
	public String responseInfo_mapMsc_network;
	public String responseInfo_mapMsc_gsmAddress_ton;
	public String responseInfo_mapMsc_gsmAddress_npi;
	public String responseInfo_mapMsc_gsmAddress_number;
	public String responseInfo_mapSgsn_country;
	public String responseInfo_mapSgsn_network;
	public String responseInfo_mapSgsn_gsmAddress_ton;
	public String responseInfo_mapSgsn_gsmAddress_npi;
	public String responseInfo_mapSgsn_gsmAddress_number;
	public String responseInfo_deliveryResult;
	public double responseInfo_routingErrorCode;
	public String responseInfo_serviceCentreTimestamp;
	public String ignoredRejectCauses_unknownSccpSmscAddress;
	public String ignoredRejectCauses_unknownMapSmscAddress;
	public String ignoredRejectCauses_conflictingSmscAddresses;
	public String ignoredRejectCauses_spoofingSccpSmscAddress;
	public String ignoredRejectCauses_spoofingMapSmscAddress;
	public String ignoredRejectCauses_spoofedOriginatorAddress;
	public String sccpCgPaOfFirstSegment_country;
	public String sccpCgPaOfFirstSegment_network;
	public double sccpCgPaOfFirstSegment_sccpAddress_signallingPointCode;
	public String sccpCgPaOfFirstSegment_sccpAddress_subsystemNumber;
	public String sccpCgPaOfFirstSegment_sccpAddress_globalTitle_numberingPlan;
	public String sccpCgPaOfFirstSegment_sccpAddress_globalTitle_number;
	public String sccpCgPaOfFirstSegment_sccpAddress_globalTitle_NAI;
	public String sccpCdPaOfFirstSegment_country;
	public String sccpCdPaOfFirstSegment_network;
	public double sccpCdPaOfFirstSegment_sccpAddress_signallingPointCode;
	public String sccpCdPaOfFirstSegment_sccpAddress_subsystemNumber;
	public String sccpCdPaOfFirstSegment_sccpAddress_globalTitle_numberingPlan;
	public String sccpCdPaOfFirstSegment_sccpAddress_globalTitle_number;
	public String sccpCdPaOfFirstSegment_sccpAddress_globalTitle_NAI;
	public String sccpCgPa_country;
	public String sccpCgPa_network;
	public double sccpCgPa_sccpAddress_signallingPointCode;
	public String sccpCgPa_sccpAddress_subsystemNumber;
	public String sccpCgPa_sccpAddress_globalTitle_numberingPlan;
	public String sccpCgPa_sccpAddress_globalTitle_number;
	public String sccpCgPa_sccpAddress_globalTitle_NAI;
	public String sccpCdPa_country;
	public String sccpCdPa_network;
	public double sccpCdPa_sccpAddress_signallingPointCode;
	public String sccpCdPa_sccpAddress_subsystemNumber;
	public String sccpCdPa_sccpAddress_globalTitle_numberingPlan	;
	public String sccpCdPa_sccpAddress_globalTitle_NAI;
	public String mapSmsc_country;
	public String mapSmsc_network;
	public String mapSmsc_gsmAddress_ton;
	public String mapSmsc_gsmAddress_npi;
	public String mapSmsc_gsmAddress_number;
	public String mapMsisdn_country;
	public String mapMsisdn_network;
	public String mapMsisdn_gsmAddress_ton;
	public String mapMsisdn_gsmAddress_npi;
	public String mapMsisdn_gsmAddress_number;
	public String mapImsi_country;
	public String mapImsi_network;
	public String mapImsi_imsi;
	public String mapLmsi;
	public String smsSubmit_smsServices_rejectDuplicates;
	public String smsSubmit_smsServices_statusReportRequest;
	public String smsSubmit_smsServices_userDataHeaderIndication;
	public String smsSubmit_smsServices_replyPath;
	public double smsSubmit_smsMessageReference;
	public double smsSubmit_smsProtocolId;
	public String smsSubmit_smsRecipient_country;
	public String smsSubmit_smsRecipient_network;
	public String smsSubmit_smsRecipient_gsmAddress_ton;
	public String smsSubmit_smsRecipient_gsmAddress_npi;
	public String smsSubmit_smsRecipient_gsmAddress_number;
	public String smsSubmit_smsDataCodingScheme_alphabet;
	public String smsSubmit_smsDataCodingScheme_messageClass;
	public String smsSubmit_smsValidityPeriod;
	public String smsSubmit_smsUserDataHeader;
	public String smsSubmit_smsUserData;
	public String smsCommand_smsServices_statusReportRequest;
	public String smsCommand_smsServices_userDataHeaderIndication;
	public double smsCommand_smsMessageReference;
	public double smsCommand_smsProtocolId;
	public String smsCommand_smsCommandType;
	public double smsCommand_smsMessageNumber;
	public String smsCommand_smsRecipient_country;
	public String smsCommand_smsRecipient_network;
	public String smsCommand_smsRecipient_gsmAddress_ton;
	public String smsCommand_smsRecipient_gsmAddress_npi;
	public String smsCommand_smsRecipient_gsmAddress_number;
	public String smsCommand_smsCommandData;
	public String smsDeliver_smsServices_moreMessagesToSend;
	public String smsDeliver_smsServices_statusReportIndication;
	public String smsDeliver_smsServices_userDataHeaderIndication;
	public String smsDeliver_smsServices_replyPath;
	public String smsDeliver_smsOriginator_country;
	public String smsDeliver_smsOriginator_network;
	public String smsDeliver_smsOriginator_gsmAddress_npi;
	public String smsDeliver_smsOriginator_gsmAddress_number;
	public double smsDeliver_smsProtocolId;
	public String smsDeliver_smsDataCodingScheme_alphabet;
	public String smsDeliver_smsDataCodingScheme_messageClass;
	public String smsDeliver_smsScTimestamp;
	public String smsDeliver_smsUserDataHeader;
	public String smsDeliver_smsUserData;
	public String statusReport_smsServices_moreMessagesToSend;
	public String statusReport_smsServices_statusReportQualifier;
	public double statusReport_smsMessageReference;
	public String statusReport_smsRecipient_country;
	public String statusReport_smsRecipient_network;
	public String statusReport_smsRecipient_gsmAddress_ton;
	public String statusReport_smsRecipient_gsmAddress_npi;
	public String statusReport_smsRecipient_gsmAddress_number;
	public String statusReport_smsScTimestamp;
	public String statusReport_smsDischargeTime;
	public double statusReport_smsStatus;
	public String infoFromHlr_mapImsi_country;
	public String infoFromHlr_mapImsi_network;
	public String infoFromHlr_mapImsi_imsi;
	public String infoFromHlr_mapMsc_country;
	public String infoFromHlr_mapMsc_network;
	public String infoFromHlr_mapMsc_gsmAddress_ton;
	public String infoFromHlr_mapMsc_gsmAddress_npi;
	public String infoFromHlr_mapMsc_gsmAddress_number;
	public String infoFromHlr_mapSgsn_country;
	public String infoFromHlr_mapSgsn_network;
	public String infoFromHlr_mapSgsn_gsmAddress_ton;
	public String infoFromHlr_mapSgsn_gsmAddress_npi;
	public String infoFromHlr_mapSgsn_gsmAddress_number;
	public String correlatedSriSm_sccpCgPa_country;
	public String correlatedSriSm_sccpCgPa_network;
	public double correlatedSriSm_sccpCgPa_sccpAddress_signallingPointCode;
	public String correlatedSriSm_sccpCgPa_sccpAddress_subsystemNumber;
	public String correlatedSriSm_sccpCgPa_sccpAddress_globalTitle_numberingPlan;
	public String correlatedSriSm_sccpCgPa_sccpAddress_globalTitle_number;
	public String correlatedSriSm_sccpCgPa_sccpAddress_globalTitle_NAI;
	public String correlatedSriSm_mapSmsc_country;
	public String correlatedSriSm_mapSmsc_network;
	public String correlatedSriSm_mapSmsc_gsmAddress_ton;
	public String correlatedSriSm_mapSmsc_gsmAddress_npi;
	public String correlatedSriSm_mapSmsc_gsmAddress_number;
	public String correlatedSriSm_mapMsisdn_country;
	public String correlatedSriSm_mapMsisdn_network;
	public String correlatedSriSm_mapMsisdn_gsmAddress_ton;
	public String correlatedSriSm_mapMsisdn_gsmAddress_npi;
	public String correlatedSriSm_mapMsisdn_gsmAddress_number;
	public String correlatedSriSm_mapImsi_country;
	public String correlatedSriSm_mapImsi_network;
	public String correlatedSriSm_mapImsi_imsi;
	public String correlatedSriSm_mapLmsi;
	public String correlatedSriSm_mapMsc_country;
	public String correlatedSriSm_mapMsc_network;
	public String correlatedSriSm_mapMsc_gsmAddress_ton;
	public String correlatedSriSm_mapMsc_gsmAddress_npi;
	public String correlatedSriSm_mapMsc_gsmAddress_number;
	public String correlatedSriSm_mapSgsn_country;
	public String correlatedSriSm_mapSgsn_network;
	public String correlatedSriSm_mapSgsn_gsmAddress_ton;
	public String correlatedSriSm_mapSgsn_gsmAddress_npi;
	public String correlatedSriSm_mapSgsn_gsmAddress_number;
	public String applicationName;
	public String applicationShortNumber_ton;
	public String applicationShortNumber_npi;
	public String applicationShortNumber_number;
	public String messageFields_protocol;
	public String messageFields_originatorAddress_country;
	public String messageFields_originatorAddress_network;
	public String messageFields_originatorAddress_gsmAddress_ton;
	public String messageFields_originatorAddress_gsmAddress_npi;
	public String messageFields_originatorAddress_gsmAddress_number;
	public String messageFields_recipientAddress_country;
	public String messageFields_recipientAddress_network;
	public String messageFields_recipientAddress_gsmAddress_ton;
	public String messageFields_recipientAddress_gsmAddress_npi;
	public String messageFields_recipientAddress_gsmAddress_number;
	public String messageFields_dataCodingScheme_alphabet;
	public String messageFields_dataCodingScheme_messageClass;
	public double messageFields_protocolIdentifier;
	public String messageFields_notificationType_deliveryNotification;
	public String messageFields_notificationType_nonDeliveryNotification;
	public String messageFields_notificationType_bufferedNotification;
	public String messageFields_userData;
	public String messageFields_userDataHeader;
	public String messageFields_moreMessagesToSend;
	public double messageFields_priority;
	public String messageFields_replyPathIndicator;
	public String messageFields_deferredDeliveryTime;
	public String messageFields_validityPeriod;
	public String messageFields_singleShotIndicator;
	public String messageFields_billingIdentifier;
	public String messageFields_serviceCentreTimestamp;
	public String messageFields_deliveryStatus;
	public double messageFields_errorCode;
	public String messageFields_deliveryTimestamp;
	public double messageFields_tariffClass;
	public double messageFields_serviceDescription;
	public String messageFields_originatedImsi_country;
	public String messageFields_originatedImsi_network;
	public String messageFields_originatedImsi_imsi;
	public String messageFields_originatedMscAddress_country;
	public String messageFields_originatedMscAddress_network;
	public String messageFields_originatedMscAddress_gsmAddress_ton;
	public String messageFields_originatedMscAddress_gsmAddress_npi;
	public String messageFields_originatedMscAddress_gsmAddress_number;
	public String messageFields_serviceCentreAddress_country;
	public String messageFields_serviceCentreAddress_network;
	public String messageFields_serviceCentreAddress_gsmAddress_ton;
	public String messageFields_serviceCentreAddress_gsmAddress_npi;
	public String messageFields_serviceCentreAddress_gsmAddress_number;
	public String messageFields_alphanumericOriginator;
	public String messageFields_alphanumericRecipient;
	public String messageFields_gsmStatusReportType;
	public double messageFields_originatingPointCode;
	public  double messageFields_portNumber;
	public double messageFields_gsmMessageReference;
	public double messageFields_sourcePort;
	public double messageFields_destinationPort;
	public String messageFields_endToEndAckRequest_readAck;
	public String messageFields_endToEndAckRequest_userAck;
	public String messageFields_endToEndMessageType;
	public double messageFields_messageReference;
	public String messageFields_privacy;
	public double messageFields_numberOfMessages;
	public String messageFields_language;
	public String messageFields_payloadType;
	public String messageFields_sourceSubAddress;
	public String messageFields_destSubAddress;
	public double messageFields_userResponseCode;
	public String messageFields_displayTime;
	public String messageFields_callbackNumbers_number_digitMode;
	public String messageFields_callbackNumbers_number_address_ton;
	public String messageFields_callbackNumbers_number_address_npi;
	public String messageFields_callbackNumbers_number_address_number;
	public String messageFields_callbackNumbers_presentation;
	public String messageFields_callbackNumbers_alphaTag;
	public String messageFields_msValidityIndicator;
	public String messageFields_msValidityPeriod_unit;
	public double messageFields_msValidityPeriod_multiplier;
	public String messageFields_alertOnMessageDelivery;
	public String messageFields_SmsSignal;
	public String messageFields_sourceBearerType;
	public String messageFields_destBearerType;
	public double messageFields_SmDefaultMsgID;
	public String messageFields_sourceNetworkType;
	public String messageFields_destNetworkType;
	public String messageFields_xsMessageType;
	public String outboundMo_submissionResult;
	public String outboundMo_smscName;
	public String outboundMo_rejectCause;
	public String outboundMt_sriSmRoutingAction;
	public String outboundMt_sriSmRejectInfo_rejectCause;
	public String outboundMt_sriSmRejectInfo_mtRoutingRule;
	public String outboundMt_sriSmRejectInfo_mtExtConditionRule;
	public String outboundMt_sriSmResponseInfo_queryResult;
	public String outboundMt_sriSmResponseInfo_mapImsi_country;
	public String outboundMt_sriSmResponseInfo_mapImsi_network;
	public String outboundMt_sriSmResponseInfo_mapImsi_imsi;
	public String outboundMt_sriSmResponseInfo_mapLmsi;
	public String outboundMt_sriSmResponseInfo_mapMsc_country;
	public String outboundMt_sriSmResponseInfo_mapMsc_network;
	public String outboundMt_sriSmResponseInfo_mapMsc_gsmAddress_ton;
	public String outboundMt_sriSmResponseInfo_mapMsc_gsmAddress_npi;
	public String outboundMt_sriSmResponseInfo_mapMsc_gsmAddress_number;
	public String outboundMt_sriSmResponseInfo_mapSgsn_country;
	public String outboundMt_sriSmResponseInfo_mapSgsn_network;
	public String outboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_ton;
	public String outboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_npi;
	public String outboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_number;
	public String outboundMt_sriSmResponseInfo_mtRoutingRule;
	public String outboundMt_mtFwdSmToMscRoutingAction;
	public String outboundMt_mtFwdSmToMscRejectInfo_rejectCause;
	public String outboundMt_mtFwdSmToMscRejectInfo_mtRoutingRule;
	public String outboundMt_mtFwdSmToMscRejectInfo_mtExtConditionRule;
	public String outboundMt_mtFwdSmToMscResponseInfo_deliveryResult;
	public String outboundMt_mtFwdSmToMscResponseInfo_mtRoutingRule;
	public String outboundMt_mtFwdSmToSgsnRoutingAction;
	public String outboundMt_mtFwdSmToSgsnRejectInfo_rejectCause;
	public String outboundMt_mtFwdSmToSgsnRejectInfo_mtRoutingRule;
	public String outboundMt_mtFwdSmToSgsnRejectInfo_mtExtConditionRule;
	public String outboundMt_mtFwdSmToSgsnResponseInfo_deliveryResult;
	public String outboundMt_mtFwdSmToSgsnResponseInfo_mtRoutingRule;
	public String outboundMt_ecResponseData_extConditionRule;
	public String outboundMt_ecResponseData_applicationName;
	public double outboundMt_ecResponseData_clientIpAddress;
	public String outboundMt_ecResponseData_evaluationResult;
	public String outboundMt_ecResponseData_attributesSet;
	public String outboundMt_ecResponseData_attributesReset;
	public double outboundMt_ecResponseData_diameterStatus;
	public String outboundMt_ecResponseData_textInEvaluationResponse;
	public String outboundAo_submissionResult;
	public String outboundAo_applicationName;
	public String outboundAo_applicationShortNumber_ton;
	public String outboundAo_applicationShortNumber_npi;
	public String outboundAo_applicationShortNumber_number;
	public String outboundAo_serviceCentreName;
	public String outboundAo_scNodeName;
	public String outboundAo_scTerminationPointName;
	public double outboundAo_routingErrorCode;
	public String outboundAo_serviceCentreTimestamp;
	public String outboundAo_smppMessageId;
	public String outboundAt_routingAction;
	public String outboundAt_rejectInfo_rejectCause;
	public String outboundAt_rejectInfo_atRoutingRule;
	public String outboundAt_rejectInfo_atExtConditionRule;
	public String outboundAt_responseInfo_deliveryResult;
	public double outboundAt_responseInfo_routingErrorCode;
	public String outboundAt_responseInfo_atRoutingRule;
	public String outboundAt_applicationName;
	public String outboundAt_applicationShortNumber_ton;
	public String outboundAt_applicationShortNumber_npi;
	public String outboundAt_applicationShortNumber_number;
	public String outboundAt_ecResponseData_extConditionRule;
	public String outboundAt_ecResponseData_applicationName;
	public double outboundAt_ecResponseData_clientIpAddress;
	public String outboundAt_ecResponseData_evaluationResult;
	public String outboundAt_ecResponseData_attributesSet;
	public String outboundAt_ecResponseData_attributesReset;
	public double outboundAt_ecResponseData_diameterStatus;
	public String outboundAt_ecResponseData_textInEvaluationResponse;
	public String storage_storageResult;
	public double storage_routingErrorCode;
	public String storage_applicationName;
	public String storage_applicationShortNumber_ton;
	public String storage_applicationShortNumber_npi;
	public String storage_applicationShortNumber_number;
	public double storage_queue;
	public String moreMessagesToSend;
	public double numberOfPreviousAttempts;
	public String serviceCentreTimestamp;
	public double messageIdentifier_amsId;
	public double messageIdentifier_msgId;
	public String isNotificationMessage;
	public String unConditionalForward;
	public double event;
	public String msgCommand;
	public String smResult;
	public String cmdOriginatorAddress_country;
	public String cmdOriginatorAddress_network;
	public String cmdOriginatorAddress_gsmAddress_ton;
	public String cmdOriginatorAddress_gsmAddress_npi;
	public String cmdOriginatorAddress_gsmAddress_number;
	public String cmdAlphanumericOriginator;
	public String cmdRecipientAddress_country;
	public String cmdRecipientAddress_network;
	public String cmdRecipientAddress_gsmAddress_ton;
	public String cmdRecipientAddress_gsmAddress_npi;
	public String cmdRecipientAddress_gsmAddress_number;
	public double foundCount;
	public String cancelMode;
	public String recipientRoutingNumber;
	public String ecResponseData_extConditionRule;
	public String ecResponseData_applicationName;
	public double ecResponseData_clientIpAddress;
	public String ecResponseData_evaluationResult;
	public String ecResponseData_attributesSet;
	public String ecResponseData_attributesReset;
	public double ecResponseData_diameterStatus;
	public String ecResponseData_textInEvaluationResponse;
	public String mtRoutingRuleSkipped;
	public String originalMessageFields_originatorAddress_country;
	public String originalMessageFields_originatorAddress_network;
	public String originalMessageFields_originatorAddress_gsmAddress_ton;
	public String originalMessageFields_originatorAddress_gsmAddress_npi;
	public String originalMessageFields_originatorAddress_gsmAddress_number;
	public String originalMessageFields_recipientAddress_country;
	public String originalMessageFields_recipientAddress_network;
	public String originalMessageFields_recipientAddress_gsmAddress_ton;
	public String originalMessageFields_recipientAddress_gsmAddress_npi;
	public String originalMessageFields_recipientAddress_gsmAddress_number;
	public String insideRejectInfo_rejectCause;
	public String insideRejectInfo_atiRoutingRule;
	public String insideRejectInfo_atiExtConditionRule;
	public String insideResponseInfo_deliveryResult;
	public double insideResponseInfo_routingErrorCode;
	public String insideResponseInfo_atiRoutingRule;
	public String ssiInfo_originatorServices;
	public String ssiInfo_recipientServices;
	public String sccpCdPa_sccpAddress_globalTitle_number;
	public String smsDeliver_smsOriginator_gsmAddress_ton;
public String getSccpCdPa_sccpAddress_globalTitle_number() {
		return sccpCdPa_sccpAddress_globalTitle_number;
	}
	public void setSccpCdPa_sccpAddress_globalTitle_number(
			String sccpCdPaSccpAddressGlobalTitleNumber) {
		sccpCdPa_sccpAddress_globalTitle_number = sccpCdPaSccpAddressGlobalTitleNumber;
	}
	public String getSmsDeliver_smsOriginator_gsmAddress_ton() {
		return smsDeliver_smsOriginator_gsmAddress_ton;
	}
	public void setSmsDeliver_smsOriginator_gsmAddress_ton(
			String smsDeliverSmsOriginatorGsmAddressTon) {
		smsDeliver_smsOriginator_gsmAddress_ton = smsDeliverSmsOriginatorGsmAddressTon;
	}
public Date getRec_time() {
		return rec_time;
	}
	public void setRec_time(Date recTime) {
		rec_time = recTime;
	}
	public double getId_file() {
		return id_file;
	}
	public void setId_file(double idFile) {
		id_file = idFile;
	}
	public double getRec_no() {
		return rec_no;
	}
	public void setRec_no(double recNo) {
		rec_no = recNo;
	}
	public String getInboundMessageType() {
		return inboundMessageType;
	}
	public void setInboundMessageType(String inboundMessageType) {
		this.inboundMessageType = inboundMessageType;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getRoutingAction() {
		return routingAction;
	}
	public void setRoutingAction(String routingAction) {
		this.routingAction = routingAction;
	}
	public String getRejectInfo_rejectCause() {
		return rejectInfo_rejectCause;
	}
	public void setRejectInfo_rejectCause(String rejectInfoRejectCause) {
		rejectInfo_rejectCause = rejectInfoRejectCause;
	}
	public String getRejectInfo_moRoutingRule() {
		return rejectInfo_moRoutingRule;
	}
	public void setRejectInfo_moRoutingRule(String rejectInfoMoRoutingRule) {
		rejectInfo_moRoutingRule = rejectInfoMoRoutingRule;
	}
	public String getRejectInfo_moExtConditionRule() {
		return rejectInfo_moExtConditionRule;
	}
	public void setRejectInfo_moExtConditionRule(String rejectInfoMoExtConditionRule) {
		rejectInfo_moExtConditionRule = rejectInfoMoExtConditionRule;
	}
	public String getRejectInfo_mtRoutingRule() {
		return rejectInfo_mtRoutingRule;
	}
	public void setRejectInfo_mtRoutingRule(String rejectInfoMtRoutingRule) {
		rejectInfo_mtRoutingRule = rejectInfoMtRoutingRule;
	}
	public String getRejectInfo_mtExtConditionRule() {
		return rejectInfo_mtExtConditionRule;
	}
	public void setRejectInfo_mtExtConditionRule(String rejectInfoMtExtConditionRule) {
		rejectInfo_mtExtConditionRule = rejectInfoMtExtConditionRule;
	}
	public String getRejectInfo_aoRoutingRule() {
		return rejectInfo_aoRoutingRule;
	}
	public void setRejectInfo_aoRoutingRule(String rejectInfoAoRoutingRule) {
		rejectInfo_aoRoutingRule = rejectInfoAoRoutingRule;
	}
	public String getRejectInfo_aoExtConditionRule() {
		return rejectInfo_aoExtConditionRule;
	}
	public void setRejectInfo_aoExtConditionRule(String rejectInfoAoExtConditionRule) {
		rejectInfo_aoExtConditionRule = rejectInfoAoExtConditionRule;
	}
	public String getRejectInfo_atRoutingRule() {
		return rejectInfo_atRoutingRule;
	}
	public void setRejectInfo_atRoutingRule(String rejectInfoAtRoutingRule) {
		rejectInfo_atRoutingRule = rejectInfoAtRoutingRule;
	}
	public String getRejectInfo_atExtConditionRule() {
		return rejectInfo_atExtConditionRule;
	}
	public void setRejectInfo_atExtConditionRule(String rejectInfoAtExtConditionRule) {
		rejectInfo_atExtConditionRule = rejectInfoAtExtConditionRule;
	}
	public String getRejectInfo_mnpViolation() {
		return rejectInfo_mnpViolation;
	}
	public void setRejectInfo_mnpViolation(String rejectInfoMnpViolation) {
		rejectInfo_mnpViolation = rejectInfoMnpViolation;
	}
	public String getResponseInfo_submissionResult() {
		return responseInfo_submissionResult;
	}
	public void setResponseInfo_submissionResult(String responseInfoSubmissionResult) {
		responseInfo_submissionResult = responseInfoSubmissionResult;
	}
	public String getResponseInfo_moRoutingRule() {
		return responseInfo_moRoutingRule;
	}
	public void setResponseInfo_moRoutingRule(String responseInfoMoRoutingRule) {
		responseInfo_moRoutingRule = responseInfoMoRoutingRule;
	}
	public String getResponseInfo_mtRoutingRule() {
		return responseInfo_mtRoutingRule;
	}
	public void setResponseInfo_mtRoutingRule(String responseInfoMtRoutingRule) {
		responseInfo_mtRoutingRule = responseInfoMtRoutingRule;
	}
	public String getResponseInfo_aoRoutingRule() {
		return responseInfo_aoRoutingRule;
	}
	public void setResponseInfo_aoRoutingRule(String responseInfoAoRoutingRule) {
		responseInfo_aoRoutingRule = responseInfoAoRoutingRule;
	}
	public String getResponseInfo_atRoutingRule() {
		return responseInfo_atRoutingRule;
	}
	public void setResponseInfo_atRoutingRule(String responseInfoAtRoutingRule) {
		responseInfo_atRoutingRule = responseInfoAtRoutingRule;
	}
	public String getResponseInfo_queryResult() {
		return responseInfo_queryResult;
	}
	public void setResponseInfo_queryResult(String responseInfoQueryResult) {
		responseInfo_queryResult = responseInfoQueryResult;
	}
	public String getResponseInfo_mapImsi_country() {
		return responseInfo_mapImsi_country;
	}
	public void setResponseInfo_mapImsi_country(String responseInfoMapImsiCountry) {
		responseInfo_mapImsi_country = responseInfoMapImsiCountry;
	}
	public String getResponseInfo_mapImsi_network() {
		return responseInfo_mapImsi_network;
	}
	public void setResponseInfo_mapImsi_network(String responseInfoMapImsiNetwork) {
		responseInfo_mapImsi_network = responseInfoMapImsiNetwork;
	}
	public String getResponseInfo_mapImsi_imsi() {
		return responseInfo_mapImsi_imsi;
	}
	public void setResponseInfo_mapImsi_imsi(String responseInfoMapImsiImsi) {
		responseInfo_mapImsi_imsi = responseInfoMapImsiImsi;
	}
	public String getResponseInfo_scrambledImsi_country() {
		return responseInfo_scrambledImsi_country;
	}
	public void setResponseInfo_scrambledImsi_country(
			String responseInfoScrambledImsiCountry) {
		responseInfo_scrambledImsi_country = responseInfoScrambledImsiCountry;
	}
	public String getResponseInfo_scrambledImsi_network() {
		return responseInfo_scrambledImsi_network;
	}
	public void setResponseInfo_scrambledImsi_network(
			String responseInfoScrambledImsiNetwork) {
		responseInfo_scrambledImsi_network = responseInfoScrambledImsiNetwork;
	}
	public String getResponseInfo_scrambledImsi_imsi() {
		return responseInfo_scrambledImsi_imsi;
	}
	public void setResponseInfo_scrambledImsi_imsi(
			String responseInfoScrambledImsiImsi) {
		responseInfo_scrambledImsi_imsi = responseInfoScrambledImsiImsi;
	}
	public String getResponseInfo_mapLmsi() {
		return responseInfo_mapLmsi;
	}
	public void setResponseInfo_mapLmsi(String responseInfoMapLmsi) {
		responseInfo_mapLmsi = responseInfoMapLmsi;
	}
	public String getResponseInfo_mapMsc_country() {
		return responseInfo_mapMsc_country;
	}
	public void setResponseInfo_mapMsc_country(String responseInfoMapMscCountry) {
		responseInfo_mapMsc_country = responseInfoMapMscCountry;
	}
	public String getResponseInfo_mapMsc_network() {
		return responseInfo_mapMsc_network;
	}
	public void setResponseInfo_mapMsc_network(String responseInfoMapMscNetwork) {
		responseInfo_mapMsc_network = responseInfoMapMscNetwork;
	}
	public String getResponseInfo_mapMsc_gsmAddress_ton() {
		return responseInfo_mapMsc_gsmAddress_ton;
	}
	public void setResponseInfo_mapMsc_gsmAddress_ton(
			String responseInfoMapMscGsmAddressTon) {
		responseInfo_mapMsc_gsmAddress_ton = responseInfoMapMscGsmAddressTon;
	}
	public String getResponseInfo_mapMsc_gsmAddress_npi() {
		return responseInfo_mapMsc_gsmAddress_npi;
	}
	public void setResponseInfo_mapMsc_gsmAddress_npi(
			String responseInfoMapMscGsmAddressNpi) {
		responseInfo_mapMsc_gsmAddress_npi = responseInfoMapMscGsmAddressNpi;
	}
	public String getResponseInfo_mapMsc_gsmAddress_number() {
		return responseInfo_mapMsc_gsmAddress_number;
	}
	public void setResponseInfo_mapMsc_gsmAddress_number(
			String responseInfoMapMscGsmAddressNumber) {
		responseInfo_mapMsc_gsmAddress_number = responseInfoMapMscGsmAddressNumber;
	}
	public String getResponseInfo_mapSgsn_country() {
		return responseInfo_mapSgsn_country;
	}
	public void setResponseInfo_mapSgsn_country(String responseInfoMapSgsnCountry) {
		responseInfo_mapSgsn_country = responseInfoMapSgsnCountry;
	}
	public String getResponseInfo_mapSgsn_network() {
		return responseInfo_mapSgsn_network;
	}
	public void setResponseInfo_mapSgsn_network(String responseInfoMapSgsnNetwork) {
		responseInfo_mapSgsn_network = responseInfoMapSgsnNetwork;
	}
	public String getResponseInfo_mapSgsn_gsmAddress_ton() {
		return responseInfo_mapSgsn_gsmAddress_ton;
	}
	public void setResponseInfo_mapSgsn_gsmAddress_ton(
			String responseInfoMapSgsnGsmAddressTon) {
		responseInfo_mapSgsn_gsmAddress_ton = responseInfoMapSgsnGsmAddressTon;
	}
	public String getResponseInfo_mapSgsn_gsmAddress_npi() {
		return responseInfo_mapSgsn_gsmAddress_npi;
	}
	public void setResponseInfo_mapSgsn_gsmAddress_npi(
			String responseInfoMapSgsnGsmAddressNpi) {
		responseInfo_mapSgsn_gsmAddress_npi = responseInfoMapSgsnGsmAddressNpi;
	}
	public String getResponseInfo_mapSgsn_gsmAddress_number() {
		return responseInfo_mapSgsn_gsmAddress_number;
	}
	public void setResponseInfo_mapSgsn_gsmAddress_number(
			String responseInfoMapSgsnGsmAddressNumber) {
		responseInfo_mapSgsn_gsmAddress_number = responseInfoMapSgsnGsmAddressNumber;
	}
	public String getResponseInfo_deliveryResult() {
		return responseInfo_deliveryResult;
	}
	public void setResponseInfo_deliveryResult(String responseInfoDeliveryResult) {
		responseInfo_deliveryResult = responseInfoDeliveryResult;
	}
	public double getResponseInfo_routingErrorCode() {
		return responseInfo_routingErrorCode;
	}
	public void setResponseInfo_routingErrorCode(double responseInfoRoutingErrorCode) {
		responseInfo_routingErrorCode = responseInfoRoutingErrorCode;
	}
	public String getResponseInfo_serviceCentreTimestamp() {
		return responseInfo_serviceCentreTimestamp;
	}
	public void setResponseInfo_serviceCentreTimestamp(
			String responseInfoServiceCentreTimestamp) {
		responseInfo_serviceCentreTimestamp = responseInfoServiceCentreTimestamp;
	}
	public String getIgnoredRejectCauses_unknownSccpSmscAddress() {
		return ignoredRejectCauses_unknownSccpSmscAddress;
	}
	public void setIgnoredRejectCauses_unknownSccpSmscAddress(
			String ignoredRejectCausesUnknownSccpSmscAddress) {
		ignoredRejectCauses_unknownSccpSmscAddress = ignoredRejectCausesUnknownSccpSmscAddress;
	}
	public String getIgnoredRejectCauses_unknownMapSmscAddress() {
		return ignoredRejectCauses_unknownMapSmscAddress;
	}
	public void setIgnoredRejectCauses_unknownMapSmscAddress(
			String ignoredRejectCausesUnknownMapSmscAddress) {
		ignoredRejectCauses_unknownMapSmscAddress = ignoredRejectCausesUnknownMapSmscAddress;
	}
	public String getIgnoredRejectCauses_conflictingSmscAddresses() {
		return ignoredRejectCauses_conflictingSmscAddresses;
	}
	public void setIgnoredRejectCauses_conflictingSmscAddresses(
			String ignoredRejectCausesConflictingSmscAddresses) {
		ignoredRejectCauses_conflictingSmscAddresses = ignoredRejectCausesConflictingSmscAddresses;
	}
	public String getIgnoredRejectCauses_spoofingSccpSmscAddress() {
		return ignoredRejectCauses_spoofingSccpSmscAddress;
	}
	public void setIgnoredRejectCauses_spoofingSccpSmscAddress(
			String ignoredRejectCausesSpoofingSccpSmscAddress) {
		ignoredRejectCauses_spoofingSccpSmscAddress = ignoredRejectCausesSpoofingSccpSmscAddress;
	}
	public String getIgnoredRejectCauses_spoofingMapSmscAddress() {
		return ignoredRejectCauses_spoofingMapSmscAddress;
	}
	public void setIgnoredRejectCauses_spoofingMapSmscAddress(
			String ignoredRejectCausesSpoofingMapSmscAddress) {
		ignoredRejectCauses_spoofingMapSmscAddress = ignoredRejectCausesSpoofingMapSmscAddress;
	}
	public String getIgnoredRejectCauses_spoofedOriginatorAddress() {
		return ignoredRejectCauses_spoofedOriginatorAddress;
	}
	public void setIgnoredRejectCauses_spoofedOriginatorAddress(
			String ignoredRejectCausesSpoofedOriginatorAddress) {
		ignoredRejectCauses_spoofedOriginatorAddress = ignoredRejectCausesSpoofedOriginatorAddress;
	}
	public String getSccpCgPaOfFirstSegment_country() {
		return sccpCgPaOfFirstSegment_country;
	}
	public void setSccpCgPaOfFirstSegment_country(
			String sccpCgPaOfFirstSegmentCountry) {
		sccpCgPaOfFirstSegment_country = sccpCgPaOfFirstSegmentCountry;
	}
	public String getSccpCgPaOfFirstSegment_network() {
		return sccpCgPaOfFirstSegment_network;
	}
	public void setSccpCgPaOfFirstSegment_network(
			String sccpCgPaOfFirstSegmentNetwork) {
		sccpCgPaOfFirstSegment_network = sccpCgPaOfFirstSegmentNetwork;
	}
	public double getSccpCgPaOfFirstSegment_sccpAddress_signallingPointCode() {
		return sccpCgPaOfFirstSegment_sccpAddress_signallingPointCode;
	}
	public void setSccpCgPaOfFirstSegment_sccpAddress_signallingPointCode(
			double sccpCgPaOfFirstSegmentSccpAddressSignallingPointCode) {
		sccpCgPaOfFirstSegment_sccpAddress_signallingPointCode = sccpCgPaOfFirstSegmentSccpAddressSignallingPointCode;
	}
	public String getSccpCgPaOfFirstSegment_sccpAddress_subsystemNumber() {
		return sccpCgPaOfFirstSegment_sccpAddress_subsystemNumber;
	}
	public void setSccpCgPaOfFirstSegment_sccpAddress_subsystemNumber(
			String sccpCgPaOfFirstSegmentSccpAddressSubsystemNumber) {
		sccpCgPaOfFirstSegment_sccpAddress_subsystemNumber = sccpCgPaOfFirstSegmentSccpAddressSubsystemNumber;
	}
	public String getSccpCgPaOfFirstSegment_sccpAddress_globalTitle_numberingPlan() {
		return sccpCgPaOfFirstSegment_sccpAddress_globalTitle_numberingPlan;
	}
	public void setSccpCgPaOfFirstSegment_sccpAddress_globalTitle_numberingPlan(
			String sccpCgPaOfFirstSegmentSccpAddressGlobalTitleNumberingPlan) {
		sccpCgPaOfFirstSegment_sccpAddress_globalTitle_numberingPlan = sccpCgPaOfFirstSegmentSccpAddressGlobalTitleNumberingPlan;
	}
	public String getSccpCgPaOfFirstSegment_sccpAddress_globalTitle_number() {
		return sccpCgPaOfFirstSegment_sccpAddress_globalTitle_number;
	}
	public void setSccpCgPaOfFirstSegment_sccpAddress_globalTitle_number(
			String sccpCgPaOfFirstSegmentSccpAddressGlobalTitleNumber) {
		sccpCgPaOfFirstSegment_sccpAddress_globalTitle_number = sccpCgPaOfFirstSegmentSccpAddressGlobalTitleNumber;
	}
	public String getSccpCgPaOfFirstSegment_sccpAddress_globalTitle_NAI() {
		return sccpCgPaOfFirstSegment_sccpAddress_globalTitle_NAI;
	}
	public void setSccpCgPaOfFirstSegment_sccpAddress_globalTitle_NAI(
			String sccpCgPaOfFirstSegmentSccpAddressGlobalTitleNAI) {
		sccpCgPaOfFirstSegment_sccpAddress_globalTitle_NAI = sccpCgPaOfFirstSegmentSccpAddressGlobalTitleNAI;
	}
	public String getSccpCdPaOfFirstSegment_country() {
		return sccpCdPaOfFirstSegment_country;
	}
	public void setSccpCdPaOfFirstSegment_country(
			String sccpCdPaOfFirstSegmentCountry) {
		sccpCdPaOfFirstSegment_country = sccpCdPaOfFirstSegmentCountry;
	}
	public String getSccpCdPaOfFirstSegment_network() {
		return sccpCdPaOfFirstSegment_network;
	}
	public void setSccpCdPaOfFirstSegment_network(
			String sccpCdPaOfFirstSegmentNetwork) {
		sccpCdPaOfFirstSegment_network = sccpCdPaOfFirstSegmentNetwork;
	}
	public double getSccpCdPaOfFirstSegment_sccpAddress_signallingPointCode() {
		return sccpCdPaOfFirstSegment_sccpAddress_signallingPointCode;
	}
	public void setSccpCdPaOfFirstSegment_sccpAddress_signallingPointCode(
			double sccpCdPaOfFirstSegmentSccpAddressSignallingPointCode) {
		sccpCdPaOfFirstSegment_sccpAddress_signallingPointCode = sccpCdPaOfFirstSegmentSccpAddressSignallingPointCode;
	}
	public String getSccpCdPaOfFirstSegment_sccpAddress_subsystemNumber() {
		return sccpCdPaOfFirstSegment_sccpAddress_subsystemNumber;
	}
	public void setSccpCdPaOfFirstSegment_sccpAddress_subsystemNumber(
			String sccpCdPaOfFirstSegmentSccpAddressSubsystemNumber) {
		sccpCdPaOfFirstSegment_sccpAddress_subsystemNumber = sccpCdPaOfFirstSegmentSccpAddressSubsystemNumber;
	}
	public String getSccpCdPaOfFirstSegment_sccpAddress_globalTitle_numberingPlan() {
		return sccpCdPaOfFirstSegment_sccpAddress_globalTitle_numberingPlan;
	}
	public void setSccpCdPaOfFirstSegment_sccpAddress_globalTitle_numberingPlan(
			String sccpCdPaOfFirstSegmentSccpAddressGlobalTitleNumberingPlan) {
		sccpCdPaOfFirstSegment_sccpAddress_globalTitle_numberingPlan = sccpCdPaOfFirstSegmentSccpAddressGlobalTitleNumberingPlan;
	}
	public String getSccpCdPaOfFirstSegment_sccpAddress_globalTitle_number() {
		return sccpCdPaOfFirstSegment_sccpAddress_globalTitle_number;
	}
	public void setSccpCdPaOfFirstSegment_sccpAddress_globalTitle_number(
			String sccpCdPaOfFirstSegmentSccpAddressGlobalTitleNumber) {
		sccpCdPaOfFirstSegment_sccpAddress_globalTitle_number = sccpCdPaOfFirstSegmentSccpAddressGlobalTitleNumber;
	}
	public String getSccpCdPaOfFirstSegment_sccpAddress_globalTitle_NAI() {
		return sccpCdPaOfFirstSegment_sccpAddress_globalTitle_NAI;
	}
	public void setSccpCdPaOfFirstSegment_sccpAddress_globalTitle_NAI(
			String sccpCdPaOfFirstSegmentSccpAddressGlobalTitleNAI) {
		sccpCdPaOfFirstSegment_sccpAddress_globalTitle_NAI = sccpCdPaOfFirstSegmentSccpAddressGlobalTitleNAI;
	}
	public String getSccpCgPa_country() {
		return sccpCgPa_country;
	}
	public void setSccpCgPa_country(String sccpCgPaCountry) {
		sccpCgPa_country = sccpCgPaCountry;
	}
	public String getSccpCgPa_network() {
		return sccpCgPa_network;
	}
	public void setSccpCgPa_network(String sccpCgPaNetwork) {
		sccpCgPa_network = sccpCgPaNetwork;
	}
	public double getSccpCgPa_sccpAddress_signallingPointCode() {
		return sccpCgPa_sccpAddress_signallingPointCode;
	}
	public void setSccpCgPa_sccpAddress_signallingPointCode(
			double sccpCgPaSccpAddressSignallingPointCode) {
		sccpCgPa_sccpAddress_signallingPointCode = sccpCgPaSccpAddressSignallingPointCode;
	}
	public String getSccpCgPa_sccpAddress_subsystemNumber() {
		return sccpCgPa_sccpAddress_subsystemNumber;
	}
	public void setSccpCgPa_sccpAddress_subsystemNumber(
			String sccpCgPaSccpAddressSubsystemNumber) {
		sccpCgPa_sccpAddress_subsystemNumber = sccpCgPaSccpAddressSubsystemNumber;
	}
	public String getSccpCgPa_sccpAddress_globalTitle_numberingPlan() {
		return sccpCgPa_sccpAddress_globalTitle_numberingPlan;
	}
	public void setSccpCgPa_sccpAddress_globalTitle_numberingPlan(
			String sccpCgPaSccpAddressGlobalTitleNumberingPlan) {
		sccpCgPa_sccpAddress_globalTitle_numberingPlan = sccpCgPaSccpAddressGlobalTitleNumberingPlan;
	}
	public String getSccpCgPa_sccpAddress_globalTitle_number() {
		return sccpCgPa_sccpAddress_globalTitle_number;
	}
	public void setSccpCgPa_sccpAddress_globalTitle_number(
			String sccpCgPaSccpAddressGlobalTitleNumber) {
		sccpCgPa_sccpAddress_globalTitle_number = sccpCgPaSccpAddressGlobalTitleNumber;
	}
	public String getSccpCgPa_sccpAddress_globalTitle_NAI() {
		return sccpCgPa_sccpAddress_globalTitle_NAI;
	}
	public void setSccpCgPa_sccpAddress_globalTitle_NAI(
			String sccpCgPaSccpAddressGlobalTitleNAI) {
		sccpCgPa_sccpAddress_globalTitle_NAI = sccpCgPaSccpAddressGlobalTitleNAI;
	}
	public String getSccpCdPa_country() {
		return sccpCdPa_country;
	}
	public void setSccpCdPa_country(String sccpCdPaCountry) {
		sccpCdPa_country = sccpCdPaCountry;
	}
	public String getSccpCdPa_network() {
		return sccpCdPa_network;
	}
	public void setSccpCdPa_network(String sccpCdPaNetwork) {
		sccpCdPa_network = sccpCdPaNetwork;
	}
	public double getSccpCdPa_sccpAddress_signallingPointCode() {
		return sccpCdPa_sccpAddress_signallingPointCode;
	}
	public void setSccpCdPa_sccpAddress_signallingPointCode(
			double sccpCdPaSccpAddressSignallingPointCode) {
		sccpCdPa_sccpAddress_signallingPointCode = sccpCdPaSccpAddressSignallingPointCode;
	}
	public String getSccpCdPa_sccpAddress_subsystemNumber() {
		return sccpCdPa_sccpAddress_subsystemNumber;
	}
	public void setSccpCdPa_sccpAddress_subsystemNumber(
			String sccpCdPaSccpAddressSubsystemNumber) {
		sccpCdPa_sccpAddress_subsystemNumber = sccpCdPaSccpAddressSubsystemNumber;
	}
	public String getSccpCdPa_sccpAddress_globalTitle_numberingPlan() {
		return sccpCdPa_sccpAddress_globalTitle_numberingPlan;
	}
	public void setSccpCdPa_sccpAddress_globalTitle_numberingPlan(
			String sccpCdPaSccpAddressGlobalTitleNumberingPlan) {
		sccpCdPa_sccpAddress_globalTitle_numberingPlan = sccpCdPaSccpAddressGlobalTitleNumberingPlan;
	}
	public String getSccpCdPa_sccpAddress_globalTitle_NAI() {
		return sccpCdPa_sccpAddress_globalTitle_NAI;
	}
	public void setSccpCdPa_sccpAddress_globalTitle_NAI(
			String sccpCdPaSccpAddressGlobalTitleNAI) {
		sccpCdPa_sccpAddress_globalTitle_NAI = sccpCdPaSccpAddressGlobalTitleNAI;
	}
	public String getMapSmsc_country() {
		return mapSmsc_country;
	}
	public void setMapSmsc_country(String mapSmscCountry) {
		mapSmsc_country = mapSmscCountry;
	}
	public String getMapSmsc_network() {
		return mapSmsc_network;
	}
	public void setMapSmsc_network(String mapSmscNetwork) {
		mapSmsc_network = mapSmscNetwork;
	}
	public String getMapSmsc_gsmAddress_ton() {
		return mapSmsc_gsmAddress_ton;
	}
	public void setMapSmsc_gsmAddress_ton(String mapSmscGsmAddressTon) {
		mapSmsc_gsmAddress_ton = mapSmscGsmAddressTon;
	}
	public String getMapSmsc_gsmAddress_npi() {
		return mapSmsc_gsmAddress_npi;
	}
	public void setMapSmsc_gsmAddress_npi(String mapSmscGsmAddressNpi) {
		mapSmsc_gsmAddress_npi = mapSmscGsmAddressNpi;
	}
	public String getMapSmsc_gsmAddress_number() {
		return mapSmsc_gsmAddress_number;
	}
	public void setMapSmsc_gsmAddress_number(String mapSmscGsmAddressNumber) {
		mapSmsc_gsmAddress_number = mapSmscGsmAddressNumber;
	}
	public String getMapMsisdn_country() {
		return mapMsisdn_country;
	}
	public void setMapMsisdn_country(String mapMsisdnCountry) {
		mapMsisdn_country = mapMsisdnCountry;
	}
	public String getMapMsisdn_network() {
		return mapMsisdn_network;
	}
	public void setMapMsisdn_network(String mapMsisdnNetwork) {
		mapMsisdn_network = mapMsisdnNetwork;
	}
	public String getMapMsisdn_gsmAddress_ton() {
		return mapMsisdn_gsmAddress_ton;
	}
	public void setMapMsisdn_gsmAddress_ton(String mapMsisdnGsmAddressTon) {
		mapMsisdn_gsmAddress_ton = mapMsisdnGsmAddressTon;
	}
	public String getMapMsisdn_gsmAddress_npi() {
		return mapMsisdn_gsmAddress_npi;
	}
	public void setMapMsisdn_gsmAddress_npi(String mapMsisdnGsmAddressNpi) {
		mapMsisdn_gsmAddress_npi = mapMsisdnGsmAddressNpi;
	}
	public String getMapMsisdn_gsmAddress_number() {
		return mapMsisdn_gsmAddress_number;
	}
	public void setMapMsisdn_gsmAddress_number(String mapMsisdnGsmAddressNumber) {
		mapMsisdn_gsmAddress_number = mapMsisdnGsmAddressNumber;
	}
	public String getMapImsi_country() {
		return mapImsi_country;
	}
	public void setMapImsi_country(String mapImsiCountry) {
		mapImsi_country = mapImsiCountry;
	}
	public String getMapImsi_network() {
		return mapImsi_network;
	}
	public void setMapImsi_network(String mapImsiNetwork) {
		mapImsi_network = mapImsiNetwork;
	}
	public String getMapImsi_imsi() {
		return mapImsi_imsi;
	}
	public void setMapImsi_imsi(String mapImsiImsi) {
		mapImsi_imsi = mapImsiImsi;
	}
	public String getMapLmsi() {
		return mapLmsi;
	}
	public void setMapLmsi(String mapLmsi) {
		this.mapLmsi = mapLmsi;
	}
	public String getSmsSubmit_smsServices_rejectDuplicates() {
		return smsSubmit_smsServices_rejectDuplicates;
	}
	public void setSmsSubmit_smsServices_rejectDuplicates(
			String smsSubmitSmsServicesRejectDuplicates) {
		smsSubmit_smsServices_rejectDuplicates = smsSubmitSmsServicesRejectDuplicates;
	}
	public String getSmsSubmit_smsServices_statusReportRequest() {
		return smsSubmit_smsServices_statusReportRequest;
	}
	public void setSmsSubmit_smsServices_statusReportRequest(
			String smsSubmitSmsServicesStatusReportRequest) {
		smsSubmit_smsServices_statusReportRequest = smsSubmitSmsServicesStatusReportRequest;
	}
	public String getSmsSubmit_smsServices_userDataHeaderIndication() {
		return smsSubmit_smsServices_userDataHeaderIndication;
	}
	public void setSmsSubmit_smsServices_userDataHeaderIndication(
			String smsSubmitSmsServicesUserDataHeaderIndication) {
		smsSubmit_smsServices_userDataHeaderIndication = smsSubmitSmsServicesUserDataHeaderIndication;
	}
	public String getSmsSubmit_smsServices_replyPath() {
		return smsSubmit_smsServices_replyPath;
	}
	public void setSmsSubmit_smsServices_replyPath(
			String smsSubmitSmsServicesReplyPath) {
		smsSubmit_smsServices_replyPath = smsSubmitSmsServicesReplyPath;
	}
	public double getSmsSubmit_smsMessageReference() {
		return smsSubmit_smsMessageReference;
	}
	public void setSmsSubmit_smsMessageReference(double smsSubmitSmsMessageReference) {
		smsSubmit_smsMessageReference = smsSubmitSmsMessageReference;
	}
	public double getSmsSubmit_smsProtocolId() {
		return smsSubmit_smsProtocolId;
	}
	public void setSmsSubmit_smsProtocolId(double smsSubmitSmsProtocolId) {
		smsSubmit_smsProtocolId = smsSubmitSmsProtocolId;
	}
	public String getSmsSubmit_smsRecipient_country() {
		return smsSubmit_smsRecipient_country;
	}
	public void setSmsSubmit_smsRecipient_country(
			String smsSubmitSmsRecipientCountry) {
		smsSubmit_smsRecipient_country = smsSubmitSmsRecipientCountry;
	}
	public String getSmsSubmit_smsRecipient_network() {
		return smsSubmit_smsRecipient_network;
	}
	public void setSmsSubmit_smsRecipient_network(
			String smsSubmitSmsRecipientNetwork) {
		smsSubmit_smsRecipient_network = smsSubmitSmsRecipientNetwork;
	}
	public String getSmsSubmit_smsRecipient_gsmAddress_ton() {
		return smsSubmit_smsRecipient_gsmAddress_ton;
	}
	public void setSmsSubmit_smsRecipient_gsmAddress_ton(
			String smsSubmitSmsRecipientGsmAddressTon) {
		smsSubmit_smsRecipient_gsmAddress_ton = smsSubmitSmsRecipientGsmAddressTon;
	}
	public String getSmsSubmit_smsRecipient_gsmAddress_npi() {
		return smsSubmit_smsRecipient_gsmAddress_npi;
	}
	public void setSmsSubmit_smsRecipient_gsmAddress_npi(
			String smsSubmitSmsRecipientGsmAddressNpi) {
		smsSubmit_smsRecipient_gsmAddress_npi = smsSubmitSmsRecipientGsmAddressNpi;
	}
	public String getSmsSubmit_smsRecipient_gsmAddress_number() {
		return smsSubmit_smsRecipient_gsmAddress_number;
	}
	public void setSmsSubmit_smsRecipient_gsmAddress_number(
			String smsSubmitSmsRecipientGsmAddressNumber) {
		smsSubmit_smsRecipient_gsmAddress_number = smsSubmitSmsRecipientGsmAddressNumber;
	}
	public String getSmsSubmit_smsDataCodingScheme_alphabet() {
		return smsSubmit_smsDataCodingScheme_alphabet;
	}
	public void setSmsSubmit_smsDataCodingScheme_alphabet(
			String smsSubmitSmsDataCodingSchemeAlphabet) {
		smsSubmit_smsDataCodingScheme_alphabet = smsSubmitSmsDataCodingSchemeAlphabet;
	}
	public String getSmsSubmit_smsDataCodingScheme_messageClass() {
		return smsSubmit_smsDataCodingScheme_messageClass;
	}
	public void setSmsSubmit_smsDataCodingScheme_messageClass(
			String smsSubmitSmsDataCodingSchemeMessageClass) {
		smsSubmit_smsDataCodingScheme_messageClass = smsSubmitSmsDataCodingSchemeMessageClass;
	}
	public String getSmsSubmit_smsValidityPeriod() {
		return smsSubmit_smsValidityPeriod;
	}
	public void setSmsSubmit_smsValidityPeriod(String smsSubmitSmsValidityPeriod) {
		smsSubmit_smsValidityPeriod = smsSubmitSmsValidityPeriod;
	}
	public String getSmsSubmit_smsUserDataHeader() {
		return smsSubmit_smsUserDataHeader;
	}
	public void setSmsSubmit_smsUserDataHeader(String smsSubmitSmsUserDataHeader) {
		smsSubmit_smsUserDataHeader = smsSubmitSmsUserDataHeader;
	}
	public String getSmsSubmit_smsUserData() {
		return smsSubmit_smsUserData;
	}
	public void setSmsSubmit_smsUserData(String smsSubmitSmsUserData) {
		smsSubmit_smsUserData = smsSubmitSmsUserData;
	}
	public String getSmsCommand_smsServices_statusReportRequest() {
		return smsCommand_smsServices_statusReportRequest;
	}
	public void setSmsCommand_smsServices_statusReportRequest(
			String smsCommandSmsServicesStatusReportRequest) {
		smsCommand_smsServices_statusReportRequest = smsCommandSmsServicesStatusReportRequest;
	}
	public String getSmsCommand_smsServices_userDataHeaderIndication() {
		return smsCommand_smsServices_userDataHeaderIndication;
	}
	public void setSmsCommand_smsServices_userDataHeaderIndication(
			String smsCommandSmsServicesUserDataHeaderIndication) {
		smsCommand_smsServices_userDataHeaderIndication = smsCommandSmsServicesUserDataHeaderIndication;
	}
	public double getSmsCommand_smsMessageReference() {
		return smsCommand_smsMessageReference;
	}
	public void setSmsCommand_smsMessageReference(
			double smsCommandSmsMessageReference) {
		smsCommand_smsMessageReference = smsCommandSmsMessageReference;
	}
	public double getSmsCommand_smsProtocolId() {
		return smsCommand_smsProtocolId;
	}
	public void setSmsCommand_smsProtocolId(double smsCommandSmsProtocolId) {
		smsCommand_smsProtocolId = smsCommandSmsProtocolId;
	}
	public String getSmsCommand_smsCommandType() {
		return smsCommand_smsCommandType;
	}
	public void setSmsCommand_smsCommandType(String smsCommandSmsCommandType) {
		smsCommand_smsCommandType = smsCommandSmsCommandType;
	}
	public double getSmsCommand_smsMessageNumber() {
		return smsCommand_smsMessageNumber;
	}
	public void setSmsCommand_smsMessageNumber(double smsCommandSmsMessageNumber) {
		smsCommand_smsMessageNumber = smsCommandSmsMessageNumber;
	}
	public String getSmsCommand_smsRecipient_country() {
		return smsCommand_smsRecipient_country;
	}
	public void setSmsCommand_smsRecipient_country(
			String smsCommandSmsRecipientCountry) {
		smsCommand_smsRecipient_country = smsCommandSmsRecipientCountry;
	}
	public String getSmsCommand_smsRecipient_network() {
		return smsCommand_smsRecipient_network;
	}
	public void setSmsCommand_smsRecipient_network(
			String smsCommandSmsRecipientNetwork) {
		smsCommand_smsRecipient_network = smsCommandSmsRecipientNetwork;
	}
	public String getSmsCommand_smsRecipient_gsmAddress_ton() {
		return smsCommand_smsRecipient_gsmAddress_ton;
	}
	public void setSmsCommand_smsRecipient_gsmAddress_ton(
			String smsCommandSmsRecipientGsmAddressTon) {
		smsCommand_smsRecipient_gsmAddress_ton = smsCommandSmsRecipientGsmAddressTon;
	}
	public String getSmsCommand_smsRecipient_gsmAddress_npi() {
		return smsCommand_smsRecipient_gsmAddress_npi;
	}
	public void setSmsCommand_smsRecipient_gsmAddress_npi(
			String smsCommandSmsRecipientGsmAddressNpi) {
		smsCommand_smsRecipient_gsmAddress_npi = smsCommandSmsRecipientGsmAddressNpi;
	}
	public String getSmsCommand_smsRecipient_gsmAddress_number() {
		return smsCommand_smsRecipient_gsmAddress_number;
	}
	public void setSmsCommand_smsRecipient_gsmAddress_number(
			String smsCommandSmsRecipientGsmAddressNumber) {
		smsCommand_smsRecipient_gsmAddress_number = smsCommandSmsRecipientGsmAddressNumber;
	}
	public String getSmsCommand_smsCommandData() {
		return smsCommand_smsCommandData;
	}
	public void setSmsCommand_smsCommandData(String smsCommandSmsCommandData) {
		smsCommand_smsCommandData = smsCommandSmsCommandData;
	}
	public String getSmsDeliver_smsServices_moreMessagesToSend() {
		return smsDeliver_smsServices_moreMessagesToSend;
	}
	public void setSmsDeliver_smsServices_moreMessagesToSend(
			String smsDeliverSmsServicesMoreMessagesToSend) {
		smsDeliver_smsServices_moreMessagesToSend = smsDeliverSmsServicesMoreMessagesToSend;
	}
	public String getSmsDeliver_smsServices_statusReportIndication() {
		return smsDeliver_smsServices_statusReportIndication;
	}
	public void setSmsDeliver_smsServices_statusReportIndication(
			String smsDeliverSmsServicesStatusReportIndication) {
		smsDeliver_smsServices_statusReportIndication = smsDeliverSmsServicesStatusReportIndication;
	}
	public String getSmsDeliver_smsServices_userDataHeaderIndication() {
		return smsDeliver_smsServices_userDataHeaderIndication;
	}
	public void setSmsDeliver_smsServices_userDataHeaderIndication(
			String smsDeliverSmsServicesUserDataHeaderIndication) {
		smsDeliver_smsServices_userDataHeaderIndication = smsDeliverSmsServicesUserDataHeaderIndication;
	}
	public String getSmsDeliver_smsServices_replyPath() {
		return smsDeliver_smsServices_replyPath;
	}
	public void setSmsDeliver_smsServices_replyPath(
			String smsDeliverSmsServicesReplyPath) {
		smsDeliver_smsServices_replyPath = smsDeliverSmsServicesReplyPath;
	}
	public String getSmsDeliver_smsOriginator_country() {
		return smsDeliver_smsOriginator_country;
	}
	public void setSmsDeliver_smsOriginator_country(
			String smsDeliverSmsOriginatorCountry) {
		smsDeliver_smsOriginator_country = smsDeliverSmsOriginatorCountry;
	}
	public String getSmsDeliver_smsOriginator_network() {
		return smsDeliver_smsOriginator_network;
	}
	public void setSmsDeliver_smsOriginator_network(
			String smsDeliverSmsOriginatorNetwork) {
		smsDeliver_smsOriginator_network = smsDeliverSmsOriginatorNetwork;
	}
	public String getSmsDeliver_smsOriginator_gsmAddress_npi() {
		return smsDeliver_smsOriginator_gsmAddress_npi;
	}
	public void setSmsDeliver_smsOriginator_gsmAddress_npi(
			String smsDeliverSmsOriginatorGsmAddressNpi) {
		smsDeliver_smsOriginator_gsmAddress_npi = smsDeliverSmsOriginatorGsmAddressNpi;
	}
	public String getSmsDeliver_smsOriginator_gsmAddress_number() {
		return smsDeliver_smsOriginator_gsmAddress_number;
	}
	public void setSmsDeliver_smsOriginator_gsmAddress_number(
			String smsDeliverSmsOriginatorGsmAddressNumber) {
		smsDeliver_smsOriginator_gsmAddress_number = smsDeliverSmsOriginatorGsmAddressNumber;
	}
	public double getSmsDeliver_smsProtocolId() {
		return smsDeliver_smsProtocolId;
	}
	public void setSmsDeliver_smsProtocolId(double smsDeliverSmsProtocolId) {
		smsDeliver_smsProtocolId = smsDeliverSmsProtocolId;
	}
	public String getSmsDeliver_smsDataCodingScheme_alphabet() {
		return smsDeliver_smsDataCodingScheme_alphabet;
	}
	public void setSmsDeliver_smsDataCodingScheme_alphabet(
			String smsDeliverSmsDataCodingSchemeAlphabet) {
		smsDeliver_smsDataCodingScheme_alphabet = smsDeliverSmsDataCodingSchemeAlphabet;
	}
	public String getSmsDeliver_smsDataCodingScheme_messageClass() {
		return smsDeliver_smsDataCodingScheme_messageClass;
	}
	public void setSmsDeliver_smsDataCodingScheme_messageClass(
			String smsDeliverSmsDataCodingSchemeMessageClass) {
		smsDeliver_smsDataCodingScheme_messageClass = smsDeliverSmsDataCodingSchemeMessageClass;
	}
	public String getSmsDeliver_smsScTimestamp() {
		return smsDeliver_smsScTimestamp;
	}
	public void setSmsDeliver_smsScTimestamp(String smsDeliverSmsScTimestamp) {
		smsDeliver_smsScTimestamp = smsDeliverSmsScTimestamp;
	}
	public String getSmsDeliver_smsUserDataHeader() {
		return smsDeliver_smsUserDataHeader;
	}
	public void setSmsDeliver_smsUserDataHeader(String smsDeliverSmsUserDataHeader) {
		smsDeliver_smsUserDataHeader = smsDeliverSmsUserDataHeader;
	}
	public String getSmsDeliver_smsUserData() {
		return smsDeliver_smsUserData;
	}
	public void setSmsDeliver_smsUserData(String smsDeliverSmsUserData) {
		smsDeliver_smsUserData = smsDeliverSmsUserData;
	}
	public String getStatusReport_smsServices_moreMessagesToSend() {
		return statusReport_smsServices_moreMessagesToSend;
	}
	public void setStatusReport_smsServices_moreMessagesToSend(
			String statusReportSmsServicesMoreMessagesToSend) {
		statusReport_smsServices_moreMessagesToSend = statusReportSmsServicesMoreMessagesToSend;
	}
	public String getStatusReport_smsServices_statusReportQualifier() {
		return statusReport_smsServices_statusReportQualifier;
	}
	public void setStatusReport_smsServices_statusReportQualifier(
			String statusReportSmsServicesStatusReportQualifier) {
		statusReport_smsServices_statusReportQualifier = statusReportSmsServicesStatusReportQualifier;
	}
	public double getStatusReport_smsMessageReference() {
		return statusReport_smsMessageReference;
	}
	public void setStatusReport_smsMessageReference(
			double statusReportSmsMessageReference) {
		statusReport_smsMessageReference = statusReportSmsMessageReference;
	}
	public String getStatusReport_smsRecipient_country() {
		return statusReport_smsRecipient_country;
	}
	public void setStatusReport_smsRecipient_country(
			String statusReportSmsRecipientCountry) {
		statusReport_smsRecipient_country = statusReportSmsRecipientCountry;
	}
	public String getStatusReport_smsRecipient_network() {
		return statusReport_smsRecipient_network;
	}
	public void setStatusReport_smsRecipient_network(
			String statusReportSmsRecipientNetwork) {
		statusReport_smsRecipient_network = statusReportSmsRecipientNetwork;
	}
	public String getStatusReport_smsRecipient_gsmAddress_ton() {
		return statusReport_smsRecipient_gsmAddress_ton;
	}
	public void setStatusReport_smsRecipient_gsmAddress_ton(
			String statusReportSmsRecipientGsmAddressTon) {
		statusReport_smsRecipient_gsmAddress_ton = statusReportSmsRecipientGsmAddressTon;
	}
	public String getStatusReport_smsRecipient_gsmAddress_npi() {
		return statusReport_smsRecipient_gsmAddress_npi;
	}
	public void setStatusReport_smsRecipient_gsmAddress_npi(
			String statusReportSmsRecipientGsmAddressNpi) {
		statusReport_smsRecipient_gsmAddress_npi = statusReportSmsRecipientGsmAddressNpi;
	}
	public String getStatusReport_smsRecipient_gsmAddress_number() {
		return statusReport_smsRecipient_gsmAddress_number;
	}
	public void setStatusReport_smsRecipient_gsmAddress_number(
			String statusReportSmsRecipientGsmAddressNumber) {
		statusReport_smsRecipient_gsmAddress_number = statusReportSmsRecipientGsmAddressNumber;
	}
	public String getStatusReport_smsScTimestamp() {
		return statusReport_smsScTimestamp;
	}
	public void setStatusReport_smsScTimestamp(String statusReportSmsScTimestamp) {
		statusReport_smsScTimestamp = statusReportSmsScTimestamp;
	}
	public String getStatusReport_smsDischargeTime() {
		return statusReport_smsDischargeTime;
	}
	public void setStatusReport_smsDischargeTime(String statusReportSmsDischargeTime) {
		statusReport_smsDischargeTime = statusReportSmsDischargeTime;
	}
	public double getStatusReport_smsStatus() {
		return statusReport_smsStatus;
	}
	public void setStatusReport_smsStatus(double statusReportSmsStatus) {
		statusReport_smsStatus = statusReportSmsStatus;
	}
	public String getInfoFromHlr_mapImsi_country() {
		return infoFromHlr_mapImsi_country;
	}
	public void setInfoFromHlr_mapImsi_country(String infoFromHlrMapImsiCountry) {
		infoFromHlr_mapImsi_country = infoFromHlrMapImsiCountry;
	}
	public String getInfoFromHlr_mapImsi_network() {
		return infoFromHlr_mapImsi_network;
	}
	public void setInfoFromHlr_mapImsi_network(String infoFromHlrMapImsiNetwork) {
		infoFromHlr_mapImsi_network = infoFromHlrMapImsiNetwork;
	}
	public String getInfoFromHlr_mapImsi_imsi() {
		return infoFromHlr_mapImsi_imsi;
	}
	public void setInfoFromHlr_mapImsi_imsi(String infoFromHlrMapImsiImsi) {
		infoFromHlr_mapImsi_imsi = infoFromHlrMapImsiImsi;
	}
	public String getInfoFromHlr_mapMsc_country() {
		return infoFromHlr_mapMsc_country;
	}
	public void setInfoFromHlr_mapMsc_country(String infoFromHlrMapMscCountry) {
		infoFromHlr_mapMsc_country = infoFromHlrMapMscCountry;
	}
	public String getInfoFromHlr_mapMsc_network() {
		return infoFromHlr_mapMsc_network;
	}
	public void setInfoFromHlr_mapMsc_network(String infoFromHlrMapMscNetwork) {
		infoFromHlr_mapMsc_network = infoFromHlrMapMscNetwork;
	}
	public String getInfoFromHlr_mapMsc_gsmAddress_ton() {
		return infoFromHlr_mapMsc_gsmAddress_ton;
	}
	public void setInfoFromHlr_mapMsc_gsmAddress_ton(
			String infoFromHlrMapMscGsmAddressTon) {
		infoFromHlr_mapMsc_gsmAddress_ton = infoFromHlrMapMscGsmAddressTon;
	}
	public String getInfoFromHlr_mapMsc_gsmAddress_npi() {
		return infoFromHlr_mapMsc_gsmAddress_npi;
	}
	public void setInfoFromHlr_mapMsc_gsmAddress_npi(
			String infoFromHlrMapMscGsmAddressNpi) {
		infoFromHlr_mapMsc_gsmAddress_npi = infoFromHlrMapMscGsmAddressNpi;
	}
	public String getInfoFromHlr_mapMsc_gsmAddress_number() {
		return infoFromHlr_mapMsc_gsmAddress_number;
	}
	public void setInfoFromHlr_mapMsc_gsmAddress_number(
			String infoFromHlrMapMscGsmAddressNumber) {
		infoFromHlr_mapMsc_gsmAddress_number = infoFromHlrMapMscGsmAddressNumber;
	}
	public String getInfoFromHlr_mapSgsn_country() {
		return infoFromHlr_mapSgsn_country;
	}
	public void setInfoFromHlr_mapSgsn_country(String infoFromHlrMapSgsnCountry) {
		infoFromHlr_mapSgsn_country = infoFromHlrMapSgsnCountry;
	}
	public String getInfoFromHlr_mapSgsn_network() {
		return infoFromHlr_mapSgsn_network;
	}
	public void setInfoFromHlr_mapSgsn_network(String infoFromHlrMapSgsnNetwork) {
		infoFromHlr_mapSgsn_network = infoFromHlrMapSgsnNetwork;
	}
	public String getInfoFromHlr_mapSgsn_gsmAddress_ton() {
		return infoFromHlr_mapSgsn_gsmAddress_ton;
	}
	public void setInfoFromHlr_mapSgsn_gsmAddress_ton(
			String infoFromHlrMapSgsnGsmAddressTon) {
		infoFromHlr_mapSgsn_gsmAddress_ton = infoFromHlrMapSgsnGsmAddressTon;
	}
	public String getInfoFromHlr_mapSgsn_gsmAddress_npi() {
		return infoFromHlr_mapSgsn_gsmAddress_npi;
	}
	public void setInfoFromHlr_mapSgsn_gsmAddress_npi(
			String infoFromHlrMapSgsnGsmAddressNpi) {
		infoFromHlr_mapSgsn_gsmAddress_npi = infoFromHlrMapSgsnGsmAddressNpi;
	}
	public String getInfoFromHlr_mapSgsn_gsmAddress_number() {
		return infoFromHlr_mapSgsn_gsmAddress_number;
	}
	public void setInfoFromHlr_mapSgsn_gsmAddress_number(
			String infoFromHlrMapSgsnGsmAddressNumber) {
		infoFromHlr_mapSgsn_gsmAddress_number = infoFromHlrMapSgsnGsmAddressNumber;
	}
	public String getCorrelatedSriSm_sccpCgPa_country() {
		return correlatedSriSm_sccpCgPa_country;
	}
	public void setCorrelatedSriSm_sccpCgPa_country(
			String correlatedSriSmSccpCgPaCountry) {
		correlatedSriSm_sccpCgPa_country = correlatedSriSmSccpCgPaCountry;
	}
	public String getCorrelatedSriSm_sccpCgPa_network() {
		return correlatedSriSm_sccpCgPa_network;
	}
	public void setCorrelatedSriSm_sccpCgPa_network(
			String correlatedSriSmSccpCgPaNetwork) {
		correlatedSriSm_sccpCgPa_network = correlatedSriSmSccpCgPaNetwork;
	}
	public double getCorrelatedSriSm_sccpCgPa_sccpAddress_signallingPointCode() {
		return correlatedSriSm_sccpCgPa_sccpAddress_signallingPointCode;
	}
	public void setCorrelatedSriSm_sccpCgPa_sccpAddress_signallingPointCode(
			double correlatedSriSmSccpCgPaSccpAddressSignallingPointCode) {
		correlatedSriSm_sccpCgPa_sccpAddress_signallingPointCode = correlatedSriSmSccpCgPaSccpAddressSignallingPointCode;
	}
	public String getCorrelatedSriSm_sccpCgPa_sccpAddress_subsystemNumber() {
		return correlatedSriSm_sccpCgPa_sccpAddress_subsystemNumber;
	}
	public void setCorrelatedSriSm_sccpCgPa_sccpAddress_subsystemNumber(
			String correlatedSriSmSccpCgPaSccpAddressSubsystemNumber) {
		correlatedSriSm_sccpCgPa_sccpAddress_subsystemNumber = correlatedSriSmSccpCgPaSccpAddressSubsystemNumber;
	}
	public String getCorrelatedSriSm_sccpCgPa_sccpAddress_globalTitle_numberingPlan() {
		return correlatedSriSm_sccpCgPa_sccpAddress_globalTitle_numberingPlan;
	}
	public void setCorrelatedSriSm_sccpCgPa_sccpAddress_globalTitle_numberingPlan(
			String correlatedSriSmSccpCgPaSccpAddressGlobalTitleNumberingPlan) {
		correlatedSriSm_sccpCgPa_sccpAddress_globalTitle_numberingPlan = correlatedSriSmSccpCgPaSccpAddressGlobalTitleNumberingPlan;
	}
	public String getCorrelatedSriSm_sccpCgPa_sccpAddress_globalTitle_number() {
		return correlatedSriSm_sccpCgPa_sccpAddress_globalTitle_number;
	}
	public void setCorrelatedSriSm_sccpCgPa_sccpAddress_globalTitle_number(
			String correlatedSriSmSccpCgPaSccpAddressGlobalTitleNumber) {
		correlatedSriSm_sccpCgPa_sccpAddress_globalTitle_number = correlatedSriSmSccpCgPaSccpAddressGlobalTitleNumber;
	}
	public String getCorrelatedSriSm_sccpCgPa_sccpAddress_globalTitle_NAI() {
		return correlatedSriSm_sccpCgPa_sccpAddress_globalTitle_NAI;
	}
	public void setCorrelatedSriSm_sccpCgPa_sccpAddress_globalTitle_NAI(
			String correlatedSriSmSccpCgPaSccpAddressGlobalTitleNAI) {
		correlatedSriSm_sccpCgPa_sccpAddress_globalTitle_NAI = correlatedSriSmSccpCgPaSccpAddressGlobalTitleNAI;
	}
	public String getCorrelatedSriSm_mapSmsc_country() {
		return correlatedSriSm_mapSmsc_country;
	}
	public void setCorrelatedSriSm_mapSmsc_country(
			String correlatedSriSmMapSmscCountry) {
		correlatedSriSm_mapSmsc_country = correlatedSriSmMapSmscCountry;
	}
	public String getCorrelatedSriSm_mapSmsc_network() {
		return correlatedSriSm_mapSmsc_network;
	}
	public void setCorrelatedSriSm_mapSmsc_network(
			String correlatedSriSmMapSmscNetwork) {
		correlatedSriSm_mapSmsc_network = correlatedSriSmMapSmscNetwork;
	}
	public String getCorrelatedSriSm_mapSmsc_gsmAddress_ton() {
		return correlatedSriSm_mapSmsc_gsmAddress_ton;
	}
	public void setCorrelatedSriSm_mapSmsc_gsmAddress_ton(
			String correlatedSriSmMapSmscGsmAddressTon) {
		correlatedSriSm_mapSmsc_gsmAddress_ton = correlatedSriSmMapSmscGsmAddressTon;
	}
	public String getCorrelatedSriSm_mapSmsc_gsmAddress_npi() {
		return correlatedSriSm_mapSmsc_gsmAddress_npi;
	}
	public void setCorrelatedSriSm_mapSmsc_gsmAddress_npi(
			String correlatedSriSmMapSmscGsmAddressNpi) {
		correlatedSriSm_mapSmsc_gsmAddress_npi = correlatedSriSmMapSmscGsmAddressNpi;
	}
	public String getCorrelatedSriSm_mapSmsc_gsmAddress_number() {
		return correlatedSriSm_mapSmsc_gsmAddress_number;
	}
	public void setCorrelatedSriSm_mapSmsc_gsmAddress_number(
			String correlatedSriSmMapSmscGsmAddressNumber) {
		correlatedSriSm_mapSmsc_gsmAddress_number = correlatedSriSmMapSmscGsmAddressNumber;
	}
	public String getCorrelatedSriSm_mapMsisdn_country() {
		return correlatedSriSm_mapMsisdn_country;
	}
	public void setCorrelatedSriSm_mapMsisdn_country(
			String correlatedSriSmMapMsisdnCountry) {
		correlatedSriSm_mapMsisdn_country = correlatedSriSmMapMsisdnCountry;
	}
	public String getCorrelatedSriSm_mapMsisdn_network() {
		return correlatedSriSm_mapMsisdn_network;
	}
	public void setCorrelatedSriSm_mapMsisdn_network(
			String correlatedSriSmMapMsisdnNetwork) {
		correlatedSriSm_mapMsisdn_network = correlatedSriSmMapMsisdnNetwork;
	}
	public String getCorrelatedSriSm_mapMsisdn_gsmAddress_ton() {
		return correlatedSriSm_mapMsisdn_gsmAddress_ton;
	}
	public void setCorrelatedSriSm_mapMsisdn_gsmAddress_ton(
			String correlatedSriSmMapMsisdnGsmAddressTon) {
		correlatedSriSm_mapMsisdn_gsmAddress_ton = correlatedSriSmMapMsisdnGsmAddressTon;
	}
	public String getCorrelatedSriSm_mapMsisdn_gsmAddress_npi() {
		return correlatedSriSm_mapMsisdn_gsmAddress_npi;
	}
	public void setCorrelatedSriSm_mapMsisdn_gsmAddress_npi(
			String correlatedSriSmMapMsisdnGsmAddressNpi) {
		correlatedSriSm_mapMsisdn_gsmAddress_npi = correlatedSriSmMapMsisdnGsmAddressNpi;
	}
	public String getCorrelatedSriSm_mapMsisdn_gsmAddress_number() {
		return correlatedSriSm_mapMsisdn_gsmAddress_number;
	}
	public void setCorrelatedSriSm_mapMsisdn_gsmAddress_number(
			String correlatedSriSmMapMsisdnGsmAddressNumber) {
		correlatedSriSm_mapMsisdn_gsmAddress_number = correlatedSriSmMapMsisdnGsmAddressNumber;
	}
	public String getCorrelatedSriSm_mapImsi_country() {
		return correlatedSriSm_mapImsi_country;
	}
	public void setCorrelatedSriSm_mapImsi_country(
			String correlatedSriSmMapImsiCountry) {
		correlatedSriSm_mapImsi_country = correlatedSriSmMapImsiCountry;
	}
	public String getCorrelatedSriSm_mapImsi_network() {
		return correlatedSriSm_mapImsi_network;
	}
	public void setCorrelatedSriSm_mapImsi_network(
			String correlatedSriSmMapImsiNetwork) {
		correlatedSriSm_mapImsi_network = correlatedSriSmMapImsiNetwork;
	}
	public String getCorrelatedSriSm_mapImsi_imsi() {
		return correlatedSriSm_mapImsi_imsi;
	}
	public void setCorrelatedSriSm_mapImsi_imsi(String correlatedSriSmMapImsiImsi) {
		correlatedSriSm_mapImsi_imsi = correlatedSriSmMapImsiImsi;
	}
	public String getCorrelatedSriSm_mapLmsi() {
		return correlatedSriSm_mapLmsi;
	}
	public void setCorrelatedSriSm_mapLmsi(String correlatedSriSmMapLmsi) {
		correlatedSriSm_mapLmsi = correlatedSriSmMapLmsi;
	}
	public String getCorrelatedSriSm_mapMsc_country() {
		return correlatedSriSm_mapMsc_country;
	}
	public void setCorrelatedSriSm_mapMsc_country(
			String correlatedSriSmMapMscCountry) {
		correlatedSriSm_mapMsc_country = correlatedSriSmMapMscCountry;
	}
	public String getCorrelatedSriSm_mapMsc_network() {
		return correlatedSriSm_mapMsc_network;
	}
	public void setCorrelatedSriSm_mapMsc_network(
			String correlatedSriSmMapMscNetwork) {
		correlatedSriSm_mapMsc_network = correlatedSriSmMapMscNetwork;
	}
	public String getCorrelatedSriSm_mapMsc_gsmAddress_ton() {
		return correlatedSriSm_mapMsc_gsmAddress_ton;
	}
	public void setCorrelatedSriSm_mapMsc_gsmAddress_ton(
			String correlatedSriSmMapMscGsmAddressTon) {
		correlatedSriSm_mapMsc_gsmAddress_ton = correlatedSriSmMapMscGsmAddressTon;
	}
	public String getCorrelatedSriSm_mapMsc_gsmAddress_npi() {
		return correlatedSriSm_mapMsc_gsmAddress_npi;
	}
	public void setCorrelatedSriSm_mapMsc_gsmAddress_npi(
			String correlatedSriSmMapMscGsmAddressNpi) {
		correlatedSriSm_mapMsc_gsmAddress_npi = correlatedSriSmMapMscGsmAddressNpi;
	}
	public String getCorrelatedSriSm_mapMsc_gsmAddress_number() {
		return correlatedSriSm_mapMsc_gsmAddress_number;
	}
	public void setCorrelatedSriSm_mapMsc_gsmAddress_number(
			String correlatedSriSmMapMscGsmAddressNumber) {
		correlatedSriSm_mapMsc_gsmAddress_number = correlatedSriSmMapMscGsmAddressNumber;
	}
	public String getCorrelatedSriSm_mapSgsn_country() {
		return correlatedSriSm_mapSgsn_country;
	}
	public void setCorrelatedSriSm_mapSgsn_country(
			String correlatedSriSmMapSgsnCountry) {
		correlatedSriSm_mapSgsn_country = correlatedSriSmMapSgsnCountry;
	}
	public String getCorrelatedSriSm_mapSgsn_network() {
		return correlatedSriSm_mapSgsn_network;
	}
	public void setCorrelatedSriSm_mapSgsn_network(
			String correlatedSriSmMapSgsnNetwork) {
		correlatedSriSm_mapSgsn_network = correlatedSriSmMapSgsnNetwork;
	}
	public String getCorrelatedSriSm_mapSgsn_gsmAddress_ton() {
		return correlatedSriSm_mapSgsn_gsmAddress_ton;
	}
	public void setCorrelatedSriSm_mapSgsn_gsmAddress_ton(
			String correlatedSriSmMapSgsnGsmAddressTon) {
		correlatedSriSm_mapSgsn_gsmAddress_ton = correlatedSriSmMapSgsnGsmAddressTon;
	}
	public String getCorrelatedSriSm_mapSgsn_gsmAddress_npi() {
		return correlatedSriSm_mapSgsn_gsmAddress_npi;
	}
	public void setCorrelatedSriSm_mapSgsn_gsmAddress_npi(
			String correlatedSriSmMapSgsnGsmAddressNpi) {
		correlatedSriSm_mapSgsn_gsmAddress_npi = correlatedSriSmMapSgsnGsmAddressNpi;
	}
	public String getCorrelatedSriSm_mapSgsn_gsmAddress_number() {
		return correlatedSriSm_mapSgsn_gsmAddress_number;
	}
	public void setCorrelatedSriSm_mapSgsn_gsmAddress_number(
			String correlatedSriSmMapSgsnGsmAddressNumber) {
		correlatedSriSm_mapSgsn_gsmAddress_number = correlatedSriSmMapSgsnGsmAddressNumber;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getApplicationShortNumber_ton() {
		return applicationShortNumber_ton;
	}
	public void setApplicationShortNumber_ton(String applicationShortNumberTon) {
		applicationShortNumber_ton = applicationShortNumberTon;
	}
	public String getApplicationShortNumber_npi() {
		return applicationShortNumber_npi;
	}
	public void setApplicationShortNumber_npi(String applicationShortNumberNpi) {
		applicationShortNumber_npi = applicationShortNumberNpi;
	}
	public String getApplicationShortNumber_number() {
		return applicationShortNumber_number;
	}
	public void setApplicationShortNumber_number(String applicationShortNumberNumber) {
		applicationShortNumber_number = applicationShortNumberNumber;
	}
	public String getMessageFields_protocol() {
		return messageFields_protocol;
	}
	public void setMessageFields_protocol(String messageFieldsProtocol) {
		messageFields_protocol = messageFieldsProtocol;
	}
	public String getMessageFields_originatorAddress_country() {
		return messageFields_originatorAddress_country;
	}
	public void setMessageFields_originatorAddress_country(
			String messageFieldsOriginatorAddressCountry) {
		messageFields_originatorAddress_country = messageFieldsOriginatorAddressCountry;
	}
	public String getMessageFields_originatorAddress_network() {
		return messageFields_originatorAddress_network;
	}
	public void setMessageFields_originatorAddress_network(
			String messageFieldsOriginatorAddressNetwork) {
		messageFields_originatorAddress_network = messageFieldsOriginatorAddressNetwork;
	}
	public String getMessageFields_originatorAddress_gsmAddress_ton() {
		return messageFields_originatorAddress_gsmAddress_ton;
	}
	public void setMessageFields_originatorAddress_gsmAddress_ton(
			String messageFieldsOriginatorAddressGsmAddressTon) {
		messageFields_originatorAddress_gsmAddress_ton = messageFieldsOriginatorAddressGsmAddressTon;
	}
	public String getMessageFields_originatorAddress_gsmAddress_npi() {
		return messageFields_originatorAddress_gsmAddress_npi;
	}
	public void setMessageFields_originatorAddress_gsmAddress_npi(
			String messageFieldsOriginatorAddressGsmAddressNpi) {
		messageFields_originatorAddress_gsmAddress_npi = messageFieldsOriginatorAddressGsmAddressNpi;
	}
	public String getMessageFields_originatorAddress_gsmAddress_number() {
		return messageFields_originatorAddress_gsmAddress_number;
	}
	public void setMessageFields_originatorAddress_gsmAddress_number(
			String messageFieldsOriginatorAddressGsmAddressNumber) {
		messageFields_originatorAddress_gsmAddress_number = messageFieldsOriginatorAddressGsmAddressNumber;
	}
	public String getMessageFields_recipientAddress_country() {
		return messageFields_recipientAddress_country;
	}
	public void setMessageFields_recipientAddress_country(
			String messageFieldsRecipientAddressCountry) {
		messageFields_recipientAddress_country = messageFieldsRecipientAddressCountry;
	}
	public String getMessageFields_recipientAddress_network() {
		return messageFields_recipientAddress_network;
	}
	public void setMessageFields_recipientAddress_network(
			String messageFieldsRecipientAddressNetwork) {
		messageFields_recipientAddress_network = messageFieldsRecipientAddressNetwork;
	}
	public String getMessageFields_recipientAddress_gsmAddress_ton() {
		return messageFields_recipientAddress_gsmAddress_ton;
	}
	public void setMessageFields_recipientAddress_gsmAddress_ton(
			String messageFieldsRecipientAddressGsmAddressTon) {
		messageFields_recipientAddress_gsmAddress_ton = messageFieldsRecipientAddressGsmAddressTon;
	}
	public String getMessageFields_recipientAddress_gsmAddress_npi() {
		return messageFields_recipientAddress_gsmAddress_npi;
	}
	public void setMessageFields_recipientAddress_gsmAddress_npi(
			String messageFieldsRecipientAddressGsmAddressNpi) {
		messageFields_recipientAddress_gsmAddress_npi = messageFieldsRecipientAddressGsmAddressNpi;
	}
	public String getMessageFields_recipientAddress_gsmAddress_number() {
		return messageFields_recipientAddress_gsmAddress_number;
	}
	public void setMessageFields_recipientAddress_gsmAddress_number(
			String messageFieldsRecipientAddressGsmAddressNumber) {
		messageFields_recipientAddress_gsmAddress_number = messageFieldsRecipientAddressGsmAddressNumber;
	}
	public String getMessageFields_dataCodingScheme_alphabet() {
		return messageFields_dataCodingScheme_alphabet;
	}
	public void setMessageFields_dataCodingScheme_alphabet(
			String messageFieldsDataCodingSchemeAlphabet) {
		messageFields_dataCodingScheme_alphabet = messageFieldsDataCodingSchemeAlphabet;
	}
	public String getMessageFields_dataCodingScheme_messageClass() {
		return messageFields_dataCodingScheme_messageClass;
	}
	public void setMessageFields_dataCodingScheme_messageClass(
			String messageFieldsDataCodingSchemeMessageClass) {
		messageFields_dataCodingScheme_messageClass = messageFieldsDataCodingSchemeMessageClass;
	}
	public double getMessageFields_protocolIdentifier() {
		return messageFields_protocolIdentifier;
	}
	public void setMessageFields_protocolIdentifier(
			double messageFieldsProtocolIdentifier) {
		messageFields_protocolIdentifier = messageFieldsProtocolIdentifier;
	}
	public String getMessageFields_notificationType_deliveryNotification() {
		return messageFields_notificationType_deliveryNotification;
	}
	public void setMessageFields_notificationType_deliveryNotification(
			String messageFieldsNotificationTypeDeliveryNotification) {
		messageFields_notificationType_deliveryNotification = messageFieldsNotificationTypeDeliveryNotification;
	}
	public String getMessageFields_notificationType_nonDeliveryNotification() {
		return messageFields_notificationType_nonDeliveryNotification;
	}
	public void setMessageFields_notificationType_nonDeliveryNotification(
			String messageFieldsNotificationTypeNonDeliveryNotification) {
		messageFields_notificationType_nonDeliveryNotification = messageFieldsNotificationTypeNonDeliveryNotification;
	}
	public String getMessageFields_notificationType_bufferedNotification() {
		return messageFields_notificationType_bufferedNotification;
	}
	public void setMessageFields_notificationType_bufferedNotification(
			String messageFieldsNotificationTypeBufferedNotification) {
		messageFields_notificationType_bufferedNotification = messageFieldsNotificationTypeBufferedNotification;
	}
	public String getMessageFields_userData() {
		return messageFields_userData;
	}
	public void setMessageFields_userData(String messageFieldsUserData) {
		messageFields_userData = messageFieldsUserData;
	}
	public String getMessageFields_userDataHeader() {
		return messageFields_userDataHeader;
	}
	public void setMessageFields_userDataHeader(String messageFieldsUserDataHeader) {
		messageFields_userDataHeader = messageFieldsUserDataHeader;
	}
	public String getMessageFields_moreMessagesToSend() {
		return messageFields_moreMessagesToSend;
	}
	public void setMessageFields_moreMessagesToSend(
			String messageFieldsMoreMessagesToSend) {
		messageFields_moreMessagesToSend = messageFieldsMoreMessagesToSend;
	}
	public double getMessageFields_priority() {
		return messageFields_priority;
	}
	public void setMessageFields_priority(double messageFieldsPriority) {
		messageFields_priority = messageFieldsPriority;
	}
	public String getMessageFields_replyPathIndicator() {
		return messageFields_replyPathIndicator;
	}
	public void setMessageFields_replyPathIndicator(
			String messageFieldsReplyPathIndicator) {
		messageFields_replyPathIndicator = messageFieldsReplyPathIndicator;
	}
	public String getMessageFields_deferredDeliveryTime() {
		return messageFields_deferredDeliveryTime;
	}
	public void setMessageFields_deferredDeliveryTime(
			String messageFieldsDeferredDeliveryTime) {
		messageFields_deferredDeliveryTime = messageFieldsDeferredDeliveryTime;
	}
	public String getMessageFields_validityPeriod() {
		return messageFields_validityPeriod;
	}
	public void setMessageFields_validityPeriod(String messageFieldsValidityPeriod) {
		messageFields_validityPeriod = messageFieldsValidityPeriod;
	}
	public String getMessageFields_singleShotIndicator() {
		return messageFields_singleShotIndicator;
	}
	public void setMessageFields_singleShotIndicator(
			String messageFieldsSingleShotIndicator) {
		messageFields_singleShotIndicator = messageFieldsSingleShotIndicator;
	}
	public String getMessageFields_billingIdentifier() {
		return messageFields_billingIdentifier;
	}
	public void setMessageFields_billingIdentifier(
			String messageFieldsBillingIdentifier) {
		messageFields_billingIdentifier = messageFieldsBillingIdentifier;
	}
	public String getMessageFields_serviceCentreTimestamp() {
		return messageFields_serviceCentreTimestamp;
	}
	public void setMessageFields_serviceCentreTimestamp(
			String messageFieldsServiceCentreTimestamp) {
		messageFields_serviceCentreTimestamp = messageFieldsServiceCentreTimestamp;
	}
	public String getMessageFields_deliveryStatus() {
		return messageFields_deliveryStatus;
	}
	public void setMessageFields_deliveryStatus(String messageFieldsDeliveryStatus) {
		messageFields_deliveryStatus = messageFieldsDeliveryStatus;
	}
	public double getMessageFields_errorCode() {
		return messageFields_errorCode;
	}
	public void setMessageFields_errorCode(double messageFieldsErrorCode) {
		messageFields_errorCode = messageFieldsErrorCode;
	}
	public String getMessageFields_deliveryTimestamp() {
		return messageFields_deliveryTimestamp;
	}
	public void setMessageFields_deliveryTimestamp(
			String messageFieldsDeliveryTimestamp) {
		messageFields_deliveryTimestamp = messageFieldsDeliveryTimestamp;
	}
	public double getMessageFields_tariffClass() {
		return messageFields_tariffClass;
	}
	public void setMessageFields_tariffClass(double messageFieldsTariffClass) {
		messageFields_tariffClass = messageFieldsTariffClass;
	}
	public double getMessageFields_serviceDescription() {
		return messageFields_serviceDescription;
	}
	public void setMessageFields_serviceDescription(
			double messageFieldsServiceDescription) {
		messageFields_serviceDescription = messageFieldsServiceDescription;
	}
	public String getMessageFields_originatedImsi_country() {
		return messageFields_originatedImsi_country;
	}
	public void setMessageFields_originatedImsi_country(
			String messageFieldsOriginatedImsiCountry) {
		messageFields_originatedImsi_country = messageFieldsOriginatedImsiCountry;
	}
	public String getMessageFields_originatedImsi_network() {
		return messageFields_originatedImsi_network;
	}
	public void setMessageFields_originatedImsi_network(
			String messageFieldsOriginatedImsiNetwork) {
		messageFields_originatedImsi_network = messageFieldsOriginatedImsiNetwork;
	}
	public String getMessageFields_originatedImsi_imsi() {
		return messageFields_originatedImsi_imsi;
	}
	public void setMessageFields_originatedImsi_imsi(
			String messageFieldsOriginatedImsiImsi) {
		messageFields_originatedImsi_imsi = messageFieldsOriginatedImsiImsi;
	}
	public String getMessageFields_originatedMscAddress_country() {
		return messageFields_originatedMscAddress_country;
	}
	public void setMessageFields_originatedMscAddress_country(
			String messageFieldsOriginatedMscAddressCountry) {
		messageFields_originatedMscAddress_country = messageFieldsOriginatedMscAddressCountry;
	}
	public String getMessageFields_originatedMscAddress_network() {
		return messageFields_originatedMscAddress_network;
	}
	public void setMessageFields_originatedMscAddress_network(
			String messageFieldsOriginatedMscAddressNetwork) {
		messageFields_originatedMscAddress_network = messageFieldsOriginatedMscAddressNetwork;
	}
	public String getMessageFields_originatedMscAddress_gsmAddress_ton() {
		return messageFields_originatedMscAddress_gsmAddress_ton;
	}
	public void setMessageFields_originatedMscAddress_gsmAddress_ton(
			String messageFieldsOriginatedMscAddressGsmAddressTon) {
		messageFields_originatedMscAddress_gsmAddress_ton = messageFieldsOriginatedMscAddressGsmAddressTon;
	}
	public String getMessageFields_originatedMscAddress_gsmAddress_npi() {
		return messageFields_originatedMscAddress_gsmAddress_npi;
	}
	public void setMessageFields_originatedMscAddress_gsmAddress_npi(
			String messageFieldsOriginatedMscAddressGsmAddressNpi) {
		messageFields_originatedMscAddress_gsmAddress_npi = messageFieldsOriginatedMscAddressGsmAddressNpi;
	}
	public String getMessageFields_originatedMscAddress_gsmAddress_number() {
		return messageFields_originatedMscAddress_gsmAddress_number;
	}
	public void setMessageFields_originatedMscAddress_gsmAddress_number(
			String messageFieldsOriginatedMscAddressGsmAddressNumber) {
		messageFields_originatedMscAddress_gsmAddress_number = messageFieldsOriginatedMscAddressGsmAddressNumber;
	}
	public String getMessageFields_serviceCentreAddress_country() {
		return messageFields_serviceCentreAddress_country;
	}
	public void setMessageFields_serviceCentreAddress_country(
			String messageFieldsServiceCentreAddressCountry) {
		messageFields_serviceCentreAddress_country = messageFieldsServiceCentreAddressCountry;
	}
	public String getMessageFields_serviceCentreAddress_network() {
		return messageFields_serviceCentreAddress_network;
	}
	public void setMessageFields_serviceCentreAddress_network(
			String messageFieldsServiceCentreAddressNetwork) {
		messageFields_serviceCentreAddress_network = messageFieldsServiceCentreAddressNetwork;
	}
	public String getMessageFields_serviceCentreAddress_gsmAddress_ton() {
		return messageFields_serviceCentreAddress_gsmAddress_ton;
	}
	public void setMessageFields_serviceCentreAddress_gsmAddress_ton(
			String messageFieldsServiceCentreAddressGsmAddressTon) {
		messageFields_serviceCentreAddress_gsmAddress_ton = messageFieldsServiceCentreAddressGsmAddressTon;
	}
	public String getMessageFields_serviceCentreAddress_gsmAddress_npi() {
		return messageFields_serviceCentreAddress_gsmAddress_npi;
	}
	public void setMessageFields_serviceCentreAddress_gsmAddress_npi(
			String messageFieldsServiceCentreAddressGsmAddressNpi) {
		messageFields_serviceCentreAddress_gsmAddress_npi = messageFieldsServiceCentreAddressGsmAddressNpi;
	}
	public String getMessageFields_serviceCentreAddress_gsmAddress_number() {
		return messageFields_serviceCentreAddress_gsmAddress_number;
	}
	public void setMessageFields_serviceCentreAddress_gsmAddress_number(
			String messageFieldsServiceCentreAddressGsmAddressNumber) {
		messageFields_serviceCentreAddress_gsmAddress_number = messageFieldsServiceCentreAddressGsmAddressNumber;
	}
	public String getMessageFields_alphanumericOriginator() {
		return messageFields_alphanumericOriginator;
	}
	public void setMessageFields_alphanumericOriginator(
			String messageFieldsAlphanumericOriginator) {
		messageFields_alphanumericOriginator = messageFieldsAlphanumericOriginator;
	}
	public String getMessageFields_alphanumericRecipient() {
		return messageFields_alphanumericRecipient;
	}
	public void setMessageFields_alphanumericRecipient(
			String messageFieldsAlphanumericRecipient) {
		messageFields_alphanumericRecipient = messageFieldsAlphanumericRecipient;
	}
	public String getMessageFields_gsmStatusReportType() {
		return messageFields_gsmStatusReportType;
	}
	public void setMessageFields_gsmStatusReportType(
			String messageFieldsGsmStatusReportType) {
		messageFields_gsmStatusReportType = messageFieldsGsmStatusReportType;
	}
	public double getMessageFields_originatingPointCode() {
		return messageFields_originatingPointCode;
	}
	public void setMessageFields_originatingPointCode(
			double messageFieldsOriginatingPointCode) {
		messageFields_originatingPointCode = messageFieldsOriginatingPointCode;
	}
	public double getMessageFields_portNumber() {
		return messageFields_portNumber;
	}
	public void setMessageFields_portNumber(double messageFieldsPortNumber) {
		messageFields_portNumber = messageFieldsPortNumber;
	}
	public double getMessageFields_gsmMessageReference() {
		return messageFields_gsmMessageReference;
	}
	public void setMessageFields_gsmMessageReference(
			double messageFieldsGsmMessageReference) {
		messageFields_gsmMessageReference = messageFieldsGsmMessageReference;
	}
	public double getMessageFields_sourcePort() {
		return messageFields_sourcePort;
	}
	public void setMessageFields_sourcePort(double messageFieldsSourcePort) {
		messageFields_sourcePort = messageFieldsSourcePort;
	}
	public double getMessageFields_destinationPort() {
		return messageFields_destinationPort;
	}
	public void setMessageFields_destinationPort(double messageFieldsDestinationPort) {
		messageFields_destinationPort = messageFieldsDestinationPort;
	}
	public String getMessageFields_endToEndAckRequest_readAck() {
		return messageFields_endToEndAckRequest_readAck;
	}
	public void setMessageFields_endToEndAckRequest_readAck(
			String messageFieldsEndToEndAckRequestReadAck) {
		messageFields_endToEndAckRequest_readAck = messageFieldsEndToEndAckRequestReadAck;
	}
	public String getMessageFields_endToEndAckRequest_userAck() {
		return messageFields_endToEndAckRequest_userAck;
	}
	public void setMessageFields_endToEndAckRequest_userAck(
			String messageFieldsEndToEndAckRequestUserAck) {
		messageFields_endToEndAckRequest_userAck = messageFieldsEndToEndAckRequestUserAck;
	}
	public String getMessageFields_endToEndMessageType() {
		return messageFields_endToEndMessageType;
	}
	public void setMessageFields_endToEndMessageType(
			String messageFieldsEndToEndMessageType) {
		messageFields_endToEndMessageType = messageFieldsEndToEndMessageType;
	}
	public double getMessageFields_messageReference() {
		return messageFields_messageReference;
	}
	public void setMessageFields_messageReference(
			double messageFieldsMessageReference) {
		messageFields_messageReference = messageFieldsMessageReference;
	}
	public String getMessageFields_privacy() {
		return messageFields_privacy;
	}
	public void setMessageFields_privacy(String messageFieldsPrivacy) {
		messageFields_privacy = messageFieldsPrivacy;
	}
	public double getMessageFields_numberOfMessages() {
		return messageFields_numberOfMessages;
	}
	public void setMessageFields_numberOfMessages(
			double messageFieldsNumberOfMessages) {
		messageFields_numberOfMessages = messageFieldsNumberOfMessages;
	}
	public String getMessageFields_language() {
		return messageFields_language;
	}
	public void setMessageFields_language(String messageFieldsLanguage) {
		messageFields_language = messageFieldsLanguage;
	}
	public String getMessageFields_payloadType() {
		return messageFields_payloadType;
	}
	public void setMessageFields_payloadType(String messageFieldsPayloadType) {
		messageFields_payloadType = messageFieldsPayloadType;
	}
	public String getMessageFields_sourceSubAddress() {
		return messageFields_sourceSubAddress;
	}
	public void setMessageFields_sourceSubAddress(
			String messageFieldsSourceSubAddress) {
		messageFields_sourceSubAddress = messageFieldsSourceSubAddress;
	}
	public String getMessageFields_destSubAddress() {
		return messageFields_destSubAddress;
	}
	public void setMessageFields_destSubAddress(String messageFieldsDestSubAddress) {
		messageFields_destSubAddress = messageFieldsDestSubAddress;
	}
	public double getMessageFields_userResponseCode() {
		return messageFields_userResponseCode;
	}
	public void setMessageFields_userResponseCode(
			double messageFieldsUserResponseCode) {
		messageFields_userResponseCode = messageFieldsUserResponseCode;
	}
	public String getMessageFields_displayTime() {
		return messageFields_displayTime;
	}
	public void setMessageFields_displayTime(String messageFieldsDisplayTime) {
		messageFields_displayTime = messageFieldsDisplayTime;
	}
	public String getMessageFields_callbackNumbers_number_digitMode() {
		return messageFields_callbackNumbers_number_digitMode;
	}
	public void setMessageFields_callbackNumbers_number_digitMode(
			String messageFieldsCallbackNumbersNumberDigitMode) {
		messageFields_callbackNumbers_number_digitMode = messageFieldsCallbackNumbersNumberDigitMode;
	}
	public String getMessageFields_callbackNumbers_number_address_ton() {
		return messageFields_callbackNumbers_number_address_ton;
	}
	public void setMessageFields_callbackNumbers_number_address_ton(
			String messageFieldsCallbackNumbersNumberAddressTon) {
		messageFields_callbackNumbers_number_address_ton = messageFieldsCallbackNumbersNumberAddressTon;
	}
	public String getMessageFields_callbackNumbers_number_address_npi() {
		return messageFields_callbackNumbers_number_address_npi;
	}
	public void setMessageFields_callbackNumbers_number_address_npi(
			String messageFieldsCallbackNumbersNumberAddressNpi) {
		messageFields_callbackNumbers_number_address_npi = messageFieldsCallbackNumbersNumberAddressNpi;
	}
	public String getMessageFields_callbackNumbers_number_address_number() {
		return messageFields_callbackNumbers_number_address_number;
	}
	public void setMessageFields_callbackNumbers_number_address_number(
			String messageFieldsCallbackNumbersNumberAddressNumber) {
		messageFields_callbackNumbers_number_address_number = messageFieldsCallbackNumbersNumberAddressNumber;
	}
	public String getMessageFields_callbackNumbers_presentation() {
		return messageFields_callbackNumbers_presentation;
	}
	public void setMessageFields_callbackNumbers_presentation(
			String messageFieldsCallbackNumbersPresentation) {
		messageFields_callbackNumbers_presentation = messageFieldsCallbackNumbersPresentation;
	}
	public String getMessageFields_callbackNumbers_alphaTag() {
		return messageFields_callbackNumbers_alphaTag;
	}
	public void setMessageFields_callbackNumbers_alphaTag(
			String messageFieldsCallbackNumbersAlphaTag) {
		messageFields_callbackNumbers_alphaTag = messageFieldsCallbackNumbersAlphaTag;
	}
	public String getMessageFields_msValidityIndicator() {
		return messageFields_msValidityIndicator;
	}
	public void setMessageFields_msValidityIndicator(
			String messageFieldsMsValidityIndicator) {
		messageFields_msValidityIndicator = messageFieldsMsValidityIndicator;
	}
	public String getMessageFields_msValidityPeriod_unit() {
		return messageFields_msValidityPeriod_unit;
	}
	public void setMessageFields_msValidityPeriod_unit(
			String messageFieldsMsValidityPeriodUnit) {
		messageFields_msValidityPeriod_unit = messageFieldsMsValidityPeriodUnit;
	}
	public double getMessageFields_msValidityPeriod_multiplier() {
		return messageFields_msValidityPeriod_multiplier;
	}
	public void setMessageFields_msValidityPeriod_multiplier(
			double messageFieldsMsValidityPeriodMultiplier) {
		messageFields_msValidityPeriod_multiplier = messageFieldsMsValidityPeriodMultiplier;
	}
	public String getMessageFields_alertOnMessageDelivery() {
		return messageFields_alertOnMessageDelivery;
	}
	public void setMessageFields_alertOnMessageDelivery(
			String messageFieldsAlertOnMessageDelivery) {
		messageFields_alertOnMessageDelivery = messageFieldsAlertOnMessageDelivery;
	}
	public String getMessageFields_SmsSignal() {
		return messageFields_SmsSignal;
	}
	public void setMessageFields_SmsSignal(String messageFieldsSmsSignal) {
		messageFields_SmsSignal = messageFieldsSmsSignal;
	}
	public String getMessageFields_sourceBearerType() {
		return messageFields_sourceBearerType;
	}
	public void setMessageFields_sourceBearerType(
			String messageFieldsSourceBearerType) {
		messageFields_sourceBearerType = messageFieldsSourceBearerType;
	}
	public String getMessageFields_destBearerType() {
		return messageFields_destBearerType;
	}
	public void setMessageFields_destBearerType(String messageFieldsDestBearerType) {
		messageFields_destBearerType = messageFieldsDestBearerType;
	}
	public double getMessageFields_SmDefaultMsgID() {
		return messageFields_SmDefaultMsgID;
	}
	public void setMessageFields_SmDefaultMsgID(double messageFieldsSmDefaultMsgID) {
		messageFields_SmDefaultMsgID = messageFieldsSmDefaultMsgID;
	}
	public String getMessageFields_sourceNetworkType() {
		return messageFields_sourceNetworkType;
	}
	public void setMessageFields_sourceNetworkType(
			String messageFieldsSourceNetworkType) {
		messageFields_sourceNetworkType = messageFieldsSourceNetworkType;
	}
	public String getMessageFields_destNetworkType() {
		return messageFields_destNetworkType;
	}
	public void setMessageFields_destNetworkType(String messageFieldsDestNetworkType) {
		messageFields_destNetworkType = messageFieldsDestNetworkType;
	}
	public String getMessageFields_xsMessageType() {
		return messageFields_xsMessageType;
	}
	public void setMessageFields_xsMessageType(String messageFieldsXsMessageType) {
		messageFields_xsMessageType = messageFieldsXsMessageType;
	}
	public String getOutboundMo_submissionResult() {
		return outboundMo_submissionResult;
	}
	public void setOutboundMo_submissionResult(String outboundMoSubmissionResult) {
		outboundMo_submissionResult = outboundMoSubmissionResult;
	}
	public String getOutboundMo_smscName() {
		return outboundMo_smscName;
	}
	public void setOutboundMo_smscName(String outboundMoSmscName) {
		outboundMo_smscName = outboundMoSmscName;
	}
	public String getOutboundMo_rejectCause() {
		return outboundMo_rejectCause;
	}
	public void setOutboundMo_rejectCause(String outboundMoRejectCause) {
		outboundMo_rejectCause = outboundMoRejectCause;
	}
	public String getOutboundMt_sriSmRoutingAction() {
		return outboundMt_sriSmRoutingAction;
	}
	public void setOutboundMt_sriSmRoutingAction(String outboundMtSriSmRoutingAction) {
		outboundMt_sriSmRoutingAction = outboundMtSriSmRoutingAction;
	}
	public String getOutboundMt_sriSmRejectInfo_rejectCause() {
		return outboundMt_sriSmRejectInfo_rejectCause;
	}
	public void setOutboundMt_sriSmRejectInfo_rejectCause(
			String outboundMtSriSmRejectInfoRejectCause) {
		outboundMt_sriSmRejectInfo_rejectCause = outboundMtSriSmRejectInfoRejectCause;
	}
	public String getOutboundMt_sriSmRejectInfo_mtRoutingRule() {
		return outboundMt_sriSmRejectInfo_mtRoutingRule;
	}
	public void setOutboundMt_sriSmRejectInfo_mtRoutingRule(
			String outboundMtSriSmRejectInfoMtRoutingRule) {
		outboundMt_sriSmRejectInfo_mtRoutingRule = outboundMtSriSmRejectInfoMtRoutingRule;
	}
	public String getOutboundMt_sriSmRejectInfo_mtExtConditionRule() {
		return outboundMt_sriSmRejectInfo_mtExtConditionRule;
	}
	public void setOutboundMt_sriSmRejectInfo_mtExtConditionRule(
			String outboundMtSriSmRejectInfoMtExtConditionRule) {
		outboundMt_sriSmRejectInfo_mtExtConditionRule = outboundMtSriSmRejectInfoMtExtConditionRule;
	}
	public String getOutboundMt_sriSmResponseInfo_queryResult() {
		return outboundMt_sriSmResponseInfo_queryResult;
	}
	public void setOutboundMt_sriSmResponseInfo_queryResult(
			String outboundMtSriSmResponseInfoQueryResult) {
		outboundMt_sriSmResponseInfo_queryResult = outboundMtSriSmResponseInfoQueryResult;
	}
	public String getOutboundMt_sriSmResponseInfo_mapImsi_country() {
		return outboundMt_sriSmResponseInfo_mapImsi_country;
	}
	public void setOutboundMt_sriSmResponseInfo_mapImsi_country(
			String outboundMtSriSmResponseInfoMapImsiCountry) {
		outboundMt_sriSmResponseInfo_mapImsi_country = outboundMtSriSmResponseInfoMapImsiCountry;
	}
	public String getOutboundMt_sriSmResponseInfo_mapImsi_network() {
		return outboundMt_sriSmResponseInfo_mapImsi_network;
	}
	public void setOutboundMt_sriSmResponseInfo_mapImsi_network(
			String outboundMtSriSmResponseInfoMapImsiNetwork) {
		outboundMt_sriSmResponseInfo_mapImsi_network = outboundMtSriSmResponseInfoMapImsiNetwork;
	}
	public String getOutboundMt_sriSmResponseInfo_mapImsi_imsi() {
		return outboundMt_sriSmResponseInfo_mapImsi_imsi;
	}
	public void setOutboundMt_sriSmResponseInfo_mapImsi_imsi(
			String outboundMtSriSmResponseInfoMapImsiImsi) {
		outboundMt_sriSmResponseInfo_mapImsi_imsi = outboundMtSriSmResponseInfoMapImsiImsi;
	}
	public String getOutboundMt_sriSmResponseInfo_mapLmsi() {
		return outboundMt_sriSmResponseInfo_mapLmsi;
	}
	public void setOutboundMt_sriSmResponseInfo_mapLmsi(
			String outboundMtSriSmResponseInfoMapLmsi) {
		outboundMt_sriSmResponseInfo_mapLmsi = outboundMtSriSmResponseInfoMapLmsi;
	}
	public String getOutboundMt_sriSmResponseInfo_mapMsc_country() {
		return outboundMt_sriSmResponseInfo_mapMsc_country;
	}
	public void setOutboundMt_sriSmResponseInfo_mapMsc_country(
			String outboundMtSriSmResponseInfoMapMscCountry) {
		outboundMt_sriSmResponseInfo_mapMsc_country = outboundMtSriSmResponseInfoMapMscCountry;
	}
	public String getOutboundMt_sriSmResponseInfo_mapMsc_network() {
		return outboundMt_sriSmResponseInfo_mapMsc_network;
	}
	public void setOutboundMt_sriSmResponseInfo_mapMsc_network(
			String outboundMtSriSmResponseInfoMapMscNetwork) {
		outboundMt_sriSmResponseInfo_mapMsc_network = outboundMtSriSmResponseInfoMapMscNetwork;
	}
	public String getOutboundMt_sriSmResponseInfo_mapMsc_gsmAddress_ton() {
		return outboundMt_sriSmResponseInfo_mapMsc_gsmAddress_ton;
	}
	public void setOutboundMt_sriSmResponseInfo_mapMsc_gsmAddress_ton(
			String outboundMtSriSmResponseInfoMapMscGsmAddressTon) {
		outboundMt_sriSmResponseInfo_mapMsc_gsmAddress_ton = outboundMtSriSmResponseInfoMapMscGsmAddressTon;
	}
	public String getOutboundMt_sriSmResponseInfo_mapMsc_gsmAddress_npi() {
		return outboundMt_sriSmResponseInfo_mapMsc_gsmAddress_npi;
	}
	public void setOutboundMt_sriSmResponseInfo_mapMsc_gsmAddress_npi(
			String outboundMtSriSmResponseInfoMapMscGsmAddressNpi) {
		outboundMt_sriSmResponseInfo_mapMsc_gsmAddress_npi = outboundMtSriSmResponseInfoMapMscGsmAddressNpi;
	}
	public String getOutboundMt_sriSmResponseInfo_mapMsc_gsmAddress_number() {
		return outboundMt_sriSmResponseInfo_mapMsc_gsmAddress_number;
	}
	public void setOutboundMt_sriSmResponseInfo_mapMsc_gsmAddress_number(
			String outboundMtSriSmResponseInfoMapMscGsmAddressNumber) {
		outboundMt_sriSmResponseInfo_mapMsc_gsmAddress_number = outboundMtSriSmResponseInfoMapMscGsmAddressNumber;
	}
	public String getOutboundMt_sriSmResponseInfo_mapSgsn_country() {
		return outboundMt_sriSmResponseInfo_mapSgsn_country;
	}
	public void setOutboundMt_sriSmResponseInfo_mapSgsn_country(
			String outboundMtSriSmResponseInfoMapSgsnCountry) {
		outboundMt_sriSmResponseInfo_mapSgsn_country = outboundMtSriSmResponseInfoMapSgsnCountry;
	}
	public String getOutboundMt_sriSmResponseInfo_mapSgsn_network() {
		return outboundMt_sriSmResponseInfo_mapSgsn_network;
	}
	public void setOutboundMt_sriSmResponseInfo_mapSgsn_network(
			String outboundMtSriSmResponseInfoMapSgsnNetwork) {
		outboundMt_sriSmResponseInfo_mapSgsn_network = outboundMtSriSmResponseInfoMapSgsnNetwork;
	}
	public String getOutboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_ton() {
		return outboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_ton;
	}
	public void setOutboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_ton(
			String outboundMtSriSmResponseInfoMapSgsnGsmAddressTon) {
		outboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_ton = outboundMtSriSmResponseInfoMapSgsnGsmAddressTon;
	}
	public String getOutboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_npi() {
		return outboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_npi;
	}
	public void setOutboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_npi(
			String outboundMtSriSmResponseInfoMapSgsnGsmAddressNpi) {
		outboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_npi = outboundMtSriSmResponseInfoMapSgsnGsmAddressNpi;
	}
	public String getOutboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_number() {
		return outboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_number;
	}
	public void setOutboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_number(
			String outboundMtSriSmResponseInfoMapSgsnGsmAddressNumber) {
		outboundMt_sriSmResponseInfo_mapSgsn_gsmAddress_number = outboundMtSriSmResponseInfoMapSgsnGsmAddressNumber;
	}
	public String getOutboundMt_sriSmResponseInfo_mtRoutingRule() {
		return outboundMt_sriSmResponseInfo_mtRoutingRule;
	}
	public void setOutboundMt_sriSmResponseInfo_mtRoutingRule(
			String outboundMtSriSmResponseInfoMtRoutingRule) {
		outboundMt_sriSmResponseInfo_mtRoutingRule = outboundMtSriSmResponseInfoMtRoutingRule;
	}
	public String getOutboundMt_mtFwdSmToMscRoutingAction() {
		return outboundMt_mtFwdSmToMscRoutingAction;
	}
	public void setOutboundMt_mtFwdSmToMscRoutingAction(
			String outboundMtMtFwdSmToMscRoutingAction) {
		outboundMt_mtFwdSmToMscRoutingAction = outboundMtMtFwdSmToMscRoutingAction;
	}
	public String getOutboundMt_mtFwdSmToMscRejectInfo_rejectCause() {
		return outboundMt_mtFwdSmToMscRejectInfo_rejectCause;
	}
	public void setOutboundMt_mtFwdSmToMscRejectInfo_rejectCause(
			String outboundMtMtFwdSmToMscRejectInfoRejectCause) {
		outboundMt_mtFwdSmToMscRejectInfo_rejectCause = outboundMtMtFwdSmToMscRejectInfoRejectCause;
	}
	public String getOutboundMt_mtFwdSmToMscRejectInfo_mtRoutingRule() {
		return outboundMt_mtFwdSmToMscRejectInfo_mtRoutingRule;
	}
	public void setOutboundMt_mtFwdSmToMscRejectInfo_mtRoutingRule(
			String outboundMtMtFwdSmToMscRejectInfoMtRoutingRule) {
		outboundMt_mtFwdSmToMscRejectInfo_mtRoutingRule = outboundMtMtFwdSmToMscRejectInfoMtRoutingRule;
	}
	public String getOutboundMt_mtFwdSmToMscRejectInfo_mtExtConditionRule() {
		return outboundMt_mtFwdSmToMscRejectInfo_mtExtConditionRule;
	}
	public void setOutboundMt_mtFwdSmToMscRejectInfo_mtExtConditionRule(
			String outboundMtMtFwdSmToMscRejectInfoMtExtConditionRule) {
		outboundMt_mtFwdSmToMscRejectInfo_mtExtConditionRule = outboundMtMtFwdSmToMscRejectInfoMtExtConditionRule;
	}
	public String getOutboundMt_mtFwdSmToMscResponseInfo_deliveryResult() {
		return outboundMt_mtFwdSmToMscResponseInfo_deliveryResult;
	}
	public void setOutboundMt_mtFwdSmToMscResponseInfo_deliveryResult(
			String outboundMtMtFwdSmToMscResponseInfoDeliveryResult) {
		outboundMt_mtFwdSmToMscResponseInfo_deliveryResult = outboundMtMtFwdSmToMscResponseInfoDeliveryResult;
	}
	public String getOutboundMt_mtFwdSmToMscResponseInfo_mtRoutingRule() {
		return outboundMt_mtFwdSmToMscResponseInfo_mtRoutingRule;
	}
	public void setOutboundMt_mtFwdSmToMscResponseInfo_mtRoutingRule(
			String outboundMtMtFwdSmToMscResponseInfoMtRoutingRule) {
		outboundMt_mtFwdSmToMscResponseInfo_mtRoutingRule = outboundMtMtFwdSmToMscResponseInfoMtRoutingRule;
	}
	public String getOutboundMt_mtFwdSmToSgsnRoutingAction() {
		return outboundMt_mtFwdSmToSgsnRoutingAction;
	}
	public void setOutboundMt_mtFwdSmToSgsnRoutingAction(
			String outboundMtMtFwdSmToSgsnRoutingAction) {
		outboundMt_mtFwdSmToSgsnRoutingAction = outboundMtMtFwdSmToSgsnRoutingAction;
	}
	public String getOutboundMt_mtFwdSmToSgsnRejectInfo_rejectCause() {
		return outboundMt_mtFwdSmToSgsnRejectInfo_rejectCause;
	}
	public void setOutboundMt_mtFwdSmToSgsnRejectInfo_rejectCause(
			String outboundMtMtFwdSmToSgsnRejectInfoRejectCause) {
		outboundMt_mtFwdSmToSgsnRejectInfo_rejectCause = outboundMtMtFwdSmToSgsnRejectInfoRejectCause;
	}
	public String getOutboundMt_mtFwdSmToSgsnRejectInfo_mtRoutingRule() {
		return outboundMt_mtFwdSmToSgsnRejectInfo_mtRoutingRule;
	}
	public void setOutboundMt_mtFwdSmToSgsnRejectInfo_mtRoutingRule(
			String outboundMtMtFwdSmToSgsnRejectInfoMtRoutingRule) {
		outboundMt_mtFwdSmToSgsnRejectInfo_mtRoutingRule = outboundMtMtFwdSmToSgsnRejectInfoMtRoutingRule;
	}
	public String getOutboundMt_mtFwdSmToSgsnRejectInfo_mtExtConditionRule() {
		return outboundMt_mtFwdSmToSgsnRejectInfo_mtExtConditionRule;
	}
	public void setOutboundMt_mtFwdSmToSgsnRejectInfo_mtExtConditionRule(
			String outboundMtMtFwdSmToSgsnRejectInfoMtExtConditionRule) {
		outboundMt_mtFwdSmToSgsnRejectInfo_mtExtConditionRule = outboundMtMtFwdSmToSgsnRejectInfoMtExtConditionRule;
	}
	public String getOutboundMt_mtFwdSmToSgsnResponseInfo_deliveryResult() {
		return outboundMt_mtFwdSmToSgsnResponseInfo_deliveryResult;
	}
	public void setOutboundMt_mtFwdSmToSgsnResponseInfo_deliveryResult(
			String outboundMtMtFwdSmToSgsnResponseInfoDeliveryResult) {
		outboundMt_mtFwdSmToSgsnResponseInfo_deliveryResult = outboundMtMtFwdSmToSgsnResponseInfoDeliveryResult;
	}
	public String getOutboundMt_mtFwdSmToSgsnResponseInfo_mtRoutingRule() {
		return outboundMt_mtFwdSmToSgsnResponseInfo_mtRoutingRule;
	}
	public void setOutboundMt_mtFwdSmToSgsnResponseInfo_mtRoutingRule(
			String outboundMtMtFwdSmToSgsnResponseInfoMtRoutingRule) {
		outboundMt_mtFwdSmToSgsnResponseInfo_mtRoutingRule = outboundMtMtFwdSmToSgsnResponseInfoMtRoutingRule;
	}
	public String getOutboundMt_ecResponseData_extConditionRule() {
		return outboundMt_ecResponseData_extConditionRule;
	}
	public void setOutboundMt_ecResponseData_extConditionRule(
			String outboundMtEcResponseDataExtConditionRule) {
		outboundMt_ecResponseData_extConditionRule = outboundMtEcResponseDataExtConditionRule;
	}
	public String getOutboundMt_ecResponseData_applicationName() {
		return outboundMt_ecResponseData_applicationName;
	}
	public void setOutboundMt_ecResponseData_applicationName(
			String outboundMtEcResponseDataApplicationName) {
		outboundMt_ecResponseData_applicationName = outboundMtEcResponseDataApplicationName;
	}
	public double getOutboundMt_ecResponseData_clientIpAddress() {
		return outboundMt_ecResponseData_clientIpAddress;
	}
	public void setOutboundMt_ecResponseData_clientIpAddress(
			double outboundMtEcResponseDataClientIpAddress) {
		outboundMt_ecResponseData_clientIpAddress = outboundMtEcResponseDataClientIpAddress;
	}
	public String getOutboundMt_ecResponseData_evaluationResult() {
		return outboundMt_ecResponseData_evaluationResult;
	}
	public void setOutboundMt_ecResponseData_evaluationResult(
			String outboundMtEcResponseDataEvaluationResult) {
		outboundMt_ecResponseData_evaluationResult = outboundMtEcResponseDataEvaluationResult;
	}
	public String getOutboundMt_ecResponseData_attributesSet() {
		return outboundMt_ecResponseData_attributesSet;
	}
	public void setOutboundMt_ecResponseData_attributesSet(
			String outboundMtEcResponseDataAttributesSet) {
		outboundMt_ecResponseData_attributesSet = outboundMtEcResponseDataAttributesSet;
	}
	public String getOutboundMt_ecResponseData_attributesReset() {
		return outboundMt_ecResponseData_attributesReset;
	}
	public void setOutboundMt_ecResponseData_attributesReset(
			String outboundMtEcResponseDataAttributesReset) {
		outboundMt_ecResponseData_attributesReset = outboundMtEcResponseDataAttributesReset;
	}
	public double getOutboundMt_ecResponseData_diameterStatus() {
		return outboundMt_ecResponseData_diameterStatus;
	}
	public void setOutboundMt_ecResponseData_diameterStatus(
			double outboundMtEcResponseDataDiameterStatus) {
		outboundMt_ecResponseData_diameterStatus = outboundMtEcResponseDataDiameterStatus;
	}
	public String getOutboundMt_ecResponseData_textInEvaluationResponse() {
		return outboundMt_ecResponseData_textInEvaluationResponse;
	}
	public void setOutboundMt_ecResponseData_textInEvaluationResponse(
			String outboundMtEcResponseDataTextInEvaluationResponse) {
		outboundMt_ecResponseData_textInEvaluationResponse = outboundMtEcResponseDataTextInEvaluationResponse;
	}
	public String getOutboundAo_submissionResult() {
		return outboundAo_submissionResult;
	}
	public void setOutboundAo_submissionResult(String outboundAoSubmissionResult) {
		outboundAo_submissionResult = outboundAoSubmissionResult;
	}
	public String getOutboundAo_applicationName() {
		return outboundAo_applicationName;
	}
	public void setOutboundAo_applicationName(String outboundAoApplicationName) {
		outboundAo_applicationName = outboundAoApplicationName;
	}
	public String getOutboundAo_applicationShortNumber_ton() {
		return outboundAo_applicationShortNumber_ton;
	}
	public void setOutboundAo_applicationShortNumber_ton(
			String outboundAoApplicationShortNumberTon) {
		outboundAo_applicationShortNumber_ton = outboundAoApplicationShortNumberTon;
	}
	public String getOutboundAo_applicationShortNumber_npi() {
		return outboundAo_applicationShortNumber_npi;
	}
	public void setOutboundAo_applicationShortNumber_npi(
			String outboundAoApplicationShortNumberNpi) {
		outboundAo_applicationShortNumber_npi = outboundAoApplicationShortNumberNpi;
	}
	public String getOutboundAo_applicationShortNumber_number() {
		return outboundAo_applicationShortNumber_number;
	}
	public void setOutboundAo_applicationShortNumber_number(
			String outboundAoApplicationShortNumberNumber) {
		outboundAo_applicationShortNumber_number = outboundAoApplicationShortNumberNumber;
	}
	public String getOutboundAo_serviceCentreName() {
		return outboundAo_serviceCentreName;
	}
	public void setOutboundAo_serviceCentreName(String outboundAoServiceCentreName) {
		outboundAo_serviceCentreName = outboundAoServiceCentreName;
	}
	public String getOutboundAo_scNodeName() {
		return outboundAo_scNodeName;
	}
	public void setOutboundAo_scNodeName(String outboundAoScNodeName) {
		outboundAo_scNodeName = outboundAoScNodeName;
	}
	public String getOutboundAo_scTerminationPointName() {
		return outboundAo_scTerminationPointName;
	}
	public void setOutboundAo_scTerminationPointName(
			String outboundAoScTerminationPointName) {
		outboundAo_scTerminationPointName = outboundAoScTerminationPointName;
	}
	public double getOutboundAo_routingErrorCode() {
		return outboundAo_routingErrorCode;
	}
	public void setOutboundAo_routingErrorCode(double outboundAoRoutingErrorCode) {
		outboundAo_routingErrorCode = outboundAoRoutingErrorCode;
	}
	public String getOutboundAo_serviceCentreTimestamp() {
		return outboundAo_serviceCentreTimestamp;
	}
	public void setOutboundAo_serviceCentreTimestamp(
			String outboundAoServiceCentreTimestamp) {
		outboundAo_serviceCentreTimestamp = outboundAoServiceCentreTimestamp;
	}
	public String getOutboundAo_smppMessageId() {
		return outboundAo_smppMessageId;
	}
	public void setOutboundAo_smppMessageId(String outboundAoSmppMessageId) {
		outboundAo_smppMessageId = outboundAoSmppMessageId;
	}
	public String getOutboundAt_routingAction() {
		return outboundAt_routingAction;
	}
	public void setOutboundAt_routingAction(String outboundAtRoutingAction) {
		outboundAt_routingAction = outboundAtRoutingAction;
	}
	public String getOutboundAt_rejectInfo_rejectCause() {
		return outboundAt_rejectInfo_rejectCause;
	}
	public void setOutboundAt_rejectInfo_rejectCause(
			String outboundAtRejectInfoRejectCause) {
		outboundAt_rejectInfo_rejectCause = outboundAtRejectInfoRejectCause;
	}
	public String getOutboundAt_rejectInfo_atRoutingRule() {
		return outboundAt_rejectInfo_atRoutingRule;
	}
	public void setOutboundAt_rejectInfo_atRoutingRule(
			String outboundAtRejectInfoAtRoutingRule) {
		outboundAt_rejectInfo_atRoutingRule = outboundAtRejectInfoAtRoutingRule;
	}
	public String getOutboundAt_rejectInfo_atExtConditionRule() {
		return outboundAt_rejectInfo_atExtConditionRule;
	}
	public void setOutboundAt_rejectInfo_atExtConditionRule(
			String outboundAtRejectInfoAtExtConditionRule) {
		outboundAt_rejectInfo_atExtConditionRule = outboundAtRejectInfoAtExtConditionRule;
	}
	public String getOutboundAt_responseInfo_deliveryResult() {
		return outboundAt_responseInfo_deliveryResult;
	}
	public void setOutboundAt_responseInfo_deliveryResult(
			String outboundAtResponseInfoDeliveryResult) {
		outboundAt_responseInfo_deliveryResult = outboundAtResponseInfoDeliveryResult;
	}
	public double getOutboundAt_responseInfo_routingErrorCode() {
		return outboundAt_responseInfo_routingErrorCode;
	}
	public void setOutboundAt_responseInfo_routingErrorCode(
			double outboundAtResponseInfoRoutingErrorCode) {
		outboundAt_responseInfo_routingErrorCode = outboundAtResponseInfoRoutingErrorCode;
	}
	public String getOutboundAt_responseInfo_atRoutingRule() {
		return outboundAt_responseInfo_atRoutingRule;
	}
	public void setOutboundAt_responseInfo_atRoutingRule(
			String outboundAtResponseInfoAtRoutingRule) {
		outboundAt_responseInfo_atRoutingRule = outboundAtResponseInfoAtRoutingRule;
	}
	public String getOutboundAt_applicationName() {
		return outboundAt_applicationName;
	}
	public void setOutboundAt_applicationName(String outboundAtApplicationName) {
		outboundAt_applicationName = outboundAtApplicationName;
	}
	public String getOutboundAt_applicationShortNumber_ton() {
		return outboundAt_applicationShortNumber_ton;
	}
	public void setOutboundAt_applicationShortNumber_ton(
			String outboundAtApplicationShortNumberTon) {
		outboundAt_applicationShortNumber_ton = outboundAtApplicationShortNumberTon;
	}
	public String getOutboundAt_applicationShortNumber_npi() {
		return outboundAt_applicationShortNumber_npi;
	}
	public void setOutboundAt_applicationShortNumber_npi(
			String outboundAtApplicationShortNumberNpi) {
		outboundAt_applicationShortNumber_npi = outboundAtApplicationShortNumberNpi;
	}
	public String getOutboundAt_applicationShortNumber_number() {
		return outboundAt_applicationShortNumber_number;
	}
	public void setOutboundAt_applicationShortNumber_number(
			String outboundAtApplicationShortNumberNumber) {
		outboundAt_applicationShortNumber_number = outboundAtApplicationShortNumberNumber;
	}
	public String getOutboundAt_ecResponseData_extConditionRule() {
		return outboundAt_ecResponseData_extConditionRule;
	}
	public void setOutboundAt_ecResponseData_extConditionRule(
			String outboundAtEcResponseDataExtConditionRule) {
		outboundAt_ecResponseData_extConditionRule = outboundAtEcResponseDataExtConditionRule;
	}
	public String getOutboundAt_ecResponseData_applicationName() {
		return outboundAt_ecResponseData_applicationName;
	}
	public void setOutboundAt_ecResponseData_applicationName(
			String outboundAtEcResponseDataApplicationName) {
		outboundAt_ecResponseData_applicationName = outboundAtEcResponseDataApplicationName;
	}
	public double getOutboundAt_ecResponseData_clientIpAddress() {
		return outboundAt_ecResponseData_clientIpAddress;
	}
	public void setOutboundAt_ecResponseData_clientIpAddress(
			double outboundAtEcResponseDataClientIpAddress) {
		outboundAt_ecResponseData_clientIpAddress = outboundAtEcResponseDataClientIpAddress;
	}
	public String getOutboundAt_ecResponseData_evaluationResult() {
		return outboundAt_ecResponseData_evaluationResult;
	}
	public void setOutboundAt_ecResponseData_evaluationResult(
			String outboundAtEcResponseDataEvaluationResult) {
		outboundAt_ecResponseData_evaluationResult = outboundAtEcResponseDataEvaluationResult;
	}
	public String getOutboundAt_ecResponseData_attributesSet() {
		return outboundAt_ecResponseData_attributesSet;
	}
	public void setOutboundAt_ecResponseData_attributesSet(
			String outboundAtEcResponseDataAttributesSet) {
		outboundAt_ecResponseData_attributesSet = outboundAtEcResponseDataAttributesSet;
	}
	public String getOutboundAt_ecResponseData_attributesReset() {
		return outboundAt_ecResponseData_attributesReset;
	}
	public void setOutboundAt_ecResponseData_attributesReset(
			String outboundAtEcResponseDataAttributesReset) {
		outboundAt_ecResponseData_attributesReset = outboundAtEcResponseDataAttributesReset;
	}
	public double getOutboundAt_ecResponseData_diameterStatus() {
		return outboundAt_ecResponseData_diameterStatus;
	}
	public void setOutboundAt_ecResponseData_diameterStatus(
			double outboundAtEcResponseDataDiameterStatus) {
		outboundAt_ecResponseData_diameterStatus = outboundAtEcResponseDataDiameterStatus;
	}
	public String getOutboundAt_ecResponseData_textInEvaluationResponse() {
		return outboundAt_ecResponseData_textInEvaluationResponse;
	}
	public void setOutboundAt_ecResponseData_textInEvaluationResponse(
			String outboundAtEcResponseDataTextInEvaluationResponse) {
		outboundAt_ecResponseData_textInEvaluationResponse = outboundAtEcResponseDataTextInEvaluationResponse;
	}
	public String getStorage_storageResult() {
		return storage_storageResult;
	}
	public void setStorage_storageResult(String storageStorageResult) {
		storage_storageResult = storageStorageResult;
	}
	public double getStorage_routingErrorCode() {
		return storage_routingErrorCode;
	}
	public void setStorage_routingErrorCode(double storageRoutingErrorCode) {
		storage_routingErrorCode = storageRoutingErrorCode;
	}
	public String getStorage_applicationName() {
		return storage_applicationName;
	}
	public void setStorage_applicationName(String storageApplicationName) {
		storage_applicationName = storageApplicationName;
	}
	public String getStorage_applicationShortNumber_ton() {
		return storage_applicationShortNumber_ton;
	}
	public void setStorage_applicationShortNumber_ton(
			String storageApplicationShortNumberTon) {
		storage_applicationShortNumber_ton = storageApplicationShortNumberTon;
	}
	public String getStorage_applicationShortNumber_npi() {
		return storage_applicationShortNumber_npi;
	}
	public void setStorage_applicationShortNumber_npi(
			String storageApplicationShortNumberNpi) {
		storage_applicationShortNumber_npi = storageApplicationShortNumberNpi;
	}
	public String getStorage_applicationShortNumber_number() {
		return storage_applicationShortNumber_number;
	}
	public void setStorage_applicationShortNumber_number(
			String storageApplicationShortNumberNumber) {
		storage_applicationShortNumber_number = storageApplicationShortNumberNumber;
	}
	public double getStorage_queue() {
		return storage_queue;
	}
	public void setStorage_queue(double storageQueue) {
		storage_queue = storageQueue;
	}
	public String getMoreMessagesToSend() {
		return moreMessagesToSend;
	}
	public void setMoreMessagesToSend(String moreMessagesToSend) {
		this.moreMessagesToSend = moreMessagesToSend;
	}
	public double getNumberOfPreviousAttempts() {
		return numberOfPreviousAttempts;
	}
	public void setNumberOfPreviousAttempts(double numberOfPreviousAttempts) {
		this.numberOfPreviousAttempts = numberOfPreviousAttempts;
	}
	public String getServiceCentreTimestamp() {
		return serviceCentreTimestamp;
	}
	public void setServiceCentreTimestamp(String serviceCentreTimestamp) {
		this.serviceCentreTimestamp = serviceCentreTimestamp;
	}
	public double getMessageIdentifier_amsId() {
		return messageIdentifier_amsId;
	}
	public void setMessageIdentifier_amsId(double messageIdentifierAmsId) {
		messageIdentifier_amsId = messageIdentifierAmsId;
	}
	public double getMessageIdentifier_msgId() {
		return messageIdentifier_msgId;
	}
	public void setMessageIdentifier_msgId(double messageIdentifierMsgId) {
		messageIdentifier_msgId = messageIdentifierMsgId;
	}
	public String getIsNotificationMessage() {
		return isNotificationMessage;
	}
	public void setIsNotificationMessage(String isNotificationMessage) {
		this.isNotificationMessage = isNotificationMessage;
	}
	public String getUnConditionalForward() {
		return unConditionalForward;
	}
	public void setUnConditionalForward(String unConditionalForward) {
		this.unConditionalForward = unConditionalForward;
	}
	public double getEvent() {
		return event;
	}
	public void setEvent(double event) {
		this.event = event;
	}
	public String getMsgCommand() {
		return msgCommand;
	}
	public void setMsgCommand(String msgCommand) {
		this.msgCommand = msgCommand;
	}
	public String getSmResult() {
		return smResult;
	}
	public void setSmResult(String smResult) {
		this.smResult = smResult;
	}
	public String getCmdOriginatorAddress_country() {
		return cmdOriginatorAddress_country;
	}
	public void setCmdOriginatorAddress_country(String cmdOriginatorAddressCountry) {
		cmdOriginatorAddress_country = cmdOriginatorAddressCountry;
	}
	public String getCmdOriginatorAddress_network() {
		return cmdOriginatorAddress_network;
	}
	public void setCmdOriginatorAddress_network(String cmdOriginatorAddressNetwork) {
		cmdOriginatorAddress_network = cmdOriginatorAddressNetwork;
	}
	public String getCmdOriginatorAddress_gsmAddress_ton() {
		return cmdOriginatorAddress_gsmAddress_ton;
	}
	public void setCmdOriginatorAddress_gsmAddress_ton(
			String cmdOriginatorAddressGsmAddressTon) {
		cmdOriginatorAddress_gsmAddress_ton = cmdOriginatorAddressGsmAddressTon;
	}
	public String getCmdOriginatorAddress_gsmAddress_npi() {
		return cmdOriginatorAddress_gsmAddress_npi;
	}
	public void setCmdOriginatorAddress_gsmAddress_npi(
			String cmdOriginatorAddressGsmAddressNpi) {
		cmdOriginatorAddress_gsmAddress_npi = cmdOriginatorAddressGsmAddressNpi;
	}
	public String getCmdOriginatorAddress_gsmAddress_number() {
		return cmdOriginatorAddress_gsmAddress_number;
	}
	public void setCmdOriginatorAddress_gsmAddress_number(
			String cmdOriginatorAddressGsmAddressNumber) {
		cmdOriginatorAddress_gsmAddress_number = cmdOriginatorAddressGsmAddressNumber;
	}
	public String getCmdAlphanumericOriginator() {
		return cmdAlphanumericOriginator;
	}
	public void setCmdAlphanumericOriginator(String cmdAlphanumericOriginator) {
		this.cmdAlphanumericOriginator = cmdAlphanumericOriginator;
	}
	public String getCmdRecipientAddress_country() {
		return cmdRecipientAddress_country;
	}
	public void setCmdRecipientAddress_country(String cmdRecipientAddressCountry) {
		cmdRecipientAddress_country = cmdRecipientAddressCountry;
	}
	public String getCmdRecipientAddress_network() {
		return cmdRecipientAddress_network;
	}
	public void setCmdRecipientAddress_network(String cmdRecipientAddressNetwork) {
		cmdRecipientAddress_network = cmdRecipientAddressNetwork;
	}
	public String getCmdRecipientAddress_gsmAddress_ton() {
		return cmdRecipientAddress_gsmAddress_ton;
	}
	public void setCmdRecipientAddress_gsmAddress_ton(
			String cmdRecipientAddressGsmAddressTon) {
		cmdRecipientAddress_gsmAddress_ton = cmdRecipientAddressGsmAddressTon;
	}
	public String getCmdRecipientAddress_gsmAddress_npi() {
		return cmdRecipientAddress_gsmAddress_npi;
	}
	public void setCmdRecipientAddress_gsmAddress_npi(
			String cmdRecipientAddressGsmAddressNpi) {
		cmdRecipientAddress_gsmAddress_npi = cmdRecipientAddressGsmAddressNpi;
	}
	public String getCmdRecipientAddress_gsmAddress_number() {
		return cmdRecipientAddress_gsmAddress_number;
	}
	public void setCmdRecipientAddress_gsmAddress_number(
			String cmdRecipientAddressGsmAddressNumber) {
		cmdRecipientAddress_gsmAddress_number = cmdRecipientAddressGsmAddressNumber;
	}
	public double getFoundCount() {
		return foundCount;
	}
	public void setFoundCount(double foundCount) {
		this.foundCount = foundCount;
	}
	public String getCancelMode() {
		return cancelMode;
	}
	public void setCancelMode(String cancelMode) {
		this.cancelMode = cancelMode;
	}
	public String getRecipientRoutingNumber() {
		return recipientRoutingNumber;
	}
	public void setRecipientRoutingNumber(String recipientRoutingNumber) {
		this.recipientRoutingNumber = recipientRoutingNumber;
	}
	public String getEcResponseData_extConditionRule() {
		return ecResponseData_extConditionRule;
	}
	public void setEcResponseData_extConditionRule(
			String ecResponseDataExtConditionRule) {
		ecResponseData_extConditionRule = ecResponseDataExtConditionRule;
	}
	public String getEcResponseData_applicationName() {
		return ecResponseData_applicationName;
	}
	public void setEcResponseData_applicationName(
			String ecResponseDataApplicationName) {
		ecResponseData_applicationName = ecResponseDataApplicationName;
	}
	public double getEcResponseData_clientIpAddress() {
		return ecResponseData_clientIpAddress;
	}
	public void setEcResponseData_clientIpAddress(
			double ecResponseDataClientIpAddress) {
		ecResponseData_clientIpAddress = ecResponseDataClientIpAddress;
	}
	public String getEcResponseData_evaluationResult() {
		return ecResponseData_evaluationResult;
	}
	public void setEcResponseData_evaluationResult(
			String ecResponseDataEvaluationResult) {
		ecResponseData_evaluationResult = ecResponseDataEvaluationResult;
	}
	public String getEcResponseData_attributesSet() {
		return ecResponseData_attributesSet;
	}
	public void setEcResponseData_attributesSet(String ecResponseDataAttributesSet) {
		ecResponseData_attributesSet = ecResponseDataAttributesSet;
	}
	public String getEcResponseData_attributesReset() {
		return ecResponseData_attributesReset;
	}
	public void setEcResponseData_attributesReset(
			String ecResponseDataAttributesReset) {
		ecResponseData_attributesReset = ecResponseDataAttributesReset;
	}
	public double getEcResponseData_diameterStatus() {
		return ecResponseData_diameterStatus;
	}
	public void setEcResponseData_diameterStatus(double ecResponseDataDiameterStatus) {
		ecResponseData_diameterStatus = ecResponseDataDiameterStatus;
	}
	public String getEcResponseData_textInEvaluationResponse() {
		return ecResponseData_textInEvaluationResponse;
	}
	public void setEcResponseData_textInEvaluationResponse(
			String ecResponseDataTextInEvaluationResponse) {
		ecResponseData_textInEvaluationResponse = ecResponseDataTextInEvaluationResponse;
	}
	public String getMtRoutingRuleSkipped() {
		return mtRoutingRuleSkipped;
	}
	public void setMtRoutingRuleSkipped(String mtRoutingRuleSkipped) {
		this.mtRoutingRuleSkipped = mtRoutingRuleSkipped;
	}
	public String getOriginalMessageFields_originatorAddress_country() {
		return originalMessageFields_originatorAddress_country;
	}
	public void setOriginalMessageFields_originatorAddress_country(
			String originalMessageFieldsOriginatorAddressCountry) {
		originalMessageFields_originatorAddress_country = originalMessageFieldsOriginatorAddressCountry;
	}
	public String getOriginalMessageFields_originatorAddress_network() {
		return originalMessageFields_originatorAddress_network;
	}
	public void setOriginalMessageFields_originatorAddress_network(
			String originalMessageFieldsOriginatorAddressNetwork) {
		originalMessageFields_originatorAddress_network = originalMessageFieldsOriginatorAddressNetwork;
	}
	public String getOriginalMessageFields_originatorAddress_gsmAddress_ton() {
		return originalMessageFields_originatorAddress_gsmAddress_ton;
	}
	public void setOriginalMessageFields_originatorAddress_gsmAddress_ton(
			String originalMessageFieldsOriginatorAddressGsmAddressTon) {
		originalMessageFields_originatorAddress_gsmAddress_ton = originalMessageFieldsOriginatorAddressGsmAddressTon;
	}
	public String getOriginalMessageFields_originatorAddress_gsmAddress_npi() {
		return originalMessageFields_originatorAddress_gsmAddress_npi;
	}
	public void setOriginalMessageFields_originatorAddress_gsmAddress_npi(
			String originalMessageFieldsOriginatorAddressGsmAddressNpi) {
		originalMessageFields_originatorAddress_gsmAddress_npi = originalMessageFieldsOriginatorAddressGsmAddressNpi;
	}
	public String getOriginalMessageFields_originatorAddress_gsmAddress_number() {
		return originalMessageFields_originatorAddress_gsmAddress_number;
	}
	public void setOriginalMessageFields_originatorAddress_gsmAddress_number(
			String originalMessageFieldsOriginatorAddressGsmAddressNumber) {
		originalMessageFields_originatorAddress_gsmAddress_number = originalMessageFieldsOriginatorAddressGsmAddressNumber;
	}
	public String getOriginalMessageFields_recipientAddress_country() {
		return originalMessageFields_recipientAddress_country;
	}
	public void setOriginalMessageFields_recipientAddress_country(
			String originalMessageFieldsRecipientAddressCountry) {
		originalMessageFields_recipientAddress_country = originalMessageFieldsRecipientAddressCountry;
	}
	public String getOriginalMessageFields_recipientAddress_network() {
		return originalMessageFields_recipientAddress_network;
	}
	public void setOriginalMessageFields_recipientAddress_network(
			String originalMessageFieldsRecipientAddressNetwork) {
		originalMessageFields_recipientAddress_network = originalMessageFieldsRecipientAddressNetwork;
	}
	public String getOriginalMessageFields_recipientAddress_gsmAddress_ton() {
		return originalMessageFields_recipientAddress_gsmAddress_ton;
	}
	public void setOriginalMessageFields_recipientAddress_gsmAddress_ton(
			String originalMessageFieldsRecipientAddressGsmAddressTon) {
		originalMessageFields_recipientAddress_gsmAddress_ton = originalMessageFieldsRecipientAddressGsmAddressTon;
	}
	public String getOriginalMessageFields_recipientAddress_gsmAddress_npi() {
		return originalMessageFields_recipientAddress_gsmAddress_npi;
	}
	public void setOriginalMessageFields_recipientAddress_gsmAddress_npi(
			String originalMessageFieldsRecipientAddressGsmAddressNpi) {
		originalMessageFields_recipientAddress_gsmAddress_npi = originalMessageFieldsRecipientAddressGsmAddressNpi;
	}
	public String getOriginalMessageFields_recipientAddress_gsmAddress_number() {
		return originalMessageFields_recipientAddress_gsmAddress_number;
	}
	public void setOriginalMessageFields_recipientAddress_gsmAddress_number(
			String originalMessageFieldsRecipientAddressGsmAddressNumber) {
		originalMessageFields_recipientAddress_gsmAddress_number = originalMessageFieldsRecipientAddressGsmAddressNumber;
	}
	public String getInsideRejectInfo_rejectCause() {
		return insideRejectInfo_rejectCause;
	}
	public void setInsideRejectInfo_rejectCause(String insideRejectInfoRejectCause) {
		insideRejectInfo_rejectCause = insideRejectInfoRejectCause;
	}
	public String getInsideRejectInfo_atiRoutingRule() {
		return insideRejectInfo_atiRoutingRule;
	}
	public void setInsideRejectInfo_atiRoutingRule(
			String insideRejectInfoAtiRoutingRule) {
		insideRejectInfo_atiRoutingRule = insideRejectInfoAtiRoutingRule;
	}
	public String getInsideRejectInfo_atiExtConditionRule() {
		return insideRejectInfo_atiExtConditionRule;
	}
	public void setInsideRejectInfo_atiExtConditionRule(
			String insideRejectInfoAtiExtConditionRule) {
		insideRejectInfo_atiExtConditionRule = insideRejectInfoAtiExtConditionRule;
	}
	public String getInsideResponseInfo_deliveryResult() {
		return insideResponseInfo_deliveryResult;
	}
	public void setInsideResponseInfo_deliveryResult(
			String insideResponseInfoDeliveryResult) {
		insideResponseInfo_deliveryResult = insideResponseInfoDeliveryResult;
	}
	public double getInsideResponseInfo_routingErrorCode() {
		return insideResponseInfo_routingErrorCode;
	}
	public void setInsideResponseInfo_routingErrorCode(
			double insideResponseInfoRoutingErrorCode) {
		insideResponseInfo_routingErrorCode = insideResponseInfoRoutingErrorCode;
	}
	public String getInsideResponseInfo_atiRoutingRule() {
		return insideResponseInfo_atiRoutingRule;
	}
	public void setInsideResponseInfo_atiRoutingRule(
			String insideResponseInfoAtiRoutingRule) {
		insideResponseInfo_atiRoutingRule = insideResponseInfoAtiRoutingRule;
	}
	public String getSsiInfo_originatorServices() {
		return ssiInfo_originatorServices;
	}
	public void setSsiInfo_originatorServices(String ssiInfoOriginatorServices) {
		ssiInfo_originatorServices = ssiInfoOriginatorServices;
	}
	public String getSsiInfo_recipientServices() {
		return ssiInfo_recipientServices;
	}
	public void setSsiInfo_recipientServices(String ssiInfoRecipientServices) {
		ssiInfo_recipientServices = ssiInfoRecipientServices;
	}
public SMSLGPData()
{
}
}

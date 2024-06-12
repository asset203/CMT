[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#import "../grid-component.ftl" as component/]
[#global title="Capacity Management Tool"/]

[#global isLogMgrSelected="selected"/]

[#assign security=JspTaglibs["http://www.springframework.org/security/tags"] /]

[@spring.bind path="dcLogSearchForm.fromDate"/]
[#assign fromDateError][@spring.showErrors "<br>" "error"/][/#assign]

[@spring.bind path="dcLogSearchForm.toDate"/]
[#assign toDateError][@spring.showErrors "<br>" "error"/][/#assign]

[#assign javaScript]
<script type="text/javascript">
    $(document).ready(function() {

    $('.date-pick').datepicker({showOn: "button"});
    $('.date-pick').datepicker({ dateFormat: "dd-M-yy" });
    [#if dcLogSearchForm.fromDate?? && dcLogSearchForm.fromDate?has_content && !fromDateError?has_content]
    var fromDate = $('.fromDateDiv').html();
    $('#fromDate').datepicker().datepicker('setDate', new Date(fromDate));
    [/#if]
    [#if dcLogSearchForm.toDate?? && dcLogSearchForm.toDate?has_content && !toDateError?has_content]
    var toDate = $('.toDateDiv').html();
    $('#toDate').datepicker().datepicker('setDate', new Date(toDate));
    [/#if]
    $('.date-pick').datepicker( "option", "dateFormat", "dd-M-yy" );

    $(function() {

    var availableTags = [
    [#if systemList?? && systemList?size>0]
    [#list systemList as system]"${system}"[#if system_has_next],[/#if][/#list]
    [/#if]
    ];
    $( ".AutoCompleteInput" ).autocomplete({
    source: availableTags
    });
    });
    }
    );

    function hideDataTable(){
        $('#datatable-load').hide();
    }


</script>
[/#assign]
[@cmt.htmlHead javaScript/]
[@cmt.navigation/]
[#if dcLogSearchForm.fromDate?? && dcLogSearchForm.fromDate?has_content && !fromDateError?has_content]
<div class="fromDateDiv" style="display:none;">${dcLogSearchForm.fromDate?date("dd-MMM-yyyy")}</div>
[/#if]
[#if dcLogSearchForm.toDate?? && dcLogSearchForm.toDate?has_content && !fromDateError?has_content && !toDateError?has_content]
<div class="toDateDiv" style="display:none;">${dcLogSearchForm.toDate?date("dd-MMM-yyyy")}</div>
[/#if]
<div class="TabContent ManageUserTabContent">
    <h1>Log Manager</h1>
    [@security.authorize access="isAuthenticated() and hasRole('ADMIN')"]
    <div class="CustomContent4">
        Display Log &nbsp; &nbsp;   |   &nbsp; &nbsp; <a href="[@spring.url '/logNotification/manageLists'/]" class="bluelable">Manage Log Notifications list </a>
    </div>
    [/@security.authorize]
    <h2>Filter Logs</h2>
    [@cmt.loadingTxt/]
    <form id="myform" action="[@spring.url '/logManager/searchLogs'/]" method="post">
        <div class="GridHeader2">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                <tr>
                    <td class="Labeltd"> <label for="cd-dropdown">Select System</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd" colspan="2">
                        [@spring.formInput path="dcLogSearchForm.systemName"
                        attributes=" size=\"25\"  minlength=\"2\"
                        class=\"CustomInput AutoCompleteInput specialChars error\"" /]
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"> <label for="">Log Type</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd RadioInputs" colspan="2">
                        [@spring.bind path="dcLogSearchForm.logType"/]
                        [#assign chkStatus=""/]
                        [#list logTypes?values as item ]
                        [#if dcLogSearchForm.logType??
                        && dcLogSearchForm.logType?upper_case == item.value?upper_case]
                        [#assign chkStatus="checked"/]
                        [#else]
                        [#assign chkStatus=""/]
                        [/#if]
                        <label class="${item.value}RadioOption">
                            <input id="" name="${spring.status.expression}" type="radio"
                                   value="${item}" ${chkStatus}>${item.value}
                        </label>
                        [/#list]
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"> <label for="">Date</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd" >
                        <b>From &nbsp;</b>
                                <span class="text SmallInput">
									[@spring.bind path="dcLogSearchForm.fromDate"/]
									<input type="text" name="${spring.status.expression}"
                                           class="date-pick error specialChars" id="fromDate"/>
                              </span>
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                    <td class="inputltd" >
                        <b>To &nbsp;</b>
                                <span class="text SmallInput">                                 
								[@spring.bind path="dcLogSearchForm.toDate"/]
								<input type="text" name="${spring.status.expression}"
                                       class="date-pick error specialChars" id="toDate"/>
                                </span>
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                </tr>
            </table>
        </div>
        <div class="GridHeader">
                    <span class="SubmitButtons">
                        <span class="SubmitButton">
                            <input type="submit"  value="Display Log" onclick="hideDataTable();">
                        </span>
                    </span>
        </div>
    </form>
    [#-- Grid Display --]
    [#if (dcLogSearchForm.systemName?? && dcLogSearchForm.systemName?has_content)
        && (dcLogSearchForm.logType?? && dcLogSearchForm.logType?has_content)
        && (dcLogSearchForm.fromDate?? && dcLogSearchForm.fromDate?has_content)
        && (dcLogSearchForm.toDate?? && dcLogSearchForm.toDate?has_content)]
        <div id="datatable-load">
            [@component.datatable/]
        </div>
    [/#if]

</div>

[@cmt.footer/]


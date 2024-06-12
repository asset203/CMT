[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]

[#global title="Capacity Management Tool"/]

[#global isDCJobsSelected="selected"/]
[@cmt.htmlHead "" /]
[@cmt.navigation/]

<script>
    $(document).ready(function () {
    loadAutoComplete();
    showNodeRow();
    [#if job?? && job.job?? && job.job.executionPeriod??
    && job.job.executionPeriod == '${executionPeriods.HOURLY}']
    $(".retryIntervalClass").hide();
    $(".retryCountClass").hide();
    [/#if]

    $(".AutoCompleteInput").autocomplete({
    change:function (event, ui) {
    var jqxhr = $.get("[@spring.url '/jobManagement/getSystemNodes'/]",
    {systemName:$('.systemName').val() }, function () {
    })
    .done(function (data) {
    $(".SecondRadioOptionRow").html(data);
    })
    .fail(function (data) {
    $(".SecondRadioOptionRow").html("<div class='ErrorMsg'>An error has occurred</div>");
    });
    }
    });

    });

    function refreshFields(control){
    var period = $(control).val();
    if(period == '${executionPeriods.HOURLY}'){
    $(".retryIntervalClass").hide();
    $(".retryCountClass").hide();
    }else{
    $(".retryIntervalClass").show();
    $(".retryCountClass").show();
    }
    }

    function getNodeNames() {
    var jqxhr = $.get("[@spring.url '/jobManagement/getSystemNodes'/]",
    {systemName:$('.systemName').val() }, function () {
    })
    .done(function (data) {
    $(".SecondRadioOptionRow").html(data);
    })
    .fail(function (data) {
    $(".SecondRadioOptionRow").html("<div class='ErrorMsg'>An error has occurred</div>");
    });
    }
        
    function getZoneUtilization(){
    var url = '[@spring.url '/jobManagement/getZoneUtilization'/]';
    $('#myform').attr("action", url);
    $('#myform').submit();
    }

    function showNodeRow(){
    [#if job.job.systemNode??]
    $(".SecondRadioOptionRow").show();
    [#else]
    $(".SecondRadioOptionRow").hide();
    [/#if]
    }


    function loadAutoComplete() {
    var availableTags0 = [
    [#if systemNames?? && systemNames?size > 0]
    [#list systemNames as system]"${system}"[#if system_has_next],[/#if][/#list]
    [/#if]
    ];

    $(".AutoCompleteInput0").autocomplete({
    source:availableTags0
    });
    }
    </script>

<div class="TabContent">
    <h1>[#if pageTitle??]${pageTitle}[#else]Define New Job[/#if]</h1>
    <div class="CustomContent1">
        <h4>Instructions</h4>
        <a href="#d" class="Expandable">
            <span class="HideContent"> [ - ] Hide</span>
            <span class="ShowContent" style="display:none"> [ + ] Show</span>
            </a>
        <div class="ExpandableContent">
            <p>Choosing "System Level Job" option and "Daily" Execution Period will only execute data collections associated with Inputs on System Level.</p>
            <p>Choosing "Node Level Job" option, "All Nodes" selection option and "Daily" Execution Period will execute both System & all Nodes' data collections.</p>
            <p>Choosing "Hourly" Execution Period will execute both System & all Nodes' data collections.</p>
            </div>
        </div>
    [@spring.bind path="job.noUpdate"/]
    [@spring.showErrors "<br>" "errors"/]
    <div class="FormContainer">
        [@cmt.loadingTxt/]
        <form id="myform" action="[@spring.url '/jobManagement/${job.jobAction}'/]" method="post">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                <tr>
                    <td class="Labeltd"><label>Job Name </label> <span class="RequiredField">*</span></td>
                    <td class="inputltd">
                        <span class="text">
							[#if pageTitle?? && pageTitle == "Update Job"]
								[@spring.formHiddenInput path="job.job.jobName"/]
                            <input type="text" class=" error" size="25" name="${job.job.jobName}" value="${job.job.jobName}" disabled>
							[#else]
								[@spring.formInput path="job.job.jobName" attributes=" size=\"25\" class=\" error specialChars\" " /]
							[/#if]
                            </span>
                        </td>
                    </tr>
                <tr><td></td><td colspan="2">[@spring.showErrors "<br>" "errors"/]</td></tr>
                <tr>
                    <td class="Labeltd"><label>Job Description</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd">
                        <span class="text">
						  [@spring.formInput path="job.job.jobDescription" attributes=" size=\"25\" class=\" error specialChars\"" /]
                            </span>
                        </td>
                    </tr>
                <tr><td></td><td colspan="2">[@spring.showErrors "<br>" "errors"/]</td></tr>
                <tr>
                    <td class="Labeltd"><label>Job Type</label> <span class="RequiredField">*</span></td>
                    <td colspan="2" class="inputltd RadioInputsSmall">
                        [@spring.bind path="job.jobLevel"/]
                        <label class="FirstRadioOption">
                            <input type="radio"  name="${spring.status.expression}" onChange="getNodeNames();"
                                   value="SYSTEM_LEVEL" [#if !job.job.systemNode??]checked[/#if]>System Level Job
                            </label>
                        <label class="SecondRadioOption">
                            <input type="radio"  name="${spring.status.expression}" onChange="getNodeNames();"
                                   value="NODE_LEVEL" [#if job.job.systemNode??]checked[/#if]>Node Level Job
                            </label>
                        </td>
                    </tr>
                <tr><td colspan="3"></td></tr>
                <tr>
                    <td class="Labeltd"><label>System Name</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd" colspan="2">
                        [@spring.formInput path="job.job.systemName"
                        attributes="
                        class=\"CustomInput AutoCompleteInput AutoCompleteInput0 systemName \"" /]
                        </td>
                    </tr>
                <tr><td></td><td colspan="2">[@spring.showErrors "<br>" "errors"/]</td></tr>
                <tr class="SecondRadioOptionRow" style="display:none" onload='getNodeNames()'>
                    [#include "nodeList.ftl"]
                    </tr>
                [#if nodeList??]
                [#if nodeList?has_content]
                [@spring.bind path="job.job.systemNode"/]
                <tr><td></td><td colspan="2">[@spring.showErrors "<br>" "errors"/]</td></tr>
                [/#if]
                [/#if]
                <tr>
                    <td class="Labeltd"><label>CRON Expression </label> <span class="RequiredField">*</span></td>
                    <td class="inputltd WithHint">
                        [@spring.formInput path="job.job.cronExpression" attributes=" size=\"25\" class=\" error
                        CustomInput specialChars\"" /]
                        <div class="HintBlock">
                            <div class="SelectionHint2">
                                <div class="SelectionHintInner2">
                                    <div>Hint</div>
                                    Please refer to Quartz Cron expression tutorial
                                    <b><a href=http://quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger
                                          target="_blank">
                                            CronTrigger Tutorial
                                            </a></b>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                <tr><td></td><td colspan="2">[@spring.showErrors "<br>" "errors"/]</td></tr>
                <tr>
                    <td class="Labeltd"><label>Execution period</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd" colspan="2">
                        [@spring.formSingleSelect path="job.job.executionPeriod" options=executionPeriods
                        attributes="class=\"cd-select  error\"    onChange=\"refreshFields(this);\" " /]
                        </td>
                    </tr>
                <tr><td></td><td colspan="2">[@spring.showErrors "<br>" "errors"/]</td></tr>
                <tr>
                    <td class="Labeltd"><label>Zones</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd" colspan="2">
                        [#if pageTitle?? && pageTitle == "Update Job"]
                        [@spring.formHiddenInput path="job.job.zone"/]
                        <span class="text">
                            <input type="text" class=" error" size="25" name="${job.job.zone}" value="${job.job.zone}" disabled>
                            </span>
                        [#else]
                        [@spring.formSingleSelect path="job.job.zone" options=zones attributes="class=\"cd-select
                        error \"" /]
                        [/#if]

                        [#if zonesUtilizationList?has_content]
                        <div class="HintBlock">
                            <div class="SelectionHint2" style="width: 500px;">
                                <div class="SelectionHintInner2">
                                    <div>Zones Utilization</div>
                                    <table cellspacing="0" border="1" cellpadding="0" width="100%" class="FormTable">
                                        <thead>
                                            <tr>
                                                <th width="25%" ><label> Zone  </label></th>
                                                <th width="25%"><label> Running Jobs </label> </th>
                                                <th width="25%"> <label> Next Running Jobs </label></th>
                                                <th width="25%"> <label> Assigned Jobs </label> </th>
                                                </tr>
                                            </thead>
                                        <tbody>
                                         [#list zonesUtilizationList as zone]
                                            <tr>  
                                                <td width="25%" style="text-align: center;"> ${zone.zoneName} </td>
                                                <td width="25%" style="text-align: center;"> ${zone.runningZoneJobs} </td>
                                                <td width="25%" style="text-align: center;"> ${zone.jobsTORun} </td>
                                                <td width="25%" style="text-align: center;"> ${zone.zoneJobs} </td>

                                                </tr>
                                         [/#list]

                                            </tbody>
                                        </table>

                                    </div>
                                </div>
                            </div>
                        [/#if]
                        </td>
                    </tr>
                <tr><td></td><td colspan="2">[@spring.showErrors "<br>" "errors"/]</td></tr>
                <tr class="retryCountClass">
                    <td class="Labeltd"><label>Retry Count</label> <span class="RequiredField">*</span></td>

                    <td class="inputltd">
                        <span class="text">
                [@spring.formInput path="job.job.retryCount" attributes=" size=\"25\" class=\" error specialChars\"" /]
                            </span>

                        </td>
                    </tr>
                <tr><td></td><td colspan="2">[@spring.showErrors "<br>" "errors"/]</td></tr>
                <tr class="retryIntervalClass">
                    <td class="Labeltd"><label>Retry Interval </label> <span class="RequiredField">*</span><br>(Minutes)</td>
                    <td class="inputltd">
                        <span class="text">
						  [@spring.formInput path="job.job.retryInterval" attributes=" size=\"25\" class=\" error specialChars\"" /]
                            </span>
                        </td>
                    </tr>
                <tr><td></td><td colspan="2">[@spring.showErrors "<br>" "errors"/]</td></tr>

                <tr>
                    <td class="Labeltd"><label></label></td>
                    <td class="inputltd">
                        <span class="SubmitButtons ">
                            <span class="SubmitButton BtnWidth1">
                                <input type="submit" value="Save" onclick="javascript:this.disabled='disabled'" class="submit">
                                </span>
                            </span>
                        <span class="SubmitButtons Gray ">
                            <span class="SubmitButton BtnWidth1">
                                <input type="reset"
                                       onClick="location.href='[@spring.url '/jobManagement/manageJobs'/]'" value="Cancel"> </span>
                            </span>

                        <span class="SubmitButtons">
                            <span class="SubmitButton BtnWidth1">
                                <input type="reset"
                                       onClick="getZoneUtilization();" value="Show Zones Utilization" class="submit"> </span>
                            </span>
                        </td>
                    </tr>
                </table>
            </form>

        </div>

    </div>
<div class="clear"></div>

[@cmt.footer/]
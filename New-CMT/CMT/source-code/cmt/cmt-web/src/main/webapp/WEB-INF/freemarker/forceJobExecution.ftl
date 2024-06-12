[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]
[#global title="Capacity Management Tool"/]

[#global isDCJobsSelected="selected"/]

[#assign javaScript]
<script>

    $(document).ready(function() {
    $('.date-pick').datepicker({showOn: "button"});
    $('.date-pick').datepicker({ dateFormat: "dd/mm/yy" });
    [#if formBean.fromDate?? && formBean.fromDate?has_content]
    var fromDate = $('.fromDateDiv').html();
    $('#fromDate').datepicker().datepicker('setDate', new Date(fromDate));
    [/#if]
    [#if formBean.toDate?? && formBean.toDate?has_content]
    var toDate = $('.toDateDiv').html();
    $('#toDate').datepicker().datepicker('setDate', new Date(toDate));
    [/#if]
    $('.date-pick').datepicker( "option", "dateFormat", "dd/mm/yy" );

    });
</script>
[/#assign]
[@cmt.htmlHead javaScript /]
[@cmt.navigation/]
[#if formBean.fromDate?? && formBean.fromDate?has_content]
<div class="fromDateDiv" style="display:none;">${formBean.fromDate?date("dd/MM/yyyy")}</div>
[/#if]
[#if formBean.toDate?? && formBean.toDate?has_content]
<div class="toDateDiv" style="display:none;">${formBean.toDate?date("dd/MM/yyyy")}</div>
[/#if]

<div class="TabContent">
    <h1>Force Job Execution</h1>
    <div class="FormContainer">
        [@cmt.loadingTxt/]
        [#if formBean?? && formBean.job??]
        <form id="myform" action="[@spring.url '/jobManagement/forceJobExecution'/]" method="post" class="mainForm">
            [@spring.formHiddenInput path="formBean.job.zone"/]
            [@spring.formHiddenInput path="formBean.job.systemName"/]
            [@spring.formHiddenInput path="formBean.job.jobName"/]
            [@spring.formHiddenInput path="formBean.job.executionPeriod"/]
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                <tr>
                    <td class="Labeltd"><label >Job Name </label> <span class="RequiredField">*</span> </td>
                    <td class="inputltd">
                        <span class="text">
                            [@spring.formInput path="formBean.job.jobName"
                                attributes="class=\"required error\" size=\"25\" disabled" /]
                            [@spring.showErrors "<br>" "error"/]
                        </span>
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"><label >From </label> <span class="RequiredField">*</span> </td>
                    <td class="inputltd">
                        <b>Date &nbsp;</b>
                        <span class="text SmallInput">
							[@spring.bind path="formBean.fromDate"/]
							<input type="text" name="${spring.status.expression}" class="date-pick error specialChars" id="fromDate"/>

                        </span>
                        [#if formBean.job.executionPeriod?? && formBean.job.executionPeriod?string == 'HOURLY']
                        <b>Hour &nbsp;</b>
                        [@spring.bind path="formBean.fromHour"/]
                        <select class="cd-select required error CustomSmallSelect"
                                name="${spring.status.expression}">
                            [#list 0..23 as hr]
                            <option value="${hr?c}">${hr?c}</option>
                            [/#list]
                        </select>
                        [@spring.showErrors "<br>" "error"/]
                        [/#if]
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"><label ></label>  </td>
                    <td>
                        [@spring.bind path="formBean.fromDate"/]
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                    <td>
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"><label >To</label> <span class="RequiredField">*</span> </td>
                    <td class="inputltd">
                        <b>Date &nbsp;</b>
                        <span class="text SmallInput">
							[@spring.bind path="formBean.toDate"/]
							<input type="text" name="${spring.status.expression}" class="date-pick error specialChars" id="toDate"/>

                        </span>
                        [#if formBean.job.executionPeriod?? && formBean.job.executionPeriod?string == 'HOURLY']
                        <b>Hour &nbsp;</b>
                        [@spring.bind path="formBean.toHour"/]
                        <select class="cd-select required error CustomSmallSelect"
                                name="${spring.status.expression}">
                            [#list 0..23 as hr]
                            <option value="${hr?c}">${hr?c}</option>
                            [/#list]
                        </select>
                        [@spring.showErrors "<br>" "error"/]
                        [/#if]
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"><label ></label>  </td>
                    <td>
                        [@spring.bind path="formBean.toDate"/]
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="Labeltd"><label ></label>  </td>
                    <td class="inputltd">
                            <span class="SubmitButtons ">
                                <span class="SubmitButton BtnWidth1">
									<input type="submit"  value="Execute" class="submit" onclick="javascript:this.disabled='disabled'">
								</span>
                            </span>
                            <span class="SubmitButtons Gray ">
                                <span class="SubmitButton BtnWidth1">
                                    <input type="reset"
                                           onClick="location.href='[@spring.url '/jobManagement/manageJobs'/]'" value="Cancel">
                                </span>
                            </span>
                    </td>
                </tr>
            </table>
        </form>
        [#else]
        <div class="NoResults">No job found</div>
        [/#if]
    </div>
</div>
[@cmt.footer/]
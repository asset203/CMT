[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]

[#global title="Capacity Management Tool"/]
[#global isSysEventsSelected="selected"/]

[#assign javaScript]
<script>
    $(document).ready(function () {
    $('.date-pick').datepicker({showOn:"button"});
    $('.date-pick').datepicker("option", "dateFormat", "dd-M-yy");
    $(".DeleteEvent").fancybox({
    padding:0,
    closeBtn:false
    });
    $(".DeleteBtns .SubmitButtons.Gray").click(function () {
    parent.$.fancybox.close();
    });
    [#if CONFIRM_EVENT_REPLACEMENT?? && CONFIRM_EVENT_REPLACEMENT?has_content]
    $(".DeleteEvent").click();
    [/#if]
    });
    function viewSelectedSystemEvents(){
    var url = '[@spring.url '/systemEvents/viewSelectedSystemEvents'/]';
    $('#postForm').attr("action", url);
    $('#hiddenField').html($('.cancelBody').html());
    $('#postForm').submit();
    }

</script>
[/#assign]
[@cmt.htmlHead javaScript/]
[@cmt.navigation/]

[@cmt.dummyForm/]

[#assign cancelBody]
[#if formBean?? && formBean.selectedSystemEvent??]
[#if  formBean.selectedSystemEvent.systemName??]
[@spring.formHiddenInput path="formBean.selectedSystemEvent.systemName"/]
[@spring.bind path="formBean.selectedSystem"/]
<input type="hidden" name="${spring.status.expression}" value="${formBean.selectedSystemEvent.systemName}"/>
[/#if]
[#if formBean.selectedSystemEvent.id??]
[@spring.formHiddenInput path="formBean.selectedSystemEvent.id"/]
[/#if]
[/#if]
[/#assign]
<div class="cancelBody" style="display:none;">${cancelBody}</div>

<div class="TabContent ManageUserTabContent">
    <h1>${pageTitle}</h1>
    [#if formBean?? && formBean.selectedSystemEvent??]
    <div class="FormContainer">
        <form id="myform" action="[@spring.url '/systemEvents/${formAction}'/]" method="post">
            [@spring.formHiddenInput path="formBean.selectedSystemEvent.systemName"/]
            [@spring.formHiddenInput path="formBean.selectedSystemEvent.id"/]
            [#if formAction?? && formAction == 'editSystemEvent']
            [@spring.formHiddenInput path="formBean.selectedSystemEventDateStr"/]
            [/#if]

            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                <tr>
                    <td class="Labeltd">
                        <label for="cname">Subject </label> <span class="RequiredField">*</span></td>
                    <td class="inputltd">
                                <span class="text">
                                    [@spring.formInput path="formBean.selectedSystemEvent.commentDesc"
                                    attributes=" size=\"25\"  minlength=\"2\" class=\"specialChars\"" /]
                                    [@spring.showErrors "<br>" "error"/]
                                </span>
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"><label for="cd-dropdown">Event Level</label>
                        <span class="RequiredField">*</span></td>
                    <td class="inputltd">
                        [@spring.bind path="formBean.selectedSystemEvent.levelType"/]
                        <select id="levelType" class="cd-select required error"
                                name="${spring.status.expression}">
                            [#list eventLevels as level]
                            <option value="${level.level?upper_case}"
                            [#if formBean.selectedSystemEvent.levelType??
                            && formBean.selectedSystemEvent.levelType == level]selected[/#if]>
                            ${level?capitalize}
                            </option>
                            [/#list]
                        </select>
                        [@spring.showErrors "<br>" "error"/]
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"><label for="cdate">Date </label>
                        [#if formAction?? && formAction != 'editSystemEvent']
                        <span class="RequiredField">*</span>
                        [/#if]
                    </td>
                    <td class="inputltd">
                                <span class="text">

                                  [#if formAction?? && formAction != 'editSystemEvent']
                                        [@spring.formInput path="formBean.selectedSystemEventDateStr"
                                  attributes=" size=\"25\" class=\"date-pick specialChars\" minlength=\"2\"" /]
                                        [@spring.showErrors "<br>" "error"/]
                                    [#else]
                                    [@spring.formInput path="formBean.selectedSystemEventDateStr"
                                    attributes=" size=\"25\" disabled minlength=\"2\"" /]
                                  [/#if]
                                </span>
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"></td>
                    <td class="inputltd">
                                <span class="SubmitButtons ">
                                    <span class="SubmitButton BtnWidth1">
                                        <input type="submit" value="Save" class="submit">
                                    </span>
                                </span>
                                <span class="SubmitButtons Gray ">
                                    <span class="SubmitButton BtnWidth1">
                                        <input type="button"
                                               onClick="viewSelectedSystemEvents();"
                                               value="Cancel">
                                    </span>
                                </span>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    [/#if]
    [@cmt.progressBar/]
</div>
[#if CONFIRM_EVENT_REPLACEMENT?? && CONFIRM_EVENT_REPLACEMENT?has_content]
<div style="display:none;">
    <a class="DeleteEvent" href="#DeleteEvent">
</div>
<div style="display:block">
    <div id="DeleteEvent" class="delteUserPopUP">
        <div class="delteUserPopUPTitle">Delete Event</div>
        <div class="delteUserPopUPInner">
            <div class="delteUserPopUPInner2">
                Another event is added on the same day by the description "${CONFIRM_EVENT_REPLACEMENT}".
                Are you sure you want to replace it?
                <div class="DeleteBtns">
                    <form action="[@spring.url '/systemEvents/confirmEventReplacement'/]" method="post">
                        [@spring.formHiddenInput path="formBean.selectedSystemEvent.systemName"/]
                        [@spring.formHiddenInput path="formBean.selectedSystemEvent.id"/]
                        [@spring.formHiddenInput path="formBean.selectedSystemEvent.commentDesc"/]
                        [@spring.formHiddenInput path="formBean.selectedSystemEvent.levelType"/]
                        [@spring.formHiddenInput path="formBean.selectedSystemEventDateStr"/]
            <span class="SubmitButtons ">
									<span class="SubmitButton BtnWidth1">
										<input type="submit" value="Yes">
									</span>
								</span>
								<span class="SubmitButtons Gray ">
									<span class="SubmitButton BtnWidth1">
										<input type="reset" value="Cancel">
									</span>
								</span>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
[/#if]
[@cmt.footer/]


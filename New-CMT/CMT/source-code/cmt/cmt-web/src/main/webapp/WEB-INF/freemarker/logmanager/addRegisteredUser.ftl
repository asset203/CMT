[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]

[#if VALIDATION_ERROR??]${VALIDATION_ERROR}[/#if]
<form id="addRegForm" action="[@spring.url '/logNotification/addRegisteredUser'/]" method="post">

    [@spring.formHiddenInput path="dcLogNotificationForm.dcLogNotificationList.id"/]
    [@spring.formHiddenInput path="dcLogNotificationForm.systemName"/]
    [@spring.formHiddenInput path="dcLogNotificationForm.logType"/]
    [@spring.formHiddenInput path="dcLogNotificationForm.errorCodeID"/]
    [@spring.bind path="dcLogNotificationForm.registeredUserAction"/]
    <input type="hidden" id="registeredUserAction"
           name="${spring.status.expression}" class="registeredUserAction" style="" value="true" />

    [#if dcLogNotificationForm.registeredUser?? && dcLogNotificationForm.registeredUser.id??]
    [@spring.formHiddenInput path="dcLogNotificationForm.registeredUser.id"/]
    [/#if]
    [#if dcLogNotificationForm.dcLogNotificationList.contactsList??]
    [#if dcLogNotificationForm.dcLogNotificationList.contactsList?size > 0]
    [@spring.formHiddenInput path="dcLogNotificationForm.dcLogNotificationList.contactsList[0].id"/]
    [/#if]
    [/#if]
    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
        <tr>
            <td class="Labeltd"> <label for="name">Username </label> <span class="RequiredField">*</span></td>
            <td class="inputltd" colspan="2">
                [@spring.formInput path="dcLogNotificationForm.registeredUser.username"
                attributes="  class=\"CustomInput AutoCompleteInput AutoCompleteInput1 specialChars error\"" /]
            </td>
        </tr>
        <tr>
            <td/>
            <td colspan="2">[@spring.showErrors "<br>" "error"/]</td>
        </tr>
        <tr>
            <td class="Labeltd"> <label for="name">Email </label></td>
            <td class="inputltd" colspan="2">
                <span class="text">
                     [@spring.formInput path="dcLogNotificationForm.registeredUser.email"
                         attributes="  class=\" specialChars error\"" /]
                 </span>
            </td>
        </tr>
        <tr>
            <td/>
            <td colspan="2">[@spring.showErrors "<br>" "error"/]</td>
        </tr>
        <tr>
            <td class="Labeltd"> <label for="name">Moblie </label></td>
            <td class="inputltd" colspan="2">
                <span class="text">
                    [@spring.formInput path="dcLogNotificationForm.registeredUser.mobile"
                        attributes="   class=\" specialChars error\"" /]
                 </span>
            </td>
        </tr>
        <tr>
            <td/>
            <td colspan="2">[@spring.showErrors "<br>" "error"/]</td>
        </tr>
        <tr>
            <td class="Labeltd"> <label for="">Notify By</label> <span class="RequiredField">*</span></td>
            <td class="inputltd RadioInputsSmall" colspan="2">
                [@spring.bind path="dcLogNotificationForm.notificationType"/]
                [#list notificationTypes?keys as key]
                [#assign checkStatus=" "/]
                [#if dcLogNotificationForm.notificationType??]
                [#if dcLogNotificationForm.notificationType == notificationTypes[key]
                || dcLogNotificationForm.notificationType?index_of(notificationTypes[key]) != -1]
                [#assign checkStatus="checked"/]
                [/#if]
                [/#if]
                <label>
                    <input name="${spring.status.expression}" name="${spring.status.expression}"
                            type="checkbox"
                           value="${notificationTypes[key]}" ${checkStatus}>${notificationTypes[key]}
                </label>
                [/#list]

            </td>
        </tr>
        <tr>
            <td/>
            <td colspan="2">
                [#if VALIDATION_ERROR_ADD_REGISTERED?? && VALIDATION_ERROR_ADD_REGISTERED == 'true']
                [@spring.showErrors "<br>" "error"/]
                [/#if]
            </td>
        </tr>
        <tr>
            <td class="Labeltd"></td>
            <td class="inputltd"  colspan="2">
                <span class="SubmitButtons ">
                    <span class="SubmitButton BtnWidth2">
                        <input type="submit"  value="Add to Selected Users" class="submit">
                    </span>
                </span>
            </td>
        </tr>
    </table>
</form>

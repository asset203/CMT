[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]

<form id="addExtForm" action="[@spring.url '/logNotification/addExternalUser'/]" method="post">
    [@spring.formHiddenInput path="dcLogNotificationForm.dcLogNotificationList.id"/]
    [@spring.formHiddenInput path="dcLogNotificationForm.systemName"/]
    [@spring.formHiddenInput path="dcLogNotificationForm.logType"/]
    [@spring.formHiddenInput path="dcLogNotificationForm.errorCodeID"/]

	[@spring.bind path="dcLogNotificationForm.registeredUserAction"/]
	<input type="hidden" id="registeredUserAction"
		name="${spring.status.expression}" class="registeredUserAction" style="" value="false" />

    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
        <tr>
            <td class="Labeltd"><label for="cname">Username</label> <span class="RequiredField">*</span> </td>
            <td class="inputltd">
                <span class="text">
                    [@spring.formInput path="dcLogNotificationForm.externalUser.userName"
                        attributes=" class=\"specialChars error\"" /]
                 </span>
            </td>
        </tr>
        <tr>
            <td/>
            <td colspan="2">[@spring.showErrors "<br>" "error"/]</td>
        </tr>
        <tr>
            <td class="Labeltd"><label for="">Email</label></td>
            <td class="inputltd">
                <span class="text">
                    [@spring.formInput path="dcLogNotificationForm.externalUser.eMail"
                        attributes=" class=\"specialChars error\"" /]
                </span>

            </td>
        </tr>
        <tr>
            <td/>
            <td colspan="2">[@spring.showErrors "<br>" "error"/]</td>
        </tr>
        <tr>
            <td class="Labeltd"><label for="">Mobile</label></td>
            <td class="inputltd">
                <span class="text">
                    [@spring.formInput path="dcLogNotificationForm.externalUser.mobileNum"
                        attributes=" class=\"specialChars error\"" /]
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
                    [#if VALIDATION_ERROR_ADD_EXT?? && VALIDATION_ERROR_ADD_EXT == 'true']
                        [@spring.showErrors "<br>" "error"/]
                    [/#if]
                </td>
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

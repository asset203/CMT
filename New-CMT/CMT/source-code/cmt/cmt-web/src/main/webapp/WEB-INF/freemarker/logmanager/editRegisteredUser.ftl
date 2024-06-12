[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]

[#if dcLogNotificationForm?? && dcLogNotificationForm.updatedRegisteredUser??]
    <h2>Edit User</h2>
    <div class="GridHeader2">
        [@spring.bind path="dcLogNotificationForm.noUpdate"/]
        [@spring.showErrors "<br>" "error"/]
        <form id="editRegForm" action="[@spring.url '/logNotification/editRegisteredUser'/]" method="post">
            [@spring.formHiddenInput path="dcLogNotificationForm.dcLogNotificationList.id"/]
            [@spring.formHiddenInput path="dcLogNotificationForm.systemName"/]
            [@spring.formHiddenInput path="dcLogNotificationForm.logType"/]
            [@spring.formHiddenInput path="dcLogNotificationForm.errorCodeID"/]
            [@spring.formHiddenInput path="dcLogNotificationForm.updatedRegisteredUser.id"/]
			[@spring.formHiddenInput path="dcLogNotificationForm.updatedRegisteredUser.username"/]
            [@spring.formHiddenInput path="dcLogNotificationForm.contactListID"/]

             <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                <tr>
                    <td class="Labeltd"><label for="cname">Username</label> <span class="RequiredField">*</span> </td>
                    <td class="inputltd">
                        <span class="text">
                            [@spring.formInput path="dcLogNotificationForm.updatedRegisteredUser.username"
                                attributes="   disabled class=\"specialChars error\"" /]
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
                            [@spring.formInput path="dcLogNotificationForm.updatedRegisteredUser.email"
                                attributes="  class=\"specialChars error\"" /]
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
                            [@spring.formInput path="dcLogNotificationForm.updatedRegisteredUser.mobile"
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
                     </td>
                     <td colspan="2">
                         [#if VALIDATION_ERROR_EDIT_REGISTERED?? && VALIDATION_ERROR_EDIT_REGISTERED == 'true']
                            [@spring.showErrors "<br>" "error"/]
                         [/#if]
                     </td>
                 </tr>
                <tr>
                    <td class="Labeltd"></td>
                    <td class="inputltd">
                        <span class="SubmitButtons ">
                            <span class="SubmitButton BtnWidth2">
                                <input type="submit"  value="Edit User" class="submit">
                            </span>
                        </span>
                        <span class="SubmitButtons ">
                            <span class="SubmitButton BtnWidth2">
                                <input type="reset"  value="Cancel" class="submit"
                                    onClick="closeWindow('editRegUserAjax');">
                            </span>
                        </span>
                    </td>
                </tr>
            </table>
        </form>
    </div>
[/#if]
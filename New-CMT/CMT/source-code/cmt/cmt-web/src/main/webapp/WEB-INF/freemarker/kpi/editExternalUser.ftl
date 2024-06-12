[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#if formBean?? && formBean.updatedExternalUser??]
<h2>Edit User</h2>
<div class="GridHeader2">
    [@spring.bind path="formBean.noUpdate"/]
    [@spring.showErrors "<br>" "error"/]
    <form id="editExtForm" action="[@spring.url '/kpiNotifications/editExternalUser'/]" method="post">
        [@spring.formHiddenInput path="formBean.systemName"/]
        [@spring.formHiddenInput path="formBean.nodeID"/]
        [@spring.formHiddenInput path="formBean.updatedExternalUser.id"/]
        [@spring.formHiddenInput path="formBean.updatedExternalUser.userName"/]
        [@spring.formHiddenInput path="formBean.contactListID"/]

        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
            <tr>
                <td class="Labeltd"><label for="cname">Username</label> <span class="RequiredField">*</span> </td>
                <td class="inputltd">
                            <span class="text">
                                [@spring.formInput path="formBean.updatedExternalUser.userName"
                                    attributes="  class=\"specialChars error\" disabled" /]
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
                                [@spring.formInput path="formBean.updatedExternalUser.eMail"
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
                                [@spring.formInput path="formBean.updatedExternalUser.mobileNum"
                                    attributes="  class=\"specialChars error\"" /]
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
                    [@spring.bind path="formBean.notificationType"/]
                    [#list notificationTypes?keys as key]
                    [#assign checkStatus=" "/]
                    [#if formBean.notificationType??]
                    [#if formBean.notificationType == notificationTypes[key]
                    || formBean.notificationType?index_of(notificationTypes[key]) != -1]
                    [#assign checkStatus="checked"/]
                    [/#if]
                    [/#if]
                    <label><input name="${spring.status.expression}" name="${spring.status.expression}" class=""
                                  type="checkbox"
                                  value="${notificationTypes[key]}" ${checkStatus}>${notificationTypes[key]}</label>
                    [/#list]
                </td>
            </tr>
            <tr>
                </td>
                <td colspan="2">
                    [#if VALIDATION_ERROR_EDIT_EXTERNAL?? && VALIDATION_ERROR_EDIT_EXTERNAL == 'true']
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
                                           onClick="closeWindow('editExtUserAjax');">
                                </span>
                            </span>
                </td>
            </tr>
        </table>
    </form>
</div>
[/#if]
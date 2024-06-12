[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]

[#if formBean?? && formBean.updatedRegisteredUser??]
<h2>Edit User</h2>
<div class="GridHeader2">
    [@spring.bind path="formBean.noUpdate"/]
    [@spring.showErrors "<br>" "error"/]
    <form id="editRegForm" action="[@spring.url '/kpiNotifications/editRegisteredUser'/]" method="post">

        [@spring.formHiddenInput path="formBean.systemName"/]
        [@spring.formHiddenInput path="formBean.nodeID"/]
        [@spring.formHiddenInput path="formBean.updatedRegisteredUser.id"/]
        [@spring.formHiddenInput path="formBean.updatedRegisteredUser.username"/]
        [@spring.formHiddenInput path="formBean.contactListID"/]

        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
            <tr>
                <td class="Labeltd"><label for="cname">Username</label> <span class="RequiredField">*</span> </td>
                <td class="inputltd">
                    <span class="text">
                        [@spring.formInput path="formBean.updatedRegisteredUser.username"
                            attributes=" class=\"specialChars error\" disabled" /]
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
                        [@spring.formInput path="formBean.updatedRegisteredUser.email"
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
                        [@spring.formInput path="formBean.updatedRegisteredUser.mobile"
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
                    [@spring.bind path="formBean.notificationType"/]
                    [#list notificationTypes?keys as key]
                    [#assign checkStatus=" "/]
                    [#if formBean.notificationType??]
                    [#if formBean.notificationType == notificationTypes[key]
                    || formBean.notificationType?index_of(notificationTypes[key]) != -1]
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
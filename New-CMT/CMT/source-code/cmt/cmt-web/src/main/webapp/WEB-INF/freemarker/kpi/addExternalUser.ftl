[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]

<form id="addExtForm" action="[@spring.url '/kpiNotifications/addExternalUser'/]" method="post">

    [@spring.formHiddenInput path="formBean.systemName"/]
    [@spring.formHiddenInput path="formBean.nodeID"/]

    [@spring.bind path="formBean.registeredUserAction"/]
    <input type="hidden" id="registeredUserAction"
           name="${spring.status.expression}" class="registeredUserAction" style="" value="false" />

    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
        <tr>
            <td class="Labeltd"><label for="cname">Username</label> <span class="RequiredField">*</span> </td>
            <td class="inputltd">
                <span class="text">
                    [@spring.formInput path="formBean.externalUser.userName"
                        attributes="  class=\"specialChars error\"" /]
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
                    [@spring.formInput path="formBean.externalUser.eMail"
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
                    [@spring.formInput path="formBean.externalUser.mobileNum"
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

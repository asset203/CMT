[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]

[#if VALIDATION_ERROR??]${VALIDATION_ERROR}[/#if]
<form id="addRegForm" action="[@spring.url '/kpiNotifications/addRegisteredUser'/]" method="post">

    [@spring.formHiddenInput path="formBean.systemName"/]
    [@spring.formHiddenInput path="formBean.nodeID"/]
    [@spring.bind path="formBean.registeredUserAction"/]

    <input type="hidden" id="registeredUserAction"
           name="${spring.status.expression}" class="registeredUserAction" style="" value="true" />

    [#if formBean.registeredUser?? && formBean.registeredUser.id??]
    [@spring.formHiddenInput path="formBean.registeredUser.id"/]
    [/#if]
    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
        <tr>
            <td class="Labeltd"> <label for="name">Username </label> <span class="RequiredField">*</span></td>
            <td class="inputltd" colspan="2">
                [@spring.formInput path="formBean.registeredUser.username"
                attributes=" class=\"CustomInput AutoCompleteInput AutoCompleteInput1 specialChars error\"" /]
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
                     [@spring.formInput path="formBean.registeredUser.email"
                         attributes="  class=\"  specialChars error\"" /]
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
                    [@spring.formInput path="formBean.registeredUser.mobile"
                        attributes=" class=\"  specialChars error\"" /]
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
                    [#if VALIDATION_ERROR_ADD_REGISTERED?? && VALIDATION_ERROR_ADD_REGISTERED == 'true']
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

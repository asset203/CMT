[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]

<form id="loadRegisteredUser" action="[@spring.url '/logNotification/addRegisteredUser'/]" method="post">
    <tr>
        <td class="Labeltd"><label for="">Email</label> <span class="RequiredField">*</span> </td>
        <td class="inputltd">
            <span class="text">
                [@spring.formInput path="dcLogNotificationForm.registeredUser.email"
                    attributes=" size=\"25\"  minlength=\"2\"" /]
                [@spring.showErrors "<br>" "error"/]
            </span>
        </td>
    </tr>
    <tr>
        <td class="Labeltd"><label for="">Mobile</label> <span class="RequiredField">*</span> </td>
        <td class="inputltd">
            <span class="text">
                [@spring.formInput path="dcLogNotificationForm.registeredUser.mobile"
                    attributes=" size=\"25\"  minlength=\"2\"" /]
                [@spring.showErrors "<br>" "error"/]
            </span>
        </td>
    </tr>
    <tr>
        <td class="Labeltd"> <label for="">Notify By</label> <span class="RequiredField">*</span></td>
        <td class="inputltd RadioInputs" colspan="2">
            <label>
                <input id="rdoAdd1" name="radio1" size="25" class="" minlength="2" type="checkbox" value="Mobile">SMS
            </label>
            <label>
                <input id="rdoAdd2" name="radio1" size="25" class="" minlength="2" type="checkbox" value="eMail">Email
            </label>
        </td>
    </tr>
    <tr>
        <td class="Labeltd"></td>
        <td class="inputltd">
            <span class="SubmitButtons "> <span class="SubmitButton BtnWidth2">
                <input type="submit"  value="Add to Selected Users" class="submit"> </span></span>
        </td>
    </tr>
</form>
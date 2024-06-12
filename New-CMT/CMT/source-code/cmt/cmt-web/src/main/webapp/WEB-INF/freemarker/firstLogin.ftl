[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]
[#global title="First Login"/]

[#assign javaScript]
    <script>
        $(document).ready(function() {
            $("#ChangePasswordForm").validate({
              rules: {
                repassword: {
                  equalTo: "#password"
                },
                password:{
                  minlength: 8
                }
              }
            });
        });
    </script>
[/#assign]
[@cmt.htmlHeadNonLogged javaScript/]
    <div class="LayoutContainer">
		<div class="tabsContainer">
			<ul class="tabs">
				<li class="loginTab selected"><a href="#d"> Change Password </a></li>
			</ul>
		</div>
		<div class="TabContent">
			<div class="ChangePasswordContent">
                <form id="UserForm" action="[@spring.url '/userManagement/changePassword'/]" method="post">
                    <input type="hidden" name="id" value="${user.id?c}">
                    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                        <tr>
                            <td class="Labeltd"><label>Old Password</label> <span class="RequiredField">*</span>  </td>
                            <td class="inputltd">
                                 <span class="text">
                                      [@spring.formPasswordInput "user.oldPassword"/]
                                      [@spring.bind path="user.oldPassword"/]
                                      [@spring.showErrors "<br>" "validationerror"/]
                                  </span>
                             </td>
                        </tr>
                        <tr>
                            <td class="Labeltd"><label>New Password</label> <span class="RequiredField">*</span></td>
                            <td class="inputltd">
                                <span class="text">
                                    [@spring.formPasswordInput "user.password"/]
                                    [@spring.bind path="user.password"/]
                                    [@spring.showErrors "<br>" "validationerror"/]
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td class="Labeltd"></td>
                            <td class="inputltd">
                                <div class="FormHint"> Minimum of 6 characters, Must have numbers and letters </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="Labeltd"><label>Re-enter your New Password</label>  <span class="RequiredField">*</span></td>
                            <td class="inputltd">
                                <span class="text">
                                    [@spring.formPasswordInput "user.rePassword"/]
                                    [@spring.bind path="user.rePassword"/]
                                    [@spring.showErrors "<br>" "validationerror"/]
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td class="Labeltd"></td>
                            <td class="inputltd">
                                <span class="SubmitButtons"> <span class="SubmitButton"> <input type="submit"
                                    value="Change Password" id="submit"> </span></span>
                            </td>
                        </tr>
				    </table>
			    </form>
			</div>
		</div>

		<div class="clear"></div>
	</div>

[@cmt.footer/]
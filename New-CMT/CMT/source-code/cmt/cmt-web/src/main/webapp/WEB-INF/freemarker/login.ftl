[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]

[#global title="Capacity Management Tool"/]


[#assign javaScript]
    <script>
        $(document).ready(function() {
            $("#UserLoginForm").validate();
        });
    </script>
[/#assign]

[@cmt.htmlHeadNonLogged javaScript/]

    <div class="tabsContainer">
        <ul class="tabs">
            <li class="loginTab selected"><a href="#d"> Login </a></li>
        </ul>
    </div>
    <div class="TabContent">
        [#if Session.SPRING_SECURITY_LAST_EXCEPTION??
            && Session.SPRING_SECURITY_LAST_EXCEPTION.message?has_content]
            <div class="ErrorMsg">
                [#if errorLogin??]
                    [@spring.message "AbstractUserDetailsAuthenticationProvider.badCredentials" /]
                [#else]
                    ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
                [/#if]
            </div>
        [#else][/#if]
        <div class="loginTabContent">
          <form id="UserLoginForm" name="loginForm" method="POST" action="[@spring.url '/loginProcess' /]">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
                <tr>
                    <td class="Labeltd"><label for="cname">Username</label> <span class="RequiredField">*</span>  </td>
                    <td class="inputltd">
                        <span class="text">
                             <input id="cname" name="j_username" size="25" class="required error" minlength="2" type="text">
                         </span>
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"><label for="password">Password</label> <span class="RequiredField">*</span></td>
                    <td class="inputltd">
                        <span class="text">
                            <input id="password" name="j_password" size="25" class="required password error" type="password">
                        </span>
                    </td>
                </tr>
                <tr>
                    <td class="Labeltd"></td>
                    <td class="inputltd">
                        <span class="SubmitButtons">
                            <span class="SubmitButton">
                                <input type="submit"  value="login" id="submit">
                            </span>
                        </span>
                    </td>
                </tr>
            </table>
          </form>
        </div>
    </div>
[@cmt.footer/]

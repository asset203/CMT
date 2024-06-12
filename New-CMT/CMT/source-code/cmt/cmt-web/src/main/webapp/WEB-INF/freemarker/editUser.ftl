[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]
[#global title="Capacity Management Tool"/]
[#global isUserMgmtSelected="selected"/]
[@cmt.htmlHead ""/]
[@cmt.navigation/]
<div class="TabContent">
  <h1>Edit User </h1>

  <div class="FormContainer">
    <form id="UserForm" action="[@spring.url '/userManagement/applyEditUser'/]" method="post">
      <input type="hidden" name="id" value="${user.id?c}">
      <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
        <tr>
          <td class="Labeltd"><label for="username">Name</label> <span class="RequiredField">*</span></td>
          <td class="inputltd">
    							<span class="text">
                  [@spring.formInput "user.username"/]
                  </span>
          </td>
          <td>
          [@spring.bind path="user.username"/]
                                [@spring.showErrors "<br>" "validationerror"/]
          </td>
        </tr>
        <tr>
          <td class="Labeltd"><label for="email">E-Mail</label></td>
          <td class="inputltd">
    							<span class="text">
                  [@spring.formInput "user.email"/]
                  </span>
          </td>
          <td>
          [@spring.bind path="user.email"/]
                                [@spring.showErrors "<br>" "validationerror"/]
          </td>
        </tr>
        <tr>
          <td class="Labeltd"><label for="mobile">Mobile</label></td>
          <td class="inputltd">
    							<span class="text">
                  [@spring.formInput "user.mobile"/]
                  </span>
          </td>
          <td>
          [@spring.bind path="user.mobile"/]
                                [@spring.showErrors "<br>" "validationerror"/]
          </td>
        </tr>
        <tr>
          <td class="Labeltd"><label for="password">Password</label> <span class="RequiredField">*</span></td>
          <td class="inputltd">
    							<span class="text">
                  [@spring.formPasswordInput "user.password"/]
                  </span>
          </td>
          <td>
          [@spring.bind path="user.password"/]
                                [@spring.showErrors "<br>" "validationerror"/]
          </td>
        </tr>
        <tr>
          <td class="Labeltd"></td>
          <td class="inputltd">
            <div class="FormHint"> Minimum of 6 characters, Must have numbers and letters</div>
          </td>
        </tr>

        <tr>
          <td class="Labeltd"><label for="rePassword">Retype Password</label> <span class="RequiredField">*</span></td>
          <td class="inputltd">
    							<span class="text">
                  [@spring.formPasswordInput "user.rePassword"/]
                  </span>
          </td>
          <td>
          [@spring.bind path="user.rePassword"/]
                                [@spring.showErrors "<br>" "validationerror"/]
          </td>
        </tr>
        <tr>
          <td class="Labeltd"><label for="cd-dropdown">Role</label> <span class="RequiredField">*</span></td>
          <td class="inputltd">
            <select id="cd-dropdown" class="cd-select required error" name="authority">
              <option value="-1">Select Role</option>
            [#list roles as role]
              <option value="${role.name}" [#if role.name == user.authority] selected [/#if]>${role.name}</option>
            [/#list]
            </select>
          </td>
          <td>
          [@spring.bind path="user.authority"/]
                                [@spring.showErrors "<br>" "validationerror"/]
          </td>
        </tr>
        <tr>
          <td class="Labeltd"><label for="cd-dropdown">Applications</label> <span class="RequiredField">*</span></td>
          <td class="inputltd">
            <select id="cd-dropdown" class="cd-select required error" name="appsToAccess">
              <option value="-1" selected>Select Application</option>
            [#list apps as app]
              <option value="${app}" [#if user.appsToAccess?? && app == user.appsToAccess]
                      selected [/#if]>${app}</option>
            [/#list]
            </select>
          </td>
          <td>
          [@spring.bind path="user.appsToAccess"/]
                                [@spring.showErrors "<br>" "validationerror"/]
          </td>
        </tr>
        <tr>
          <td class="Labeltd"></td>
          <td class="inputltd">
            <span class="SubmitButtons "> <span class="SubmitButton BtnWidth1"><input type="submit" value="Save"
                                                                                      class="submit"> </span></span>
    							<span class="SubmitButtons Gray ">
                                    <span class="SubmitButton BtnWidth1">
                                        <input type="button"
                                               onClick="location.href='[@spring.url '/userManagement/userManagement'/]'"
                                               value="Cancel">
                                    </span>
                                </span>
          </td>
        </tr>
      </table>
    </form>

  </div>

</div>
[@cmt.footer/]
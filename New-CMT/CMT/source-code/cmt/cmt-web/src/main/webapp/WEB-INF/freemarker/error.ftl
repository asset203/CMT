[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]

[#assign security=JspTaglibs["http://www.springframework.org/security/tags"] /]

[#global title="Error"/]

[@cmt.htmlHead ""/]
    [@cmt.navigation/]
       <div class="LayoutContainer">
            <div class="TabContent">
                <div class="ErrorTabContent">
					<p>
						<font size="2" color="red">

						   An error has occurred[#if errorMsg??]:<br>${errorMsg}[/#if]
						  <br>Please contact system administrator providing error code:
						    [#if errorCode??][<strong>${errorCode}<strong>][#else][/#if]
						</font>
					</p>
					<div>
						[@security.authorize access="isAuthenticated()"]
                            <span class="SubmitButtons Big">
                                <span class="SubmitButton ">
                                    <form action="[@spring.url '/index/home'/]">
                                        <input type="submit" class="submit" value="Return to homepage">
                                    </form>
                                </span>
                            </span>
                        [/@security.authorize]
					</div>
                </div>
            </div>

            <div class="clear"></div>
       </div>

[@cmt.footer/]

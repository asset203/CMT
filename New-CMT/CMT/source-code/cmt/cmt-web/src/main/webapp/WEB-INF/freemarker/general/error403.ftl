[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#global title="Error"/]

[#assign security=JspTaglibs["http://www.springframework.org/security/tags"] /]

[@cmt.htmlHead ""/]
   <div class="LayoutContainer">
   		<div class="tabsContainer">
   			<ul class="tabs">
   				<li class="loginTab selected"><a href="#d"> Error </a></li>
   			</ul>
   		</div>
   		<div class="TabContent">
   			<div class="ErrorTabContent">
                <p>
                   Access denied
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
[@cmt.footer/]

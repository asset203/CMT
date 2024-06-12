[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]
[#global title="Access Denied"/]

[@cmt.htmlHead ""/]
    [@cmt.navigation/]
    <div class="TabContent">
        <label class="error">
            Access Denied [[#if errorCode??]${errorCode}[#else][/#if]]
        </label>
	</div>

[@cmt.footer/]

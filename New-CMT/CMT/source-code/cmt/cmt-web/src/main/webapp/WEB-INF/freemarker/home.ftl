[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]
[#global title="Capacity Management Tool"/]
[@cmt.htmlHead ""/]
    [@cmt.navigation/]
    <div class="TabContent">
	
		<h1>Welcome to Vodafone Capacity Management Tool</h1>
		[#if successMsg??]${successMsg}[/#if]
		[#if errorMsg??]${errorMsg}[/#if]
	</div>

[@cmt.footer/]


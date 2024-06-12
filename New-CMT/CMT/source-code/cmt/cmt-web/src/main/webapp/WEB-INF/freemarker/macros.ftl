[#ftl/]
[#import "spring.ftl" as spring /]

[#assign security=JspTaglibs["http://www.springframework.org/security/tags"] /]

[#macro htmlHead userScript]
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
    <title>
        [#if title??]${title}[#else]Capacity Management Tool[/#if]
    </title>

    <script type="text/javascript" src="[@spring.url '/js/jquery-1.9.0.min.js'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/smartpaginator.js'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/jquery-ui-1.10.2.custom.min.js'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/jquery.validate.js'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/modernizr.custom.63321.js'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/jquery.AEdropContent.js'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/jquery.fancybox.js?v=2.1.4'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/jquery.tablesorter.min.js'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/jquery.metadata.js'/]"></script>

    <script type="text/javascript" src="[@spring.url '/js/common.js'/]"></script>
    <link rel="SHORTCUT ICON" href="[@spring.url '/images/favicon.ico'/]">
    <link rel="stylesheet" type="text/css" href="[@spring.url '/css/jquery.fancybox.css'/]">
    <link rel="stylesheet" type="text/css" href="[@spring.url '/css/jquery-ui-1.10.2.custom.min.css'/]">
    <link rel="stylesheet" type="text/css" href="[@spring.url '/css/layout.css'/]">
    [#if userScript??]
    ${userScript}
    [#else]
    <script>
        $(document).ready(function () {
        $('.date-pick').datepicker({showOn:"button"});
        });
    </script>
    [/#if]
</head>
<body class="PageContainer">
[/#macro]

[#macro htmlHeadNonLogged javaScript]
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
    <title>
        [#if title??]${title}[#else]Capacity Management Tool[/#if]
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
    <script type="text/javascript" src="[@spring.url '/js/jquery-1.9.0.min.js'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/common.js'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/jquery.validate.js'/]"></script>
    <script type="text/javascript" src="[@spring.url '/js/modernizr.custom.63321.js'/]"></script>

    <link rel="SHORTCUT ICON" href="[@spring.url '/images/favicon.ico'/]">
    <link rel="stylesheet" type="text/css" href="[@spring.url '/css/layout.css'/]">
    [#if javaScript??]${javaScript}[/#if]
</head>
<body class="PageContainer">
<div class="MainContainer">
    <div class="HeaderContainer">
        <div class="LogoSection">
            <a href="[@spring.url '/index/home'/]">
                <img src="[@spring.url '/images/capacity_managment_tool_logo.png'/]"
                     alt="Capacity Management Tool"/>
            </a>
        </div>
        <div class="clear"></div>
    </div>
    <div class="LayoutContainer">

        [/#macro]
        [#macro navigation]

        <div class="MainContainer">
            <div class="HeaderContainer">
                <div class="WelcomeSection">
                    <div> Welcome
                        [#if Session.SPRING_SECURITY_CONTEXT?exists]
                        [#if Session.SPRING_SECURITY_CONTEXT.authentication?exists]
                        [#if Session.SPRING_SECURITY_CONTEXT.authentication.principal?exists]
                        [@security.authentication property="principal.username" /]
                        [#else][/#if]
                        [#else][/#if]
                        [#else][/#if]

                    </div>
                    <a href="[@spring.url '/logout'/]" class="logout">Logout</a>

                </div>
                <div class="LogoSection">
                    <a href="[@spring.url '/index/home'/]">
                        <img src="[@spring.url '/images/capacity_managment_tool_logo.png'/]"
                             alt="Capacity Management Tool"/>
                    </a>
                </div>
                <div class="clear"></div>
            </div>
            <div class="LayoutContainer">
                <div class="tabsContainer">
                    <ul class="tabs">
                        [@security.authorize access="isAuthenticated() and hasRole('ADMIN')"]
                        <li class="UserManagemnt [#if isUserMgmtSelected??]${isUserMgmtSelected}[#else][/#if]">
                            <a href="[@spring.url '/userManagement/userManagement'/]">
                                User Management </a>
                        </li>
                        [/@security.authorize]
                        <li class="LogManager [#if isLogMgrSelected??]${isLogMgrSelected}[#else][/#if]">
                            <a href="[@spring.url '/logManager/viewLogs'/]"> Log Manager </a></li>
                        [@security.authorize access="isAuthenticated() and hasRole('ADMIN')"]
                        <li class="ManageDataColections [#if isDCSelected??]${isDCSelected}[#else][/#if]">
                            <a href="[@spring.url '/dataCollection/manage'/]">
                                Manage Data Collections </a>
                        </li>
                        [/@security.authorize]
                        [@security.authorize access="isAuthenticated() and hasRole('ADMIN')"]
                        <li class="ManageDataColectionsSystems [#if isDCSystemSelected??]${isDCSystemSelected}[#else][/#if]">
                            <a href="[@spring.url '/manageSystem/list'/]">
                                Manage Data Collections Systems</a>
                        </li>
                        [/@security.authorize]
                        [@security.authorize access="isAuthenticated() and hasRole('ADMIN')"]
                        <li class="ManageDataColectionsjobs [#if isDCJobsSelected??]${isDCJobsSelected}[#else][/#if]">
                            <a href="[@spring.url '/jobManagement/manageJobs'/]">
                                Manage Data Collections Jobs </a>
                        </li>
                        [/@security.authorize]

                        <li class="SystemEvents [#if isSysEventsSelected??]${isSysEventsSelected}[#else][/#if]">
                            <a href="[@spring.url '/systemEvents/viewEvents'/]">
                                System Events </a>
                        </li>


                        [@security.authorize access="isAuthenticated() and hasRole('ADMIN')"]
                        <li class="KPINotifications [#if isKPISelected??]${isKPISelected}[#else][/#if]">
                            <a href="[@spring.url '/manageKpis/systemKpis'/]">
                                KPI Notifications</a>
                        </li>
                        [/@security.authorize]
                    </ul>
                </div>

                [/#macro]
                [#macro loadingTxt]
                <div id="loadingTxt" class="NoResults" style="display:none;">In progress...</div>
                [/#macro]
                [#macro progressBar]
                <div class="loading" id="progressBar" style="display:none;"></div>
                [/#macro]
                [#macro footer]
                <div class="clear"></div>
            </div>   [#-- /LayoutContainer --]
        </div>   [#-- /MainContainer --]
</body>
</html>
[/#macro]
[#macro footerDIV]
</div>   [#-- /LayoutContainer --]
</div>   [#-- /MainContainer --]
[/#macro]
[#macro footerClose]
<div class="clear"></div>
</body>
</html>
[/#macro]

[#macro dummyForm]
<form id='postForm' action='#' method='POST'>
    <div id="hiddenField"></div>
</form>
[/#macro]


[#macro cmtFormDateInput id name value]
[@spring.bind value /]
<input id="${id}" name="${name}"
       value=${value?eval} size="25" class="required error date-pick" minlength="2" type="text">
[/#macro]

[#macro formCheckbox path attributes=""]
[@spring.bind path /]
<input type="hidden" name="_${spring.status.expression}" value="false"/>
<input type="checkbox" id="${spring.status.expression}" name="${spring.status.expression}"
[#if spring.status.value?? && spring.status.value?string=="true"]checked="true"[/#if]
${attributes}
[@spring.closeTag/]
[/#macro]

[#--given list[index].propertName and return propertyName --]
[#macro getColumnName itemName]

[#assign splittedString=itemName?split(".")/]
[#if splittedString?? && splittedString?has_content && splittedString[1]?? ]
[#assign name=splittedString[1]?split("Name")[0] /]
${name}
[/#if]

[/#macro]

[#--given list[index].propertName and return index+1 --]
[#macro getItemIndex itemName]

[#assign splittedString=itemName?split("[")/]
[#if splittedString?? && splittedString?has_content && splittedString[1]?? ]
[#assign index=splittedString[1]?split("]")[0]/]
[#assign num = index?number + 1]
${num}
[/#if]
[/#macro]
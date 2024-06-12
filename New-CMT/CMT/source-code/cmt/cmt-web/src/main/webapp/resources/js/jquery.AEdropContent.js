(function($){
	$.fn.extend ({
		dropDownContent : function(options){
		var
			defaults = {
				dropContainer: ".dropContainer", /***** the drop-down container name *****/
				activeTrigger: "active", /***** the active class name *****/
				delayOnMouseIn: 150, /***** + *****/
				delayOnMouseOut: 100, /***** + *****/
				dropContainerAlignment: "left", /***** center/right/left *****/
				leftPush: 0, /***** +/- *****/
				rightPush: 0, /***** +/- *****/
				topPush : 0, /***** +/- *****/
				centerClassName : "centerTheContainer",
				rightClassName : "rightTheContainer"
				},
			settings = $.extend(defaults, options);
			
/*var element = $(this).find(settings.dropContainer).parent("li");*/

/************************************************************/
/************************************************************/
/************************************************************/
$(this).each(function(){
/************************************************************/
/************************************************************/
	var thisElement = $(this);
	var hideDelayTimer = null;
	var containerChain = (thisElement).parent().find(settings.dropContainer);
	
/************************************************************/
function defaultAction() {
	$(containerChain)
	.hide()
	.appendTo("body");}
	
defaultAction();
/************************************************************/
/************************************************************/
/************************************************************/
$(thisElement).mouseover(function() {
	if (hideDelayTimer) clearTimeout(hideDelayTimer);
/********************/
/********************/
	var containerChainWidth =  $(containerChain).width();
	var containerChainOuterWidth = $(containerChain).outerWidth(); /*$(containerChain).outerWidth( true );*/
	
if ((settings.dropContainerAlignment) == "center") {
	var containerChainLeftValue = (thisElement).offset().left + ((thisElement).width() / 2) - (containerChainOuterWidth / 2);
	$(containerChain).addClass(settings.centerClassName);
} else if ((settings.dropContainerAlignment) == "right") {
	var containerChainLeftValue = $(thisElement).offset().left + (thisElement).outerWidth() - containerChainOuterWidth - (settings.rightPush);
	$(containerChain).addClass(settings.rightClassName);
} else {
	var containerChainLeftValue = (thisElement).offset().left + (settings.leftPush);
}
/********************/
/********************/
	hideDelayTimer = setTimeout(function(){
		$(thisElement).addClass(settings.activeTrigger);
		$(containerChain)
		.css({
			 top: (thisElement).offset().top + (thisElement).height() + (settings.topPush),
			 left: containerChainLeftValue,
			 width: containerChainWidth
			 })
		/*.show();*/
		.fadeIn(250);
	}, settings.delayOnMouseIn);
/************************************************************/	
}).mouseout(function(){
	if (hideDelayTimer) clearTimeout(hideDelayTimer);
	hideDelayTimer = setTimeout(function(){
		$(thisElement).removeClass(settings.activeTrigger);
		hideDelayTimer = null;
		$(containerChain)
		/*.hide();*/
		.fadeOut(150);		
	}, settings.delayOnMouseOut);
/************************************************************/
$(containerChain).mouseover(function() {
	if (hideDelayTimer) clearTimeout(hideDelayTimer);

/************************************************************/	
}).mouseout(function(){
	if (hideDelayTimer) clearTimeout(hideDelayTimer);
	hideDelayTimer = setTimeout(function(){
		$(thisElement).removeClass(settings.activeTrigger);
		hideDelayTimer = null;
		$(containerChain)
		.hide();		
	}, settings.delayOnMouseOut);
});

/************************************************************/
/************************************************************/	
	});
/************************************************************/
/************************************************************/
/************************************************************/
$(thisElement).click(function() {
	return false;
});
/************************************************************/
/************************************************************/
/************************************************************/
			
});
	}	
	});
})(jQuery);
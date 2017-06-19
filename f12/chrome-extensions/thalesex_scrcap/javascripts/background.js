var takeScreenshot = {
	/**
	 * @description ID of current tab
	 * @type {Number}
	 */
	tabId: null,

	/**
	 * @description Canvas element
	 * @type {Object}
	 */
	screenshotCanvas: null,

	/**
	 * @description 2D context of screenshotCanvas element
	 * @type {Object}
	 */
	screenshotContext: null,

	/**
	 * @description Number of pixels by which to move the screen
	 * @type {Number}
	 */
	scrollBy: 0,

	/**
	 * @description Sizes of page
	 * @type {Object}
	 */
	size: {
		width: 0,
		height: 0
	},

	/**
	 * @description Keep original params of page
	 * @type {Object}
	 */
	originalParams: {
		overflow: "",
		scrollTop: 0
	},

	/**
	 * @description Initialize plugin
	 */
	initialize: function () {
		this.screenshotCanvas = document.createElement("canvas");
		this.screenshotContext = this.screenshotCanvas.getContext("2d");

		this.bindEvents();
	},

	/**
	 * @description Bind plugin events
	 */
	bindEvents: function () {
		// handle onClick plugin icon event
		chrome.browserAction.onClicked.addListener(function (tab) {
			this.tabId = tab.id;
			console.log(tab.id);

			chrome.tabs.sendMessage(tab.id, {
				"msg": "getPageDetails"
			});
		}.bind(this));

		// handle chrome requests
		chrome.runtime.onMessage.addListener(function (request, sender, callback) {
			console.log("onMessage" + request);
			
			if (request.msg === "setPageDetails") {
				this.size = request.size;
				this.scrollBy = request.scrollBy;
				this.originalParams = request.originalParams;

				// set width & height of canvas element
				this.screenshotCanvas.width = this.size.width;
				this.screenshotCanvas.height = this.size.height;

				this.scrollTo(0);
			} else if (request.msg === "capturePage") {
				
				
				//chrome.tabs.getSelected(null, function)
				
				chrome.tabs.captureVisibleTab(null, {
					"format": "png"
					}, function (dataURI) {
						console.log("captured");
						chrome.tabs.sendMessage(sender.tab.id, {msg:"screenImg",dataURI:dataURI},function(res){});

				});
				
				//this.capturePage(request.position, request.lastCapture);
			} /*else if (request.msg === "print") {
				
				
				
				setDataCode = "document.getElementById('#thalesex_scrcap-img').src=" + request.dataURI+";";
				printCode = "window.print();";
				
				chrome.tabs.create({url:chrome.extension.getURL("res/print.html")},
									function(tab){
										
										chrome.tabs.executeScript(
											tab.id,
											{code:setDataCode}
										);
										
										chrome.tabs.executeScript(
											tab.id,
											{code:printCode}
										);
										
									});
				
				
			} */else if (request.msg === "anything") {
				console.log("onMessage: anything");
				this.capturePage(0, true);
			} else if ('screenCapture' === request.msg) {
				console.log("onMessage: screenCapture");
				this.capturePage(0, true);
			}
		}.bind(this));
	},

	/**
	 * @description Send request to scroll page on given position
	 * @param {Number} position
	 */
	scrollTo: function (position) {
		chrome.tabs.sendMessage(this.tabId, {
			"msg": "scrollPage",
			"size": this.size,
			"scrollBy": this.scrollBy,
			"scrollTo": position
		});
	},


	/**
	 * @description Send request to set original params of page
	 */
	resetPage: function () {
		chrome.tabs.sendMessage(this.tabId, {
			"msg": "resetPage",
			"originalParams": this.originalParams
		});
	}
};


takeScreenshot.initialize();


var AR = {
	baseUrl : 'https://ar-learn.appspot.com/rest/'
};

AR.network = {
	doGet : function(params) {
		params.method = 'GET';
		AR.network.networkOperation(params);
	},

	doPost : function(params) {
		params.method = 'POST';
		AR.network.networkOperation(params);
	},

	doDelete : function(params) {
		params.method = 'DELETE';
		AR.network.networkOperation(params);
	}
};

AR.auth = {
	token : '',
	login : function(username, password, tokenCallback, failure) {
		AR.network.doPost({
			url : AR.baseUrl + 'login',
			postData : username + '\n' + password,
			contentType : 'text/plain',
			onResponse : function(responseText) {
				if (responseText == '') {
					if (failure) failure();
				} else {
					var json = JSON.parse(responseText);
					AR.auth.setToken(json.auth);
					if (tokenCallback != null) {
						tokenCallback(AR.auth.token);
					}
				}
			},
			onFailure: function(status, response) {
				if (failure)
					failure(status, response);
			}
		});
		
	},
	setToken: function(token) {
		AR.auth.token=token;
//		AR.channel.init();
	}
};

AR.games = {
	myGames : function(params) {
		if (params == null)
			params = {};
		params.url = AR.baseUrl + 'myGames', AR.network.doGet(params);
	}
};

AR.runs = {
	myRuns : function(params) {
		params = params || {};
		params.url = AR.baseUrl + 'myRuns/participate', AR.network
				.doGet(params);
	},
	listeners : [],

	processModification : function(json) {
		AR.runs.listeners.forEach(function(el) {
			switch (json.modificationType) {
			case 1:
				el.onCreate(json.run);
				break;
			case 2:
				el.onDelete(json.run);
				break;
			case 3:
				el.onAltered(json.run);
				break;
			default:
				break;
			}
		});
	},

	subscribeListener : function(listener) {
		AR.runs.listeners.push(listener);
	},

	unSubscribeListener : function(listener) {
		AR.runs.listeners = AR.runs.listeners.filter(function(
				el) {
			if (el !== fn) {
				return el;
			}
		});
	}
};

AR.generalItems = {

	getGeneralItemsGame : function(gameId, params) {
		if (params == null)
			params = {};
		params.url = AR.baseUrl + 'generalItems/gameId/' + gameId, AR.network
				.doGet(params);
	},
	getGeneralItemsRun : function(runId, params) {
		if (params == null)
			params = {};
		params.url = AR.baseUrl + 'generalItems/runId/' + runId, AR.network
				.doGet(params);
	},
	getGeneralItemsRunAll : function(runId) {
		if (params == null)
			params = {};
		params.url = AR.baseUrl + 'generalItems/runId/' + runId + "/all",
				AR.network.doGet(params);
	},
	listeners : [],

	processModification : function(json) {
		AR.generalItems.listeners.forEach(function(el) {

			switch (json.modificationType) {
			case 1:
				el.onCreate(json.runId, json.generalItem);
				break;
			case 2:
				el.onDelete(json.runId, json.generalItem);
				break;
			case 3:
				el.onAltered(json.runId, json.generalItem);
				break;
			case 4:
				el.onVisible(json.runId, json.generalItem);
				break;
			default:
				break;
			}
		});
	},

	subscribeListener : function(listener) {
		AR.generalItems.listeners.push(listener);
	},

	unSubscribeListener : function(listener) {
		AR.generalItems.listeners = AR.generalItems.listeners.filter(function(
				el) {
			if (el !== fn) {
				return el;
			}
		});
	}
};

AR.channel = {
	channel: null,
	socket: null,
	
	init : function() {

		AR.channel.getToken({
			onResponseJson : function(json) {
				AR.channel.destroy();
				AR.channel.channel = new goog.appengine.Channel(json.token);
				AR.channel.socket = AR.channel.channel.open({
					//  Socket is ready to receive messages.
					onopen: function() {
					},

					onmessage: function(message) {
						var jsonMessage = JSON.parse(message.data);
						if (jsonMessage.type == "org.celstec.arlearn2.beans.notification.RunModification") {
							AR.runs.processModification(jsonMessage);
						}
						if (jsonMessage.type == "org.celstec.arlearn2.beans.notification.GeneralItemModification") {
							AR.generalItems.processModification(jsonMessage);
						}
						if (jsonMessage.type == "org.celstec.arlearn2.beans.notification.Ping") {
							AR.channel.pong(jsonMessage.to,jsonMessage.from, 0, jsonMessage.timestamp);
						}
					},

					// err.code: 401 (Unauthorized)
					// err.description
					// An onerror call is always followed by an onclose call and the channel
					// object will have to be recreated after this event
					onerror: function(err) {
						alert('channel error: ' + err.code + ' ' + err.description);
					},

					// When the socket is closed, it cannot be reopened. Use the open()
					// method on a goog.appengine.Channel object to create a new socket.
					onclose: function() {
					}
				});
			}
		});
	},

	getToken : function(params) {
		if (params == null)
			params = {};
		params.url = AR.baseUrl + 'channelAPI/token', AR.network.doGet(params);
	},
	
	pong : function(from, to, origRequest, origTimeStamp) {
		var params = { url : AR.baseUrl + 'channelAPI/pong/'+from+'/'+to+'/'+origRequest+'/'+origTimeStamp }; 
		AR.network.doPost(params);
	},
	
	destroy: function() {
		if (AR.channel.socket != null) {
			AR.channel.socket.close();
		}
		AR.channel.socket=null;
		AR.channel.channel=null;
	}
};

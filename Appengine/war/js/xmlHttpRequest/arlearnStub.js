AR.network.networkOperation = function(params) {
	var xhr = new XMLHttpRequest();

	xhr.onload = function() {
		if (xhr.status == 200) {
			if (params != null && params.onResponse != null) {
				params.onResponse(this.responseText);
			}

			if (params != null && params.onResponseJson != null) {
				params.onResponseJson(JSON.parse(this.responseText));
			}
		} else {
			if (params.onFailure != null)
				params.onFailure(xhr.status, xhr.responseText);
		}
	};

	if (params != null) {
		xhr.open(params.method, params.url);
		if (AR.auth.token != '') {
			xhr.setRequestHeader('Authorization', 'GoogleLogin auth='
					+ AR.auth.token);
		}
		if (params.contentType != null) {
			xhr.setRequestHeader('Content-Type', params.contentType);
		}
		if (params.Accept != null) {
			xhr.setRequestHeader('Accept', params.Accept);
		}
		if (params.postData != null) {
			xhr.send(params.postData);
		} else {
			xhr.send();
		}
	}
};
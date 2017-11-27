/**
 *js的工具类
 *zhouzhiyuan 
 */

//str字符串是否包含substr字符串
function isContains(str, substr) {
    return str.indexOf(substr) >= 0;
}

/**
 * aj_fileDownload 下载工具类
 * $.aj_fileDownload(option);
 * options为一个json对象包含下列属性
 * httpMethod 默认为post方式提交 可以不传
 * dUrl 下载的地址（必传）
 * data 传给后台的json参数，传参方式类似ajax传参 可以不传
 * prepareCallback 预备上传文件时的回调函数 可以不传
 * successCallback 上传文件成功的回调函数 可以不传
 * failCallback 上传文件失败的回调函数 可以不传
 * @param $
 * @returns
 */
(function($) {
	$.extend({
		aj_fileDownload: function(options) {
			var defaults = {
				httpMethod: "POST",
			};
			var opts = $.extend(defaults, options);
			$.fileDownload(opts.dUrl,{
				httpMethod: opts.httpMethod,
				data: opts.data,
				prepareCallback: function (url) {
					loading();
					opts.prepareCallback == null || opts.prepareCallback == "undefined" || opts.prepareCallback == "" 
						|| typeof(opts.prepareCallback) == "undefined" || opts.prepareCallback == "null" ? "" : opts.prepareCallback(url);
				},
				successCallback: function (url) {
					closeloading();
					opts.successCallback == null || opts.successCallback == "undefined" || opts.successCallback == "" 
						|| typeof(opts.successCallback) == "undefined" || opts.successCallback == "null" ? "" : opts.successCallback(url);
				},
				abortCallback: function (url) {
				},
				failCallback: function (responseHtml, url, error) {
					closeloading();
					//alert("failCallback:"+responseHtml+"|"+url+"|"+error);
					if(isContains(responseHtml,"404")) {
						window.location.href=URL+"/toError.action";
					}else if(isContains(responseHtml,"loginbody")) {
						if(isContains(responseHtml,"登录失效")) {
							alert("登录失效，请重新登录！");
						}else if(isContains(responseHtml,"账号在其它地方登录")) {
							alert("账号在其它地方登录，请重新登录！");
						}
						window.location.href=URL+"/logout.action";
					}
					
					opts.failCallback == null || opts.failCallback == "undefined" || opts.failCallback == "" 
						|| typeof(opts.failCallback) == "undefined" || opts.failCallback == "null" ? "" : opts.failCallback(responseHtml, url, error);
				}
			});
		}
	});
})(jQuery);
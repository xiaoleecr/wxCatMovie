/*
 $.ax(option);
 option为一个json对象包含下列属性
 type 默认 post 可以不传
 async 是否异步 默认是true 可以不传
 data 是要传到后台的json数据 可以 不传
 beforesend 为在ajax发送请求前执行的函数 默认为null 
 complete  为ajax执行完毕之后调用的回调函数 默认为null
 success   ajax调用成功之后的回调函数
 error     ajax调用错误之后的回调函数
 param_aj 为要传到success的参数 默认可以不填
 url 必填项，ajax的请求地址
 * */
(function($) {
	$.extend({
		ax: function(options) {
			var defaults = {
				type: "post",
				async: true,
				cache: false,
				data: {
					"date": new Date().getTime()
				},
				contextType: "application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				beforeSend: loading,
				complete:closeloading,
				success:null,
				error:null,
				param_aj:null
				
			};
			var opts = $.extend(defaults, options);
			$.ajax({
				type: opts.type,
				async: opts.async,
				data: opts.data,
				url: opts.url,
				cache: opts.cache,
				dataType: opts.dataType,
				contextType:opts.contextType,
				beforeSend: function(xhr) {
					opts.beforeSend == null || opts.beforeSend == "" || typeof(opts.beforeSend) == "undefined" ? "" : opts.beforeSend(xhr, opts.param_aj);
				},
				success: function(d) {
					opts.success == null || opts.success == "" || typeof(opts.success) == "undefined" ? "" : opts.success(d, opts.param_aj);
				},
				error: function(xhr) {
					toError(xhr);
					opts.errorfn == null || opts.errorfn == "" || typeof(opts.errorfn) == "undefined" ? "" : opts.errorfn(xhr, opts.param_aj);
				},
				complete: function(xhr)
				{
					opts.complete == null || opts.complete == "" || typeof(opts.complete) == "undefined" ? "" : opts.complete(xhr, opts.param_aj);
				}
			});
		}
	});
})(jQuery);
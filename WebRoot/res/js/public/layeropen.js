/*
 
 * */
(function($) {
	$.extend({
		layeropen: function(options) {
			var defaults = {
				type: 0, //可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）。 若你采用layer.open({type: 1})方式调用，则type为必填项（信息框除外）
				title: "提示", //弹出框的标题默认为”提示“，
				content: "", //弹出框的内容 type为1 的时候是个页面id对象 type为2 的时候是一个页面请求地址
				shade: 0.4, //默认0.4
				anim: 0, //0-6的动画形式，-1不开启 默认0
				area: 'auto', //弹出框的大小  ['750px', '430px']
				closeBtn: 1, //不显示关闭按钮 默认1 表示显示
				btn: '确认',//不显示关闭按钮 默认"确认"
				success:null, //成功之后的回调函数
				yes:null, //按钮1的回调函数
				btn2:null, //按钮2的回调函数
				cancle:cancelall,//关闭按钮的回调函数
				ids:null //需要传递的参数
				
			};
			var opts = $.extend(defaults, options);
			var indexlayer = layer.open({
				type: opts.type, //可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）。 若你采用layer.open({type: 1})方式调用，则type为必填项（信息框除外）
				title: opts.title, //弹出框的标题
				content: opts.content, //弹出框的内容
				shade: opts.shade,
				anim: opts.anim, //0-6的动画形式，-1不开启
				area: opts.area, //弹出框的大小  ['750px', '430px']
				closeBtn: opts.closeBtn, //不显示关闭按钮
				btn: opts.btn,
				success: function(layero, index) {
					if(opts.success) {
						opts.success(index, opts.ids);
					}
				},
				yes: function(index, layero) {
					if(opts.yes) {
						opts.yes(index, opts.ids);
					}

				},
				btn2: function(index, layero) {
					if(opts.btn2) {
						opts.btn2(index, opts.ids);
					}
				},
				cancel: function(index, layero) {
					if(opts.cancel) {
						opts.cancel(index, opts.ids);
					}
				}
			});
			return indexlayer;
		}

	});

})(jQuery);
;$(function () {
    $.ajax({
        type: 'post',
        data: {
            url: window.location.href,
        },
        url: URL + '/getJSKSign',
        success: function (d) {
            var data = JSON.parse(d);

            wx.config({
                debug: false,
                appId:'wxd42462bf296501b1',
                timestamp:data.timestamp,
                nonceStr:data.nonceStr,
                signature:data.signature,
                jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone']
            })
            ;

        }
    })

    wx.ready(function(){
        // 获取"分享到朋友圈"按钮点击状态及自定义分享内容接口
        wx.onMenuShareTimeline({
            title: '慧锐通电子书架', // 分享标题
            link: 'http://www.wrtrd.net/book/',
            imgUrl: 'http://www.wrtrd.net/book/images/wxbook.jpg' // 分享图标
        });


        // 获取"分享给朋友"按钮点击状态及自定义分享内容接口
        wx.onMenuShareAppMessage({
            title: '慧锐通电子书架', // 分享标题
            desc: '慧锐通产品介绍的电子画册，含数字对讲、模拟对讲、云对讲、智能互联、蓝牙门禁等系统！', // 分享描述
            link: 'http://www.wrtrd.net/book/',
            imgUrl: 'http://www.wrtrd.net/book/images/wxbook.jpg', // 分享图标
            type: 'link' // 分享类型,music、video或link，不填默认为link
        });


        //获取"分享到QQ"按钮点击状态及自定义分享内容接口
        wx.onMenuShareQQ({
            title: '慧锐通电子书架', // 分享标题
            desc: '慧锐通产品介绍的电子画册，含数字对讲、模拟对讲、云对讲、智能互联、蓝牙门禁等系统！', // 分享描述
            link: 'http://www.wrtrd.net/book/', // 分享链接
            imgUrl: 'http://www.wrtrd.net/book/images/wxbook.jpg' // 分享图标
        });


        //获取"分享到腾讯微博"按钮点击状态及自定义分享内容接口
        wx.onMenuShareWeibo({
            title: '慧锐通电子书架', // 分享标题
            desc: '慧锐通产品介绍的电子画册，含数字对讲、模拟对讲、云对讲、智能互联、蓝牙门禁等系统！', // 分享描述
            link: 'http://www.wrtrd.net/book/', // 分享链接
            imgUrl: 'http://www.wrtrd.net/book/images/wxbook.jpg' // 分享图标
        });


        //获取"分享到QQ空间"按钮点击状态及自定义分享内容接口
        wx.onMenuShareQZone({
            title: '慧锐通电子书架', // 分享标题
            desc: '慧锐通产品介绍的电子画册，含数字对讲、模拟对讲、云对讲、智能互联、蓝牙门禁等系统！', // 分享描述
            link: 'http://www.wrtrd.net/book/', // 分享链接
            imgUrl: 'http://www.wrtrd.net/book/images/wxbook.jpg' // 分享图标
        });

    });
})
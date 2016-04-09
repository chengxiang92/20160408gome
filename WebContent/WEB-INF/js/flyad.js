$(function () {
    var ww = $(window).innerWidth();
    var wh = $(window).innerHeight();
    var percentw = ww / 640;
    var percenth = wh / 1008;
    // 分享点击
    $("#share").live("click", function () {
        showShareLayout();
    });
    // 隐藏分享浮层
    $("#shareLayout,#graybg").click(function () {
        hideLayout();
    });
    // 页面整屏&背景整屏（加载页，容器页，整屏页）。区分三者，为了不同用途
    $("#loading,#container,.fullpage,.innerfullpage").each(function () {
        $(this).css({ "width": ww + "px", "height": wh + "px", "background-size": ww + "px " + wh + "px", "position": "absolute" });
    });

    // 绝对定位,两个属性 left / top
    $(".locate").each(function () {
        var l = parseInt($(this).attr("left"));
        var t = parseInt($(this).attr("top"));
        var w = parseInt($(this).attr("width"));
        var h = parseInt($(this).attr("height"));
        var r = parseInt($(this).attr("right"));
        var b = parseInt($(this).attr("bottom"));
        if (l != undefined) {
            $(this).css({ "left": l * percentw + "px" });
        }
        if (t != undefined) {
            $(this).css({ "top": t * percenth + "px" });
        }
        if (r != undefined) {
            $(this).css({ "right": r * percentw + "px" });
        }
        if (b != undefined) {
            $(this).css({ "bottom": b * percenth + "px" });
        }
        if (h != undefined) {
            $(this).css({ "height": h * percenth + "px" });
        }
        if (w != undefined) {
            $(this).css({ "width": w * percenth + "px" });
        }
    });
    // 适配大小
    $(".resize").each(function () {
        var realh = $(this).attr("height");
        var realw = $(this).attr("width");
        var h = realh * percenth;
        var w = realw * percenth;
        var ml = (realw - w) / 2;
        $(this).css({ "width": w + "px", "height": h + "px", "margin-left": ml + "px" });
    });
});
// 动态设置页面标题（兼容android与IOS）
function setTitle(title) {
    var $body = $('body');
    document.title = title;
    // hack在微信等webview中无法修改document.title的情况
    var $iframe = $('<iframe src="http://i.fly-ad.cn/bpoint.jpg" width="0" height="0"></iframe>');
    $iframe.on('load', function () {
        setTimeout(function () {
            $iframe.off('load').remove();
        }, 0);
    }).appendTo($body);
}
// 机型判断
function isAndroid() {
    var ua = navigator.userAgent;
    if (/Android (\d+\.\d+)/.test(ua)) {
        return true;
    } else {
        return false;
    }
}
// 显示页面
function showPage(ele) {
    $(".fullpage").addClass("hide");
    $(ele).removeClass("hide");
}
// ajax
var isrequesting = false;
function ajax(url, data, successcallback, errorcallback) {
    if (isrequesting) {
        return false;
    }

    isrequesting = true;
    try {
        $.ajax({
            url: url,
            type: "post",
            dataType: "json",
            data: data,
            error: function (ex, b, c) {
                isrequesting = false;
                if (errorcallback) {
                    errorcallback(ex, b, c);
                }
            },
            success: function (data) {
                isrequesting = false;
                successcallback(data);
            }
        });
    } catch (ex) {
        isrequesting = false;
    }
}
function jsClass(src, callback) {
    var script = document.createElement('script'), cb = "callback_" + (new Date()).getMilliseconds() + parseInt(Math.random() * 1000);
    //整理cb参数
    if (callback) {
        if (src.indexOf("?") != -1) { src += "&cb=" + cb; }
        else { src += "?cb=" + cb; }
    }
    //回调
    window[cb] = function (a) {
        callback(a);
        window[cb] = null;
        return;
    }
    //写入
    script.setAttribute('type', 'text/javascript');
    script.setAttribute('src', src);
    document.getElementsByTagName("head")[0].appendChild(script);
}

var phoneWidth = parseInt(window.screen.width);
var phoneScale = phoneWidth / 640;
var ua = navigator.userAgent;
if (/Android (\d+\.\d+)/.test(ua)) {
    var version = parseFloat(RegExp.$1);
    // andriod 2.3
    if (version > 2.3) {
        document.write('<meta name="viewport" content="width=640, minimum-scale = ' + phoneScale + ', maximum-scale = ' + phoneScale + ', target-densitydpi=device-dpi">');
        // andriod 2.3以上
    } else {
        document.write('<meta name="viewport" content="width=640, target-densitydpi=device-dpi">');
    }
    // 其他系统
} else {
    document.write('<meta name="viewport" content="width=640, user-scalable=no, target-densitydpi=device-dpi">');
}
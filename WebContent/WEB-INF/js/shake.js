  var Shake = {
        Params: {
            direct: "", // 方向
            minshaketimes: 3, // 最小晃动次数
            shaketimes: 0, // 横竖屏切换次数
            isshake: false, // 是否晃动手机
            predate: null, // 上一次摇动的时间
            isstopListen: false, // 是否停止监听
            stoptimespan: 500, // 多少毫秒内不摇手机，视为停止，默认500ms
        },
        // 侦听手机摇一摇
        StartShakeListener: function () {
            // 启动侦听
            if (window.DeviceMotionEvent) {
                window.addEventListener('devicemotion', deviceMotionHandler, false);
            } else {
                alert('本设备不支持devicemotion事件');
            }
 
            // 侦听
            function deviceMotionHandler(eventData) {
                if (Shake.Params.isstopListen) return;
                var acceleration = eventData.accelerationIncludingGravity,
                    x, y, z;
 
                x = acceleration.x;
                y = acceleration.y;
                z = acceleration.z;
                if ((Math.abs(y) < Math.abs(x) && Math.abs(y) <= 5 && Math.abs(x) >= 5) ||
                    (Math.abs(y) < Math.abs(z) && Math.abs(y) <= 5 && Math.abs(z) >= 5)) {
                    //"横屏";
                    CheckShake(new Date(), "h");
                } else {
                    //"竖屏"
                    CheckShake(new Date(), "v");
                }
            }
 
            // 检查手机是否摇
            function CheckShake(d, curd) {
                if (Shake.Params.predate == null) {
                    Shake.Params.predate = d;
                } else {
                    if (Shake.Params.direct == curd) return;
                    var secspan = d.getTime() - Shake.Params.predate.getTime();
                    Shake.Params.predate = d;
                    // 时间间隔大于
                    if (secspan > 100 && secspan < Shake.Params.stoptimespan) {
                        Shake.Params.shaketimes++;
                        if (Shake.Params.shaketimes >= Shake.Params.minshaketimes) {
                            // 摇晃了手机
                            DeviceShake();
                            Shake.Params.isshake = true;
                            StopShakeHandler();
                        }
                    }
                }
                Shake.Params.direct = curd;
            }
 
            // 摇晃手机停止设置
            function StopShakeHandler() {
                function StopCheck() {
                    if (!Shake.Params.isshake) return false;
                    var date = new Date();
                    var timespan = date.getTime() - Shake.Params.predate.getTime();
                    // 超过500ms算停止摇一摇
                    if (timespan > Shake.Params.stoptimespan || Shake.Params.isstopListen) {
                        // 停止摇晃了
                        Shake.Params.isshake = false;
                        Shake.Params.shaketimes = 0;
                        Shake.Params.isstopListen = true;
                        DeviceStopShake();
                        return false;
                    }
                    setTimeout(function () {
                        StopCheck();
                    }, Shake.Params.stoptimespan);
                }
                StopCheck();
            }
        },
        // 停止监听
        StopShakeListener: function () {
            Shake.Params.isstopListen = true;
        },
        // 重新监听
        RestartShakeListener: function () {
            Shake.Params.isstopListen = false;
        },
        // 设置停止的监听间隔
        SetStopTimespan: function (t) {
            Shake.Params.stoptimespan = t;
        }
    }
    // 停止晃动回调方法
    function DeviceStopShake() {
    	//alert("晃动了" + Shake.Params.shaketimes + "下");
    	setTimeout(function () {
    		yaoyiyao();
    	},500);
       Shake.RestartShakeListener();
    }
    // 晃动回调方法
    function DeviceShake() {
        //alert("开始晃动");
    }
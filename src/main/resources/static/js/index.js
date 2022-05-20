$(function () {
    let r = getParam('r');
    if (r) {
        toRegister_view(r);
    }
})

function toLogin_view() {
    $("#index-view").hide(200)
    $("#login-view").show(200)
}

function toRegister_view(r) {
    $("#index-view").hide(200)
    $("#register-view").show(200)
    let imgVerify = $("#imgVerify").get(0);
    getVerify(imgVerify);
    if (r) {
        $("#r-code")
            .val(r)
            .attr("disabled", "disabled")
    }
}

function back() {
    $("#login-view").hide(200)
    $("#register-view").hide(200)
    $("#index-view").show(200)
}

function getParam(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURI(r[2]);
    }
    return null;
}

function submit() {
    let tips = $("#tips-r")
    tips.css("visibility", "hidden")
    let email = $("#r-email").val()
    let pwd = $("#r-pwd").val()
    if (!isEmail(email)) {
        tips.css("visibility", "visible").text("请输入正确的邮箱格式")
        return
    }
    if (pwd.length < 6) {
        tips.css("visibility", "visible").text("密码过短")
        return
    }
    let verify = $("#verify-code").val()
    if (verify.length < 4) {
        tips.css("visibility", "visible").text("请输入验证码")
        return
    }
    let code = $("#r-code").val()
    let data = {
        verifyCode: verify,
        user: {
            email: email,
            passwd: pwd,
            code: code,
        }
    }
    $.ajax({
        url: "/user/verify",
        method: "POST",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(data),
        success: function (res) {
            if (res.code === 1) {
                $("#register-view").hide(200)
                $("#confirm-view").show(200)
                $("#h-email").val(email)
                loopText("#h-btn", 60)
                return
            }
            tips.css("visibility", "visible").text(res.data)
        },
        error: function () {
            alert("致命错误，请稍后重试")
        }

    })

}

function reSend() {
    let email = $("#h-email").val()
    $.ajax({
        url: "/user/retry",
        method: "GET",
        data: {
            email: email
        },
        success: function (res) {
            console.log(res)
            loopText("#h-btn", 60)
            alert(res.data)
        },
        error: function () {
            alert("当前与服务器通信失败，请稍后重试")
        }
    })
}

function verifyEmail() {
    let email = $("#h-email").val();
    let code = $("#h-code").val()
    $.ajax({
        url: "/user/register",
        method: "POST",
        data: {
            email: email,
            code: code
        },
        success: function (res) {
            if (res.code === 1) {
                //完成注册，跳转/home
                window.location.replace("/home");
            } else if (res.code === 2) {
                //邮箱已注册，跳转/index
                alert("邮箱已被注册")
                window.location.replace("/index");
            } else if (res.code === 3) {
                // 自动登录失败
                alert("注册成功")
                window.location.replace("/index");
            } else {
                $("#tips-h").text(res.data)
            }
        },
        error: function () {
            alert("致命错误，请稍后重试")
        }
    })

}

function login(obj) {
    let tips = $("#tips")
    tips.css("visibility", "hidden")
    let email = $("#l-email").val()
    let pwd = $("#l-pwd").val()
    if (!isEmail(email)) {
        tips.css("visibility", "visible").text("请输入正确的邮箱格式")
        return
    }
    if (pwd.length < 6) {
        tips.css("visibility", "visible").text("密码过短")
        return
    }
    let user = {
        email: email,
        passwd: pwd
    }
    $.ajax({
        url: "/user/login",
        method: "POST",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(user),
        success: function (res) {

            if (res.code === 1) {
                // window.location.href = "/home";
                window.location.replace("/home");
            } else {
                tips.css("visibility", "visible").text(res.data)
                $("#l-pwd").val("")
            }

        },
        error: function (ex) {
            alert("登陆失败")
        },
    })
}

function isEmail(str) {
    let reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
    return reg.test(str);
}

function getVerify(obj) {
    $.ajax({
        type: "GET",
        url: "/event/captcha",
        success: function (res) {
            if (res.code === 1) {
                obj.src = "data:image/jpeg;base64," + res.data.img;
            } else {
                alert(res.data)
            }
        }
    });
}

/**
 * 循环更新控件文本
 * @param obj 对象
 * @param text 内容
 * @param end 结束时间，单位秒。
 * @returns {number} 定时器id，可手动清除定时器
 */
function loopText(obj, end) {
    let start = 0
    let view = $(obj)
    view.addClass("disabled");
    let loop = setInterval(function () {
        start++
        view.text((end - start) + "秒后可发送")
        if (start >= end) {
            clearInterval(loop)
            view.text("获取邮件验证码")
            view.removeClass("disabled")
        }
    }, 1000)
    return loop
}


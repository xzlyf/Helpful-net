let w = null;

$(function () {
    getTask()
})


function getTask(obj) {
    $(obj).addClass("disable")
    let url = "/taskhall/get?r=" + Math.random();
    $.ajax({
        url: url,
        method: "GET",
        success: function (result, status, xhr) {
            $(obj).removeClass("disable")
            if (result.code === -1) {
                let html = "<h1>"+result.data+"</h1>"
                $("#preview").html(html)

            } else {
                $(".info-view").html(result)
            }
        },
        error: function () {
            $(obj).removeClass("disable")
            alert("请求失败")
            // console.log("请求失败")
        },
    })
}

function startTask(obj) {
    let taskId = $("#remote_id").val()
    $.ajax({
        url: "/taskhall/startTask?taskId=" + taskId,
        method: "get",
        timeout: 30000,
        beforeSend: function () {
            newWindows(obj)
        },
        success: function (res) {
            if (res.code === 1) {
                if (w) {
                    w.close();
                    doneTask(taskId, res.data)
                }
            } else {
                alert("错误代码：" + res.code)
                if (w) {
                    w.close();
                }
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("与服务器通信失败，请检查网络")
            if (w) {
                w.close();
            }
        },

    })
}

function doneTask(taskId, r) {
    $("#controller-bar").css("display", "none")
    $("#verify-bar").css("display", "")
    $.ajax({
        url: "/taskhall/doneTask?taskId=" + taskId + "&r=" + r,
        method: "get",
        success: function (res) {
            if (res.code === 1) {
                getTask();
            } else {
                alert(res.data)
            }
        },
        error: function () {
            console.log("当前与服务器连接失败，请稍后重试！")
        },
        complete: function () {
            $("#controller-bar").css("display", "")
            $("#verify-bar").css("display", "none")
        }
    })
}

function newWindows(obj) {
    let url = $("#remote_url").val()
    let btn = $(obj)
    let size = (100 / 15).toFixed(2)
    let i = 1
    updateProgress(0)
    btn.addClass("disable")
    w = window.open(url + "?autoplay=1", "b-windows", "width=600,height=400,top=0,left=0,toolbar=no,menubar=no,location=no,status=no")
    const loop = setInterval(function () {
        updateProgress((i * size).toFixed(1))
        i++
        if (w && w.closed) {
            console.log('我被关闭了')
            btn.removeClass("disable")
            clearInterval(loop);
            updateProgress(0)
            w = null
        }
    }, 1000)
}

function updateProgress(progress) {
    /*为啥x0.75，因为div宽度不够长，我也不晓得怎么弄，将就一下*/
    $("#progress").css("width", (progress * 0.75) + "%")
    $("#progress_num").text(progress + "%")
}
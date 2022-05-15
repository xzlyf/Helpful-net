function checkBV() {
    let bvView = $("#i-bv");
    let bv = bvView.val()
    if (!bv) {
        bvView.attr("is-error", "")
        return
    } else {
        bvView.removeAttr("is-error")
    }
    $("#btn-check").addClass("loading")
    $.ajax({
        url: "/taskManager/checkBV?bv=" + bv + "&r=" + Math.random(),
        method: "get",
        success: function (res) {
            if (res.code === -1) {
                alert(res.data)
            } else {
                $("#task-check").html(res);
            }
        },
        error: function () {
            alert("当前与服务器通信失败，请稍后重试！")
        },
        complete: function () {
            $("#btn-check").removeClass("loading")
        }
    })
}

function affirm() {
    $("#btn-affirm").addClass("loading")
    $.ajax({
        url: "/taskManager/affirmBV",
        method: "get",
        success: function (res) {
            if (res.code === -1) {
                alert(res.data)
            } else {
                $("#task-check").html(res);
            }
        },
        error: function () {
            alert("当前与服务器通信失败，请稍后重试！")
        },
        complete: function () {
            $("#btn-affirm").removeClass("loading")
        }
    })
}
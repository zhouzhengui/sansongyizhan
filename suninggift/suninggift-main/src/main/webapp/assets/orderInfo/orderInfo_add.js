/**
 * 添加或者修改页面
 */
var OrderInfoInfoDlg = {
    data: {
        taobaoId: "",
        status: "",
        createTime: "",
        updateTime: "",
        name: "",
        operator: "",
        phone: "",
        totalFee: "",
        transferId: "",
        userId: "",
        itemId: "",
        processStates: "",
        operatorOrderId: "",
        debitOrderId: "",
        contractId: "",
        authNo: "",
        payeeLogonId: "",
        payeeUserId: "",
        freezeMonth: "",
        orderPayAmount: "",
        productId: "",
        outPortNo: "",
        specPsnId: "",
        field1: "",
        field2: "",
        field3: ""
    }
};

layui.use(['form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;

    //让当前iframe弹层高度适应
    admin.iframeAuto();

    //表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/orderInfo/addItem", function (data) {
            Feng.success("添加成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();

        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        return false;
    });

});
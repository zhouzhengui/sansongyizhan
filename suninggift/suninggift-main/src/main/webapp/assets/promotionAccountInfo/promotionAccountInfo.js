layui.use(['table', 'admin', 'ax', 'func'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;

    /**
     * 推广商信息表，收集跟支付宝、微信等平台交互的账户信息管理
     */
    var PromotionAccountInfo = {
        tableId: "promotionAccountInfoTable"
    };

    /**
     * 初始化表格的列
     */
    PromotionAccountInfo.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'accountName', sort: true, title: ''},
            {field: 'accountPid', sort: true, title: ''},
            {field: 'accountNickName', sort: true, title: ''},
            {field: 'appId', sort: true, title: ''},
            {field: 'privateKey', sort: true, title: ''},
            {field: 'publicKey', sort: true, title: ''},
            {field: 'platformPublicKey', sort: true, title: ''},
            {field: 'charset', sort: true, title: ''},
            {field: 'signType', sort: true, title: ''},
            {field: 'createTime', sort: true, title: ''},
            {field: 'updateTime', sort: true, title: ''},
            {field: 'approveName', sort: true, title: ''},
            {field: 'remark', sort: true, title: ''},
            {field: 'field1', sort: true, title: ''},
            {field: 'field2', sort: true, title: ''},
            {field: 'field3', sort: true, title: ''},
            {field: 'status', sort: true, title: '1可以0不可以'},
            {field: 'sysServicePid', sort: true, title: ''},
            {field: 'platformType', sort: true, title: ''},
            {field: 'format', sort: true, title: ''},
            {field: 'royaltPids', sort: true, title: ''},
            {field: 'defaultRoyaltPid', sort: true, title: ''},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    PromotionAccountInfo.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        table.reload(PromotionAccountInfo.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 弹出添加对话框
     */
    PromotionAccountInfo.openAddDlg = function () {
        func.open({
            title: '添加推广商信息表，收集跟支付宝、微信等平台交互的账户信息',
            content: Feng.ctxPath + '/promotionAccountInfo/add',
            tableId: PromotionAccountInfo.tableId
        });
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    PromotionAccountInfo.openEditDlg = function (data) {
        func.open({
            title: '修改推广商信息表，收集跟支付宝、微信等平台交互的账户信息',
            content: Feng.ctxPath + '/promotionAccountInfo/edit?id=' + data.id,
            tableId: PromotionAccountInfo.tableId
        });
    };

    /**
     * 导出excel按钮
     */
    PromotionAccountInfo.exportExcel = function () {
        var checkRows = table.checkStatus(PromotionAccountInfo.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    PromotionAccountInfo.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/promotionAccountInfo/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(PromotionAccountInfo.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + PromotionAccountInfo.tableId,
        url: Feng.ctxPath + '/promotionAccountInfo/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: PromotionAccountInfo.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        PromotionAccountInfo.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        PromotionAccountInfo.openAddDlg();
    });

    // 导出excel
    $('#btnExp').click(function () {
        PromotionAccountInfo.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + PromotionAccountInfo.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            PromotionAccountInfo.openEditDlg(data);
        } else if (layEvent === 'delete') {
            PromotionAccountInfo.onDeleteItem(data);
        }
    });
});

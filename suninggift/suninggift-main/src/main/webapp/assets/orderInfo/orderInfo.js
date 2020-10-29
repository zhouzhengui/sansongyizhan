layui.use(['table', 'admin', 'ax', 'func', 'laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var laydate = layui.laydate;

    /**
     * 订单表管理
     */
    var OrderInfo = {
        tableId: "orderInfoTable"
    };

    layui.use('laytpl', function(){
        var laytpl = layui.laytpl;
        //时间戳的处理
        layui.laytpl.changeStatus = function(status){
            if(status == 0){
                return '代付款';
            }else if(status == 1){
                return '支付成功';
            }else if(status == 2){
                return '合约办理成功';
            }else if(status == 3){
                return '合约办理失败';
            }
        };
    });


    /**
     * 初始化表格的列
     */
    OrderInfo.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'outTradeNo', sort: true, title: '天猫订单号'},
            // {field: 'status', templet:'#statusTpl', sort: true, title: '天猫订单状态'},
            {field: 'status', templet:function (d) {
                    if(d.status == 0){
                        return '代付款';
                    }else if(d.status == 1){
                        return '支付成功';
                    }else if(d.status == 2){
                        return '合约办理成功';
                    }else if(d.status == 3){
                        return '合约办理失败';
                    }else{
                        return '其他状态';
                    }
                }, sort: true, title: '天猫订单状态'},
            // {field: 'status', sort: true, title: '天猫订单状态'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'name', sort: true, title: '订单名称'},
            {field: 'phone', sort: true, title: '手机号'},
            {field: 'totalFee', sort: true, title: '冻结金额'},
            {field: 'processStates',templet:function (d) {
                    switch (d.processStates){
                        case 'init':return '初始化';
                        case 'three_cert_checking':return '运营商三户校验中';
                        case 'dissatisfy_business_handling':return '不满足业务办理';
                        case 'satisfy_business_handling':return '满足业务办理';
                        case 'business_handling':return '业务办理中';
                        case 'business_handling_failed':return '业务办理失败';
                        case 'business_handling_success':return '业务办理成功';
                        case 'debit_create_success':return '调拨创建成功';
                        case 'debit_create_failed':return '调拨创建失败';
                        case 'debit_subscribe_success':return '调拨订阅成功';
                        case 'debit_subscribe_failed':return '调拨订阅失败';
                        case 'debit_loan_success':return '调拨放款成功';
                        case 'debit_loan_failed':return '调拨放款失败';
                        case 'debit_trade_create_success':return '调拨交易创建成功';
                        case 'debit_trade_create_failed':return '调拨交易创建失败';
                        case 'debit_trade_cancel_success':return '调拨交易取消成功';
                        case 'debit_trade_cancel_failed':return '调拨交易取消失败';
                        case 'debit_trade_pay_success':return '调拨交易支付结果成功';
                        case 'debit_trade_pay_failed':return '调拨交易支付结果失败';
                        case 'debit_settle_payables_success':return '调拨放款成功';
                        case 'system_error':return '系统异常';
                    }
                }, sort: true, title: '流程状态'},
            {field: 'freezeMonth', sort: true, title: '冻结期数'},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };



    /**
     * 点击查询按钮
     */
    OrderInfo.search = function () {
        var queryData = {};
        if('' != $("#outTradeNo").val()){
            queryData['outTradeNo'] = $("#outTradeNo").val();
        }
        if('' != $("#status").val()){
            queryData['status'] = $("#status").val();
        }
        if('' != $("#phone").val()){
            queryData['phone'] = $("#phone").val();
        }
        if('' != $("#processStates").val()){
            queryData['processStates'] = $("#processStates").val();
        }
        if('' != $("#startTime").val()){
            queryData['startTime'] = $("#startTime").val();
        }
        if('' != $("#endTime").val()){
            queryData['endTime'] = $("#endTime").val();
        }

        table.reload(OrderInfo.tableId, {
            where: queryData, page: {curr: 1},done: function(){
                this.where={};
            }
        });
    };

    /**
     * 弹出添加对话框
     */
    OrderInfo.openAddDlg = function () {
        func.open({
            title: '添加订单表',
            content: Feng.ctxPath + '/orderInfo/add',
            tableId: OrderInfo.tableId
        });
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    OrderInfo.openEditDlg = function (data) {
        func.open({
            title: '修改订单表',
            content: Feng.ctxPath + '/orderInfo/edit?outTradeNo=' + data.outTradeNo,
            tableId: OrderInfo.tableId
        });
    };

    /**
     * 导出excel按钮
     */
    OrderInfo.exportExcel = function () {
        var checkRows = table.checkStatus(OrderInfo.tableId);
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
    OrderInfo.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/orderInfo/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(OrderInfo.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("outTradeNo", data.outTradeNo);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    //渲染时间选择框
    laydate.render({
        elem: '#startTime',type: 'datetime'
    });

    //渲染时间选择框
    laydate.render({
        elem: '#endTime',type: 'datetime'
    });

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + OrderInfo.tableId,
        url: Feng.ctxPath + '/orderInfo/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: OrderInfo.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        OrderInfo.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        OrderInfo.openAddDlg();
    });

    // 导出excel
    $('#btnExp').click(function () {
        OrderInfo.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + OrderInfo.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            OrderInfo.openEditDlg(data);
        } else if (layEvent === 'delete') {
            OrderInfo.onDeleteItem(data);
        }
    });
});

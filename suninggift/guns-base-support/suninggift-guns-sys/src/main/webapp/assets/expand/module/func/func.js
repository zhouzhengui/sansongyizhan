/**
 * 高频方法集
 *
 * @author fengshuonan
 * @Date 2019/7/29 21:20
 */
layui.define(['jquery', 'layer', 'admin', 'table'], function (exports) {
    var $ = layui.$;
    var layer = layui.layer;
    var admin = layui.admin;
    var table = layui.table;

    var func = {

        /**
         * 获取内部高度，返回数值
         */
        getClientHeight: function () {
            let clientHeight = 0;
            if (document.body.clientHeight && document.documentElement.clientHeight) {
                clientHeight = (document.body.clientHeight < document.documentElement.clientHeight) ? document.body.clientHeight : document.documentElement.clientHeight;
            } else {
                clientHeight = (document.body.clientHeight > document.documentElement.clientHeight) ? document.body.clientHeight : document.documentElement.clientHeight;
            }
            return clientHeight;
        },

        /**
         * 获取内部高度，返回字符串
         */
        getClientHeightPx: function () {
            return Feng.getClientHeight() + 'px';
        },

        /**
         * 打开表单的弹框
         */
        open: function (param) {

            //计算高度
            let clientHeight = func.getClientHeight();
            if (param.height) {
                if (clientHeight < param.height) {
                    param.area = ['1000px', clientHeight + "px"];
                } else {
                    param.area = ['1000px', param.height + "px"];
                }
            } else {
                param.area = ['1000px', clientHeight + "px"];
            }

            param.skin = 'layui-layer-admin';
            param.offset = '35px';
            param.type = 2;

            admin.putTempData('formOk', false);
            param.end = function () {
                layer.closeAll('tips');
                admin.getTempData('formOk') && table.reload(param.tableId);

                if (param.endCallback) {
                    admin.getTempData('formOk') && param.endCallback();
                }

            };
            param.fixed = false;
            param.resize = false;
            param.shade = .1;

            var thisIndex = top.layui.layer.open(param);

            //按键监听esc关闭对话框
            $(window).keydown(function (event) {
                if (event.keyCode === 27) {
                    parent.layer.close(thisIndex)
                }
            });

            return thisIndex;
        }
    };

    exports('func', func);
});
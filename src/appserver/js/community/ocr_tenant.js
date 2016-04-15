Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var dataSex = [['男', '男'], ['女', '女']];
    var storeSex = new Ext.data.SimpleStore({fields: ['value', 'key'], data: dataSex});

    var formPanel = new Ext.form.FormPanel({
        plain: true,
        labelWidth: 100,
        border: false,
        loadMask: {msg: '正在加载数据，请稍后.....'},
        labelAlign: 'right',
        buttonAlign: 'left',
        defaultType: 'textfield',
        defaults: {
            width: 300,
            allowBlank: false,
            blankText: '该项不能为空！'
        },
        items: [
            {name: 'tenant.id', xtype: 'hidden', id: 'tenant.id.info'},
            {fieldLabel: '姓名', name: 'tenant.name'}
            , {
                fieldLabel: '性别', hiddenName: 'tenant.sex',
                xtype: 'combo',
                mode: 'local',
                emptyText: '--请选择--',
                editable: false,
                typeAhead: true,
                forceSelection: true,
                triggerAction: 'all',
                displayField: "key", valueField: "value",
                store: storeSex,
                value: '男'
            }
            , {fieldLabel: '民族', name: 'tenant.mz'}
            , {fieldLabel: '出生日期', name: 'tenant.birth'}
            , {fieldLabel: '住址', name: 'tenant.address'}
            , {fieldLabel: '公民身份号码', name: 'tenant.idCard'}
        ],
        buttons: [new Ext.Toolbar.Spacer({width: 10}), {
            text: '刷新',
            handler: function () {
                if (Ext.getCmp('picPane.info') != undefined) {
                    Ext.getCmp('picPane.info').destroy();
                }
                nextPic();
            }
        }, new Ext.Toolbar.Spacer({width: 10}), {
            id: 'insert.info',
            text: '保存',
            handler: function () {
                if (formPanel.form.isValid()) {
                    formPanel.getForm().submit({
                        url: '../../TenantOCRAction_saveOcr.action',
                        method: 'POST',
                        waitTitle: '系统提示',
                        waitMsg: '正在保存,请稍后...',
                        success: function (form, action) {
                            var msg = action.result.msg;
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 250,
                                msg: msg,
                                animEl: 'insert.info',
                                buttons: {'ok': '确定', 'no': '取消'},
                                icon: Ext.MessageBox.INFO,
                                closable: false,
                                fn: function (e) {
                                    if (e == 'ok') {
                                        Ext.getCmp('picPane.info').destroy();
                                        nextPic();
                                    }
                                }
                            });
                        },
                        failure: function () {
                            var result = Ext.util.JSON.decode(response.responseText);
                            //var success = result.success;
                            var msg = result.msg;
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 250,
                                msg: msg,
                                buttons: {'ok': '确定'},
                                icon: Ext.MessageBox.ERROR,
                                closable: false
                            });
                        }
                    });
                }
            }
        },{
            id: 'next.info',
            text: '下一张',
            handler: function () {
                Ext.Ajax.request({
                    url: '../../TenantOCRAction_nextOcr.action',
                    method: 'POST',
                    params:{id:Ext.getCmp('tenant.id.info').getValue()},
                    waitTitle: '系统提示',
                    waitMsg: '正在处理,请稍后...',
                    success: function (response) {
                        Ext.getCmp('picPane.info').destroy();
                        nextPic();
                    },
                    failure: function () {
                        var result = Ext.util.JSON.decode(response.responseText);
                        //var success = result.success;
                        var msg = result.msg;
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 250,
                            msg: msg,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                });
            }
        }]
    });


    var panel = new Ext.Panel({
        frame: true,
        border: false,
        autoScroll: true,
        layout: 'column',
        items: [{
            width: 500,
            items: [{
                id: 'card.info',
                xtype: 'fieldset',
                title: '身份证照片',
                items: []
            }]
        }, {
            width: 5,
            items: [{
                xtype: 'displayfield',
                width: 10
            }]
        }, {
            width: 500,
            items: [{
                xtype: 'fieldset',
                title: '识别结果',
                items: [formPanel]
            }]
        }]
    });

    var port = new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        items: [panel]
    });
    nextPic();
});




function nextPic() {
    Ext.Ajax.request({
        url: "../../TenantOCRAction_getPic.action",
        success: function (response) {
            var result = Ext.util.JSON.decode(response.responseText);
            var success = result.success;
            var msg = result.msg;
            if (!success) {
                Ext.MessageBox.show({
                    title: '信息',
                    width: 250,
                    msg: msg,
                    buttons: {'ok': '确定'},
                    icon: Ext.MessageBox.WARNING,
                    closable: false
                });
            } else {
                var id = result.id;
                Ext.getCmp('tenant.id.info').setValue(id);
                var picPanel = new Ext.form.FormPanel({
                    id: 'picPane.info',
                    plain: true,
                    labelWidth: 100,
                    border: false,
                    loadMask: {msg: '正在加载数据，请稍后.....'},
                    labelAlign: 'right',
                    buttonAlign: 'left',
                    defaultType: 'textfield',
                    defaults: {
                        width: 200,
                        allowBlank: false,
                        blankText: '该项不能为空！'
                    },
                    items: [
                        {
                            xtype: 'box',//或者xtype: 'component',
                            width: 450, //图片宽度
                            height: 300, //图片高度
                            autoEl: {
                                tag: 'img',    //指定为img标签
                                src: '../TenantOCRAction_loadPic.action?id=' + id   //指定url路径
                            }
                        }
                    ]
                });
                var fieldset = Ext.getCmp('card.info');
                fieldset.add(picPanel);
                fieldset.doLayout();
            }
        }
    });
}



Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    /*var company_point_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id","name"],
            totalProperty: 'totalCount',
            root: 'root'
        })
    });*/

    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'name', mapping: 'name'},
        {name: 'sex', mapping: 'sex'},
        {name: 'idCard', mapping: 'idCard'},
        {name: 'mz', mapping: 'mz'},
        {name: 'birth', mapping: 'birth'},
        {name: 'sign', mapping: 'sign'},
        {name: 'address', mapping: 'address'},
        {name: 'DN', mapping: 'DN'},
        {name: 'validity', mapping: 'validity'},
        {name: 'company', mapping: 'company'},
        {name: 'phone', mapping: 'phone'},
        {name: 'xzz', mapping: 'xzz'},
        {name: 'description', mapping: 'description'},
        {name: 'community_id', mapping: 'community_id'},
        {name: 'community_name', mapping: 'community_name'},
        {name: 'community_address', mapping: 'community_address'},
        {name: 'community_build', mapping: 'community_build'},
        {name: 'community_build_unit', mapping: 'community_build_unit'},
        {name: 'community_ssxq', mapping: 'community_ssxq'},
        {name: 'community_principal', mapping: 'community_principal'},
        {name: 'community_principal_phone', mapping: 'community_principal_phone'},
        {name: 'doorplate', mapping: 'doorplate'},
        {name: 'room', mapping: 'room'},
        {name: 'initDate', mapping: 'initDate'},
        {name: 'lastDate', mapping: 'lastDate'},
        {name: 'status', mapping: 'status'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../CommunityDoorplateAction_findPersonByAccount.action?type=2"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows",
        id: 'id'
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });
//
//    store.load({
//        params: {
//            start: start, limit: pageSize
//        }
//    });
    //var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        //boxM,
        rowNumber,
        {header: "姓名", dataIndex: "name", align: 'center', sortable: true, menuDisabled: true},
        {header: "性别", dataIndex: "sex", align: 'center', sortable: true, menuDisabled: true},
        {header: "身份证", dataIndex: "idCard", align: 'center', sortable: true, menuDisabled: true},
        {header: "小区", dataIndex: "community_name", align: 'center', sortable: true, menuDisabled: true},
        {header: "楼栋单元", dataIndex: "community_build", align: 'center', sortable: true, menuDisabled: true,renderer:show_build_unit},
        {header: "门牌", dataIndex: "doorplate", align: 'center', sortable: true, menuDisabled: true,renderer:show_door_z},
        {header: "房号", dataIndex: "room", align: 'center', sortable: true, menuDisabled: true},
        {header: "电话", dataIndex: "phone", align: 'center', sortable: true, menuDisabled: true},
        {header: "初次采集时间", dataIndex: "initDate", align: 'center', sortable: true, menuDisabled: true},
        {header: "最近采集时间", dataIndex: "lastDate", align: 'center', sortable: true, menuDisabled: true},
        {
            header: '操作标记',
            dataIndex: "flag",
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_flag,
            width: 100
        }
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize: pageSize,
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });

    var company_build_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "value"],
            totalProperty: 'totalCount',
            root: 'root'
        })
    });

    var company_build_unit_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "value"],
            totalProperty: 'totalCount',
            root: 'root'
        })
    });

    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
        plain: true,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        //bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        cm: colM,
        //sm: boxM,
        store: store,
        tbar: [{
            xtype: 'tbspacer',
            width: 10
        },'楼栋', {
            xtype: 'combo',
            mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
            border: true,
            frame: true,
            id: 'company_build_id.tb.info',
            pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
            // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
            editable: false,
            //fieldLabel: '快递公司',
            emptyText: '请选择楼栋',
            //hiddenName : 'user.company.code',
            triggerAction: "all",// 是否开启自动查询功能
            store: company_build_store,// 定义数据源
            valueField: "id", // 关联某一个逻辑列名作为显示值
            displayField: "value", // 关联某一个逻辑列名作为显示值
            //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
            //name: 'user.company.name',
            allowBlank: false,
            blankText: "请选择楼栋",
            listeners: {
                select: function () {
                    var value = this.getValue();
                    company_build_unit_store.proxy = new Ext.data.HttpProxy({
                        url: "../../CommunityBuildUnitAction_find.action?company_build_id=" + value,
                        method: "POST"
                    })
                    company_build_unit_store.load();
                },
                render: function () {
                    company_build_store.proxy = new Ext.data.HttpProxy({
                        url: "../../CommunityBuildAction_findByAccount.action",
                        method: "POST"
                    })
                }
            }
        },{
            xtype: 'tbspacer',
            width: 10
        }, '单元', {
            id: 'company_build_unit_id.tb.info',
            xtype: 'combo',
            mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
            border: true,
            frame: true,
            pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
            // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
            editable: false,
            //fieldLabel: '快递公司',
            emptyText: '请选择单元',
            //hiddenName : 'user.company.code',
            triggerAction: "all",// 是否开启自动查询功能
            store: company_build_unit_store,// 定义数据源
            valueField: "id", // 关联某一个逻辑列名作为显示值
            displayField: "value", // 关联某一个逻辑列名作为显示值
            //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
            //name: 'user.company.name',
            forceSelection:false,
            allowBlank: true,
            blankText: "请选择单元"
        }, {
            xtype: 'tbspacer',
            width: 10
        }, '门牌号', {
            id: 'doorplate.tb.info',
            xtype: 'textfield',
            regex:/^[0-9]*$/,
            regexText:'请输入数字',
            emptyText: '请输入门牌号',
            width: 100
        },{
            xtype: 'tbspacer',
            width: 10
        },'姓名', {
            id: 'name.tb.info',
            xtype: 'textfield',
            emptyText: '请输入姓名',
            width: 100
        },  '身份证', {
            id: 'idCard.tb.info',
            xtype: 'textfield',
            emptyText: '请输入身份证',
            width: 100
        }, {
            xtype: 'tbspacer',
            width: 10
        }, '电话', {
            id: 'phone.tb.info',
            xtype: 'textfield',
            emptyText: '请输入电话',
            width: 100
        }, {
            xtype: 'tbspacer',
            width: 10
        },{
            text: '查询',
            iconCls: 'select',
            listeners: {
                click: function () {

                    var idCard = Ext.fly("idCard.tb.info").dom.value == '请输入身份证'
                        ? null
                        : Ext.getCmp('idCard.tb.info').getValue();

                    var name = Ext.fly("name.tb.info").dom.value == '请输入姓名'
                        ? null
                        : Ext.getCmp('name.tb.info').getValue();


                    /* var community_id = Ext.fly("community_id.tb.info").dom.value == '请输入小区'
                     ? null
                     : Ext.getCmp('community_id.tb.info').getValue();
                     */
                    var phone = Ext.fly("phone.tb.info").dom.value == '请输入电话'
                        ? null
                        : Ext.getCmp('phone.tb.info').getValue();

                    var company_build_id = Ext.fly("company_build_id.tb.info").dom.value == '请选择楼栋'
                        ? null
                        : Ext.getCmp('company_build_id.tb.info').getValue();

                    var company_build_unit_id = Ext.fly("company_build_unit_id.tb.info").dom.value == '请选择单元'
                        ? null
                        : Ext.getCmp('company_build_unit_id.tb.info').getValue();

                    var doorplate = Ext.fly("doorplate.tb.info").dom.value == '请输入门牌号'
                        ? null
                        : Ext.getCmp('doorplate.tb.info').getValue();

                    store.setBaseParam('company_build_id', company_build_id);
                    store.setBaseParam('company_build_unit_id', company_build_unit_id);
                    store.setBaseParam('doorplate', doorplate);

                    store.setBaseParam('idCard', idCard);
                    store.setBaseParam('name', name);
                    //store.setBaseParam('community_id', community_id);
                    store.setBaseParam('phone', phone);
                    store.load({
                        params: {
                            start: start,
                            limit: pageSize
                        }
                    });
                }
            }
        }],
        bbar: page_toolbar,
        //title: '资源配置',
        columnLines: true,
        autoScroll: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        height: 300,
        iconCls: 'icon-grid'
    });

    var port = new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        items: [grid_panel]
    });

});

function show_build_z(value){
    return value + '幢';
}
function show_unit_z(value){
    return value + '单元';
}
function show_door_z(value){
    return value + '室';
}

function show_build_unit(value,o,r) {
    return value + '幢' + r.get('community_build_unit') + '单元';
}

function show_attention(value) {
    if (value == '1') {
        return '<span style="color:green;">普通</span>';
    } else if(value == '2') {
        return '<span style="color:yellow;">警戒</span>';
    } else if(value == '3') {
        return '<span style="color:red;">高危</span>';
    } else {
        return '<span style="color:green;">普通</span>';
    }
}

function show_flag() {
    return String.format(
        '<a id="info_express.info" href="javascript:void(0);" onclick="viewInfo();return false;" style="color: green;">详细</a>&nbsp;&nbsp;&nbsp;&nbsp;'+
        '<a id="delete_express.info" href="javascript:void(0);" onclick="delete_rule();return false;" style="color: green;">删除</a>'

    );
}

function delete_rule() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确定删除这条记录？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: "../../UserAction_remove.action",
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {
                        id: recode.get("id")
                    },
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid_panel.getStore().reload();
                    },
                    failure: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}

function viewInfo() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        baseCls: 'x-plain',
        labelWidth: 180,
        labelAlign: 'right',
        layout: 'form',
        border: false,
        defaults: {
            anchor: "95%"
        },
        items: [
            new Ext.form.DisplayField({
                fieldLabel: '姓名',
                value: recode.get("name")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '性别',
                value: recode.get("sex")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '生日',
                value: recode.get("birth")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '身份证号码',
                value: recode.get("idCard")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '身份证地址',
                value: recode.get("address")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '签发机构',
                value: recode.get("sign")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '有效期',
                value: recode.get("validity")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '小区',
                value: recode.get("community_name")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '小区地址',
                value: recode.get("community_address")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '楼栋单元',
                value: recode.get("community_build")+'幢'+recode.get("community_build_unit")+'单元'
            }),
            new Ext.form.DisplayField({
                fieldLabel: '门牌房号',
                value: recode.get('doorplate') + '室-' + recode.get("room")
            }),

            new Ext.form.DisplayField({
                fieldLabel: '电话',
                value: recode.get("phone")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '初次采集时间',
                value: recode.get("initDate")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '最近采集时间',
                value: recode.get("lastDate")
            })
        ]
    });

    var select_Win = new Ext.Window({
        title: "用户详细",
        width: 650,
        layout: 'fit',
        height: 500,
        modal: true,
        items: formPanel
    });
    select_Win.show();
}

function add_win(grid, store) {

  /*  var community_point_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id","name"],
            totalProperty: 'totalCount',
            root: 'root'
        })
    });*/

    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        labelWidth: 120,
        labelAlign: 'right',
        defaultWidth: 300,
        autoWidth: true,
        layout: 'form',
        border: false,
        defaults: {
            anchor: '90%',
            allowBlank: false,
            xtype: 'textfield',
            blankText: '该项不能为空！'
        },
        items: [{
            id: 'phone.insert.info',
            fieldLabel: "手机号",
            name: 'user.phone',
            regex: /^.{2,30}$/,
            regexText: '请输入任意2--30个字符',
            emptyText: '请输入任意2--30个字符',
            listeners: {
                blur: function () {
                    var phone = this.getValue();
                    if (phone.length > 0) {
                        var myMask = new Ext.LoadMask(Ext.getBody(), {
                            msg: '正在校验,请稍后...',
                            removeMask: true
                        });
                        myMask.show();
                        Ext.Ajax.request({
                            url: '../../UserAction_check.action',
                            params: {phone: phone},
                            method: 'POST',
                            success: function (r, o) {
                                myMask.hide();
                                var respText = Ext.util.JSON.decode(r.responseText);
                                if (respText.success != true) {
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        width: 250,
                                        msg: respText.msg,
                                        buttons: {'ok': '确定'},
                                        icon: Ext.MessageBox.INFO,
                                        closable: false,
                                        fn: function (e) {
                                            if (e == 'ok') {
                                                Ext.getCmp('phone.insert.info').setValue('');
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        }, {
            id: 'password.info',
            fieldLabel: "密码",
            name: 'user.password',
            inputType: 'password',
            regex: /^.{6,20}$/,
            regexText: '密码规则:6~20位!',
            emptyText: '请输入密码!'
        }, {
            id: 'password2.info',
            fieldLabel: "确认密码",
            inputType: 'password',
            regex: /^.{6,20}$/,
            regexText: '密码规则:6~20位!',
            emptyText: '请输入密码!',
            listeners: {
                blur: function () {
                    var password = Ext.getCmp('password.info').getValue();
                    if (password.length > 0) {
                        var password2 = this.getValue();
                        if (password != password2 && password2.length > 0) {
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 250,
                                msg: '<font color="red">"确认密码"和"密码"不一致!</font>',
                                animEl: 'password2.info',
                                buttons: {'ok': '确定'},
                                icon: Ext.MessageBox.INFO,
                                closable: false,
                                fn: function (e) {
                                    if (e == 'ok') {
                                        Ext.getCmp('password2.info').setValue('');
                                    }
                                }
                            });
                        }
                    } else {
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 270,
                            msg: '<font color="red">请先输入"密码",再输入"确认密码"!</font>',
                            animEl: 'password2.info',
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.INFO,
                            closable: false,
                            fn: function (e) {
                                if (e == 'ok') {
                                    Ext.getCmp('password2.info').setValue('');
                                }
                            }
                        });
                    }
                }
            }
        }, {
            fieldLabel: "真实姓名",
            name: 'user.name',
            regex: /^.{2,30}$/,
            regexText: '请输入任意2--30个字符',
            emptyText: '请输入任意2--30个字符'
        }, {
            fieldLabel: "身份证号码",
            name: 'user.idCard',
            emptyText: '请输入身份证号码',
            regex : /^(\d{18,18}|\d{15,15}|\d{17,17}x)$/,
            regexText : '输入正确的身份号码'
        },/* new Ext.form.ComboBox({
            mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
            border: true,
            frame: true,
            pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
            // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
            editable: false,
            fieldLabel: '小区',
            emptyText: '请选择小区',
            hiddenName : 'user.community.id',
            triggerAction: "all",// 是否开启自动查询功能
            store: community_point_store,// 定义数据源
            valueField: "id", // 关联某一个逻辑列名作为显示值
            displayField: "name", // 关联某一个逻辑列名作为显示值
            //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
            //name: 'user.company.name',
            allowBlank: false,
            blankText: "请选择小区",
            listeners: {
                render: function () {
                    community_point_store.proxy = new Ext.data.HttpProxy({
                        url: '../../CommunityAction_find.action',
                        method: "POST"
                    })
                }
            }
        }),*/ {
            fieldLabel: "工号",
            name: 'user.number',
            emptyText: '请输入工号'
        }]
    });
    var win = new Ext.Window({
        title: "注册信息",
        width: 650,
        layout: 'fit',
        height: 390,
        modal: true,
        items: formPanel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '注册',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../../UserAction_insertByAccount.action',
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在保存,请稍后...',
                            success: function (form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    buttons: {'ok': '确定', 'no': '取消'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn: function (e) {
                                        if (e == 'ok') {
                                            grid.render();
                                            store.reload();
                                            win.close();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 200,
                            msg: '请填写完成再提交!',
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            }, {
                text: '关闭',
                handler: function () {
                    win.close();
                }
            }
        ]
    }).show();
}

function add_win_batch(grid, store) {
    alert("add_win_batch");
}


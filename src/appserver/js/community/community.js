Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var toolbar = new Ext.Toolbar({
        plain: true,
        height: 30,
        items: ['小区名', {
            id: 'name.tb.info',
            xtype: 'textfield',
            emptyText: '请输入小区名',
            width: 100
        }, {
            xtype: 'tbspacer',
            width: 10
        }, '小区地址', {
            id: 'address.tb.info',
            xtype: 'textfield',
            emptyText: '请输入小区地址',
            width: 100
        }, {
            xtype: 'tbspacer',
            width: 10
        }, {
            text: '查询',
            iconCls: 'select',
            listeners: {
                click: function () {

                    var name = Ext.fly("name.tb.info").dom.value == '请输入小区名'
                        ? null
                        : Ext.getCmp('name.tb.info').getValue();

                    var address = Ext.fly("address.tb.info").dom.value == '请输入小区地址'
                        ? null
                        : Ext.getCmp('address.tb.info').getValue();

                    store.setBaseParam('name', name);
                    store.setBaseParam('address', address);
                    store.load({
                        params: {
                            start: start,
                            limit: pageSize
                        }
                    });
                }
            }
        }]
    });
    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'name', mapping: 'name'},
        {name: 'principal', mapping: 'principal'},
        {name: 'principal_phone', mapping: 'principal_phone'},
        {name: 'logo', mapping: 'logo'},
        {name: 'ssxq', mapping: 'ssxq'},
        {name: 'buildCount', mapping: 'buildCount'},
        {name: 'unitCount', mapping: 'unitCount'},
        {name: 'doorplateCount', mapping: 'doorplateCount'},
        {name: 'roomCount', mapping: 'roomCount'},
        {name: 'czCount', mapping: 'czCount'},
        {name: 'zzCount', mapping: 'zzCount'},
        {name: 'tenantCount', mapping: 'tenantCount'},
        {name: 'address', mapping: 'address'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../CommunityAction_find.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root",
        id: 'id'
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });

    store.load({
        params: {
            start: start, limit: pageSize
        }
    });
    //var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        //boxM,
        rowNumber,
        {header: "小区名", dataIndex: "name", align: 'center', sortable: true, menuDisabled: true},
        {header: "省市县区", dataIndex: "ssxq", align: 'center', sortable: true, menuDisabled: true},
        {header: "详细地址", dataIndex: "address", align: 'center', sortable: true, menuDisabled: true},
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
    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
        plain: true,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        cm: colM,
        //sm: boxM,
        store: store,
        tbar: [new Ext.Button({
            id: 'add.info',
            text: '新增',
            iconCls: 'add',
            handler: function () {
                add_win(grid_panel, store);     //连接到 新增 面板
            }
        })],
        listeners: {
            render: function () {
                toolbar.render(this.tbar);
            }
        },
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
        frame: true,
        iconCls: 'icon-grid'
    });

    var port = new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        items: [grid_panel]
    });

});

function show_flag() {
    return String.format(
        '<a id="logo.info" href="javascript:void(0);" onclick="logo();return false;" style="color: green;">图标</a>&nbsp;&nbsp;&nbsp;' +
        '<a id="delete.info" href="javascript:void(0);" onclick="delete_rule();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;' +
        '<a id="info.info" href="javascript:void(0);" onclick="viewInfo();return false;" style="color: green;">详细</a>&nbsp;&nbsp;&nbsp;'
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
                    url: "../../CommunityAction_remove.action",
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
    var logo = recode.get("logo");
    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        baseCls: 'x-plain',
        labelWidth: 120,
        labelAlign: 'right',
        layout: 'form',
        border: false,
        defaults: {
            anchor: "95%"
        },
        items: [
            new Ext.form.DisplayField({
                fieldLabel: '小区名',
                value: recode.get("name")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '省市县区',
                value: recode.get("ssxq")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '小区地址',
                value: recode.get("address")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '联系人',
                value: recode.get("principal")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '联系人电话',
                value: recode.get("principal_phone")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '联系人',
                value: recode.get("principal")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '楼栋数',
                value: recode.get("buildCount")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '单元数',
                value: recode.get("unitCount")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '门牌数',
                value: recode.get("doorplateCount")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '房间数',
                value: recode.get("roomCount")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '出租户',
                value: recode.get("czCount")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '自住户',
                value: recode.get("zzCount")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '租客数',
                value: recode.get("tenantCount")
            })
        ]
    });

    var select_Win = new Ext.Window({
        title: "详细",
        width: 500,
        layout: 'fit',
        height: 200,
        modal: true,
        items: formPanel
    });
    select_Win.show();
}

function add_win(grid, store) {
    var province_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "code", "name"],
            totalProperty: 'totalCount',
            root: 'root'
        })
    });

    var city_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "code", "name"],
            totalProperty: 'totalCount',
            root: 'root'
        })
    });

    var district_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "code", "name"],
            totalProperty: 'totalCount',
            root: 'root'
        })
    });
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
            fieldLabel: "小区名",
            name: 'community.name',
            emptyText: '请输入小区名称'
        }, /*{
         fieldLabel: "省市县区",
         name: 'community.ssxq',
         emptyText: '请输入省市县区'
         },*/ new Ext.form.ComboBox({
            mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
            border: true,
            frame: true,
            pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
            // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
            editable: false,
            fieldLabel: '省',
            emptyText: '请选择省份',
            triggerAction: "all",// 是否开启自动查询功能
            store: province_store,// 定义数据源
            valueField: "code", // 关联某一个逻辑列名作为显示值
            displayField: "name", // 关联某一个逻辑列名作为显示值
            //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
            //name: 'user.company.name',
            allowBlank: false,
            blankText: "请选择省份",
            listeners: {
                select: function () {
                    var value = this.getValue();
                    city_store.proxy = new Ext.data.HttpProxy({
                        url: "../../RegionAction_cityByProvince.action?parentCode=" + value,
                        method: "POST"
                    })
                    city_store.load();
                },
                render: function () {
                    province_store.proxy = new Ext.data.HttpProxy({
                        url: '../../RegionAction_province.action',
                        method: "POST"
                    })
                }
            }
        }), new Ext.form.ComboBox({
            xtype: 'combo',
            mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
            border: true,
            frame: true,
            fieldLabel: '市',
            pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
            // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
            editable: false,
            //fieldLabel: '快递公司',
            emptyText: '请选择所在市区',
            triggerAction: "all",// 是否开启自动查询功能
            store: city_store,// 定义数据源
            valueField: "code", // 关联某一个逻辑列名作为显示值
            displayField: "name", // 关联某一个逻辑列名作为显示值
            //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
            //name: 'user.company.name',
            allowBlank: false,
            blankText: "请选择小区楼栋",
            listeners: {
                select: function () {
                    var value = this.getValue();
                    district_store.proxy = new Ext.data.HttpProxy({
                        url: "../../RegionAction_districtByCity.action?parentCode=" + value,
                        method: "POST"
                    })
                    district_store.load();
                }
            }
        }),
            new Ext.form.ComboBox({
                xtype: 'combo',
                mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                border: true,
                frame: true,
                fieldLabel: '区',
                pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                editable: false,
                //fieldLabel: '快递公司',
                emptyText: '请选择所在区县',
                triggerAction: "all",// 是否开启自动查询功能
                store: district_store,// 定义数据源
                valueField: "code", // 关联某一个逻辑列名作为显示值
                displayField: "name", // 关联某一个逻辑列名作为显示值
                hiddenName:'community.region.code',
                //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                //name: 'user.company.name',
                allowBlank: false,
                blankText: "请选择小区楼栋"
            }), {
                fieldLabel: "小区地址",
                name: 'community.address',
                emptyText: '请输入小区详细地址'
            }, {
                fieldLabel: "联系人",
                name: 'community.principal',
                emptyText: '请输入联系人'
            }, {
                fieldLabel: "联系人电话",
                name: 'community.principal_phone',
                emptyText: '请输入联系人电话'
            }]
    });
    var win = new Ext.Window({
        title: "新增",
        width: 500,
        layout: 'fit',
        height: 280,
        modal: true,
        items: formPanel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../../CommunityAction_insert.action',
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

function clickHandler(url) {
    var win = new Ext.Window({
        width: 650,
        layout: 'fit',
        height: 450,
        modal: true,
        maximizable: true,
        minimizable: true,
        items: [
            {
                xtype: 'box',//或者xtype: 'component',
                width: 650, //图片宽度
//                height: 300, //图片高度
                autoEl: {
                    tag: 'img',    //指定为img标签
                    src: url   //指定url路径
                }
            }
        ]
    }).show();
}

function logo() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        var sender_url = '../../CommunityAction_loadLogo.action?id=' + recode.get("id");
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            autoScroll: true,
            labelWidth: 150,
            labelAlign: 'right',
            defaultWidth: 300,
            autoWidth: true,
            fileUpload: true,
            layout: 'form',
            border: false,
            defaults: {
                width: 250,
                allowBlank: false,
                blankText: '该项不能为空！'
            },
            items: [
                {
                    xtype: 'box',//或者xtype: 'component',
                    fieldLabel: '原图标',
                    width: 100, //图片宽度
                    //height: 200, //图片高度
                    autoEl: {
                        tag: 'img',    //指定为img标签
                        src: sender_url, //指定url路径
                        onclick: 'clickHandler(\'' + sender_url + '\')'
                    }
                },
                {
                    id: 'uploadFile',
                    fieldLabel: '新图标',
                    xtype: 'textfield',
                    inputType: 'file',
                    editable: false,
                    allowBlank: true
                }
            ]
        });
        var win = new Ext.Window({
            title: "图标",
            width: 500,
            layout: 'fit',
            height: 250,
            modal: true,
            items: formPanel,
            bbar: [
                '->',
                {
                    id: 'insert_win.info',
                    text: '修改',
                    handler: function () {
                        var myMask = new Ext.LoadMask(Ext.getBody(), {
                            msg: '正在修改,请稍后...',
                            removeMask: true
                        });
                        myMask.show();
                        if (formPanel.form.isValid()) {
                            formPanel.getForm().submit({
                                url: '../../CommunityAction_modify_logo.action',
                                timeout: 20 * 60 * 1000,
                                params: {id: recode.get("id")},
                                method: 'POST',
                                success: function (form, action) {
                                    var msg = action.result.msg;
                                    myMask.hide();
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        width: 500,
                                        msg: msg,
                                        buttons: Ext.MessageBox.OK,
                                        buttons: {'ok': '确定'},
                                        icon: Ext.MessageBox.OK,
                                        closable: true
                                    });
                                },
                                failure: function (form, action) {
                                    var msg = action.result.msg;
                                    myMask.hide();
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        width: 500,
                                        msg: msg,
                                        buttons: Ext.MessageBox.OK,
                                        buttons: {'ok': '确定'},
                                        icon: Ext.MessageBox.ERROR,
                                        closable: false
                                    });
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 250,
                                msg: '请填写完成再提交!',
                                buttons: Ext.MessageBox.OK,
                                buttons: {'ok': '确定'},
                                icon: Ext.MessageBox.ERROR,
                                closable: false
                            });
                        }
                    }
                }, {
                    text: '重置',
                    handler: function () {
                        formPanel.getForm().reset();
                    }
                }
            ]
        }).show();
    }
};






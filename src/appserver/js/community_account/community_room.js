Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var company_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "name"],
            totalProperty: 'totalCount',
            root: 'root'
        })
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

    var company_doorplate_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "value"],
            totalProperty: 'totalCount',
            root: 'root'
        })
    });

    var start = 0;
    var pageSize = 15;
    var toolbar = new Ext.Toolbar({
        plain: true,
        height: 30,
        items: [
            '楼栋', {
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
            }, '小区单元', {
                id: 'company_build_unit_id.tb.info',
                xtype: 'combo',
                mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                border: true,
                frame: true,
                pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                editable: false,
                //fieldLabel: '快递公司',
                emptyText: '请选择小区单元',
                //hiddenName : 'user.company.code',
                triggerAction: "all",// 是否开启自动查询功能
                store: company_build_unit_store,// 定义数据源
                valueField: "id", // 关联某一个逻辑列名作为显示值
                displayField: "value", // 关联某一个逻辑列名作为显示值
                //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                //name: 'user.company.name',
                allowBlank: false,
                blankText: "请选择小区单元",
                listeners: {
                    select: function () {
                        var value = this.getValue();
                        company_doorplate_store.proxy = new Ext.data.HttpProxy({
                            url: "../../CommunityDoorplateAction_find.action?company_build_unit_id=" + value,
                            method: "POST"
                        })
                        company_doorplate_store.load();
                    }
                }
            }, '小区门牌', {
                id: 'company_doorplate_id.tb.info',
                xtype: 'combo',
                mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                border: true,
                frame: true,
                pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                editable: false,
                //fieldLabel: '快递公司',
                emptyText: '请选择小区门牌',
                //hiddenName : 'user.company.code',
                triggerAction: "all",// 是否开启自动查询功能
                store: company_doorplate_store,// 定义数据源
                valueField: "id", // 关联某一个逻辑列名作为显示值
                displayField: "value", // 关联某一个逻辑列名作为显示值
                //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                //name: 'user.company.name',
                allowBlank: false,
                blankText: "请选择小区门牌"
            }, {
                xtype: 'tbspacer',
                width: 10
            }, '房间号', {
                id: 'room.tb.info',
                xtype: 'textfield',
                emptyText: '请输入房间号',
                width: 100
            }, {
                xtype: 'tbspacer',
                width: 10
            }, {
                text: '查询',
                iconCls: 'select',
                listeners: {
                    click: function () {

                        var community = Ext.fly("community.tb.info").dom.value == '请选择小区'
                            ? null
                            : Ext.getCmp('community.tb.info').getValue();

                        var company_build_id = Ext.fly("company_build_id.tb.info").dom.value == '请选择小区楼栋'
                            ? null
                            : Ext.getCmp('company_build_id.tb.info').getValue();

                        var company_build_unit_id = Ext.fly("company_build_unit_id.tb.info").dom.value == '请选择小区单元'
                            ? null
                            : Ext.getCmp('company_build_unit_id.tb.info').getValue();

                        var company_doorplate_id = Ext.fly("company_doorplate_id.tb.info").dom.value == '请输入门牌号'
                            ? null
                            : Ext.getCmp('company_doorplate_id.tb.info').getValue();

                        var room = Ext.fly("room.tb.info").dom.value == '请输入门房号'
                            ? null
                            : Ext.getCmp('room.tb.info').getValue();

                        store.setBaseParam('community_id', community);
                        store.setBaseParam('company_build_id', company_build_id);
                        store.setBaseParam('company_build_unit_id', company_build_unit_id);
                        store.setBaseParam('company_doorplate_id', company_doorplate_id);
                        store.setBaseParam('room', room);
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
        {name: 'community_id', mapping: 'community_id'},
        {name: 'community_name', mapping: 'community_name'},
        {name: 'community_address', mapping: 'community_address'},
        {name: 'community_build', mapping: 'community_build'},
        {name: 'community_build_unit', mapping: 'community_build_unit'},
        {name: 'community_ssxq', mapping: 'community_ssxq'},
        {name: 'community_principal', mapping: 'community_principal'},
        {name: 'community_principal_phone', mapping: 'community_principal_phone'},
        {name: 'status', mapping: 'status'},
        {name: 'room', mapping: 'room'},
        {name: 'tenant_ids', mapping: 'tenant_ids'},
        {name: 'tenantCount', mapping: 'tenantCount'},
        {name: 'doorplate', mapping: 'doorplate'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../CommunityRoomAction_find.action"
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
        {header: "小区名", dataIndex: "community_name", align: 'center', sortable: true, menuDisabled: true},
        {header: "楼栋", dataIndex: "community_build", align: 'center', sortable: true, menuDisabled: true},
        {header: "单元", dataIndex: "community_build_unit", align: 'center', sortable: true, menuDisabled: true},
        {header: "门牌号", dataIndex: "doorplate", align: 'center', sortable: true, menuDisabled: true},
        {header: "房间号", dataIndex: "room", align: 'center', sortable: true, menuDisabled: true},
        {header: "类型", dataIndex: "status", align: 'center', sortable: true, menuDisabled: true, renderer: show_status},
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

function show_status(value) {
    if (value == "1") {
        return String.format('<span style="color:red;">出租房</span>');
    } else {
        return String.format('<span style="color:green;">自住房</span>');
    }
}

function show_flag(value,o,r) {
    var tenant_ids = r.get("tenant_ids");
    return String.format(
        '<a id="qrcode.info" href="javascript:void(0);" onclick="qrcode();return false;" style="color: green;">二维码</a>&nbsp;&nbsp;&nbsp;' +
        '<a id="tenant.info" href="javascript:void(0);" onclick="tenant(\''+tenant_ids+'\');return false;" style="color: green;">租客</a>&nbsp;&nbsp;&nbsp;' +
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
                    url: "../../CommunityRoomAction_remove.action",
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
        labelWidth: 150,
        labelAlign: 'right',
        layout: 'form',
        border: false,
        defaults: {
            anchor: "95%"
        },
        items: [
            new Ext.form.DisplayField({
                fieldLabel: '小区名',
                value: recode.get("community_name")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '省市县区',
                value: recode.get("community_ssxq")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '小区地址',
                value: recode.get("community_address")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '楼栋号',
                value: recode.get("community_build")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '单元号',
                value: recode.get("community_build_unit")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '门牌号',
                value: recode.get("doorplate")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '房间号',
                value: recode.get("room")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '小区联系人',
                value: recode.get("community_principal")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '小区联系人电话',
                value: recode.get("community_principal_phone")
            }), new Ext.form.DisplayField({
                fieldLabel: '租客数',
                value: recode.get("tenantCount")
            })
        ]
    });

    var select_Win = new Ext.Window({
        title: "详细",
        width: 500,
        layout: 'fit',
        height: 300,
        modal: true,
        items: formPanel
    });
    select_Win.show();
}

function add_win(grid, store) {

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

    var company_doorplate_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "doorplate"],
            totalProperty: 'total',
            root: 'rows'
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
        items: [
            {
                id:'build.info',
                xtype: 'combo',
                mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                border: true,
                frame: true,
                fieldLabel: "楼栋",
                pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                editable: false,
                //fieldLabel: '快递公司',
                emptyText: '请选择小区楼栋',
                hiddenName : 'communityDoorplate.communityBuildUnit.communityBuild.id',
                //hiddenName : 'user.company.code',
                triggerAction: "all",// 是否开启自动查询功能
                store: company_build_store,// 定义数据源
                valueField: "id", // 关联某一个逻辑列名作为显示值
                displayField: "value", // 关联某一个逻辑列名作为显示值
                //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                //name: 'user.company.name',
                allowBlank: false,
                blankText: "请选择小区楼栋",
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
            }, {
                xtype: 'combo',
                mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                border: true,
                fieldLabel: "单元",
                frame: true,
                pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                editable: false,
                //fieldLabel: '快递公司',
                emptyText: '请选择小区单元',
                hiddenName: 'communityDoorplate.communityBuildUnit.id',
                triggerAction: "all",// 是否开启自动查询功能
                store: company_build_unit_store,// 定义数据源
                valueField: "id", // 关联某一个逻辑列名作为显示值
                displayField: "value", // 关联某一个逻辑列名作为显示值
                //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                //name: 'user.company.name',
                allowBlank: false,
                blankText: "请选择小区单元",
                listeners: {
                    select: function () {
                        var value = this.getValue();
                        company_doorplate_store.proxy = new Ext.data.HttpProxy({
                            url: "../../CommunityDoorplateAction_find.action?company_build_unit_id=" + value,
                            method: "POST"
                        })
                        company_doorplate_store.load();
                    }
                }
            }, {
                xtype: 'combo',
                fieldLabel: "门牌号",
                mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                border: true,
                frame: true,
                pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                editable: false,
                //fieldLabel: '快递公司',
                emptyText: '请选择小区门牌',
                hiddenName: 'communityRoom.communityDoorplate.id',
                //hiddenName : 'user.company.code',
                triggerAction: "all",// 是否开启自动查询功能
                store: company_doorplate_store,// 定义数据源
                valueField: "id", // 关联某一个逻辑列名作为显示值
                displayField: "doorplate", // 关联某一个逻辑列名作为显示值
                //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                //name: 'user.company.name',
                allowBlank: false,
                blankText: "请选择小区门牌"
            }, {
                fieldLabel: "房间号",
                name: 'communityRoom.room',
                emptyText: '请输入门牌'
            }]
    });
    var win = new Ext.Window({
        title: "新增",
        width: 500,
        layout: 'fit',
        height: 220,
        modal: true,
        items: formPanel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '新增',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../../CommunityRoomAction_insert.action',
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

function tenant(ids) {
    var lType = "租客";
    if(ids==undefined||ids.length==0){
        Ext.MessageBox.show({
            title:'信息',
            width:250,
            msg:'没有人员',
            buttons:{'ok':'确定'},
            icon:Ext.MessageBox.WARNING,
            closable:false
        });

    } else {
        var panel = new Ext.Panel({
            frame:true,
            border:false,
            width:600,
            autoScroll:true,
            layout:'form',
            items:[]
        });
        var win = new Ext.Window({
            title: "租客信息",
            width: 630,
            layout: 'fit',
            height: 430,
            items: [panel]
        }).show();
        Ext.Ajax.request({
            url: '../../TenantAction_findByIds.action?ids=' + ids,
            success: function(response){
                var result = Ext.util.JSON.decode(response.responseText);
                var total = result.total;
                var idx = 0;
                for(var i = 0; i < total; i ++) {
                    idx ++;
                    var row = result.rows[i];
                    var userPanel = new Ext.Panel({
                        id:'userPanel-' + row.id,
                        plain:true,
                        layout:'column',
                        items:[{
                            columnWidth:.7,
                            id:'userPanel-1-' + row.id,
                            plain:true,
                            labelWidth:100,
                            xtype:'form',
                            border:true,
                            loadMask : { msg : '正在加载数据，请稍后.....' },
                            labelAlign:'right',
                            buttonAlign:'left',
                            defaultType:'displayfield',
                            defaults : {
                                width : 200,
                                allowBlank : false,
                                blankText : '该项不能为空！'
                            },
                            items:[]
                        },{
                            columnWidth:.2,
                            id:'userPanel-2-' + row.id,
                            xtype: 'box',
                            width:200,
                            autoEl: {
                                tag: 'img',    //指定为img标签
                                src: "../../TenantAction_loadTenantHead.action?id=" + row.id       //指定url路径
                            }
                        },{
                            columnWidth:.1,
                            id:'userPanel-3-' + row.id
                        }]

                    });
                    var userPanel2 = new Ext.form.FormPanel({
                        id:'userPanel-4-' + row.id,
                        plain:true,
                        labelWidth:100,
                        border:true,
                        loadMask : { msg : '正在加载数据，请稍后.....' },
                        labelAlign:'right',
                        buttonAlign:'left',
                        defaultType:'displayfield',
                        defaults : {
                            width : 200,
                            allowBlank : false,
                            blankText : '该项不能为空！'
                        },
                        items:[]
                    });
                    var userPanelsun = Ext.getCmp('userPanel-1-' + row.id);
                    userPanelsun.add(new Ext.form.DisplayField({ id:'name.info' + row.id , fieldLabel:'姓名' ,value:row.name}));
                    userPanelsun.add(new Ext.form.DisplayField({ id:'sex.info' + row.id , fieldLabel:'性别',value:row.sex }));
                    userPanelsun.add(new Ext.form.DisplayField({ id:'mz.info' + row.id , fieldLabel:'民族',value:row.mz }));
                    userPanelsun.add(new Ext.form.DisplayField({ id:'birth.info' + row.id , fieldLabel:'出生日期',value:row.birth }));
                    userPanelsun.add(new Ext.form.DisplayField({ id:'idCard.info' + row.id , fieldLabel:'公民身份号码',value:row.idCard }));
                    userPanel2.add(new Ext.form.DisplayField({ id:'address.info' + row.id , fieldLabel:'身份证地址',value: row.address }));
                    userPanel2.add(new Ext.form.DisplayField({ id:'sign.info' + row.id , fieldLabel:'签发机构',value: row.sign }));
                    userPanel2.add(new Ext.form.DisplayField({ id:'validity.info' + row.id , fieldLabel:'有效期',value: row.validity }));
                    userPanel2.add(new Ext.form.DisplayField({ id:'phone.info' + row.id , fieldLabel:'联系电话',value: row.phone }));
                    userPanel2.add(new Ext.form.DisplayField({ id:'fh.info' + row.id , fieldLabel:'房号',value: row.room }));/*
                     userPanelsun.add(new Ext.form.DisplayField({ id:'type.info' + row.id , fieldLabel:'类型',value:show_status_type(row.status) }));*/
                    userPanelsun.doLayout();
                    var fieldSet = new Ext.form.FieldSet({
                        id:'fieldSet-' + row.id,
                        title: lType + idx,
                        border: false,
                        autoScroll: true,
                        items: []
                    });
                    userPanel.doLayout();
                    userPanel2.doLayout();

                    fieldSet.add(userPanel);
                    fieldSet.add(userPanel2);
                    fieldSet.doLayout();
                    panel.add(fieldSet);
                    panel.doLayout();
                }
            }
        });
    }
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

function qrcode() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        var url = "../../CommunityRoomAction_qrcode.action?id="+recode.get("id");
        new Ext.Window({
            title: "二维码",
            width: 300,
            layout: 'fit',
            height: 300,
            modal: true,
            items: [
                {
                    xtype: 'box',//或者xtype: 'component',
                    fieldLabel: '二维码',
                    width: 300, //图片宽度
                    height: 300, //图片高度
                    autoEl: {
                        tag: 'img',    //指定为img标签
                        src: url, //指定url路径
                        onclick: 'clickHandler(\'' + url + '\')'
                    }
                }
            ]
        }).show();
    }
}






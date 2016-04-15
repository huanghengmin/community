Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var company_point_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "name"],
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
            new Ext.form.ComboBox({
                mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                border: true,
                frame: true,
                pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                editable: false,
                fieldLabel: '小区',
                emptyText: '请选择小区',
                id: 'community.tb.info',
                triggerAction: "all",// 是否开启自动查询功能
                store: company_point_store,// 定义数据源
                valueField: "id", // 关联某一个逻辑列名作为显示值
                displayField: "name", // 关联某一个逻辑列名作为显示值
                //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                //name: 'user.company.name',
                allowBlank: false,
                blankText: "请选择小区",
                listeners: {
                    render: function () {
                        company_point_store.proxy = new Ext.data.HttpProxy({
                            url: '../../CommunityAction_find.action',
                            method: "POST"
                        })
                    }
                }
            }), {
                xtype: 'tbspacer',
                width: 10
            }, '小区楼栋', {
                id: 'value.tb.info',
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

                        var community = Ext.fly("community.tb.info").dom.value == '请选择小区'
                            ? null
                            : Ext.getCmp('community.tb.info').getValue();

                        var value = Ext.fly("value.tb.info").dom.value == '请输入小区地址'
                            ? null
                            : Ext.getCmp('value.tb.info').getValue();

                        store.setBaseParam('community_id', community);
                        store.setBaseParam('value', value);
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
        {name: 'value', mapping: 'value'},
        {name: 'community_name', mapping: 'community_name'},
        {name: 'community_address', mapping: 'community_address'},
        {name: 'community_ssxq', mapping: 'community_ssxq'},
        {name: 'community_principal', mapping: 'community_principal'},
        {name: 'community_principal_phone', mapping: 'community_principal_phone'},
        {name: 'unitCount', mapping: 'unitCount'},
        {name: 'doorplateCount', mapping: 'doorplateCount'},
        {name: 'roomCount', mapping: 'roomCount'},
        {name: 'zzCount', mapping: 'zzCount'},
        {name: 'czCount', mapping: 'czCount'},
        {name: 'tenantCount', mapping: 'tenantCount'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../CommunityBuildAction_find.action"
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
        {header: "小区名", dataIndex: "community_name", align: 'center', sortable: true, menuDisabled: true},
        //{header: "小区地址", dataIndex: "community_address", align: 'center', sortable: true, menuDisabled: true},
        {header: "省市县区", dataIndex: "community_ssxq", align: 'center', sortable: true, menuDisabled: true},
        {header: "小区楼栋", dataIndex: "value", align: 'center', sortable: true, menuDisabled: true},
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

function show_build_z(value){
    return value + '幢';
}
function show_unit_z(value){
    return value + '单元';
}
function show_door_z(value){
    return value + '室';
}

function show_flag() {
    return String.format(
        '<a id="delete.info" href="javascript:void(0);" onclick="delete_rule();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;' +
        '<a id="update.info" href="javascript:void(0);" onclick="update_win();return false;" style="color: green;">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;'+
        '<a id="info.info" href="javascript:void(0);" onclick="viewInfo();return false;" style="color: green;">详细</a>&nbsp;&nbsp;&nbsp;'
    );
}

function update_win() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
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
                fieldLabel: '楼栋(幢)',
                xtype:'displayfield',
                name: 'communityBuild.value',
                value: show_build_z(recode.get("value")),
                /* listeners:{
                 blur:function(){
                 var build = this.getValue();
                 var value  = recode.get("value");
                 if(build.length>0&&build!=value){
                 var myMask = new Ext.LoadMask(Ext.getBody(),{
                 msg:'正在校验,请稍后...',
                 removeMask:true
                 });
                 myMask.show();
                 Ext.Ajax.request({
                 url: '../../CommunityBuildAction_checkBuild.action',
                 params :{build:build},
                 method:'POST',
                 success : function(r,o) {
                 var respText = Ext.util.JSON.decode(r.responseText);
                 var msg = respText.msg;
                 myMask.hide();
                 if(msg != 'true'){
                 Ext.MessageBox.show({
                 title:'信息',
                 width:250,
                 msg:msg,
                 buttons:{'ok':'确定'},
                 icon:Ext.MessageBox.INFO,
                 closable:false,
                 fn:function(e){
                 if(e=='ok'){
                 Ext.getCmp('build.insert.info').setValue('');
                 }
                 }
                 });
                 }
                 }
                 });
                 }
                 }
                 }*/
            }, /*{
             xtype:'hidden',name: 'communityBuild.id',value:recode.get('id')
             }, {
             xtype:'hidden',name: 'communityBuild.value',value:recode.get('value')
             }, */{
                fieldLabel: "负责人",
                name: 'communityBuild.principal',
                emptyText: '请输入负责人',
                value: recode.get("community_principal")
            }, {
                fieldLabel: "负责人联系方式",
                name: 'communityBuild.principal_phone',
                emptyText: '请输入负责人联系方式',
                value: recode.get("community_principal_phone")
            }]
    });
    var win = new Ext.Window({
        title: "修改楼栋信息",
        width: 400,
        layout: 'fit',
        height: 200,
        modal: true,
        items: formPanel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '修改',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../../CommunityBuildAction_update.action',
                            method: 'POST',
                            waitTitle: '系统提示',
                            params:{id:recode.get("id")},
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
                                            grid_panel.render();
                                            grid_panel.getStore().reload();
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

function delete_rule() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确定删除这条记录？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: "../../CommunityBuildAction_remove.action",
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
                value: recode.get("value")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '大楼负责人',
                value: recode.get("community_principal")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '负责人电话',
                value: recode.get("community_principal_phone")
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
                fieldLabel: '自住户',
                value: recode.get("zzCount")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '出租户',
                value: recode.get("czCount")
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
        height: 250,
        modal: true,
        items: formPanel
    });
    select_Win.show();
}

function add_win(grid, store) {

    var company_point_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "name"],
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
        items: [
            new Ext.form.ComboBox({
                mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                border: true,
                frame: true,
                pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                editable: false,
                fieldLabel: '小区',
                emptyText: '请选择小区',
                triggerAction: "all",// 是否开启自动查询功能
                store: company_point_store,// 定义数据源
                valueField: "id", // 关联某一个逻辑列名作为显示值
                displayField: "name", // 关联某一个逻辑列名作为显示值
                //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                hiddenName: 'communityBuild.community.id',
                allowBlank: false,
                blankText: "请选择小区",
                listeners: {
                    render: function () {
                        company_point_store.proxy = new Ext.data.HttpProxy({
                            url: '../../CommunityAction_find.action',
                            method: "POST"
                        })
                    }
                }
            }), {
                fieldLabel: "楼栋(幢)",
                id:'build.insert.info',
                name: 'communityBuild.value',
                regex:/^[0-9]*$/,
                regexText:'请输入数字',
                emptyText: '请输入楼栋信息(数字)',
                listeners: {
                    blur: function () {
                        var build = this.getValue();
                        if (build.length > 0) {
                            var myMask = new Ext.LoadMask(Ext.getBody(), {
                                msg: '正在校验,请稍后...',
                                removeMask: true
                            });
                            myMask.show();
                            Ext.Ajax.request({
                                url: '../../CommunityBuildAction_check.action',
                                params: {build: build},
                                method: 'POST',
                                success: function (r, o) {
                                    var respText = Ext.util.JSON.decode(r.responseText);
                                    var msg = respText.msg;
                                    myMask.hide();
                                    if (msg != 'true') {
                                        Ext.MessageBox.show({
                                            title: '信息',
                                            width: 250,
                                            msg: msg,
                                            buttons: {'ok': '确定'},
                                            icon: Ext.MessageBox.INFO,
                                            closable: false,
                                            fn: function (e) {
                                                if (e == 'ok') {
                                                    Ext.getCmp('build.insert.info').setValue('');
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
                fieldLabel: "联系人",
                name: 'communityBuild.principal',
                emptyText: '请输入联系人'
            }, {
                fieldLabel: "联系人电话",
                name: 'communityBuild.principal_phone',
                emptyText: '请输入联系人电话'
            }]
    });
    var win = new Ext.Window({
        title: "新增信息",
        width: 500,
        layout: 'fit',
        height: 200,
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
                            url: '../../CommunityBuildAction_insert.action',
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






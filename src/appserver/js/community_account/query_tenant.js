Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var toolbar = new Ext.Toolbar({
        plain: true,
        height: 30,
        items: ['身份证', {
            id: 'idCard.tb.info',
            xtype: 'textfield',
            emptyText: '请输入身份证',
            width: 100
        }, {
            xtype: 'tbspacer',
            width: 10
        }, '姓名', {
            id: 'name.tb.info',
            xtype: 'textfield',
            emptyText: '请输入姓名',
            width: 100
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


                    store.setBaseParam('idCard', idCard);
                    store.setBaseParam('name', name);
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
        {name: 'idCard', mapping: 'idCard'},
        {name: 'name', mapping: 'name'},
        {name: 'sex', mapping: 'sex'},
        {name: 'mz', mapping: 'mz'},
        {name: 'birth', mapping: 'birth'},
        {name: 'sign', mapping: 'sign'},
        {name: 'address', mapping: 'address'},
        {name: 'DN', mapping: 'DN'},
        {name: 'validity', mapping: 'validity'},
        {name: 'phone', mapping: 'phone'},
        {name: 'description', mapping: 'description'},
        {name: 'status', mapping: 'status'},
        {name: 'attention', mapping: 'attention'},
        {name: 'ocr', mapping: 'ocr'},
        {name: 'initDate', mapping: 'initDate'},
        {name: 'lastDate', mapping: 'lastDate'},
        {name: 'community_name', mapping: 'community_name'},
        {name: 'community_address', mapping: 'community_address'},
        {name: 'community_build', mapping: 'community_build'},
        {name: 'community_build_unit', mapping: 'community_build_unit'},
        {name: 'community_doorplate', mapping: 'community_doorplate'},
        {name: 'community_room', mapping: 'community_room'}

    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../TenantAction_findByAccount.action"
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
        {header: "姓名", dataIndex: "name", align: 'center', sortable: true, menuDisabled: true},
        {header: "性别", dataIndex: "sex", align: 'center', sortable: true, menuDisabled: true},
        {header: "身份证", dataIndex: "idCard", align: 'center', sortable: true, menuDisabled: true},
        {header: "小区", dataIndex: "community_name", align: 'center', sortable: true, menuDisabled: true},
        {header: "楼栋", dataIndex: "community_build", align: 'center', sortable: true, menuDisabled: true},
        {header: "单元", dataIndex: "community_build_unit", align: 'center', sortable: true, menuDisabled: true},
        {header: "门牌", dataIndex: "community_doorplate", align: 'center', sortable: true, menuDisabled: true},
        {header: "房号", dataIndex: "community_room", align: 'center', sortable: true, menuDisabled: true},
        {header: "地址", dataIndex: "community_address", align: 'center', sortable: true, menuDisabled: true},
        {header: "电话", dataIndex: "phone", align: 'center', sortable: true, menuDisabled: true},
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
        loadMask: {msg: '正在加载数据，请稍后...'},
        cm: colM,
        //sm: boxM,
        store: store,
        tbar: toolbar,
        bbar: page_toolbar,
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


function show_flag() {
    return String.format(
        '<a id="info_express.info" href="javascript:void(0);" onclick="viewInfo();return false;" style="color: green;">详细</a>&nbsp;&nbsp;&nbsp;'
    );
}

function viewInfo() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();

    var userPanel = new Ext.Panel({
        border: false,
        layout:'column',
        items:[{
            columnWidth:.7,
            labelWidth:100,
            xtype:'form',
            border:false,
            loadMask : { msg : '正在加载数据，请稍后.....' },
            labelAlign:'right',
            buttonAlign:'left',
            defaultType:'displayfield',
            defaults : {
                width : 200,
                allowBlank : false,
                blankText : '该项不能为空！'
            },
            items:[new Ext.form.DisplayField({
                fieldLabel: '姓名',
                value: recode.get("name")
            }), new Ext.form.DisplayField({
                fieldLabel: '性别',
                value: recode.get("sex")
            }),
                new Ext.form.DisplayField({
                    fieldLabel: '身份证',
                    value: recode.get("idCard")
                }),   new Ext.form.DisplayField({
                    fieldLabel: '民族',
                    value: recode.get("mz")
                }),   new Ext.form.DisplayField({
                    fieldLabel: '生日',
                    value: recode.get("birth")
                }),
                new Ext.form.DisplayField({
                    fieldLabel: '签发机构',
                    value: recode.get("sign")
                }),

                new Ext.form.DisplayField({
                    fieldLabel: '身份证地址',
                    value: recode.get("address")
                }),


                new Ext.form.DisplayField({
                    fieldLabel: '有效期',
                    value: recode.get("validity")
                }),

                new Ext.form.DisplayField({
                    fieldLabel: '生日',
                    value: recode.get("birth")
                })]
        },{
            columnWidth:.2,
            xtype: 'box',
            width:200,
            autoEl: {
                tag: 'img',    //指定为img标签
                src: "../../LardLordAction_loadLardHead.action?id=" + recode.get("id")       //指定url路径
            }
        },{
            columnWidth:.1
        }]

    });

    var userPanel2 = new Ext.form.FormPanel({
        plain:true,
        labelWidth:100,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:'right',
        buttonAlign:'left',
        defaultType:'displayfield',
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[ new Ext.form.DisplayField({
            fieldLabel: '小区',
            value: recode.get("community_name")
        }),
            new Ext.form.DisplayField({
                fieldLabel: '小区地址',
                value: recode.get("community_address")
            }),

            new Ext.form.DisplayField({
                fieldLabel: '楼栋',
                value: recode.get("community_build")
            }),

            new Ext.form.DisplayField({
                fieldLabel: '单元',
                value: recode.get("community_build_unit")
            }),

            new Ext.form.DisplayField({
                fieldLabel: '门牌',
                value: recode.get("community_doorplate")
            }),

            new Ext.form.DisplayField({
                fieldLabel: '房号',
                value: recode.get("community_room")
            }),

            new Ext.form.DisplayField({
                fieldLabel: '电话',
                value: recode.get("phone")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '录入时间',
                value: recode.get("initDate")
            }),
            new Ext.form.DisplayField({
                fieldLabel: '修改时间',
                value: recode.get("lastDate")
            })]
    });


    var select_Win = new Ext.Window({
        title: "用户详细",
        width: 650,
        layout: 'fit',
        height: 500,
        modal: true,
        items: [{
            xtype:'fieldset',
            border: false,
            autoScroll: true,
            items: [userPanel,userPanel2]
        }]
    });
    select_Win.show();
}


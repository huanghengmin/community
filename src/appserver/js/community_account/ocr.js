Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var dataSex = [['男','男'],['女','女']];
    var storeSex = new Ext.data.SimpleStore({fields:['value','key'],data:dataSex});

    var formPanel = new Ext.form.FormPanel({
        plain:true,
        labelWidth:100,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:'right',
        buttonAlign:'left',
        defaultType:'textfield',
        defaults : {
            width : 300,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items: [
            {name:'lardLord.id',xtype:'hidden',id:'lardlord.id.info'},
            //{name:'str',xtype:'hidden',id:'lardlord.str.info'},
            {fieldLabel:'姓名',name:'lardLord.name'}
            ,{fieldLabel:'性别',hiddenName:'lardLord.sex',
                xtype:'combo',
                mode:'local',
                emptyText :'--请选择--',
                editable : false,
                typeAhead:true,
                forceSelection: true,
                triggerAction:'all',
                displayField:"key",valueField:"value",
                store:storeSex,
                value:'男'}
            ,{fieldLabel:'民族',name:'lardLord.mz'}
            ,{fieldLabel:'出生日期',name:'lardLord.birth'}
            ,{fieldLabel:'住址',name:'lardLord.address'}
            ,{fieldLabel:'公民身份号码',name:'lardLord.idCard'}
            ,{fieldLabel:'签发机构',name:'lardLord.sign',allowBlank : true}
            ,{fieldLabel:'有效期',name:'lardLord.validity',allowBlank : true}
            ,{fieldLabel:'备注',name:'lardLord.description',allowBlank : true}
        ],
        buttons:[new Ext.Toolbar.Spacer({width:100}),{
            text:'刷新',
            handler:function(){
                if(Ext.getCmp('picPane.info')!=undefined){
                    Ext.getCmp('picPane.info').destroy();
                }
                nextPic();
                formPanel.form.reset();
            }
        },new Ext.Toolbar.Spacer({width:10}),{
                id:'insert.info',
                text:'保存',
                handler:function(){
                    if(formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:'../../LardLordAction_saveOcr.action',
                            method:'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在保存,请稍后...',
                            success : function(form,action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    animEl:'insert.info',
                                    buttons:{'ok':'确定','no':'取消'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
                                            Ext.getCmp('picPane.info').destroy();
                                            nextPic();
                                            formPanel.form.reset();
                                        }
                                    }
                                });
                            },
                            failure:function(){
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.ERROR,
                                    closable:false
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
                    url: '../../LardLordAction_nextOcr.action',
                    method: 'POST',
                    params:{id:Ext.getCmp('lardlord.id.info').getValue()},
                    waitTitle: '系统提示',
                    waitMsg: '正在处理,请稍后...',
                    success: function (response) {
                        Ext.getCmp('picPane.info').destroy();
                        nextPic();
                    },
                    failure: function () {
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
        frame:true,
        border:false,
//        width:500,
        autoScroll:true,
        layout:'column',
        items:[{
//            columnWidth:.4,
            width:500,
            items:[{
                id:'card.info',
                xtype:'fieldset',
                title:'身份证照片',
                items:[]
            }]
        },{
//            columnWidth:.01,
            width:5,
            items:[{
                xtype:'displayfield',
                width:10
            }]
        },{
//            columnWidth:.4,
            width:500,
            items:[{
                xtype:'fieldset',
                title:'识别结果',
                items:[formPanel]
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

function nextPic(){
    var myMask = new Ext.LoadMask(Ext.getBody(),{
        msg:'正在加载,请稍后...',
        removeMask:true
    });
    myMask.show();
    Ext.Ajax.request({
        url:"../../LardLordAction_getPic.action",
        success: function(response){
            var result = Ext.util.JSON.decode(response.responseText);
            var success = result.success;
            var msg = result.msg;
            myMask.hide();
            if(!success){
                Ext.MessageBox.show({
                    title:'信息',
                    width:250,
                    msg:msg,
                    buttons:{'ok':'确定'},
                    icon:Ext.MessageBox.WARNING,
                    closable:false
                });
            } else {
                var id = result.id;
                var str = result.str;
                var url = '../LardLordAction_loadPic.action?id='+id;
                Ext.getCmp('lardlord.id.info').setValue(id);
                var picPanel = new Ext.form.FormPanel({
                    id:'picPane.info',
                    plain:true,
                    labelWidth:100,
                    border:false,
                    loadMask : { msg : '正在加载数据，请稍后.....' },
                    labelAlign:'right',
                    buttonAlign:'left',
                    defaultType:'textfield',
                    defaults : {
                        width : 200,
                        allowBlank : false,
                        blankText : '该项不能为空！'
                    },
                    items: [
                        {
                            xtype: 'box',//或者xtype: 'component',
                            width: 450, //图片宽度
                            height: 300, //图片高度
                            autoEl: {
                                tag: 'img',    //指定为img标签
                                src: url,   //指定url路径
                                onclick:'clickHandler(\''+url+'\')'
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
function clickHandler(url){
    var win = new Ext.Window({
        width: 800,
        layout: 'fit',
        height: 500,
        modal: true,
        maximizable: true,
        minimizable: true,
        items: [
            {
                xtype: 'box',//或者xtype: 'component',
                width: 800, //图片宽度
//                height: 300, //图片高度
                autoEl: {
                    tag: 'img',    //指定为img标签
                    src: url   //指定url路径
                }
            }
        ]
    }).show();
}

function viewInfo() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();


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


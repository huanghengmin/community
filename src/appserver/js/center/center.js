Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var panel = new Ext.Panel({
        frame:true,
        width:600,
        border:false,
        items:[{
            id:'panel.info',
            border:false,
            xtype:'fieldset',
            //title:'信息',
            width:530,
            items:[]
        }]
    });
    new Ext.Viewport({
        layout :'fit',
        renderTo:Ext.getBody(),
        autoScroll:true,
        items:[panel]
    });
    Ext.Ajax.request({
        url:"../../CenterAction_find.action",
        success: function(response){
            var result = Ext.util.JSON.decode(response.responseText);
            var total = result.totalCount;
            var userPanel = new Ext.form.FormPanel({
                id:'centerPanel-1',
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
                items:[ ]
            });

            for(var i = 0; i < total; i ++) {
                var row = result.root[i];
                userPanel.add(new Ext.form.DisplayField({ id: row.name + '.info' , fieldLabel:''+ row.fieldName, value: row.value }));
                userPanel.doLayout();
            }
            var fieldset = Ext.getCmp('panel.info');
            fieldset.add(userPanel);
            fieldset.doLayout();
        }
    });
});



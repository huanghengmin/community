<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html;charset=utf-8" import="com.hzih.community.web.SiteContext"%>
<%@include file="/taglib.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>管理中心</title>

        <script language="JavaScript">
            function reloadVerifyCode() {
                document.getElementById('verifyCodeImg').src = "<c:url value="/RandomCodeCtrl"/>"+"?tmp="+Math.random(100);
            }
            function checkForm(){
                var form = document.forms.loginForm;
                if(form.name.value==null||form.name.value.length==0){
                    alert("用户名不能为空！");
                    return false;
                }
                var password = form.pwd.value;
                if(password==null||password.length==0){
                    alert("密码不能为空！");
                    return false;
                }
                if(form.vcode.value==null||form.vcode.value.length==0){
                    alert("验证码不能为空！");
                    return false;
                } else if(form.vcode.value.length > 4){
                    alert("验证码不能大于4位！");
                    return false;
                } else if(form.vcode.value.length < 4){
                    alert("验证码不能小于4位！");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body style="background-color: #2e394b;text-align: center">

        <%--<div style=" vertical-align: middle; width: 800px; height: 450px; border: 0px solid #000000;
            margin-left:auto;margin-right:auto;margin-top: 8%;
            background: url(img/login.png) no-repeat;">--%>
        <div style="width: 800px; height: 450px; border: 1px solid #01162b; position: relative;
            margin-left:auto;margin-right:auto;margin-top: 5%;
            background: url(img/login.png) no-repeat;"
            >
            <div style="position: absolute; top:0;right: 0"><img width="150" height="150" src="ClientVersionAction_qrcode.action"/></div>
            <div style="margin-left:auto;margin-right:auto;margin-top:180px;border: 0px solid #acd67a;
                /*width: 400px;*/
                /*padding: 100px 0 0 350px;*/">
                <form name="loginForm" action="login.action" method="post" onsubmit="return checkForm();">
                    <table border=0px style="float:left;margin-left:280px;padding: 10px 10px 10px 10px;" id=myTable>
                        <tr>
                            <td align=right>&nbsp;用户名：</td>
                            <td colspan="1"><input type="text" name="name" autocomplete="off" style="width:140px;" /></td>
                        </tr>
                        <tr>
                            <td align=right>&nbsp;密<font style="color: #ffffff;">一</font>码：</td>
                            <td colspan="1"><input type="password" name="pwd" autocomplete="off" style="width:140px;" /></td>
                        </tr>
                        <tr>
                            <td align=right>&nbsp;验证码：</td>
                            <td><INPUT TYPE="text" autocomplete="off" NAME="vcode" style="width: 80px;vertical-align:middle;"><img
                                src="<c:url value="/RandomCodeCtrl"/>" height="23" width="60" style="vertical-align:middle; "
                                align="middle" id="verifyCodeImg" onclick="reloadVerifyCode();"
                                alt="单击更换验证码" /></td>
                        </tr>
                        <tr>
                            <td/>
                            <td colspan="1"><input type="submit" value="登&nbsp;&nbsp;录" />&nbsp;&nbsp;<input
                                type="reset" value="取&nbsp;&nbsp;消" /></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </body>
</html>


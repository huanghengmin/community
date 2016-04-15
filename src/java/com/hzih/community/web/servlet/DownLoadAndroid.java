package com.hzih.community.web.servlet;

import com.hzih.community.utils.StringContext;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class DownLoadAndroid extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(DownLoadAndroid.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String qrcode = StringContext.systemPath + "/client/weixin.html";
            BufferedReader reader = null;
            int line = 0;     //行号
            try {
                //System.out.println("以行为单位读取文件内容，一次读一整行：");
                reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(qrcode),"GBK"));
                String tempString = null;
                while ((tempString = reader.readLine()) != null) {//一次读入一行，直到读入null为文件结束
                    response.getWriter().write(tempString);
                }
                reader.close();
            } catch (IOException e) {
                logger.error("文件"+qrcode+"不存在!");
                try {
                    Thread.sleep(5*1000);
                } catch (InterruptedException e1) {
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
            response.getWriter().close();
        } catch (Exception e){
            logger.error("DownLoadAndroid 错误",e);
        } finally {

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}

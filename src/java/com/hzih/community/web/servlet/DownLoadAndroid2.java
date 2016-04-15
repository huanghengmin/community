package com.hzih.community.web.servlet;

import com.hzih.community.entity.Version;
import com.hzih.community.utils.StringContext;
import com.hzih.community.utils.VersionUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;


public class DownLoadAndroid2 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(DownLoadAndroid2.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            response.setCharacterEncoding("utf-8");
            String msg = null;
            String path = null;
            InputStream inputStream=null;
            OutputStream outputStream=null;
            try {
//                String Agent = request.getHeader("User-Agent");//weixin  Mozilla/5.0 (Linux; U; Android 5.1; zh-cn; vivo X6D Build/LMY47I) AppleWebKit/533.1 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.4 TBS/025489 Mobile Safari/533.1 MicroMessenger/6.3.13.49_r4080b63.740 NetType/WIFI Language/zh_CN
//                StringTokenizer st = new StringTokenizer(Agent, ";");
//                st.nextToken();
//                String userBrowser = st.nextToken();
                Version av = null;
                String android_version = StringContext.systemPath + "/client/android" + "/version.xml";
                File android_info = new File(android_version);
                if (android_info.exists()) {
                    av = VersionUtils.readInfo(android_info);
                    String name = av.getName();
                    path = StringContext.systemPath + "/client/android" + "/" + name;
                }
//                File source = new File(path);
//                String name = source.getName();
//                FileUtil.downType(response, name, userBrowser);
//                response = FileUtil.copy(source, response);



                File file=new File(path);
                inputStream=new BufferedInputStream(new FileInputStream(file));

                //设置为流下载
                response.setContentType("application/octet-sream");
                //设置响应大小
                response.setContentLength((int) file.length());

//                response.setHeader("Content-type", "text/html;charset=UTF-8");
                //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
                response.setCharacterEncoding("UTF-8");

                String fileName=file.getName();
                //浏览器下载
                response.addHeader("Content-Disposition", "attachment;filename="+ new String( fileName.getBytes("gb2312"), "ISO8859-1" ));

                response.addHeader("Content-Type", "application/vnd.android.package-archive");
                outputStream=new BufferedOutputStream(response.getOutputStream());

                // 缓冲区大小1024
                byte[] s=new byte[10240];
                int len=0;
                //避免最后一次读取数据时，不满10240b的数据被填充，造成数据不准确性
                while((len=inputStream.read(s))!=-1)
                {
                    outputStream.write(s, 0, len);

                }
                if (inputStream!=null) {
                    inputStream.close();
                }
                response.flushBuffer();
                if (outputStream!=null) {
                    outputStream.close();
                }



                msg = "下载Android 快递 客户端成功"+request.getRemoteAddr();
                logger.info(msg+",时间:"+new Date());
            } catch (IOException e) {

            } catch (Exception e) {
//                msg = "下载Android 社区 客户端失败"+request.getRemoteAddr();
                logger.error("下载Android 快递 客户端失败"+request.getRemoteAddr(), e);
            }finally {
                if(outputStream!=null) {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(inputStream!=null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

//            String json = "{\"success\":true,\"msg\":\"" + msg + "\"}";
//            PrintWriter writer = response.getWriter();
//            writer.write(json);
//            writer.flush();
//            writer.close();
       /* } catch (Exception e){
            logger.error("DownLoadAndroid 错误",e);
        } finally {

        }*/
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

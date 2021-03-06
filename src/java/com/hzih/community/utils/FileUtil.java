package com.hzih.community.utils;

import com.inetec.common.client.ECommonUtil;
import com.inetec.common.client.util.LogBean;
import com.inetec.common.client.util.XChange;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Blob;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-11
 * Time: 下午2:15
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {

    public static HttpServletResponse copy(InputStream in, HttpServletResponse response) { //下载
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            byte[] content = new byte[1024 * 1024];
            int length;
            while ((length = in.read(content, 0, content.length)) != -1) {
                out.write(content, 0, length);
                out.flush();
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
        return response;
    }

    /**
     * 上传文件
     * @param savePath          保存路径
     * @param uploadFile        上传文件
     * @param uploadFileFileName  上传文件文件名
     * @throws IOException
     */
    public static void upload(String savePath,File uploadFile,String uploadFileFileName) throws IOException {
        File dir = new File(savePath);
        if(!dir.exists()){
            dir.mkdir();
        }
        String newFile = dir+"/"+uploadFileFileName;
        copy(uploadFile, newFile);
    }

    /**
     *
     * @param from   被复制文件
     * @param to     保存后文件地址
     */
    public static void copy(File from,String to) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        bis = new BufferedInputStream(
                new FileInputStream(from));
        bos = new BufferedOutputStream(
                new FileOutputStream(
                        new File(to)));
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = bis.read(buf))!=-1){
            bos.write(buf,0,len);
        }
        bos.flush();
        bos.close();
        bis.close();
    }


    public static void copy(InputStream in,String to) throws IOException {
        FileOutputStream out = new FileOutputStream(to);
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = in.read(buf))!=-1){
            out.write(buf,0,len);
        }
        out.flush();
        out.close();
        in.close();
    }


    public static byte[] blob2ByteArr(Blob blob) throws Exception {
        InputStream in = blob.getBinaryStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] content = new byte[1024*1024];
        int length;
        while ((length = in.read(content, 0, content.length)) != -1){
            out.write(content, 0, length);
            out.flush();
        }
        in.close();
        out.flush();
        out.close();
        return  out.toByteArray();
    }

    /**
     * 读取
     * @return
     */
    public static String readFileNames(String path) {
        String[] files = readFileName(path);
        String json = null;
        if(files.length==0){
            json = "{'success':true,'total':"+files.length+",rows:[,]}";
        }else{
            json = "{'success':true,'total':"+files.length+",rows:[";
            int count = 0;
            for (int i = 0; i<files.length; i++){
//                if(i==start&& count<limit){
//                    start ++;
//                    count ++;
                    json += "{'fileName':'"+files[i]+"'},";
//                }
            }
            json += "]}";
        }
        return json;
    }

    /**
     *
     * @param path  文件夹路径 rizhi
     * @return      文件夹中所有文件名
     */
    public static String[] readFileName(String path){
        File file = new File(path);
        File[] files = file.listFiles();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < files.length; i++) {
			if(files[i].getName().indexOf(".log")>0){
                String length = setLength(files[i].length());
			    String logName = files[i].getName()+"("+length+")";
				list.add(logName);
			}
		}
        return list.toArray(new String[list.size()]);
    }

    public static String readRemoteFileNames() throws XChange {
         String[] files = readRemoteLogFileName();
        String json = null;
        if(files.length==0){
            json = "{'success':true,'total':"+files.length+",rows:[,]}";
        }else{
            json = "{'success':true,'total':"+files.length+",rows:[";
            int count = 0;
            for (int i = 0; i<files.length; i++){
//                if(i==start&& count<limit){
//                    start ++;
//                    count ++;
                    json += "{'fileName':'"+files[i]+"'},";
//                }
            }
            json += "]}";
        }
        return json;
    }

    /**
	 * 读取外网服务器日志
	 */
	public static String[] readRemoteLogFileName() throws XChange {
		ECommonUtil ecu = new ECommonUtil();
		LogBean[] bean = ecu.getLogFiles();
		int total = bean.length;
		List<String> logs = new ArrayList<String>();
		for (int i = 0; i < total; i++) {
			String length = setLength(bean[i].getLogFileLength());
			String externalLog = bean[i].getLogFileName()+"("+length+")";
			logs.add(externalLog);
		}
		return logs.toArray(new String[logs.size()]);
	}

    /**
     * 计算long成*MB*Kb
     * @param l
     * @return
     */
    public static String setLength(long l) {
		String a = "0";
		if(l>0){
			if(l<512){
				a =l+"B";
			}else if(l >=512&&l <= 10485){
				a = new DecimalFormat("0.00").format((double)l/(1024));
				String[] b = a.split("\\.");
				if(b[1].equals("00")){
					a = b[0]+"KB";
				}else{
					a +="KB";
				}
			}else if(l > 10485){
				a = new DecimalFormat("0.00").format((double)l/(1024*1024));
				String[] b = a.split("\\.");
				if(b[1].equals("00")){
					a = b[0]+"MB";
				}else{
					a +="MB";
				}
			}
		}
		return a;
	}

    public static void downType(HttpServletResponse response,String filename,String userAgent) throws UnsupportedEncodingException {
		response.reset();
		response.setBufferSize(5*1024*1024);
        String rtn = null;
        String  new_filename = URLEncoder.encode(filename, "UTF8");
       // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
        rtn = "filename=\"" + new_filename + "\"";
        if (userAgent != null){
            userAgent = userAgent.toLowerCase();
            // IE浏览器，只能采用URLEncoder编码 
            if (userAgent.indexOf("msie") != -1) {
                rtn = "filename=\"" + new_filename + "\"";
            }// Opera浏览器只能采用filename*
            else if (userAgent.indexOf("opera") != -1){
                rtn = "filename*=UTF-8''" + new_filename;
            } // Safari浏览器，只能采用ISO编码的中文输出
            else if (userAgent.indexOf("safari") != -1 ){
                rtn = "filename=\"" + new String(filename.getBytes("UTF-8"),"ISO8859-1") + "\"";
            }// Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
            else if (userAgent.indexOf("applewebkit") != -1 ){
                new_filename = MimeUtility.encodeText(filename, "UTF8", "B");
                rtn = "filename=\"" + new_filename + "\"";
            } // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
            else if (userAgent.indexOf("mozilla") != -1){
                rtn = "filename*=UTF-8''" + new_filename;
            }
            else if(userAgent.indexOf("firefox")!=-1){
                rtn = "filename=" + new String(filename.getBytes("UTF-8"),"ISO8859-1");
                //response.setHeader("Pragma", "No-cache");
                //response.setHeader("Cache-Control", "no-cache");
           }
        response.addHeader("Content-Disposition", "attachment;"+rtn);
//        response.setContentType("multipart/form-data");
		response.setContentType("application/octet-stream; charset=UTF-8");
        }
	}

    /**
     *
     * @param from       被复制文件
     * @param response   传输响应 用于文件下载时
     */
    public static HttpServletResponse copy(File from,HttpServletResponse response){ //下载
//    	response.addHeader("Content-Length", ""+from);

        ServletOutputStream out =null;
        BufferedInputStream in = null;
        try {
            out = response.getOutputStream();
            in = new BufferedInputStream(new FileInputStream(from));
            byte[] content = new byte[1024*1024];
            int length;
            while ((length = in.read(content, 0, content.length)) != -1){
                out.write(content, 0, length);
                out.flush();
            }

            out.flush();
//            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return response;
    }

}

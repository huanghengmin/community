package com.hzih.community.utils.qrcode;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.log4j.Logger;


public class ZXingCode {
    private Logger logger = Logger.getLogger(ZXingCode.class);

   public static void main(String[] args) throws WriterException {
        String content = "彩虹城一幢二单元101室";
        String filePath = "D:/二维码/weixin.jpg";
        try {
            File file = new File(filePath);
            ZXingCode zp = new ZXingCode();
            BufferedImage bim = zp.getQR_CODEBufferedImage(content, BarcodeFormat.QR_CODE, 300, 300, zp.getDecodeHintType());
            ImageIO.write(bim, "jpeg", file);
            String logo_qrcode_path = "D:/二维码/TDC-" + "test.png";
            zp.addLogo_QRCode(file, new File("D:/二维码/logo.jpg"), logo_qrcode_path, new LogoConfig());
            Thread.sleep(5000);
            boolean flag = zp.parseQR_CODEImage(new File("D:/二维码生成/TDC-test.png"));
            if (flag) {
                //生成成功
            } else {
                //生成失败
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String buildQRCode(String content, String qrcodePath, String logoPath, String logoQrcodepath) {
        try {
            File file = new File(qrcodePath);
            ZXingCode zp = new ZXingCode();
            BufferedImage bim = zp.getQR_CODEBufferedImage(content, BarcodeFormat.QR_CODE, 300, 300, zp.getDecodeHintType());
            ImageIO.write(bim, "jpeg", file);
            if(logoPath!=null&&logoQrcodepath!=null){
                zp.addLogo_QRCode(file, new File(logoPath), logoQrcodepath, new LogoConfig());
                Thread.sleep(5000);
                boolean flag = zp.parseQR_CODEImage(new File(logoQrcodepath));
                if (flag) {
                    //生成成功
                    return logoQrcodepath;
                } else {
                    //生成失败
                    return null;
                }
            }else {
                boolean flag = zp.parseQR_CODEImage(new File(qrcodePath));
                if (flag) {
                    //生成成功
                    return qrcodePath;
                } else {
                    //生成失败
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 给二维码图片添加Logo
     *
     * @param qrPic
     * @param logoPic
     */
    public void addLogo_QRCode(File qrPic, File logoPic, String path, LogoConfig logoConfig) {
        try {
            if (!qrPic.isFile() || !logoPic.isFile()) {
                logger.error("file not find !");
                System.exit(0);
            }

            /**
             * 读取二维码图片，并构建绘图对象
             */
            BufferedImage image = ImageIO.read(qrPic);
            Graphics2D g = image.createGraphics();

            /**
             * 读取Logo图片
             */
            BufferedImage logo = ImageIO.read(logoPic);
            /**
             * 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
             */
            int widthLogo = logo.getWidth(null) > image.getWidth() * 2 / 10 ? (image.getWidth() * 2 / 10) : logo.getWidth(null),
                    heightLogo = logo.getHeight(null) > image.getHeight() * 2 / 10 ? (image.getHeight() * 2 / 10) : logo.getWidth(null);

            // 计算图片放置位置
            /**
             * logo放在中心
             */
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;
            /**
             * logo放在右下角
             */
//            int x = (image.getWidth() - widthLogo);
//            int y = (image.getHeight() - heightLogo);

            //开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, widthLogo, heightLogo);

            g.dispose();
            logo.flush();
            image.flush();

            ImageIO.write(image, "png", new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 二维码的解析
     *
     * @param file
     */
    public boolean parseQR_CODEImage(File file) {
        try {
            MultiFormatReader formatReader = new MultiFormatReader();
            if (!file.exists()) {
                return false;
            }
            BufferedImage image = ImageIO.read(file);

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            Result result = formatReader.decode(binaryBitmap, hints);

            logger.info("result = " + result.toString());
            logger.info("resultFormat = " + result.getBarcodeFormat());
            logger.info("resultText = " + result.getText());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将二维码生成为文件
     *
     * @param bm
     * @param file
     */
    public void decodeQR_CODE2ImageFile(BitMatrix bm, File file) {
        try {
            if (null == file || file.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("文件异常，或扩展名有问题！");
            }

            BufferedImage bi = fileToBufferedImage(bm);
            ImageIO.write(bi, "apng", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 构建初始化二维码
     *
     * @param bm
     * @return
     */
    public BufferedImage fileToBufferedImage(BitMatrix bm) {
        BufferedImage image = null;
        try {
            int w = bm.getWidth(), h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 生成二维码bufferedImage图片
     *
     * @param content       编码内容
     * @param barcodeFormat 编码类型
     * @param width         图片宽度
     * @param height        图片高度
     * @param hints         设置参数
     * @return
     */
    public BufferedImage getQR_CODEBufferedImage(String content, BarcodeFormat barcodeFormat, int width, int height, Map<EncodeHintType, ?> hints) {
        MultiFormatWriter multiFormatWriter = null;
        BitMatrix bm = null;
        BufferedImage image = null;
        try {
            multiFormatWriter = new MultiFormatWriter();

            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            bm = multiFormatWriter.encode(content, barcodeFormat, width, height, hints);

            int w = bm.getWidth();
            int h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 设置二维码的格式参数
     *
     * @return
     */
    public Map<EncodeHintType, Object> getDecodeHintType() {
        // 用于设置QR二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.MAX_SIZE, 350);
        hints.put(EncodeHintType.MIN_SIZE, 100);

        return hints;
    }
}


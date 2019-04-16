package com.youmai.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class ZXingUtil {

    // 嵌套图片宽度的一半
    private static final int NEST_IMAGE_WIDTH = 50;

    /**
     * 根据url生成二维码
     *
     * @param codeUrl 二维码Url
     * @param width   宽度
     * @param height  高度
     * @return
     * @throws WriterException
     */
    public static Bitmap encode(String codeUrl, int width, int height)
            throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        /*
         * H = ~30% correction L = ~7% correction M = ~15% correction Q = ~25%
         * correction
         */
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);

        // 生成二维码的关键代码 图像数据转换，使用了矩阵转换
        BitMatrix matrix = writer.encode(codeUrl, BarcodeFormat.QR_CODE, width,
                height, hints);
        /*
         * int halfW = matrix.getWidth()/2; int halfH = matrix.getHeight()/2;
         */
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                /*
                 * if (matrix.get(x, y)) { pixels[y * halfW + x] = 0xff000000; }
                 */
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else { // 无信息设置像素点为透明
                    pixels[y * width + x] = 0x00000000;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 生成二维码
     *
     * @param content   二维码内容
     * @param width     二维码的宽度
     * @param height    二维码的高度
     * @param nestImage 中间嵌套的图片
     * @return
     * @throws WriterException
     */
    public static Bitmap encode(String content, int width, int height,
                                Bitmap nestImage) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);

        // 缩放图片
        Matrix m = new Matrix();
        float sx = (float) 2 * NEST_IMAGE_WIDTH / nestImage.getWidth();
        float sy = (float) 2 * NEST_IMAGE_WIDTH / nestImage.getHeight();
        m.setScale(sx, sy);
        // 重新构造一个100*100的图片
        nestImage = Bitmap.createBitmap(nestImage, 0, 0, nestImage.getWidth(),
                nestImage.getHeight(), m, false);

        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > halfW - NEST_IMAGE_WIDTH && x < halfW + NEST_IMAGE_WIDTH
                        && y > halfH - NEST_IMAGE_WIDTH
                        && y < halfH + NEST_IMAGE_WIDTH) {
                    pixels[y * width + x] = nestImage.getPixel(x - halfW
                            + NEST_IMAGE_WIDTH, y - halfH + NEST_IMAGE_WIDTH);
                } else {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else { // 无信息设置像素点为透明
                        pixels[y * width + x] = 0x00000000;
                    }
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}

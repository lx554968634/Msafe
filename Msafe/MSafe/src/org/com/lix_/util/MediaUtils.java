package org.com.lix_.util;

import java.util.HashMap;

import org.com.lix_.Define;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Thumbnails;

public class MediaUtils {

	public static boolean checkMediaName(String sPath) {
		int nCount = Define.MEDIA_SPLIT.length;
		for (int i = 0; i < nCount; i++) {
			if (sPath.indexOf("." + Define.MEDIA_SPLIT[i]) != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽b度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	private static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	private static String TAG = "MediaUtils";

	public static Bitmap getVideoThumbnail(String filePath) {
		 MediaMetadataRetriever fmmr = new MediaMetadataRetriever();  
		Bitmap bitmap = null;
		try {
			fmmr.setDataSource(filePath);
			bitmap = fmmr.getFrameAtTime();
			if (bitmap != null) {
				Bitmap b2 = fmmr.getFrameAtTime(1,
						MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
				if (b2 != null) {
					bitmap = b2;
				}
				bitmap = ThumbnailUtils.extractThumbnail(bitmap, 150, 150,
						ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} finally {
			fmmr.release();
		}
		return bitmap;
	}

	public static Bitmap getMediaImage(String sPath) {
		if (checkMediaName(sPath)) {
			Bitmap pBitmap = null;
			try {
				pBitmap = getVideoThumbnail(sPath);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return pBitmap;
		}
		return null;
	}
}

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
	 * ����ָ����ͼ��·���ʹ�С����ȡ����ͼ �˷���������ô��� 1.
	 * ʹ�ý�С���ڴ�ռ䣬��һ�λ�ȡ��bitmapʵ����Ϊnull��ֻ��Ϊ�˶�ȡ��Ⱥ͸߶ȣ�
	 * �ڶ��ζ�ȡ��bitmap�Ǹ��ݱ���ѹ������ͼ�񣬵����ζ�ȡ��bitmap����Ҫ������ͼ�� 2.
	 * ����ͼ����ԭͼ������û�����죬����ʹ����2.2�汾���¹���ThumbnailUtils��ʹ ������������ɵ�ͼ�񲻻ᱻ���졣
	 * 
	 * @param imagePath
	 *            ͼ���·��
	 * @param width
	 *            ָ�����ͼ��Ŀ�b��
	 * @param height
	 *            ָ�����ͼ��ĸ߶�
	 * @return ���ɵ�����ͼ
	 */
	private static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // ��Ϊ false
		// �������ű�
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
		// ���¶���ͼƬ����ȡ���ź��bitmap��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// ����ThumbnailUtils����������ͼ������Ҫָ��Ҫ�����ĸ�Bitmap����
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

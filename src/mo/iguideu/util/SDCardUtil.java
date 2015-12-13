package mo.iguideu.util;

import java.io.File;
import android.os.Environment;
import android.os.StatFs;

public class SDCardUtil
{

	// Unit: MB
	public static final int NO_SPACE_LEFT_NEARLY_VALUE = 100;

	private static final int SIZE_UNIT = 0;
	private static final int SIZE_NUM = 1;

	public static boolean isSDCardExistAndUseful()
	{
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				&& !Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED_READ_ONLY))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public static boolean isNearlyNoSpaceLeftOnSDCard()
	{
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			File path = Environment.getExternalStorageDirectory();

			StatFs stat_fs = new StatFs(path.getPath());

			long block_size = stat_fs.getBlockSize();
			long available_block = stat_fs.getAvailableBlocks();

			String[] remain_size_array = getFileSizeString((available_block * block_size));

			if(remain_size_array == null)
			{
				return false;
			}
			
			if (Integer.parseInt(remain_size_array[SIZE_NUM]) <= NO_SPACE_LEFT_NEARLY_VALUE)
			{
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return false;
		}
	}

	public static String getSDCardRemainSize()
	{
		String remain_size = "";

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			File path = Environment.getExternalStorageDirectory();

			StatFs stat_fs = new StatFs(path.getPath());

			long block_size = stat_fs.getBlockSize();
			long available_block = stat_fs.getAvailableBlocks();

			String[] remain_size_array = getFileSizeString((available_block * block_size));

			remain_size = remain_size_array[SIZE_NUM]
					+ remain_size_array[SIZE_UNIT];
		}

		return remain_size;
	}

	private static String[] getFileSizeString(long size)
	{
		String[] return_size = new String[2];

		if (size >= 1024)
		{
			return_size[SIZE_NUM] = String.valueOf(size /= 1024);

			return_size[SIZE_UNIT] = "KB";

			if (size >= 1024)
			{
				return_size[SIZE_NUM] = String.valueOf(size /= 1024);

				return_size[SIZE_UNIT] = "MB";
			}
		}

		return return_size;
	}
}

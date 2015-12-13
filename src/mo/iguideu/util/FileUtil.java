package mo.iguideu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

public class FileUtil
{
	public static final int 	FILE_TYPE_UNKNOW 		= -1;
	public static final int 	FILE_TYPE_AUDIO 		= 0;
	public static final int 	FILE_TYPE_VIDEO 		= 1;
	public static final int 	FILE_TYPE_IMAGE 		= 2;
	public static final int 	FILE_TYPE_APK	 		= 3;
	public static final int 	FILE_TYPE_FLASH	 		= 4;
	
	private static Activity 	_activity = null;
	
	public static void construce(Activity  activity)
	{
		_activity = activity;
	}
	

	public static void deleteForlder(File dir)
	{
		if (dir.isDirectory()) 
		{
			String[] children = dir.list();
			for (double i=0; i<children.length; i++) 
			{
				deleteForlder(new File(dir, children[(int) i]));
			}
		}
		
		dir.delete();
	}
	
	public static int getMIMEType(File file)
	{
		int	   type = FILE_TYPE_UNKNOW;
		String extension = file.getName().substring(file.getName().lastIndexOf(".")+1).toLowerCase();
		
		if
			(
				extension.equals("m4a")||
				extension.equals("mp3")||
				extension.equals("mid")||
				extension.equals("xmf")||
				extension.equals("ogg")||
				extension.equals("wav")
			)
		{
			type = FILE_TYPE_AUDIO;
		}
		
		else if
			(
				extension.equals("3gp")||
				extension.equals("avi")||
				extension.equals("mp4")
			)
		{
			type = FILE_TYPE_VIDEO;
		}
		
		else if
			(
				extension.equals("jpg")||
				extension.equals("gif")||
				extension.equals("png")||
				extension.equals("jpeg")||
				extension.equals("bmp")
			)
		{
			type = FILE_TYPE_IMAGE;
		}

		else if(extension.equals("swf")||extension.equals("zip"))
		{
			type = FILE_TYPE_FLASH;
		}
		else if(extension.equals("apk"))
		{
			type = FILE_TYPE_APK;
		}
		
		return type;
	}
	
	public static String resolveMIMEType(File file)
	{
		String type = "";
		String extension = file.getName()
				.substring(file.getName().lastIndexOf(".") + 1).toLowerCase();

		if (extension.equals("m4a") || extension.equals("mp3")
				|| extension.equals("mid") || extension.equals("xmf")
				|| extension.equals("ogg") || extension.equals("wav"))
		{
			type = "audio";
		}

		else if (extension.equals("3gp") || extension.equals("mp4"))
		{
			type = "video";
		}

		else if (extension.equals("jpg") || extension.equals("gif")
				|| extension.equals("png") || extension.equals("jpeg")
				|| extension.equals("bmp"))
		{
			type = "image";
		} else if (extension.equals("apk"))
		{
			type = "application/vnd.android.package-archive";
		} else
		{
			type = "*";
		}

		if (extension.equals("apk"))
		{

		} else
		{
			type += "/*";
		}

		return type;
	}

	public static void deleteTempFile(String path)
	{
		File temp_file = new File(path);

		if (temp_file.exists())
		{
			temp_file.delete();

			Log.i("TempFile", "deleted!");

			return;
		}

		Log.i("TempFile", "deleted failed!");
	}

	public static boolean isSDcardExist()
	{
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
		{
			return true;
		} else
		{
			return false;
		}
	}
	
	public static boolean createFileOnInternalStorage(String path) throws IOException
	{
		boolean result = false;
		
		ContextWrapper cw = new ContextWrapper(_activity);
		
		String prefix = "/data/data/" + cw.getPackageName()+ File.separator;
		
		path = path.replace(prefix, "");
		
		String[] folders = path.split(File.separator);
		
		
		for(String folder : folders)
		{
			if(folder.equals(""))
			{
				continue;
			}
			
			if(!folder.contains("."))
			{
				File new_folder = new File(prefix+File.separator+folder);
				
				if(!new_folder.exists())
				{
					Log.i("FileUtil", "created internal folder: "+ folder);;
					
					prefix = new_folder.getAbsolutePath();
					
					new_folder.mkdir();
				}

				prefix = new_folder.getAbsolutePath();
			}
			else
			{
				File new_file = new File(prefix+File.separator+folder);
				
				if(!new_file.exists())
				{
					new_file.createNewFile();
					
					Log.i("FileUtil", "created file "+folder);
				}
			}
		}
		
		return result;
	}
	
	public static boolean createFile(String path) throws IOException
	{
		boolean result = false;
		
		if(path.contains(Environment.getExternalStorageDirectory().getAbsolutePath()))
		{
			result = createFileOnSDCard(path);
			
			return result;
		}
		
		if(path.contains(File.separator))
		{
			String[] folders = path.split(File.separator);
			
			String prefix = "/";
			
			for(String folder : folders)
			{
				if(folder.equals(""))
				{
					continue;
				}
				
				if(!folder.contains("."))
				{
					File new_folder = new File(prefix+File.separator+folder);
					
					if(!new_folder.exists())
					{
						Log.i("FileUtil", "created folder folder: "+new_folder.mkdir());;
						
						prefix = new_folder.getAbsolutePath();
					}
					
					new_folder.mkdir();
					
					prefix = new_folder.getAbsolutePath();
				}
				else
				{
					File new_file = new File(prefix+File.separator+folder);
					
					if(!new_file.exists())
					{
						new_file.createNewFile();
						
						Log.i("FileUtil", "created file "+folder);
					}
				}
			}
		}
		
		return result;
	}
	
	public static boolean createFileOnSDCard(String path)
	{
		boolean result = false;
		
		if(!SDCardUtil.isSDCardExistAndUseful())
		{
			result = false;
			
			return result;
		}
		
		if(path.contains("."))
		{
			String path_besides_file_name = path.substring(0, path.lastIndexOf(File.separator));
			
			createDirectoryOnSDCard(path_besides_file_name);
			
			Log.i("FileUtil", "preparing create "+path_besides_file_name+" .is file ");
		}
		else
		{
			createDirectoryOnSDCard(path);
			
			Log.i("FileUtil", "preparing create "+path+" .is directory ");
			
			return true;
		}
		
		if(!new File(path).exists())
		{
			try
			{
				new File(path).createNewFile();
				
				result = true;
				
				Log.i("FileUtil","createFileOnSDCard -- create file "+path+" OK ");
			} 
			catch (IOException e)
			{
				Log.e("FileUtil", "createFileOnSDCard -- "+e.toString());
				result = false;
			}
		}
		
		return result;
	}
	
	public static boolean createDirectoryOnSDCard(String path)
	{
		boolean result = false;
		
		if(!SDCardUtil.isSDCardExistAndUseful())
		{
			result = false;
			
			return result;
		}
		
		String sd_root = Environment.getExternalStorageDirectory().getAbsolutePath();
		
//		path = sd_root + path.contains(File.separator) != null?"":File.separator + path;
		
		if(path.contains(sd_root))
		{
			//Create directory on SD card
			
			path = path.substring( sd_root.length() );
			
			Log.i("FileUtil", "createDirectoryOnSDCard -- after sub string"+path);
			
			String[] folders = path.split(File.separator);
			
			String prefix = sd_root;
			
			for(String folder : folders)
			{
				if(folder.equals(""))
				{
					continue;
				}
				
				if(!folder.contains("."))
				{
					File new_folder = new File(prefix+File.separator+folder);
					
					if(!new_folder.exists())
					{
						Log.i("FileUtil", "created folder folder: "+folder+new_folder.mkdir());;
						
						prefix = new_folder.getAbsolutePath();
					}
					
					new_folder.mkdir();
					
					prefix = new_folder.getAbsolutePath();
				}
				else
				{
					File new_file = new File(prefix+File.separator+folder);
					
					if(!new_file.exists())
					{
						try
						{
							new_file.createNewFile();
						} 
						catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Log.i("FileUtil", "created file "+folder);
					}
				}
			}
			
			result = true;
		}
		
		return result;
	}

	public static boolean copyFile(String srcPath, String desPath)
	{
		if(new File(srcPath).exists())
		{
			try
			{
				if(!new File(desPath).exists())
				{
					new File(desPath).createNewFile();
				}
				
				FileInputStream fis = new FileInputStream(new File(srcPath));
				
				FileOutputStream fos=new FileOutputStream(desPath);
				
				byte[] buffer = new byte[4096];
				
				int bufferLength 	= 0;
//				int loaded 			= 0;
				
				while((bufferLength = fis.read(buffer))>0) 
				{
					fos.write(buffer,0,bufferLength);
					
//					loaded += bufferLength;
				}

				fos.flush();
				fos.close();
				fis.close();
				
				Log.i("FileUtil", "copyFile done! From: "+srcPath+" to: "+desPath);
				
				return true;
			}
			catch(Exception e)
			{
				Log.i("FileUtil", "copyFile Failed! "+e.toString());
				
				return false;
			}
		}
		else
		{
			Log.i("FileUtil", "copyFile Source file not exist!");
			
			return false;
		}
	}
}

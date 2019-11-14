/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description:文件操作类的实现文件
 * </p>
 * @Title FileUtil.java
 * @Package com.zte.iptvclient.android.common
 * @version 1.0
 * @author jamesqiao10065075
 * @date 2012-2-29
 */

package com.wizarpos.log.util;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.text.TextUtils;
import android.util.Log;


/**
 * 
 * 文件操作类，用于操作文件，包括创建，删除，写，读取等。
 * @ClassName:FileUtil 
 * @Description: 用于操作文件，包括创建，删除，写，读取等。
 * @author: 10065075
 * @date: 2012-2-29
 *
 */
public class FileUtil
{
    public static final String LOG_TAG = "FileUtil";

    /** 本地图片的标志 */
    public static final String STR_TAG_LOCALIMG_ASSET = "file:///android_asset/";

    public static final String STR_TAG_LOCALIMG_RAW = "file:///android_raw/";

    public static final String STR_TAG_LOCALIMG_RES = "file:///android_res/";

    public static final String STR_TAG_LOCALIMG_MNT = "/data/";

    public static final String STR_TAG_LOCALIMG = "file://";

    /** 状态正常*/
    public static final int FILE_UTIL_STATUS_SUCCESS = 0;
    /** 文件已存在*/
    public static final int FILE_UTIL_STATUS_FILE_EXIST = 1;
    /** 文件名为空*/
    public static final int FILE_UTIL_STATUS_FILENAME_NULL = 2;
    /** 写入内容为空*/
    public static final int FILE_UTIL_STATUS_CONTENT_NULL = 3;
    /** 文件对象为空*/
    public static final int FILE_UTIL_STATUS_FILEOBJECT_NULL = 4;
    /** 文件创建失败*/
    public static final int FILE_UTIL_STATUS_FILE_CREATE_FAIL = 5;
    /** 文件不存在*/
    public static final int FILE_UTIL_STATUS_FILE_NOT_EXIST = 8;
    /** 其他错误*/
    public static final int FILE_UTIL_STATUS_OTHER_EXCEPTION = 10;

    /**
     * 不允许创建实例，隐藏构造函数
     */
    private FileUtil()
    {

    }
    
    /**
     * 
     * 是否本地文件
     * <p>
     * Description: 是否本地文件
     * <p>
     * @date 2015-4-13 
     * @author ztevuser
     * @param strPath 文件路径
     * @return 是否本地文件
     */
    public static boolean isFileExist(String strPath)
    {
        File f = new File(strPath);

        return f.exists();
    }

    /**
     * 创建文件/文件夹
     * @param strPath 文件路径
     * @param isDir 是否文件夹标志
     * @return 成功true
     */
    public static boolean createFile(String strPath, boolean isDir)
    {
        if (null == strPath)
        {
            return false;
        }

        if (!isDir && "".equals(strPath))
        {
            return false;
        }

        File f = new File(strPath);

        if (!f.exists())
        {
            if (isDir)
            {
                f.mkdirs();
            }
            else
            {
                File pf = f.getParentFile();
                if (pf.exists() || pf.mkdirs())
                {
                    try
                    {
                        f.createNewFile();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * 写入文件
     * @param strPath 文件路径
     * @param buffer 二进制流
     * @return 成功true
     */
    public static boolean write2File(String strPath, byte[] buffer)
    {
        if (null == strPath || null == buffer)
        {
            return false;
        }

        if (!createFile(strPath, false))
        {
            return false;
        }

        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(strPath);
            fos.write(buffer);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (null != fos)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
    * 
    * 把内容附加到某个文件中，没有则新建。
    * @date 2012-2-29 
    * @param strFilePath 待写的文件完整路径，包括文件名
    * @param strContent 要写的内容
    * @return 0代表成功，其他代表错误码（2文件名为空，3内容为空，10其他异常）
    */
    public static int append2File(String strFilePath, String strContent)
    {
        //文件路径不能为空
        if (TextUtils.isEmpty(strFilePath))
        {
            return FILE_UTIL_STATUS_FILENAME_NULL;
        }
        //内容不能为空
        if (TextUtils.isEmpty(strContent))
        {
            return FILE_UTIL_STATUS_CONTENT_NULL;
        }

        DataOutputStream dos = null;
        File fileRecord = null;

        try
        {
            fileRecord = new File(strFilePath);
            if (!fileRecord.exists())
            {
            	File parent = fileRecord.getParentFile();
            	if (!parent.exists()) {
            		parent.mkdirs();
            	}
                fileRecord.createNewFile();
            }
            FileOutputStream fileOutpueStream = new FileOutputStream(fileRecord, true);
            dos = new DataOutputStream(fileOutpueStream);
            dos.writeUTF(strContent);
            dos.close();
            fileOutpueStream.close();

            return FILE_UTIL_STATUS_SUCCESS;
        }
        catch (FileNotFoundException exFile)
        {
            for (StackTraceElement stackTraceElement : exFile.getStackTrace())
            {
                Log.d("Common_FileUtil",
                    "FileNotFoundException : " + stackTraceElement.toString());
            }
        }
        catch (IOException exIO)
        {
            for (StackTraceElement stackTraceElement : exIO.getStackTrace())
            {
                Log.d("Common_FileUtil",
                    "FileNotFoundException : " + stackTraceElement.toString());
            }
        }
        finally
        {

        }

        return FILE_UTIL_STATUS_OTHER_EXCEPTION;
    }
    
    public static int appendContent2File(String filePath, String strContent)
            throws IOException
    {
        return appendContent2File(filePath, strContent, null);
    }
    
    public static int appendContent2File(String filePath, String strContent, String charset)
    		throws IOException
    {
    	//文件路径不能为空
        if (TextUtils.isEmpty(filePath))
        {
            return FILE_UTIL_STATUS_FILENAME_NULL;
        }
        //内容不能为空
        if (TextUtils.isEmpty(strContent))
        {
            return FILE_UTIL_STATUS_CONTENT_NULL;
        }
        
        return appendContent2File(new File(filePath), strContent, charset);
    }
    
    public static int appendContent2File(File fileWriteTo, String strContent)
            throws IOException
    {
        return appendContent2File(fileWriteTo, strContent, null);
    }

    /**
    * 
    * 把内容附加到指定文件--包括创建文件
    * @date 2012-2-29 
    * @param fileWriteTo 写入文件
    * @param strContent 要写的内容    
    * @return 0代表成功写入新文件，1代表文件已存在，其他代表错误码（3内容为空，4文件对象为空，5文件创建失败，10其他异常）
    * @throws IOException 
    */
    public static int appendContent2File(File fileWriteTo, String strContent, String charset)
            throws IOException
    {
        //文件对象不能为空
        if (null == fileWriteTo)
        {
            return FILE_UTIL_STATUS_FILEOBJECT_NULL;
        }
        //内容不能为空
        if (TextUtils.isEmpty(strContent))
        {
            return FILE_UTIL_STATUS_CONTENT_NULL;
        }
        
        if (!fileWriteTo.exists())
        {
        	File parent = fileWriteTo.getParentFile();
        	if (!parent.exists()) {
        		parent.mkdirs();
        	}
        	fileWriteTo.createNewFile();
        }

        FileOutputStream outputStream = new FileOutputStream(fileWriteTo, true);
        byte[] buffer = null;
        if (null != charset) {
            try {
                buffer = strContent.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                try {
                    buffer = strContent.getBytes("utf-8");
                } catch (UnsupportedEncodingException e1) {
                    
                }
            }
            
        }
        
        if (null == buffer) {
            buffer = strContent.getBytes();
        }
        
        outputStream.write(buffer);

        //关闭文件流
        outputStream.close();

        return FILE_UTIL_STATUS_SUCCESS;
    }
    
    public static int append2File(File fileWriteTo, String strContent) throws IOException
    {
        return append2File(fileWriteTo, strContent, null);
    }

    /**
     * 
     * 把内容附加到指定文件（此文件需新建）中
     * @date 2012-2-29 
     * @param fileWriteTo 待写的文件对象
     * @param strContent 要写的内容
     * @return 0代表成功写入新文件，1代表文件已存在，其他代表错误码（3内容为空，4文件对象为空，5文件创建失败，10其他异常）
    * @throws IOException 
     */
    public static int append2File(File fileWriteTo, String strContent, String charset) throws IOException
    {
        //文件对象不能为空
        if (null == fileWriteTo)
        {
            return FILE_UTIL_STATUS_FILEOBJECT_NULL;
        }
        //内容不能为空
        if (TextUtils.isEmpty(strContent))
        {
            return FILE_UTIL_STATUS_CONTENT_NULL;
        }

        if (fileWriteTo.exists())
        {
            return FILE_UTIL_STATUS_FILE_EXIST;
        }
        else
        {
            appendContent2File(fileWriteTo, strContent, charset);
        }

        return FILE_UTIL_STATUS_SUCCESS;

    }

    /**
    * 判断指定路径文件是否存在
    * @date 2012-3-16 
    * @param strFilePath 指定文件路径
    * @return 路径不存在或者为空则返回FILE_UTIL_STATUS_FILE_NOT_EXIST，存在返回FILE_UTIL_STATUS_SUCCESS   
    */
    public static int checkFileExist(String strFilePath)
    {
        if (TextUtils.isEmpty(strFilePath))
        {
            return FILE_UTIL_STATUS_FILE_NOT_EXIST;
        }

        File fileCheck = new File(strFilePath);
        if (fileCheck.exists())
        {
            return FILE_UTIL_STATUS_SUCCESS;
        }
        else
        {
            return FILE_UTIL_STATUS_FILE_NOT_EXIST;
        }
    }

    /**
     * 
     * 这里写方法名
     * <p>
     * Description: 这里用一句话描述这个方法的作用
     * <p>
     * @date 2014年3月7日 
     * @author Administrator
     * @param strFilePath
     * @return
     */
    public static int deleteFile(String strFilePath)
    {
        if (TextUtils.isEmpty(strFilePath))
        {
            return FILE_UTIL_STATUS_FILE_NOT_EXIST;
        }

        File fileCheck = new File(strFilePath);

        if (fileCheck.delete())
        {
            return FILE_UTIL_STATUS_SUCCESS;
        }
        else
        {
            return FILE_UTIL_STATUS_OTHER_EXCEPTION;
        }
    }

    /**
     * 
     * 循环删除文件或者文件夹所有子内容
     * <p>
     * Description: 递归删除文件和文件夹，如果是文件，直接删除，否则删除该文件夹的所有内容（包括文件 或者子文件夹等）
     * <p>
     * @date 2013年12月6日 
     * @author Randy.Wang 10125301
     * @param file 文件流
     */
    public static void recursionDeleteFile(File file)
    {
        if (null == file)
        {
            LogEx.w(LOG_TAG, "file is null!");
            return;
        }
        if (file.isFile())
        {
            file.delete();
            return;
        }
        if (file.isDirectory())
        {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0)
            {
                file.delete();
                return;
            }
            for (File f : childFile)
            {
                recursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 
     * 循环删除文件或者文件夹所有子内容
     * <p>
     * Description: 递归删除文件和文件夹，如果是文件，直接删除，否则删除该文件夹的所有内容（包括文件 或者子文件夹等）
     * <p>
     * @date 2013年12月6日 
     * @author Randy.Wang 10125301
     * @param strFilePath 文件或者文件夹路径
     */
    public static void recursionDeleteFile(String strFilePath)
    {
        File file = new File(strFilePath);
        recursionDeleteFile(file);
    }

    /**  
     *   
     * @param fromFile 被复制的文件  
     * @param toFile 复制的目录文件  
     * @param rewrite 是否重新创建文件  
     *   
     * <p>文件的复制操作方法  
     */
    public static boolean copyfile(File fromFile, File toFile, Boolean rewrite)
    {

        if (!fromFile.exists())
        {
            LogEx.w(LOG_TAG, "fromFile is not exits");
            return false;
        }

        if (!fromFile.isFile())
        {
            LogEx.w(LOG_TAG, "fromFile is not a file");
            return false;
        }
        if (!fromFile.canRead())
        {
            LogEx.w(LOG_TAG, "fromFile can't read");
            return false;
        }
        if (!toFile.getParentFile().exists())
        {
            LogEx.w(LOG_TAG, "toFile not read");
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite)
        {
            toFile.delete();
        }

        try
        {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);

            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            //关闭输入、输出流  
            fosfrom.close();
            fosto.close();

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * 
     * 这里写方法名
     * <p>
     * Description: 这里用一句话描述这个方法的作用
     * <p>
     * @date 2014年12月20日 
     * @author Administrator
     * @param strPath
     * @return
     */
    public static boolean isAssetFile(String strPath)
    {
        if (!TextUtils.isEmpty(strPath))
        {
            return strPath.startsWith(STR_TAG_LOCALIMG_ASSET);
        }
        else
        {
            return false;
        }
    }

    /**
     * 
     * 这里写方法名
     * <p>
     * Description: 这里用一句话描述这个方法的作用
     * <p>
     * @date 2014年12月20日 
     * @author Administrator
     * @param strURL
     * @return
     */
    public static boolean isLocalFile(String strURL)
    {
        if ((strURL != null && strURL.startsWith(FileUtil.STR_TAG_LOCALIMG))
            || (null != strURL && !strURL.startsWith("http://") && !strURL.startsWith("https://") 
            &&!strURL.startsWith("http")))
        {
            return true;
        }

        return false;
    }

    /**
     * 
     * 获取文件大小或者文件夹下所有内容大小
     * <p>
     * Description: 若是文件，则直接获取其文件大小，若是文件夹，使用递归计算文件夹下内容的总大小
     * <p>
     * @date 2014年08月13日 
     * @author cuiguohui 6407000220
     * @param file 文件或者文件夹
     */
    public static double getFileSize(File file)
    {
        //判断文件是否存在     
        if (file.exists())
        {
            //如果是目录则递归计算其内容的总大小    
            if (file.isDirectory())
            {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getFileSize(f);
                return size;
            }
            else
            {//如果是文件则直接返回其大小
                double size = (double) file.length();
                return size;
            }
        }
        else
        {
            Log.d(LOG_TAG, "Don't exsits file!!!");
            return 0.0;
        }
    }
}

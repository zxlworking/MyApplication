package com.zxl.river.chief.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;

import com.zxl.river.chief.R;
import com.zxl.river.chief.http.HttpUtil;
import com.zxl.river.chief.utils.CommonUtils;
import com.zxl.river.chief.utils.DebugUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by uidq0955 on 2017/12/1.
 */

public class TestUploadImageActivity extends BaseActivity {

    private static final String TAG = "TestUploadImageActivity";

    @Override
    public int getContentView() {
        return R.layout.test_activity;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        CommonUtils.checkStoragePermission(mActivity);

        HandlerThread mHandlerThread = new HandlerThread("test");
        mHandlerThread.start();

//        String path = "/storage/sdcard0/zxl_test_1/1024x1024/b047e.jpg";
        String path = "/storage/emulated/0/zxl_test_1/zxl_test_1/1024x1024/b047e.jpg";
        List<File> mFiles = new ArrayList<>();
        File file = new File(path);
        System.out.println("zxl--->file--->"+file.exists());
        mFiles.add(file);

        Subscription test = HttpUtil.uploadFiles(mFiles, new Handler(), new Subscriber<ResponseBody>() {
            @Override
            public void onStart() {
                super.onStart();
                DebugUtils.d(TAG,"HttpUtil.test::onStart--->"+Thread.currentThread());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    DebugUtils.d(TAG,"HttpUtil.test::onNext--->"+Thread.currentThread()+"--->"+responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DebugUtils.d(TAG,"HttpUtil.test::onError--->"+Thread.currentThread()+"--->"+e);
            }

            @Override
            public void onCompleted() {
                DebugUtils.d(TAG,"HttpUtil.test::onCompleted--->"+Thread.currentThread());
            }
        });
    }
}

/*
//设置编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        try {
            //设置系统环境
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //文件存储的路径
            String storePath = "D:\\test\\file";
            //判断传输方式  form  enctype=multipart/form-data
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if(!isMultipart)
            {
                pw.write("传输方式有错误！");
                return;
            }
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setFileSizeMax(4*1024*1024);//设置单个文件大小不能超过4M
            upload.setSizeMax(4*1024*1024);//设置总文件上传大小不能超过6M
            //监听上传进度
            upload.setProgressListener(new ProgressListener() {

                //pBytesRead：当前以读取到的字节数
                //pContentLength：文件的长度
                //pItems:第几项
                public void update(long pBytesRead, long pContentLength,
                        int pItems) {
                    System.out.println("已读去文件字节 :"+pBytesRead+" 文件总长度："+pContentLength+"   第"+pItems+"项");

                }
            });
            //解析
            Map<String,List<FileItem>> itemMap = upload.parseParameterMap(request);

            Set<String> keys = itemMap.keySet();

            for(String key : keys) {
            	List<FileItem> items = itemMap.get(key);
            	for(FileItem item: items){
                    if(item.isFormField()){  //普通字段，表单提交过来的
                        String name = item.getFieldName();
                        String value = item.getString("UTF-8");
                        System.out.println(name+"=="+value);
                    }else{
//                      String mimeType = item.getContentType(); 获取上传文件类型
//                      if(mimeType.startsWith("image")){
                        InputStream in =item.getInputStream();
                        String fileName = item.getName();
                        if(fileName==null || "".equals(fileName.trim())) {
                            continue;
                        }
                        fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
                        fileName = UUID.randomUUID()+"_"+fileName;

                        //按日期来建文件夹

                        String newStorePath = storePath;
                        String storeFile = newStorePath+"\\"+fileName;
                        OutputStream out = new FileOutputStream(storeFile);
                        byte[] b = new byte[1024];
                        int len = -1;
                        while((len = in.read(b))!=-1){
                             out.write(b,0,len);
                        }
                        in.close();
                        out.close();
                        item.delete();//删除临时文件
                    }
                  }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
*/
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.ProgressListener;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;


public class Test extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("zxl--->Test");
		//设置编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        try {
            //设置系统环境
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //文件存储的路径
            String storePath = "E:\\test\\file";
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
                        System.out.println("zxl--->para--->"+name+"=="+value);
                    }else{
//                      String mimeType = item.getContentType(); 获取上传文件类型
//                      if(mimeType.startsWith("image")){
                        InputStream in = item.getInputStream();
                        String fileName = item.getName();
                        System.out.println("zxl--->fileName--->"+fileName+"--->"+item.getFieldName());
                        if(fileName==null || "".equals(fileName.trim())) {
                            continue;
                        }
                        fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
                        System.out.println("zxl--->fileName2--->"+fileName);
                        //fileName = UUID.randomUUID()+"_"+fileName;
                        
                        fileName = ""+UUID.randomUUID();
                        System.out.println("zxl--->fileName3--->"+fileName);

                        //按日期来建文件夹

                        String newStorePath = storePath;
                        String storeFile = newStorePath+"\\"+fileName;
                        
                        System.out.println("zxl--->storeFile--->"+storeFile);
                        
                        File mStoreFile = new File(storeFile);
                        
                        System.out.println("zxl--->mStoreFile.exists()--->"+mStoreFile.exists());
                        
                        if(!mStoreFile.exists()){
                        	mStoreFile.createNewFile();
                        }
                        
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
        	response.getWriter().write("zxl--->e--->"+e.toString());
            e.printStackTrace();
        }
        response.getWriter().write("zxl--->ok");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}

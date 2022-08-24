package com.ljp.controller;

import com.ljp.concur.FtpUploadRunnable;
import com.ljp.util.DateUtils;
import com.ljp.util.ResponseBeanUtils;
import com.ljp.vo.ResponseBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
@RequestMapping("/")
@CrossOrigin
public class FileUploadController {

    @Value("${webapi.upload.img.file.folder}")
    private String uploadFileFolder;
    @Value("${webapi.upload.sftp.username}")
    private String username;
    @Value("${webapi.upload.sftp.password}")
    private String password;
    @Value("${webapi.upload.sftp.host}")
    private String host;
    @Value("${webapi.upload.sftp.port}")
    private int port;
    @Value("${webapi.file.path}")
    private String webApiPath;

    @PostMapping(value = "/imgsUpload")
    public ResponseBean upload(HttpServletRequest request,
                        @RequestParam(value = "files", required = false)
                                MultipartFile[] files) {
        List<Map<String, String>> rtnList =
                new ArrayList<Map<String, String>>();
        ResponseBean responseBean = ResponseBeanUtils.buildErrorBean();
        if (files == null || files.length == 0) {
            responseBean.setRspMsg("并未发现文件，请重新选择文件提交。");
        } else {
            for (MultipartFile file : files){
                // 文件后缀
                String extend = file.getOriginalFilename()
                        .substring(file.getOriginalFilename()
                        .lastIndexOf(".") + 1)
                        .toLowerCase();
                // 通过后缀名检查是否是图片文件
                if (isImg(extend)){
                    try{
                        InputStream is = file.getInputStream();
                        ByteArrayOutputStream baos =
                                new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];

                        int len=-1;

                        while ( (len = is.read(buffer)) !=-1) {
                            baos.write(buffer, 0, len);
                        }
                        baos.flush();

                        InputStream input1 =
                                new ByteArrayInputStream(baos.toByteArray());


                        String saveFileName = UUID.randomUUID().toString()
                                + "." + extend;
                        float fileSize = Float.valueOf((float) file.getSize())
                                .floatValue();
                        String diskPath = uploadFileFolder + "/upload/"
                                + DateUtils.getSortSystemTime();

                        // 上传文件到FTP服务器的线程的启动
                        FtpUploadRunnable ftpUploadRunnable =
                                new FtpUploadRunnable(username, password, host,
                                        port, diskPath, saveFileName, input1);
                        Thread threadStarter = new Thread(ftpUploadRunnable);
                        threadStarter.start();

                        if(input1!= null){
                            input1.close();
                        }
                        // 查看图片大小
                        String width = "0";
                        String  heigth = "0";
                        if (isImg(extend)) {
                            InputStream input2 = new ByteArrayInputStream(
                                    baos.toByteArray()
                            );
                            BufferedImage bis = ImageIO.read(input2);
                            width = bis.getWidth()+"";
                            heigth = bis.getHeight()+"";
                            if(input2!= null){
                                input2.close();
                            }
                        }

                        Map<String, String> tmpImgInfoMap = new HashMap<String, String>();
                        tmpImgInfoMap.put("mime", extend);
                        tmpImgInfoMap.put("fileName", saveFileName);
                        tmpImgInfoMap.put("fileSize", Float.valueOf(fileSize) + "");
                        tmpImgInfoMap.put("width",width);
                        tmpImgInfoMap.put("height", heigth);
                        tmpImgInfoMap.put("oldName", file.getOriginalFilename());
                        tmpImgInfoMap.put("webApiPath", webApiPath);
                        tmpImgInfoMap.put("urlPath", diskPath + "/" + saveFileName);
                        rtnList.add(tmpImgInfoMap);
                    } catch (Exception e) {
                        responseBean.setRspMsg("上传失败");
                    }
                } else {
                    //这里可以另行其他非图片文件的处理。
                }
            }
        }
        responseBean = ResponseBeanUtils.buildSuccessBean();
        responseBean.setData(rtnList);
        return responseBean;

    }

    public static boolean isImg(String extend) {
        boolean ret = false;
        if ("jpg".equals(extend)) {
            ret = true;
        } else if ("jpeg".equals(extend)) {
            ret = true;
        } else if ("bmp".equals(extend)) {
            ret = true;
        } else if ("gif".equals(extend)) {
            ret = true;
        } else if ("tif".equals(extend)) {
            ret = true;
        } else if ("png".equals(extend)) {
            ret = true;
        }
        return ret;
    }

}

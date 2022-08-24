package com.ljp.concur;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;

public class FtpUploadRunnable implements Runnable {
    private  String username;

    private  String password;

    private  String host;

    private  int port;

    private String filePath;

    private String fileName;

    private InputStream input;

    public FtpUploadRunnable(String username, String password,
                             String host, int port, String filePath,
                             String fileName, InputStream input) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.filePath = filePath;
        this.fileName = fileName;
        this.input = input;
    }

    @Override
    public void run() {
        FTPClient ftpClient = new FTPClient();
        try {
            int reply;
            // 连接FTP服务器
            ftpClient.connect(host, port);
            // 如果允许匿名登录，则可以使用anonymous以及空密码进行登录
            ftpClient.login(username, password);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }
            //切换到上传目录
            if (!ftpClient.changeWorkingDirectory(filePath)) {
                //如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = "";
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)){
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {
                        if (!ftpClient.makeDirectory(tempPath)) {
                        } else {
                            ftpClient.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            //设置上传文件的类型为二进制类型
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            //上传文件
            ftpClient.storeFile(
                    new String(fileName.getBytes("UTF-8"),
                            "iso-8859-1"), input) ;
            input.close();
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
    }
}

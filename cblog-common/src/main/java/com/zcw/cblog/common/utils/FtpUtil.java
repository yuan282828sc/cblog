package com.zcw.cblog.common.utils;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.Properties;

@Component
public class FtpUtil {
    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);
    /**
     * ftp服务器ip地址
     */
    @Value("${ftp.host}")
    private static String host = "192.168.60.137";

//    public void setHost(String val){
//        FtpUtil.host = val;
//    }
    /**
     * 端口
     */
    @Value("${ftp.port}")
    private static int port = 22;

//    public void setPort(int val){
//        FtpUtil.port = val;
//    }
    /**
     * 用户名
     */
    @Value("${ftp.userName}")
    private static String userName = "root";

//    public void setUserName(String val){
//        FtpUtil.userName = val;
//    }
    /**
     * 密码
     */
    @Value("${ftp.password}")
    private static String password = "z123";

//    public void setPassword(String val){
//        FtpUtil.password = val;
//    }
    /**
     * 存放图片的根目录
     */
    @Value("${ftp.rootPath}")
//    private static String rootPath = "/usr/share/nginx/static/cblog/images/avatar";
    private static String rootPath = "/mydata/nginx/html";
//    public void setRootPath(String val){
//        FtpUtil.rootPath = val;
//    }
    /**
     * 存放图片的路径
     */
    @Value("${ftp.img.url}")
    private static String imgUrl = "http://192.168.60.137:80/";

//    public void setImgUrl(String val){
//        FtpUtil.imgUrl = val;
//    }
    /**
     * 获取连接
     */
    private static ChannelSftp getChannel() throws Exception{
        JSch jsch = new JSch();
        //->ssh root@host:port
        Session sshSession = jsch.getSession(userName,host,port);
        //密码
        sshSession.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();
        Channel channel = sshSession.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }
    /**
     * ftp上传图片
     * @param inputStream 图片io流
     * @param imagePath 路径，不存在就创建目录
     * @param imagesName 图片名称
     * @return urlStr 图片的存放路径
     */
    public static Integer putImages(InputStream inputStream, String imagesName,String p){
        try {
            ChannelSftp sftp = getChannel();
            String path = rootPath + p;
            createDir(path,sftp);
            //上传文件
            sftp.put(inputStream, path + imagesName);
            logger.info("上传成功！");
            sftp.quit();
            sftp.exit();
//            //处理返回的路径
//            String resultFile;
//            resultFile = imgUrl  + imagesName;
            return 1;
        } catch (Exception e) {
            logger.error("上传失败：" + e.getMessage());
        }
        return 0;
    }
    /**
     * 创建目录
     */
    private static void createDir(String path,ChannelSftp sftp) throws SftpException {
        String[] folders = path.split("/");
        sftp.cd("/");
        for ( String folder : folders ) {
            if ( folder.length() > 0 ) {
                try {
                    sftp.cd( folder );
                }catch ( SftpException e ) {
                    sftp.mkdir( folder );
                    sftp.cd( folder );
                }
            }
        }
    }
    /**
     * 删除图片
     */
    public static void delImages(String imagesName){
        try {
            ChannelSftp sftp = getChannel();
            String path = rootPath + imagesName;
            sftp.rm(path);
            sftp.quit();
            sftp.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

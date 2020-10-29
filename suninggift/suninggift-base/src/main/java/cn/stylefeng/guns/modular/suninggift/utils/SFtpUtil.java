package cn.stylefeng.guns.modular.suninggift.utils;


import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

/**
 * 
 * @ClassName: SFTPUtil
 * @Description: sftp连接工具类
 * @date 2017年5月22日 下午11:17:21
 * @version 1.0.0
 */
@Slf4j
public class SFtpUtil {

    private ChannelSftp sftp;
      
    private Session session;
    /** FTP 登录用户名*/  
    private String username;
    /** FTP 登录密码*/  
    private String password;
    /** 私钥 */  
    private String privateKey;
    /** FTP 服务器地址IP地址*/  
    private String host;
    /** FTP 端口*/
    private int port;
      
  
    /** 
     * 构造基于密码认证的sftp对象
     * @param password 
     * @param host 
     * @param port 
     */  
    public SFtpUtil(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }
  
    /** 
     * 构造基于秘钥认证的sftp对象
     * @param host
     * @param port
     * @param privateKey
     */
    public SFtpUtil(String username, String host, int port, String privateKey) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }
  
    public SFtpUtil(){}
  
    /**
     * 连接sftp服务器
     *
     * @throws Exception 
     */
    public void login(){
        try {
            JSch jsch = new JSch();
            if (privateKey != null) {
                jsch.addIdentity(privateKey);// 设置私钥
                log.info("sftp connect,path of private key file：{}," + privateKey);
            }
            log.info("sftp connect by host:{} username:{},"+ host+ ","+username);
  
            session = jsch.getSession(username, host, port);
            log.info("Session is build");
            if (password != null) {
                session.setPassword(password);  
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
              
            session.setConfig(config);
            session.connect();
            log.info("Session is connected");
            
            Channel channel = session.openChannel("sftp");
            channel.connect();
            log.info("channel is connected");
  
            sftp = (ChannelSftp) channel;
            log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
        } catch (JSchException e) {
            log.info("Cannot connect to specified sftp server : {}:{} \n Exception message is: {}, " + new Object[]{host, port, e.getMessage()});
        }
    }  
  
    /**
     * 关闭连接 server 
     */
    public void logout(){
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                log.info("sftp is closed already");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                log.info("sshSession is closed already");
            }
        }
    }
  
    /** 
     * 将输入流的数据上传到sftp作为文件 
     *  
     * @param directory 
     *            上传到该目录 
     * @param sftpFileName 
     *            sftp端文件名
     *            输入流 
     * @throws SftpException  
     * @throws Exception 
     */  
    public boolean upload(String directory, String sftpFileName, InputStream input) throws SftpException{
        try {  
            sftp.cd(directory);
        } catch (SftpException e) {
            log.error("异常", e);
            this.createDir(directory);
            sftp.cd(directory);
            return false;
        }
        sftp.put(input, sftpFileName);
        log.info("file:{} is upload successful" + sftpFileName);
        return false;
    }
  
    /** 
     * 上传单个文件
     *
     * @param directory 
     *            上传到sftp目录 
     * @param uploadFile
     *            要上传的文件,包括路径 
     * @throws FileNotFoundException
     * @throws SftpException
     * @throws Exception
     */
    public boolean upload(String directory, String uploadFile) throws FileNotFoundException, SftpException{
        File file = new File(uploadFile);
        return upload(directory, file.getName(), new FileInputStream(file));
    }
  
    /**
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。
     * 
     * @param directory
     *            上传到sftp目录
     * @param sftpFileName
     *            文件在sftp端的命名
     * @param byteArr
     *            要上传的字节数组
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException{
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));
    }
  
    /** 
     * 将字符串按照指定的字符编码上传到sftp
     *  
     * @param directory
     *            上传到sftp目录
     * @param sftpFileName
     *            文件在sftp端的命名
     * @param dataStr
     *            待上传的数据
     * @param charsetName
     *            sftp上的文件，按该字符编码保存
     * @throws UnsupportedEncodingException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException{  
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));  
    }
  
    /**
     * 下载文件 
     *
     * @param directory
     *            下载目录 
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     * @throws SftpException
     * @throws FileNotFoundException
     * @throws Exception
     */  
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException{
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
        log.info("file:{} is download successful," + downloadFile);
    }
    /** 
     * 下载文件
     * @param directory 下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     * @throws SftpException
     * @throws IOException
     * @throws Exception
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException{
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);
        
        byte[] fileData = IOUtils.toByteArray(is);
        
        log.info("file:{} is download successful," + downloadFile);
        return fileData;
    }
  
    /**
     * 删除文件
     *  
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @throws SftpException
     * @throws Exception
     */
    public void delete(String directory, String deleteFile) throws SftpException{
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }
  
    /**
     * 列出目录下的文件
     * 
     * @param directory
     *            要列出的目录
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }
    
//    public static void main(String[] args) throws SftpException, IOException {
////        SFTPUtil sftp = new SFTPUtil("mysftp", "daihou99", "59.110.219.43", 52117);
//        //秘钥文件登录
//    	SFtpUtil sftp = new SFtpUtil("SFTP_DSFQ_LTWK_SDJY","59.110.219.43",52121,"C:\\gzlp\\ftp\\webank-test");
//        sftp.login();
//        //File file = new File("C:\\Users\\Administrator\\Desktop\\ftptest.txt");
//        //InputStream is = new FileInputStream(file);
//
//        //sftp.upload("/upload", "ftptest1.txt", is);
////        sftp.download("/upload", "ftptest.txt", "C:\\Users\\Administrator\\Desktop\\test");
//        log.info("文件列表");
//        Vector<LsEntry> listFiles = (Vector<LsEntry>)sftp.listFiles("/webank_push/REPAYMENT/20190424/");
//        for (LsEntry lsEntry : listFiles) {
//            log.info(lsEntry.getFilename());
//                    if(!lsEntry.getFilename().equals(".")&&!lsEntry.getFilename().equals("..")){
//                        sftp.download("/webank_push/REPAYMENT/20190424/", lsEntry.getFilename(), "C:\\lepeng\\webank\\"+lsEntry.getFilename());
//
//                    }
//
//        }
//        sftp.logout();
//    }

    /**
     * 创建一个文件目录
     */
    public void createDir(String createpath) {
        try {
            if (isDirExist(createpath)) {
                this.sftp.cd(createpath);
                return;
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString())) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                    log.info("目录不存在，现已成功创建目录： === " + path);
                }
            }
        } catch (SftpException e) {
            log.error("系统错误",e);
        }
    }

    /**
     * 判断目录是否存在
     */
    public boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            log.error("系统错误", e);
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    public SftpATTRS stat(String dir) {
        try {
            return sftp.stat(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public OutputStream put(String dst){
        try{
            return sftp.put(dst);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}

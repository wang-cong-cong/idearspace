package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

/**
 * @author cong
 */
@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String trackerServerUrl;

    @RequestMapping("/upload.do")
    public Result upload(MultipartFile file){

        try {
            //获取上传文件的原始名称
            String originalFilename = file.getOriginalFilename();
            //获取扩展名
            String extensionName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            //将上传的文件保存到FastDFS服务器
            String uploadFile = client.uploadFile(file.getBytes(), extensionName);

            //Fast服务器保存好文件的路径
            String complationURl = trackerServerUrl+uploadFile;

            return new Result(true,complationURl);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}

package com.tan.controller;

import com.tan.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * @author TanS
 * @date 2022/4/13
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${takeout.path}")
    private String path;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R upload(MultipartFile file) {
        String s = UUID.randomUUID().toString();
        String s1 = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        String fileName = s + "." + s1;
        File isFile = new File(path);
        if (!isFile.exists()) {
            isFile.mkdirs();
        }
        try {
            file.transferTo(new File(path + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载，主要通过响应流发送
     *
     * @param name
     * @param response
     * @return
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        FileInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(path + name));
            outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int length = 0;
            byte[] bytes = new byte[1024];

            while ((length =inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

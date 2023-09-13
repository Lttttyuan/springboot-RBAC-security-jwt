package com.example.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")
    private String port;

    private static final String ip = "http://localhost";

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); //获取源文件名称
        //定义文件的唯一标识（前缀）
        String flag = IdUtil.fastSimpleUUID();
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "static/files/";

        String rootFilePath = realPath + flag + "_" + originalFilename;//获取上传的路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath);//把文件写入到上传的路径
        return Result.success(ip + ":" + port + "/files/" + flag); //返回结果 url
    }

    /**
     * 下载文件
     *
     * @param flag
     * @param response
     * @throws FileNotFoundException
     */
    @GetMapping("/{flag}")
    public void getFiles(@PathVariable String flag, HttpServletResponse response) throws FileNotFoundException {
        OutputStream os; //新建一个输出流
        String basePath = ResourceUtils.getURL("classpath:").getPath() + "static/files/"; //定义文件上传的根路径


        List<String> fileNames = FileUtil.listFileNames(basePath);

        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");//找到跟参数一致的文件

        try {
            if (StrUtil.isNotEmpty(fileName)) {

                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");

                byte[] bytes = FileUtil.readBytes(basePath + fileName);//通过文件的路径读取文件的字节流

                os = response.getOutputStream(); //通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

}

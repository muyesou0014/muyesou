package com.example.upload.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class FileController {

    /**上传地址*/
    @Value("${file.upload.path}")//${student.birthday}
    private String filePath;

    public FileController() throws FileNotFoundException {
    }

    // 跳转上传页面
    @RequestMapping("test")
    public String test() {
        return "uploadFile";
    }

    // 执行上传
//    @RequestMapping("")
    public void upload(@RequestParam("file") MultipartFile file, Model model) {
        // 获取上传文件名
        String filename = file.getOriginalFilename();
        // 定义上传文件保存路径
        String path = filePath;
        // 新建文件
        File filepath = new File(path, filename);
        // 判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        try {
            // 写入文件
            file.transferTo(new File(path + File.separator + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imgUrl="http://39.108.128.165:8080/images/"+filename;
        String imgInput="<p><img src=\"" + imgUrl + "\"  style=\"width:1125px;height:1160px;\"/></p>";
        if(model.getAttribute("filename")!=null){
            List<String> list = (ArrayList<String>)model.getAttribute("filename");
            list.add(imgInput);
            model.addAttribute("filename",list);
        }else {
            // 将src路径发送至html页面

            model.addAttribute("filename", Stream.of(imgInput).collect(Collectors.toList()));
        }
        return ;
    }
    @RequestMapping("upload")
    @ResponseBody
    public List<String> upload2(MultipartFile[] attachment,@RequestParam("applyId") String applyId, Model model){

        if(attachment!=null&&attachment.length>0) {
            for (int i = 0; i <attachment.length ; i++) {
                upload(attachment[i],model);
            }
        }
        return (List<String>)model.getAttribute("filename");
    }


}
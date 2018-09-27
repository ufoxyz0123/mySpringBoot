package cn.fzk.mySpringBoot.controller;



import cn.fzk.mySpringBoot.Util.ResModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/16.
 */
@Controller
public class FileUploadController{

    @RequestMapping("/file")
    public String file(){
        return"/file";
    }

    @RequestMapping("/email")
    public String moreFile(){
        return"email/email";
    }


    @RequestMapping(value = "/upload",produces="text/plain;charset=UTF-8")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file")MultipartFile file){
        if(!file.isEmpty()){
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File("E:\\data\\"+file.getOriginalFilename())));
                out.write(file.getBytes());
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return"上传失败,"+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return"上传失败,"+e.getMessage();
            }
            return"上传成功";
        }else{
            return"上传失败，因为文件是空的.";
        }
    }

    @RequestMapping(value="/batch/upload", method= RequestMethod.POST)
    @ResponseBody
    public ResModel handleFileUpload(HttpServletRequest request){
        ResModel resModel = new ResModel(0,"操作成功");
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File("E:\\data\\"+file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                    Map<String,String> map = new HashMap<>();
                    map.put("name",file.getOriginalFilename());
                    map.put("path","E:\\data\\"+file.getOriginalFilename());
                    resModel.setRows(map);
                } catch (Exception e) {
                    stream =  null;
                    resModel.setCode(-1);
                    resModel.setDesc("上传异常，具体异常："+e.getMessage());
                    return resModel;
                }
            } else {
                resModel.setCode(-1);
                resModel.setDesc("上传失败，因为文件为空");
                return resModel;
            }
        }
        return resModel;
    }

}

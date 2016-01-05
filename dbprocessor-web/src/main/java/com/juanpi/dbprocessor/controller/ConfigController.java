package com.juanpi.dbprocessor.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.juanpi.dbprocessor.biz.DBProcessorService;
import com.juanpi.dbprocessor.biz.TopicService;
import com.juanpi.dbprocessor.entity.DBProcessorEntity;
import com.juanpi.dbprocessor.entity.TopicEntity;
import com.juanpi.dbprocessor.entity.TypeEntity;

@Controller
@RequestMapping("/configController")
public class ConfigController {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private TopicService topicService;
    
    @Resource
    private DBProcessorService dbProcessorService;
    
    private String[] types;
    
    
    @RequestMapping("/config")
    public ModelAndView config(@RequestParam("host") String host){
        ModelAndView view = new ModelAndView("dbprocessor/config");
        List<TypeEntity> topicTypeList = this.topicService.getFreeTopicType(host);
        view.addObject("topicTypeList", topicTypeList);
        view.addObject("host", host);
        return view;
    }
    
    @RequestMapping("/saveConfig")
    public ModelAndView saveConfig(@RequestParam("host") String host,HttpServletRequest request){
        String[] types = request.getParameterValues("type");
        this.topicService.pushTopicConfig(host, types);
        ModelAndView modelAndView = dbprocessorList();
        modelAndView.addObject("message", "修改成功");
        return modelAndView;
    }
    
    @RequestMapping("/dbprocessorList")
    public ModelAndView dbprocessorList(){
        ModelAndView view = new ModelAndView("dbprocessor/dbprocessorList");
        List<DBProcessorEntity> dbprocessorList = this.dbProcessorService.findDBProcessorList();
        List<String> topicTypeList = this.topicService.getAllTopicType();
        view.addObject("topicTypeList", topicTypeList);
        view.addObject("dbprocessorList", dbprocessorList);
        return view;
    }
    
    @RequestMapping("/topicList")
    public ModelAndView topicList(HttpServletRequest request){
        ModelAndView view = new ModelAndView("dbprocessor/topicList");
        String topicType = request.getParameter("topicType");
        List<String> topicTypeList = this.topicService.getAllTopicType();
        List<TopicEntity> topicList = new ArrayList();
        if(null != topicType && topicType.length() > 0){
            topicList = this.topicService.findTopicByType(topicType);
            view.addObject("topicType", topicType);
        }else{
            topicList = this.topicService.findAllTopic();
        }
        view.addObject("topicList", topicList);
        view.addObject("topicTypeList", topicTypeList);
        return view;
    }
    
    @RequestMapping("/upload")
    public ModelAndView upload(){
        ModelAndView view = new ModelAndView("dbprocessor/upload");
        boolean upload = true;
        List<DBProcessorEntity> dbProcessorList = dbProcessorService.findDBProcessorList();
        if(dbProcessorList.size() > 0){
            for (DBProcessorEntity dbProcessorEntity : dbProcessorList) {
                if(dbProcessorEntity.getTopicTypeList().size() > 0){
                    upload = false;
                    break;
                }
            }
        }
        //是否已上传配置并且已经被使用
        view.addObject("upload", upload);
        return view;
    } 
    
    
    @RequestMapping(value="/uploadFile", method=RequestMethod.POST)  
    public @ResponseBody String uploadFile(@RequestParam("file") CommonsMultipartFile[] files){  
        for(int i = 0;i<files.length;i++){  
            if(!files[i].isEmpty()){  
                try {  
                   saveFile(files[i]);
                   topicService.saveTopic(files[i].getInputStream());
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return "上传成功";
    }  
    
    private void saveFile(CommonsMultipartFile file){
        try {
            String fileUrl = ConfigController.class.getResource("/").toString();
            fileUrl = fileUrl.replace("file:/", "");
            log.info("fileUrl:" + fileUrl);
            String localFileUrl = fileUrl + file.getOriginalFilename();
            FileOutputStream os = new FileOutputStream(localFileUrl);
            InputStream in =  file.getInputStream();  
            //以写字节的方式写文件  
            int b = 0;  
            while((b=in.read()) != -1){  
                os.write(b);  
            }  
            os.flush();  
            os.close();  
            in.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        } 
         
    }
    
    public String[] getTypes() {
        return types;
    }

    
    public void setTypes(String[] types) {
        this.types = types;
    }
    
    
}

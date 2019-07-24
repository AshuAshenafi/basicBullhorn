package com.example.demo;


import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    messageRepository msgRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listMessages(Model model){
        model.addAttribute("msgs", msgRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String todoForm(Model model){
        model.addAttribute("msg", new Messages());
        return "messageform";
    }

    @PostMapping("/add")
    public String processMessage(@ModelAttribute Messages msg,
                                 @RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            return "redirect:/add";
        }
        try{
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
                    msg.setHeadshot(uploadResult.get("url").toString());
                    msgRepository.save(msg);
                    }
        catch (IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
    }

    @PostMapping("/process")
    public String processForm(@Valid Messages msg, BindingResult result){
        if(result.hasErrors()){
            return "messageform";
        }

        msgRepository.save(msg);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("msg", msgRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("msg", msgRepository.findById(id).get());
        return "messageform";
    }

    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id){
        msgRepository.deleteById(id);
        return "redirect:/";
    }

}
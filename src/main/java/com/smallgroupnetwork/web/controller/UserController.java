package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.model.Attachment;
import com.smallgroupnetwork.model.User;
import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.service.IUserService;
import com.smallgroupnetwork.web.util.AttachmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * User: gleb
 * Date: 8/4/2014
 * Time: 1:13 PM
 */
@Controller
@RequestMapping(Routes.USER)
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = {"/avatar", "/avatar/"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Attachment updateAvatar(@RequestParam(required = true, value = "attachment") MultipartFile file) throws IOException {
        Attachment attachment = AttachmentUtil.createAttachment(file);
        User user = AccountHolder.getUser();
        attachment = userService.updateAvatar(user.getId(), attachment);
        user.setAvatar(attachment);
        return attachment;
    }

//    @RequestMapping(value = {"/", ""}, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public User update(@RequestBody User user) {
//        userService.saveOrUpdate(user);
//        return userService.read(user.getId());
//    }

}

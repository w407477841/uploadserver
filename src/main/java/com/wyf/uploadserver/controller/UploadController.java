package com.wyf.uploadserver.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.wyf.uploadserver.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@Slf4j
public class UploadController {

    @Autowired
    private UploadProperties properties;

    /**
     * 本地上传文件接口
     *
     * @return
     * @throws IOException
     */
    @CrossOrigin
    @PostMapping("/upload")
    public Object upload(String username, MultipartFile file, HttpServletRequest req) throws IOException {

        Map<String, Object> result = new HashMap<>();
        if (StrUtil.isBlank(username)) {
            result.put("code", 400);
            result.put("msg", "缺少参数[username]");
            return result;
        }
        if (null == file) {
            result.put("code", 400);
            result.put("msg", "缺少参数[file]");
            return result;
        }


        //获取上传时的文件名称
        String filename = file.getOriginalFilename();
        filename = newFileName(filename);

        File f = new File(properties.getBaseUrl() + username, filename);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        if (!f.exists()) {
            f.createNewFile();
        }
        //将上传的文件存储到指定位
        file.transferTo(f);
        log.info("用户[{}],原文件[{}],新文件[{}]", username, file.getOriginalFilename(), filename);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", "/" + username + "/" + filename);
        return result;

    }


    private String newFileName(String filename) {
        String type = StrUtil.sub(filename, StrUtil.indexOf(filename, '.'), filename.length());

        return UUID.randomUUID().toString() + type;
    }


}

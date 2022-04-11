package com.fsoft.apidemo.controller;

import com.fsoft.apidemo.models.ResponseObject;
import com.fsoft.apidemo.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/fileUpload")
public class FileUploadController {
    @Autowired
    private IStorageService storageService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file")MultipartFile file){
        try {
            String generatedFileName = storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Upload file successfully", generatedFileName)
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", e.getMessage(), "")
            );
        }
    }

    //get image's url
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName){
        try {
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(bytes);
        } catch (Exception e){
            return ResponseEntity.noContent().build();
        }
    }

    //load all uploaded files
    @GetMapping("")
    public ResponseEntity<ResponseObject> getuploadedFiles(){
        try {
            List<String> urls = storageService.loadAll()
                    .map(path -> {
                        //convert fileName to url(send request "readDetailFile")
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "readDetailFile", path.getFileName().toString()).build().toUri().toString();
                    return urlPath;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseObject("OK", "List files successfully", urls));
        } catch (Exception e){
            return ResponseEntity.ok(new ResponseObject("Failed", "List files failed", new String[]{}));

        }
    }
}

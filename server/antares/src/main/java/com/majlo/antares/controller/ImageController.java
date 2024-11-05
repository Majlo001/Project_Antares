package com.majlo.antares.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.majlo.antares.service.ImageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping(value="/upload", consumes=MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = imageService.saveImage(file);
            return new ResponseEntity<>(fileUrl, HttpStatus.OK);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Błąd podczas przesyłania pliku!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String filename) {
        try {
            byte[] image = imageService.getImage(filename);
            String fileExtension = filename.substring(filename.lastIndexOf(".") + 1);
            MediaType mediaType = fileExtension.equals("jpg") || fileExtension.equals("jpeg")
                    ? MediaType.IMAGE_JPEG
                    : MediaType.IMAGE_PNG;

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(image);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

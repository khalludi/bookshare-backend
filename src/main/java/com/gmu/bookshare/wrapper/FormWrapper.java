package com.gmu.bookshare.wrapper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class FormWrapper {

    private List<MultipartFile> images;

    private String data;
}

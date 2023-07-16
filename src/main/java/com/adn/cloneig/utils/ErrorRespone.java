package com.adn.cloneig.utils;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorRespone {
    private Date timestamp = new Date();
    private Integer status;
    private String error;
    private String path;
    private List<String> message;

//    public ErrorRespone(Integer status, String error, String path, List<String> message) {
//        this.status = status;
//        this.error = error;
//        this.path = path;
//        this.message = message;
//    }

}

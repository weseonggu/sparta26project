package com.sparta26.baemin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FailMessage {
    private String timeStamp;
    private String endPoint;
    private List<String> errorDetails;
}

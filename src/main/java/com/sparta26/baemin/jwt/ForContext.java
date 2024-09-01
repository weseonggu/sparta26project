package com.sparta26.baemin.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForContext {

    private Long id;
    private String email;
    private String role;
}

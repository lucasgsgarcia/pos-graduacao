package br.edu.utfpr.exemplo.model.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long id;

    private String Name;

    private String Email;

    private String Password;

    private String PhoneNumber;

    private String Document;
}

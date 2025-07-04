package br.edu.utfpr.exemplo.controller.requests.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}

package br.edu.utfpr.exemplo.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppExceptionBase extends Exception {

    private int errorCode;
    private String message;

    public AppExceptionBase(String message, int codeDescription) {
        this.errorCode = codeDescription;
        this.message = message;
    }
}
package br.edu.utfpr.exemplo.exceptions;

public class NotFoundException extends AppExceptionBase {

    public NotFoundException(String message, int codeDescription) {
        super(message, codeDescription);
        this.setErrorCode(codeDescription);
        this.setMessage(message);
    }
}

package br.edu.utfpr.exemplo.exceptions.authorization;

import br.edu.utfpr.exemplo.exceptions.AppExceptionBase;

public class UnauthorizedOperationException extends AppExceptionBase {

    public UnauthorizedOperationException(String message, int codeDescription) {
        super(message, codeDescription);
        this.setMessage(message);
        this.setErrorCode(codeDescription);
    }
}

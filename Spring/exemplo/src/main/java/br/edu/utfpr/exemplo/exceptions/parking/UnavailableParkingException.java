package br.edu.utfpr.exemplo.exceptions.parking;

import br.edu.utfpr.exemplo.exceptions.AppExceptionBase;

public class UnavailableParkingException extends AppExceptionBase {

    public UnavailableParkingException(String message, int codeDescription) {
        super(message, codeDescription);
        this.setMessage(message);
        this.setErrorCode(codeDescription);
    }
}

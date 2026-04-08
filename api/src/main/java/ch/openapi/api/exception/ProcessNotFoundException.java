package ch.openapi.api.exception;

public class ProcessNotFoundException extends RuntimeException {

    public ProcessNotFoundException(String processId) {
        super("Process not found: " + processId);
    }
}

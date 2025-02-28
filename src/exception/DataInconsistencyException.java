package exception;

/*
 * Exceção customizada para indicar inconsistência de dados.
 */
public class DataInconsistencyException extends Exception {

    public DataInconsistencyException(String message) {
        super(message);
    }

    public DataInconsistencyException(String message, Throwable cause) {
        super(message, cause);
    }
}

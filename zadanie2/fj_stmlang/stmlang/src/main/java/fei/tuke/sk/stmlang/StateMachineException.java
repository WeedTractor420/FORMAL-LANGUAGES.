package fei.tuke.sk.stmlang;

/**
 * A custom exception class for errors encountered during the parsing and handling
 * of state machine definitions.
 */
public class StateMachineException extends RuntimeException {

    /**
     * Constructs a new state machine exception with {@code null} as its
     * detail message. The cause is not initialized.
     */
    public StateMachineException() {
        super();
    }

    /**
     * Constructs a new state machine exception with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     */
    public StateMachineException(String message) {
        super(message);
    }

    /**
     * Constructs a new state machine exception with the specified detail message
     * and cause.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   The cause (which is saved for later retrieval by the
     *                {@link #getCause()} method). (A {@code null} value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public StateMachineException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new state machine exception with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of {@code cause}).
     *
     * @param cause The cause (which is saved for later retrieval by the
     *              {@link #getCause()} method). (A {@code null} value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    public StateMachineException(Throwable cause) {
        super(cause);
    }
}

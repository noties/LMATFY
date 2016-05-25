package ru.noties.lmatfy;

/**
 * Created by Dimitry Ivanov on 25.05.2016.
 */
public class AttachException extends IllegalStateException {

    static AttachException newInstance(String message, Object... args) {
        return new AttachException(String.format(message, args));
    }

    static AttachException newInstance(Throwable cause) {
        return new AttachException(cause);
    }

    AttachException(Throwable cause) {
        super(cause);
    }

    AttachException(String s) {
        super(s);
    }
}

package Exceptions;

public class NoCommandException extends RuntimeException {
    public NoCommandException(String msg){
        super(msg);
    }
}

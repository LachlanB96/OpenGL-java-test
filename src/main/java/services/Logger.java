package services;

public class Logger{

    public enum LOG_SEVERITY {
        GOOD,
        BAD,
        INFO,
        WARN,
        CRIT
    }
    public static void logAndPrint(String message, LOG_SEVERITY severity){
        System.out.printf("[[s]] [%s]", severity.toString(), message);
    }
}

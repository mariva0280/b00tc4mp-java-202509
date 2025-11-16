package com.b00tc4mp.error;


public class ExceptionProvider {

    public static Class lookUp(String className) throws ClassNotFoundException {
        switch (className) {
            case "DuplicityException":
                return DuplicityException.class;
            case "ValidationException":
                return ValidationException.class;
            default:
                throw new ClassNotFoundException(className + " not found.");

        }
    }

    public static Exception newInstance(String className, String message) throws SystemException {
        try {
            Class<? extends Exception> exceptionClass = ExceptionProvider.lookUp(className);
            return exceptionClass.getConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }
    
}

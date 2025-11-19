package com.b00tc4mp.error;

import java.lang.reflect.InvocationTargetException;

public class ExceptionProvider {

    // Define all your checked business exceptions here
    @SuppressWarnings("unchecked")
    public static <T extends Exception> T newInstance(String className, String message) throws T {
        try {
            Class<? extends Exception> exceptionClass = lookUp(className);

            // This will throw the actual exception type T
            Exception ex = exceptionClass.getConstructor(String.class).newInstance(message);

            // This cast is safe because we know exceptionClass extends Exception
            throw (T) ex;

        } catch (InvocationTargetException e) {
            // Constructor threw something — unwrap it if possible
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException rte) {
                throw rte;
            }
            if (cause instanceof Error err) {
                throw err;
            }
            // Should never happen with String constructor
            throw new SystemException("Failed to instantiate exception", cause);

        } catch (ReflectiveOperationException e) {
            throw new SystemException("Exception class not found or invalid: " + className, e);
        }
    }

    // Keep your lookup method unchanged (or make it private)
    private static Class<? extends Exception> lookUp(String className) throws ClassNotFoundException {
        return switch (className) {
            case "DuplicityException" ->
                DuplicityException.class;
            case "ValidationException" ->
                ValidationException.class;
            // add more as needed
            case "NotFoundException" ->
                NotFoundException.class;
            case "CredentialException" ->
                CredentialException.class;
            default ->
                throw new ClassNotFoundException(className + " not found");
        };
    }
}

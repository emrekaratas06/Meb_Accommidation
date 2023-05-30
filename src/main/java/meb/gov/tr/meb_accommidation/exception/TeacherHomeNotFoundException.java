package meb.gov.tr.meb_accommidation.exception;

public class TeacherHomeNotFoundException extends RuntimeException {
    public TeacherHomeNotFoundException(int id) {
        super("Teacher home not found with id: " + id);
    }
}
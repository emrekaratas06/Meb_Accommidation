package meb.gov.tr.meb_accommidation.entity;

public enum EmployeeType {

    EMPLOYEE("Employee Personal"),
    MANAGER("Manager Personal"),
    ADMIN("Admin Personal"),
    SUPERADIN("SuperAdmin Personal"),
    OTHER("Other Personal");

    private final String displayName;

    EmployeeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

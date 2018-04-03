package ua.markfolkin.nc.DAOs;

import ua.markfolkin.nc.Exceptions.PermissionDeniedException;
import ua.markfolkin.nc.Main;
import ua.markfolkin.nc.Objects.Department;
import ua.markfolkin.nc.Objects.Employee;

import java.sql.SQLException;
import java.util.HashMap;

public class Cache {

    private static HashMap<Long, Employee> employees = new HashMap<>();
    private static HashMap<Long, Department> departments = new HashMap<>();
    private static HashMap<String, Boolean> privileges = new HashMap<>();
    private static HashMap<Long, Long> parentIDs = new HashMap<>();

    public static void addEmployee(Employee emp) throws SQLException, PermissionDeniedException {
        employees.put(emp.getEmpNo(Main.role), emp);
    }

    public static void addDepartment(Department dept) throws SQLException, PermissionDeniedException {
        departments.put(dept.getDeptNo(Main.role), dept);
    }

    public static void addPrivilege(String ident, boolean perm) {
        privileges.put(ident, perm);
    }

    public static void addParentId(long id, long parentId) {
        parentIDs.put(id, parentId);
    }

    public static Employee getEmployee(long empNo) {
        return employees.get(empNo);
    }

    public static Department getDepartment(long deptNo) {
        return departments.get(deptNo);
    }

    public static Boolean getPrivilege(String ident) {
        return privileges.get(ident);
    }

    public static Long getParentId(long id) {
        return parentIDs.get(id);
    }

    public static int getEmpVersion(long empNo) throws SQLException, PermissionDeniedException {
        if (employees.get(empNo) == null)
            return -1;
        else
            return employees.get(empNo).getVersion(Main.role);
    }

    public static int getDeptVersion(long deptNo) throws SQLException, PermissionDeniedException {
        if (departments.get(deptNo) == null)
            return -1;
        else
            return departments.get(deptNo).getVersion(Main.role);
    }

}

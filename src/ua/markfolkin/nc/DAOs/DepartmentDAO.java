package ua.markfolkin.nc.DAOs;

import ua.markfolkin.nc.DBUtils.CommandExecutor;
import ua.markfolkin.nc.Exceptions.PermissionDeniedException;
import ua.markfolkin.nc.Objects.Department;
import ua.markfolkin.nc.Objects.Roles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static ua.markfolkin.nc.DAOs.IDs.*;

public class DepartmentDAO {

    private CommandExecutor ce;
    private Roles chief = Roles.CHIEF;

    public DepartmentDAO(CommandExecutor ce) {
        this.ce = ce;
    }

    public ArrayList<Department> getDepartments() throws SQLException, PermissionDeniedException {
        ArrayList<Department> departments = new ArrayList<>();
        ResultSet rs = ce.getResultSet("SELECT OBJECTS.OBJECT_ID AS deptNo\n" +
                "FROM OBJECT_TYPES\n" +
                "JOIN OBJECTS ON OBJECT_TYPES.OBJECT_TYPE_ID = OBJECTS.OBJECT_TYPE_ID\n" +
                "WHERE OBJECT_TYPES.NAME = 'DEPT'");
        while (rs.next()) {
            Long deptNo = rs.getLong("deptNo");
            departments.add(getDepartmentByDeptNo(deptNo));
        }
        return departments;
    }

    public Department getDepartmentByDeptNo(long deptNo) throws SQLException, PermissionDeniedException {
        if (Cache.getDeptVersion(deptNo) == -1 || Cache.getDeptVersion(deptNo) < getVersionOfDept(deptNo)) {
            return getDepartmentByDeptNoFromDB(deptNo);
        } else {
            return Cache.getDepartment(deptNo);
        }

    }

    public Department getDepartmentByDeptNoFromDB(long deptNo) throws SQLException, PermissionDeniedException {
        ResultSet rs = ce.getResultSet("SELECT OBJECTS.OBJECT_ID AS deptNo, OBJECTS.NAME AS dname, loc.TEXT_VALUE AS loc,\n" +
                "version.NUMBER_VALUE AS version\n" +
                "FROM OBJECT_TYPES\n" +
                "JOIN OBJECTS ON OBJECT_TYPES.OBJECT_TYPE_ID = OBJECTS.OBJECT_TYPE_ID\n" +
                "JOIN ATTRIBUTES loc_attr ON loc_attr.NAME = 'LOC'\n" +
                "JOIN PARAMS loc ON loc.ATTR_ID = loc_attr.ATTR_ID AND loc.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "JOIN ATTRIBUTES version_attr ON version_attr.NAME = 'VERSION'\n" +
                "JOIN PARAMS version ON version.ATTR_ID = version_attr.ATTR_ID AND version.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "WHERE OBJECT_TYPES.NAME = 'DEPT' AND OBJECTS.OBJECT_ID = " + deptNo);
        String dname = null;
        String loc = null;
        int version = 0;
        if (!rs.next())
            return null;
        dname = rs.getString("dname");
        loc = rs.getString("loc");
        version = rs.getInt("version");
        Department dept = new Department(deptNo, dname, loc, version);
        Cache.addDepartment(dept);
        return dept;
    }

    public int getVersionOfDept(long deptNo) throws SQLException {
        ResultSet rs = ce.getResultSet("SELECT version.NUMBER_VALUE AS version\n" +
                "FROM OBJECT_TYPES\n" +
                "JOIN OBJECTS ON OBJECT_TYPES.OBJECT_TYPE_ID = OBJECTS.OBJECT_TYPE_ID\n" +
                "JOIN ATTRIBUTES version_attr ON  version_attr.NAME = 'VERSION'\n" +
                "JOIN PARAMS version ON version.ATTR_ID = version_attr.ATTR_ID AND version.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "WHERE OBJECT_TYPES.NAME = 'DEPT' AND OBJECTS.OBJECT_ID = " + deptNo);
        rs.next();
        return rs.getInt("version");
    }

    public void commitChanges(Department dept) throws SQLException, PermissionDeniedException {
        System.out.println("Commiting");
        long deptNo = dept.getDeptNo(chief);
        Department old = getDepartmentByDeptNoFromDB(deptNo);
        boolean changed = false;
        ArrayList<String> changes = new ArrayList<>();
        if (!old.getdName(chief).equals(dept.getdName(chief))) {
            System.out.println("Dif names");
            changes.add("UPDATE OBJECTS SET NAME = '" + dept.getdName(chief) +
                    "' WHERE OBJECT_ID = " + deptNo);
            changed = true;
        }
        if (!old.getLoc(chief).equals(dept.getLoc(chief))) {
            changes.add("UPDATE PARAMS SET TEXT_VALUE = '" + dept.getLoc(chief) +
                    "' WHERE OBJECT_ID = " + deptNo + " AND ATTR_ID = " + ATTR_LOC);
            changed = true;
        }
        if (changed) {
            int newVersion = dept.getVersion(chief) + 1;
            System.out.println("new version = " + newVersion);
            changes.add("UPDATE PARAMS SET NUMBER_VALUE = " + newVersion +
                    " WHERE OBJECT_ID = " + deptNo + " AND ATTR_ID = " + ATTR_DEPT_VERSION);
            dept.setVersion(newVersion, chief);
        }
        for(String s : changes){
            System.out.println(s);
        }
        ce.executeCommands(changes);
    }

}

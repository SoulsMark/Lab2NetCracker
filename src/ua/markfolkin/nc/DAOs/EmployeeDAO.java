package ua.markfolkin.nc.DAOs;

import ua.markfolkin.nc.DBUtils.CommandExecutor;
import ua.markfolkin.nc.Exceptions.PermissionDeniedException;
import ua.markfolkin.nc.Objects.Employee;
import ua.markfolkin.nc.Objects.Roles;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static ua.markfolkin.nc.DAOs.IDs.*;

public class EmployeeDAO {

    private CommandExecutor ce;
    private Roles chief = Roles.CHIEF;

    public EmployeeDAO(CommandExecutor ce) {
        this.ce = ce;
    }

    public ArrayList<Employee> getEmployees() throws SQLException, PermissionDeniedException {
        ArrayList<Employee> employees = new ArrayList<>();
        ResultSet rs = ce.getResultSet("SELECT OBJECTS.OBJECT_ID AS empNo\n" +
                "FROM OBJECT_TYPES\n" +
                "JOIN OBJECTS ON OBJECT_TYPES.OBJECT_TYPE_ID = OBJECTS.OBJECT_TYPE_ID\n" +
                "WHERE OBJECT_TYPES.NAME = 'EMP'");
        while (rs.next()) {
            Long empNo = rs.getLong("empNo");
            employees.add(getEmployeeByEmpNo(empNo));
        }
        return employees;
    }

    public Employee getEmployeeByEmpNo(long empNo) throws SQLException, PermissionDeniedException {
        if (Cache.getEmpVersion(empNo) == -1 || Cache.getEmpVersion(empNo) < getVersionOfEmp(empNo)) {
            return getEmployeeByEmpNoFromDB(empNo);
        } else {
            return Cache.getEmployee(empNo);
        }
    }

    private Employee getEmployeeByEmpNoFromDB(long empNo) throws SQLException, PermissionDeniedException {
        ResultSet rs = ce.getResultSet("SELECT OBJECTS.OBJECT_ID AS empNo, OBJECTS.NAME AS ename, job.TEXT_VALUE AS job,\n" +
                "mrg.NUMBER_VALUE AS mrg, hiredate.DATE_VALUE AS hiredate, sal.NUMBER_VALUE AS sal,\n" +
                "comm.NUMBER_VALUE AS comm, dept.NUMBER_VALUE AS deptno, version.NUMBER_VALUE AS version\n" +
                "FROM OBJECT_TYPES\n" +
                "JOIN OBJECTS ON OBJECT_TYPES.OBJECT_TYPE_ID = OBJECTS.OBJECT_TYPE_ID\n" +
                "JOIN ATTRIBUTES job_attr ON  job_attr.NAME = 'JOB'\n" +
                "JOIN PARAMS job ON job.ATTR_ID = job_attr.ATTR_ID AND job.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "JOIN ATTRIBUTES mrg_attr ON  mrg_attr.NAME = 'MRG'\n" +
                "JOIN PARAMS mrg ON mrg.ATTR_ID = mrg_attr.ATTR_ID AND mrg.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "JOIN ATTRIBUTES hiredate_attr ON  hiredate_attr.NAME = 'HIRE_DATE'\n" +
                "JOIN PARAMS hiredate ON hiredate.ATTR_ID = hiredate_attr.ATTR_ID AND hiredate.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "JOIN ATTRIBUTES sal_attr ON  sal_attr.NAME = 'SALL'\n" +
                "JOIN PARAMS sal ON sal.ATTR_ID = sal_attr.ATTR_ID AND sal.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "JOIN ATTRIBUTES comm_attr ON  comm_attr.NAME = 'COMM'\n" +
                "JOIN PARAMS comm ON comm.ATTR_ID = comm_attr.ATTR_ID AND comm.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "JOIN ATTRIBUTES dept_attr ON  dept_attr.NAME = 'DEPT_NO'\n" +
                "JOIN PARAMS dept ON dept.ATTR_ID = dept_attr.ATTR_ID AND dept.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "JOIN ATTRIBUTES version_attr ON  version_attr.NAME = 'VERSION'\n" +
                "JOIN PARAMS version ON version.ATTR_ID = version_attr.ATTR_ID AND version.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "WHERE OBJECT_TYPES.NAME = 'EMP' AND OBJECTS.OBJECT_ID = " + empNo);
        ArrayList<Long> depts = new ArrayList<>();
        String ename = null;
        String job = null;
        Long mrg = null;
        Date hiredate = null;
        int sal = 0;
        Integer comm = null;
        int version = 0;
        int i = 0;
        while (rs.next()) {
            if (i == 0) {
                ename = rs.getString("ename");
                job = rs.getString("job");
                mrg = rs.getLong("mrg");
                hiredate = rs.getDate("hiredate");
                sal = rs.getInt("sal");
                comm = rs.getInt("comm");
                version = rs.getInt("version");
            }
            Long deptNo = rs.getLong("deptno");
            if (deptNo != null) {
                depts.add(deptNo);
            }
            i++;
        }
        Employee emp;
        if (depts.isEmpty()) {
            emp = new Employee(empNo, ename, job, mrg, hiredate, sal, comm, null, version);
        } else {
            Long[] deptArr = new Long[depts.size()];
            for (i = 0; i < depts.size(); i++) {
                deptArr[i] = depts.get(i);
            }
            emp = new Employee(empNo, ename, job, mrg, hiredate, sal, comm, deptArr, version);
        }
        Cache.addEmployee(emp);
        return emp;
    }

    public int getVersionOfEmp(long empNo) throws SQLException {
        ResultSet rs = ce.getResultSet("SELECT version.NUMBER_VALUE AS version\n" +
                "FROM OBJECT_TYPES\n" +
                "JOIN OBJECTS ON OBJECT_TYPES.OBJECT_TYPE_ID = OBJECTS.OBJECT_TYPE_ID\n" +
                "JOIN ATTRIBUTES version_attr ON  version_attr.NAME = 'VERSION'\n" +
                "JOIN PARAMS version ON version.ATTR_ID = version_attr.ATTR_ID AND version.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "WHERE OBJECT_TYPES.NAME = 'EMP' AND OBJECTS.OBJECT_ID = " + empNo);
        rs.next();
        return rs.getInt("version");
    }

    public void commitChanges(Employee emp) throws SQLException, PermissionDeniedException {
        Employee old = getEmployeeByEmpNoFromDB(emp.getEmpNo(chief));
        long empNo = emp.getEmpNo(chief);
        boolean changed = false;
        ArrayList<String> changes = new ArrayList<>();
        if (!old.geteName(chief).equals(emp.geteName(chief))) {
            changes.add("UPDATE OBJECTS SET NAME = '" + emp.geteName(chief) +
                    "' WHERE OBJECT_ID = " + empNo);
            changed = true;
        }
        if (!old.getJob(chief).equals(emp.getJob(chief))) {
            changes.add("UPDATE PARAMS SET TEXT_VALUE = '" + emp.getJob(chief) +
                    "' WHERE OBJECT_ID = " + empNo + " AND ATTR_ID = " + ATTR_JOB);
            changed = true;
        }
        if (!old.getMrg(chief).equals(emp.getMrg(chief))) {
            changes.add("UPDATE PARAMS SET NUMBER_VALUE = " + emp.getMrg(chief) +
                    " WHERE OBJECT_ID = " + empNo + " AND ATTR_ID = " + ATTR_MRG);
            changed = true;
        }
        if (!old.getHireDate(chief).equals(emp.getHireDate(chief))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            changes.add("UPDATE PARAMS SET DATE_VALUE = TO_DATE('" + sdf.format(emp.getHireDate(chief)) + "', 'yyyy/mm/dd')" +
                    " WHERE OBJECT_ID = " + empNo + " AND ATTR_ID = " + ATTR_HIRE_DATE);
            changed = true;
        }
        if (old.getSal(chief) != emp.getSal(chief)) {
            changes.add("UPDATE PARAMS SET NUMBER_VALUE = " + emp.getSal(chief) +
                    " WHERE OBJECT_ID = " + empNo + " AND ATTR_ID = " + ATTR_SALL);
            changed = true;
        }
        if (!old.getComm(chief).equals(emp.getComm(chief))) {
            changes.add("UPDATE PARAMS SET NUMBER_VALUE = " + emp.getComm(chief) +
                    " WHERE OBJECT_ID = " + empNo + " AND ATTR_ID = " + ATTR_COMM);
            changed = true;
        }
        if (!old.getDeptNo(chief).equals(emp.getDeptNo(chief))) {
            changes.add("DELETE FROM PARAMS " +
                    " WHERE OBJECT_ID = " + empNo + " AND ATTR_ID = " + ATTR_DEPT_NO);
            for (long deptNo : emp.getDeptNo(chief)) {
                changes.add("INSERT INTO PARAMS (NUMBER_VALUE, OBJECT_ID, ATTR_ID)" +
                        " VALUES (" + deptNo + ", " + empNo + ", " + ATTR_DEPT_NO);
            }
            changed = true;
        }
        if (changed) {
            int newVersion = emp.getVersion(chief) + 1;
            System.out.println("new version = " + newVersion);
            changes.add("UPDATE PARAMS SET NUMBER_VALUE = " + newVersion +
                    " WHERE OBJECT_ID = " + empNo + " AND ATTR_ID = " + ATTR_EMP_VERSION);
            emp.setVersion(newVersion, chief);
        }
        ce.executeCommands(changes);
    }

}

package ua.markfolkin.nc.Objects;

import ua.markfolkin.nc.Exceptions.PermissionDeniedException;
import static ua.markfolkin.nc.DAOs.IDs.*;
import static ua.markfolkin.nc.Main.privilegesDAO;

import java.sql.SQLException;
import java.util.Date;

public class Employee {

    private long empNo;
    private String eName;
    private String job;
    private Long mrg;
    private Date hireDate;
    private int sal;
    private Integer comm;
    private Long[] deptNo;
    private int version;

    public Employee() {
    }

    public Employee(long empNo, String eName, String job, Long mrg, Date hireDate, int sal, Integer comm, Long[] deptNo, int version) {
        this.empNo = empNo;
        this.eName = eName;
        this.job = job;
        this.mrg = mrg;
        this.hireDate = hireDate;
        this.sal = sal;
        this.comm = comm;
        this.deptNo = deptNo;
        this.version = version;
    }

    public long getEmpNo(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, READ)) {
            return empNo;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setEmpNo(long empNo, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, WRITE)) {
            this.empNo = empNo;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public String geteName(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, READ)) {
            return eName;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void seteName(String eName, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, WRITE)) {
            this.eName = eName;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public String getJob(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, READ)
                && privilegesDAO.getPrivilegeForAttr(ATTR_JOB, role, READ)) {
            return job;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setJob(String job, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, WRITE)
                && privilegesDAO.getPrivilegeForAttr(ATTR_JOB, role, WRITE)) {
            this.job = job;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public Long getMrg(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, READ)
                && privilegesDAO.getPrivilegeForAttr(ATTR_MRG, role, READ)) {
            return mrg;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setMrg(Long mrg, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, WRITE)
                && privilegesDAO.getPrivilegeForAttr(ATTR_MRG, role, WRITE)) {
            this.mrg = mrg;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public Date getHireDate(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, READ)
                && privilegesDAO.getPrivilegeForAttr(ATTR_HIRE_DATE, role, READ)) {
            return hireDate;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setHireDate(Date hireDate, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, WRITE)
                && privilegesDAO.getPrivilegeForAttr(ATTR_HIRE_DATE, role, WRITE)) {
            this.hireDate = hireDate;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public int getSal(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, READ)
                && privilegesDAO.getPrivilegeForAttr(ATTR_SALL, role, READ)) {
            return sal;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setSal(int sal, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, WRITE)
                && privilegesDAO.getPrivilegeForAttr(ATTR_SALL, role, WRITE)) {
            this.sal = sal;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public Integer getComm(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, READ)
                && privilegesDAO.getPrivilegeForAttr(ATTR_COMM, role, READ)) {
            return comm;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setComm(Integer comm, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, WRITE)
                && privilegesDAO.getPrivilegeForAttr(ATTR_COMM, role, WRITE)) {
            this.comm = comm;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public Long[] getDeptNo(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, READ)
                && privilegesDAO.getPrivilegeForAttr(ATTR_DEPT_NO, role, READ)) {
            return deptNo;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setDeptNo(Long[] deptNo, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, WRITE)
                && privilegesDAO.getPrivilegeForAttr(ATTR_DEPT_NO, role, WRITE)) {
            this.deptNo = deptNo;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public int getVersion(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, READ)
                && privilegesDAO.getPrivilegeForAttr(ATTR_EMP_VERSION, role, READ)) {
            return version;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setVersion(int version, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(empNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_EMP, role, WRITE)
                && privilegesDAO.getPrivilegeForAttr(ATTR_EMP_VERSION, role, WRITE)) {
            this.version = version;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

}

package ua.markfolkin.nc.Objects;

import ua.markfolkin.nc.Exceptions.PermissionDeniedException;

import java.sql.SQLException;

import static ua.markfolkin.nc.DAOs.IDs.*;
import static ua.markfolkin.nc.Main.privilegesDAO;

public class Department {

    private long deptNo;
    private String dName;
    private String loc;
    private int version;

    public Department() {
    }

    public Department(long deptNo, String dName, String loc, int version) {
        this.deptNo = deptNo;
        this.dName = dName;
        this.loc = loc;
        this.version = version;
    }

    public long getDeptNo(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(deptNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_DEPT, role, READ)) {
            return deptNo;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setDeptNo(long deptNo, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(deptNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_DEPT, role, WRITE)) {
            this.deptNo = deptNo;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public String getdName(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(deptNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_DEPT, role, READ)) {
            return dName;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setdName(String dName, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(deptNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_DEPT, role, WRITE)) {
            this.dName = dName;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public String getLoc(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(deptNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_DEPT, role, READ)
                && privilegesDAO.getPrivilegeForAttr(ATTR_LOC, role, READ)) {
            return loc;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setLoc(String loc, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(deptNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_DEPT, role, WRITE)
                && privilegesDAO.getPrivilegeForAttr(ATTR_LOC, role, WRITE)) {
            this.loc = loc;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public int getVersion(Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(deptNo, role, READ)
                && privilegesDAO.getPrivilegeForType(OT_DEPT, role, READ)
                && privilegesDAO.getPrivilegeForAttr(ATTR_DEPT_VERSION, role, READ)) {
            return version;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

    public void setVersion(int version, Roles role) throws SQLException, PermissionDeniedException {
        if (privilegesDAO.getPrivilegeForObject(deptNo, role, WRITE)
                && privilegesDAO.getPrivilegeForType(OT_DEPT, role, WRITE)
                && privilegesDAO.getPrivilegeForAttr(ATTR_DEPT_VERSION, role, WRITE)) {
            this.version = version;
        } else {
            throw new PermissionDeniedException("Permission denied for " + role.name());
        }
    }

}

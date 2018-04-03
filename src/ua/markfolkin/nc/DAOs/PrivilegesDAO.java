package ua.markfolkin.nc.DAOs;

import ua.markfolkin.nc.DBUtils.CommandExecutor;
import ua.markfolkin.nc.Objects.Roles;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.markfolkin.nc.DAOs.IDs.*;

public class PrivilegesDAO {

    private CommandExecutor ce;

    public PrivilegesDAO(CommandExecutor ce) {
        this.ce = ce;
    }

    public boolean getPrivilegeForAttr(long attrId, Roles role, int action) throws SQLException {
        if (Cache.getPrivilege(role.name() + attrId + action) == null) {
            ResultSet rs;
            if (action == 1) {
                rs = ce.getResultSet("SELECT READ FROM ATTR_PRIVILEGES\n" +
                        "WHERE ROLE = '" + role.name() + "'\n" +
                        "AND ATTR_ID = " + attrId);
            } else {
                rs = ce.getResultSet("SELECT WRITE FROM ATTR_PRIVILEGES\n" +
                        "WHERE ROLE = '" + role.name() + "'\n" +
                        "AND ATTR_ID = " + attrId);
            }
            if (!rs.next()) {
                Cache.addPrivilege(role.name() + attrId + action, false);
                return false;
            } else {
                boolean privilege = (rs.getInt(1) == 1 ? true : false);
                Cache.addPrivilege(role.name() + attrId + action, privilege);
                return privilege;
            }
        } else {
            return Cache.getPrivilege(role.name() + attrId + action);
        }
    }

    public boolean getPrivilegeForType(long objectTypeId, Roles role, int action) throws SQLException {
        if (Cache.getPrivilege(role.name() + objectTypeId + action) == null) {
            ResultSet rs;
            if (action == 1) {
                rs = ce.getResultSet("SELECT READ FROM OBJECT_TYPES_PRIVILEGES\n" +
                        "WHERE ROLE = '" + role.name() + "'\n" +
                        "AND OBJECT_TYPE_ID = " + objectTypeId);
            } else {
                rs = ce.getResultSet("SELECT WRITE FROM OBJECT_TYPES_PRIVILEGES\n" +
                        "WHERE ROLE = '" + role.name() + "'\n" +
                        "AND OBJECT_TYPE_ID = " + objectTypeId);
            }
            if (!rs.next()) {
                Long parentId = getObjectTypeParrentId(objectTypeId);
                if (parentId == null) {
                    Cache.addPrivilege(role.name() + objectTypeId + action, false);
                    return false;
                } else {
                    return getPrivilegeForType(parentId, role, action);
                }
            } else {
                boolean privilege = (rs.getInt(1) == 1 ? true : false);
                Cache.addPrivilege(role.name() + objectTypeId + action, privilege);
                return privilege;
            }
        } else {
            return Cache.getPrivilege(role.name() + objectTypeId + action);
        }
    }

    public Long getObjectTypeParrentId(long id) throws SQLException {
        if (Cache.getParentId(id) == null) {
            ResultSet rs = ce.getResultSet("SELECT PARENT_ID FROM OBJECT_TYPES\n" +
                    "WHERE OBJECT_TYPE_ID = " + id);
            if (!rs.next()) {
                Cache.addParentId(id, -1);
                return null;
            } else {
                long parentId = rs.getLong(1);
                Cache.addParentId(id, parentId);
                return parentId;
            }
        } else if (Cache.getParentId(id) == -1) {
            return null;
        } else {
            return Cache.getParentId(id);
        }
    }

    public boolean getPrivilegeForObject(long objectId, Roles role, int action) throws SQLException {
        if (Cache.getPrivilege(role.name() + objectId + action) == null) {
            ResultSet rs;
            if (action == 1) {
                rs = ce.getResultSet("SELECT READ FROM OBJECTS_PRIVILEGES\n" +
                        "WHERE ROLE = '" + role.name() + "'\n" +
                        "AND OBJECT_ID = " + objectId);
            } else {
                rs = ce.getResultSet("SELECT WRITE FROM OBJECTS_PRIVILEGES\n" +
                        "WHERE ROLE = '" + role.name() + "'\n" +
                        "AND OBJECT_ID = " + objectId);
            }
            if (!rs.next()) {
                Long parentId = getObjectParrentId(objectId);
                if (parentId == null) {
                    Cache.addPrivilege(role.name() + objectId + action, true);
                    return false;
                } else {
                    return getPrivilegeForObject(parentId, role, action);
                }
            } else {
                boolean privilege = (rs.getInt(1) == 1 ? true : false);
                Cache.addPrivilege(role.name() + objectId + action, privilege);
                return privilege;
            }
        } else {
            return Cache.getPrivilege(role.name() + objectId + action);
        }
    }

    public Long getObjectParrentId(long id) throws SQLException {
        if (Cache.getParentId(id) == null) {
            ResultSet rs = ce.getResultSet("SELECT NUMBER_VALUE FROM PARAMS\n" +
                    "WHERE ATTR_ID = " + IDs.ATTR_DEPT_NO + " AND OBJECT_ID = " + id);
            if (!rs.next()) {
                Cache.addParentId(id, -1);
                return null;
            } else {
                long parentId = rs.getLong(1);
                Cache.addParentId(id, parentId);
                return parentId;
            }
        } else if (Cache.getParentId(id) == -1) {
            return null;
        } else {
            return Cache.getParentId(id);
        }
    }

}

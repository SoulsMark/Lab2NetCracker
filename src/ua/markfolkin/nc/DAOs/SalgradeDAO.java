package ua.markfolkin.nc.DAOs;

import ua.markfolkin.nc.DBUtils.CommandExecutor;
import ua.markfolkin.nc.Objects.Salgrade;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SalgradeDAO {

    private CommandExecutor ce;

    public SalgradeDAO(CommandExecutor ce) {
        this.ce = ce;
    }

    public Salgrade getSalgradeForSal(int sal) throws SQLException {
        ResultSet rs = ce.getResultSet("SELECT grade.NUMBER_VALUE AS grade, losal.NUMBER_VALUE AS losal, hisal.NUMBER_VALUE AS hisal\n" +
                "FROM OBJECT_TYPES\n" +
                "JOIN OBJECTS ON OBJECT_TYPES.OBJECT_TYPE_ID = OBJECTS.OBJECT_TYPE_ID\n" +
                "LEFT OUTER JOIN ATTRIBUTES grade_attr ON grade_attr.NAME = 'GRADE'\n" +
                "LEFT OUTER JOIN PARAMS grade ON grade.ATTR_ID = grade_attr.ATTR_ID AND grade.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "LEFT OUTER JOIN ATTRIBUTES losal_attr ON losal_attr.NAME = 'LOSAL'\n" +
                "LEFT OUTER JOIN PARAMS losal ON losal.ATTR_ID = losal_attr.ATTR_ID AND losal.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "LEFT OUTER JOIN ATTRIBUTES hisal_attr ON hisal_attr.NAME = 'HISAL'\n" +
                "LEFT OUTER JOIN PARAMS hisal ON hisal.ATTR_ID = hisal_attr.ATTR_ID AND hisal.OBJECT_ID = OBJECTS.OBJECT_ID\n" +
                "WHERE OBJECT_TYPES.NAME = 'SALEGRADE' AND hisal.NUMBER_VALUE >= " + sal + " AND losal.NUMBER_VALUE <= " + sal);
        if (!rs.next())
            return null;
        int grade = rs.getInt("grade");
        int losal = rs.getInt("losal");
        int hisal = rs.getInt("hisal");
        Salgrade s = new Salgrade(grade, losal, hisal);
        return s;
    }

}

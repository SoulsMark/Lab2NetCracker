package ua.markfolkin.nc;


import ua.markfolkin.nc.DAOs.DepartmentDAO;
import ua.markfolkin.nc.DAOs.EmployeeDAO;
import ua.markfolkin.nc.DAOs.PrivilegesDAO;
import ua.markfolkin.nc.DBUtils.CommandExecutor;
import ua.markfolkin.nc.Exceptions.PermissionDeniedException;
import ua.markfolkin.nc.Objects.Department;
import ua.markfolkin.nc.Objects.Employee;
import ua.markfolkin.nc.Objects.Roles;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static CommandExecutor ce;
    public static PrivilegesDAO privilegesDAO;
    private static EmployeeDAO employeeDAO;
    private static DepartmentDAO departmentDAO;
    public static Roles role;
    private static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        try {
            ce = new CommandExecutor();
            employeeDAO = new EmployeeDAO(ce);
            departmentDAO = new DepartmentDAO(ce);
            privilegesDAO = new PrivilegesDAO(ce);
            System.out.println("Sign in as: ");
            int numOfVariants = showRoles();
            int choice;
            while (true) {
                choice = getNumber();
                if (setRole(choice)) {
                    break;
                } else {
                    System.out.println("Wrong input, try again.");
                }
            }
            while (true) {
                workWithObjects();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static int showRoles() {
        Roles[] roles = Roles.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.println((i + 1) + ". " + roles[i]);
        }
        return roles.length;
    }

    private static int getNumber() {
        System.out.print("Enter number: ");
        String input = sc.nextLine();
        if (input.matches("\\d+")) {
            return Integer.parseInt(input);
        } else {
            System.out.println("Wrong input, try again");
            return getNumber();
        }
    }

    private static long getLong() {
        System.out.print("Enter number: ");
        String input = sc.nextLine();
        if (input.matches("\\d+")) {
            return Long.parseLong(input);
        } else {
            System.out.println("Wrong input, try again");
            return getLong();
        }
    }

    private static boolean setRole(int choice) {
        if (choice > Roles.values().length) {
            return false;
        } else {
            role = Roles.values()[choice - 1];
            return true;
        }
    }

    private static void workWithObjects() throws SQLException, ParseException {
        System.out.println("Choose object type: ");
        System.out.println("1. Employee;\n" +
                "2. Department;\n" +
                "3. Exit");
        int choice;
        while (true) {
            choice = getNumber();
            if (choice <= 3 && choice > 0) {
                break;
            } else {
                System.out.println("Wrong input, try again.");
            }
        }
        switch (choice) {
            case 1:
                workWithEmp();
                break;
            case 2:
                workWithDept();
                break;
            case 3:
                System.out.println("Good bye!");
                System.exit(01);
                break;
            default:
                System.out.println("Wrong input! Try again");
                break;
        }
    }

    private static void workWithEmp() throws SQLException, ParseException {
        System.out.println("Enter EmpNo");
        long choice = getLong();
        Employee emp = null;
        try {
            emp = employeeDAO.getEmployeeByEmpNo(choice);
        } catch (PermissionDeniedException e) {
            System.out.println(e.getMessage());
        }
        if (emp != null) {
            loop:
            while (true) {
                System.out.println("What to do?");
                System.out.println("1. get EmpNo;\n" +
                        "2. get eName;\n" +
                        "3. set eName;\n" +
                        "4. get Job;\n" +
                        "5. set Job;\n" +
                        "6. get MRG;\n" +
                        "7. set MRG;\n" +
                        "8. get HireDate;\n" +
                        "9. set HireDate;\n" +
                        "10. get SAL;\n" +
                        "11. set SAL;\n" +
                        "12. get Comm;\n" +
                        "13. set Comm;\n" +
                        "14. get DeptNo;\n" +
                        "15. set DeptNo;\n" +
                        "16. commit changes and exit;\n" +
                        "17. exit without commit;");
                int choice2 = getNumber();
                try {
                    switch (choice2) {
                        case 1:
                            System.out.println(emp.getEmpNo(role));
                            break;
                        case 2:
                            System.out.println(emp.geteName(role));
                            break;
                        case 3:
                            System.out.println("Enter new Name");
                            String newName = sc.nextLine();
                            emp.seteName(newName, role);
                            break;
                        case 4:
                            System.out.println(emp.getJob(role));
                            break;
                        case 5:
                            System.out.println("Enter new Job");
                            String newJob = sc.nextLine();
                            emp.setJob(newJob, role);
                            break;
                        case 6:
                            System.out.println(emp.getMrg(role));
                            break;
                        case 7:
                            System.out.println("Enter EmpNo of the new MRG");
                            long newMRG = getLong();
                            emp.setMrg(newMRG, role);
                            break;
                        case 8:
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyy.MMMMM.dd");
                            System.out.println(sdf.format(emp.getHireDate(role)));
                            break;
                        case 9:
                            System.out.println("Enter new HireDate in format\"YYYY.MM.DD\"");
                            String newDateS = sc.nextLine();
                            sdf = new SimpleDateFormat("yyyyy.MM.dd");
                            Date newDate = sdf.parse(newDateS);
                            emp.setHireDate(newDate, role);
                            break;
                        case 10:
                            System.out.println(emp.getSal(role));
                            break;
                        case 11:
                            System.out.println("Enter new Sal");
                            int newSal = getNumber();
                            emp.setSal(newSal, role);
                            break;
                        case 12:
                            System.out.println(emp.getComm(role));
                            break;
                        case 13:
                            System.out.println("Enter new Comm");
                            int newComm = getNumber();
                            emp.setComm(newComm, role);
                            break;
                        case 14:
                            System.out.println(Arrays.toString(emp.getDeptNo(role)));
                            break;
                        case 15:
                            System.out.println("Enter deptNo of departments, enter something except number to stop");
                            ArrayList<Long> newDeptNo = new ArrayList<>();
                            String deptNo;
                            while (true) {
                                deptNo = sc.nextLine();
                                System.out.println("s " + deptNo);
                                if (deptNo.matches("\\d+")) {
                                    long d = Long.parseLong(deptNo);
                                    System.out.println("d " + d);
                                    newDeptNo.add(d);
                                } else {
                                    break;
                                }
                            }
                            Long[] arr = new Long[newDeptNo.size()];
                            for (int i = 0; i < arr.length; i++) {
                                System.out.println(newDeptNo.get(i));
                                arr[i] = newDeptNo.get(i);
                            }
                            emp.setDeptNo(arr, role);
                            break;
                        case 16:
                            employeeDAO.commitChanges(emp);
                            break loop;
                        case 17:
                            break loop;
                        default:
                            System.out.println("Wrong input, try again!");
                            break;
                    }
                } catch (PermissionDeniedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static void workWithDept() throws SQLException {
        System.out.println("Enter DeptNo");
        long choice = getLong();
        Department dept = null;
        try {
            dept = departmentDAO.getDepartmentByDeptNo(choice);
        } catch (PermissionDeniedException e) {
            System.out.println(e.getMessage());
        }
        if (dept != null) {
            loop:
            while (true) {
                System.out.println("What to do?");
                System.out.println("1. get DeptNo;\n" +
                        "2. get dName;\n" +
                        "3. set dName;\n" +
                        "4. get Loc;\n" +
                        "5. set Loc;\n" +
                        "6. commit changes and exit;\n" +
                        "7. exit without commit;");
                int choice2 = getNumber();
                try {
                    switch (choice2) {
                        case 1:
                            System.out.println(dept.getDeptNo(role));
                            break;
                        case 2:
                            System.out.println(dept.getdName(role));
                            break;
                        case 3:
                            System.out.println("Enter new Name");
                            String newName = sc.nextLine();
                            dept.setdName(newName, role);
                            break;
                        case 4:
                            System.out.println(dept.getLoc(role));
                            break;
                        case 5:
                            System.out.println("Enter new Loc");
                            String newJob = sc.nextLine();
                            dept.setLoc(newJob, role);
                            break;
                        case 6:
                            departmentDAO.commitChanges(dept);
                            System.out.println("Committed!");
                            break loop;
                        case 7:
                            break loop;
                        default:
                            System.out.println("Wrong input, try again!");
                            break;
                    }
                } catch (PermissionDeniedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}

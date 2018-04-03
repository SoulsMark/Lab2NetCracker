package ua.markfolkin.nc.Objects;

public class Salgrade {

    private int grade;
    private int loSal;
    private int hiSal;

    public Salgrade() {
    }

    public Salgrade(int grade, int loSal, int hiSal) {
        this.grade = grade;
        this.loSal = loSal;
        this.hiSal = hiSal;
    }

    public int getGrade() {
        return grade;
    }

    public int getLoSal() {
        return loSal;
    }

    public int getHiSal() {
        return hiSal;
    }

    @Override
    public String toString() {
        return "Salgrade{" +
                "grade=" + grade +
                ", loSal=" + loSal +
                ", hiSal=" + hiSal +
                '}';
    }
}

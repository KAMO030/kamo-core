package comment.pojo;

public class Student extends Person {
    private String sno;
    private String className;

    public Student(String sno, String className) {
        this.sno = sno;
        this.className = className;
    }

    public Student() {
    }
    public Student(String sno, String className,String name, Integer age) {
        super(name, age);
        this.sno = sno;
        this.className = className;
    }

    public Student(String name, Integer age) {
        super(name, age);
    }

    public String getSno() {
        return sno;
    }

    public Student setSno(String sno) {
        this.sno = sno;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public Student setClassName(String className) {
        this.className = className;
        return this;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sno='" + sno + '\'' +
                ", className='" + className + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }


}

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String JDBC_USER = "your_db_username";
    private static final String JDBC_PASSWORD = "your_db_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static int insertStudent(String regNo, String studentName, String dob, String contactNumber, String emailAddress, String fatherName, String motherName, String parentContact, String stream, String branch) throws SQLException {
        String sql = "INSERT INTO student_info (reg_no, name, DOB, contact, email, father_name, mother_name, parent_contact, stream, branch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, regNo);
            pstmt.setString(2, studentName);
            pstmt.setDate(3, java.sql.Date.valueOf(dob));
            pstmt.setString(4, contactNumber);
            pstmt.setString(5, emailAddress);
            pstmt.setString(6, fatherName);
            pstmt.setString(7, motherName);
            pstmt.setString(8, parentContact);
            pstmt.setString(9, stream);
            pstmt.setString(10, branch);

            return pstmt.executeUpdate();
        }
    }

    public static ResultSet getStudentByRegNo(String regNo) throws SQLException {
        String sql = "SELECT * FROM student_info WHERE reg_no = ?";
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, regNo);
        return pstmt.executeQuery();
    }
}

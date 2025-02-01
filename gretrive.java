import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RetrieveSubjectDetails")
public class gretrive extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String rollNo = request.getParameter("roll_no");
        // String name = request.getParameter("name");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL query to retrieve subject details ordered by year and semester
            String query = "SELECT * FROM subject_grades WHERE roll_no = ? ORDER BY year ASC, sem ASC";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, rollNo);
            // stmt.setString(2, name);

            rs = stmt.executeQuery();

            out.println("<html><body>");
            out.println("<html><head><title>University Management System</title><link rel='stylesheet' href='styles.css'></head><body>");

            out.println(" <header><div class='logo-container'>");
            out.println(" <img src='university-logo.avif' height='100px' width='100px'><br><br>");
            out.println("<h1><strong> SHMN University</strong></h1>");
            out.println("</div><br><br>");
            out.println("<nav>");
            out.println("<a href='./Admin.html'>Back</a>");
            out.println("</nav>");
            out.println("</header>");
            out.println("<div class='portfolio-container'>");
            out.println("<div class='portfolio'>");
            out.println("<h1>Subject Details for Roll Number: " + rollNo + "</h1>");

            while (rs.next()) {
                String subjectCode = rs.getString("subject_code");
                String subjectName = rs.getString("subject_name");
                int year = rs.getInt("year");
                int sem = rs.getInt("sem");
                String grade = rs.getString("grade");

                out.println("<div class='details'><strong>Subject Code: " + subjectCode + "</div>");
                out.println("<div class='details'><strong>Subject Name: " + subjectName + "</div>");
                out.println("<div class='details'><strong>Year: " + year + "</div>");
                out.println("<div class='details'><strong>Semester: " + sem + "</div>");
                out.println("<div class='details'><strong>Grade: " + grade + "</div>");
                out.println("<hr>");
            }
            out.println("</div>");
            out.println("</div>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<html><body>");
            out.println("<h1>Error occurred: " + e.getMessage() + "</h1>");
            out.println("</body></html>");
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}

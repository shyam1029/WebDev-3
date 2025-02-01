import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddSubjectGrade")
public class addsubjectgrade extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String subjectCode = request.getParameter("subject_code");
        String rollNo = request.getParameter("roll_no");
        String subjectName = request.getParameter("subject_name");
        int year = Integer.parseInt(request.getParameter("year"));
        int sem = Integer.parseInt(request.getParameter("sem"));
        String grade = request.getParameter("grade");

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL query to insert subject grades
            String query = "INSERT INTO subject_grades (subject_code, roll_no, subject_name, year, sem, grade) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, subjectCode);
            stmt.setString(2, rollNo);
            stmt.setString(3, subjectName);
            stmt.setInt(4, year);
            stmt.setInt(5, sem);
            stmt.setString(6, grade);

            // Execute the query
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                out.println("<html><body>");
                out.println("<h1>Grade added successfully!</h1>");
                out.println("<p><a href='AddSubjectGrades.html'>Add another grade</a></p>");
                out.println("</body></html>");
            } else {
                out.println("<html><body>");
                out.println("<h1>Failed to add grade.</h1>");
                out.println("<p><a href='AddSubjectGrades.html'>Try again</a></p>");
                out.println("</body></html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<html><body>");
            out.println("<h1>Error occurred: " + e.getMessage() + "</h1>");
            out.println("<p><a href='AddSubjectGrades.html'>Try again</a></p>");
            out.println("</body></html>");
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}

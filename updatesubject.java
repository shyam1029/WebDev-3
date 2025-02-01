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

@WebServlet("/UpdateSubject")
public class updatesubject extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String rollNo = request.getParameter("roll_no");
        String subjectCode = request.getParameter("subject_code");
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

            // SQL query to update subject grades
            String query = "UPDATE subject_grades SET grade = ? WHERE roll_no = ? AND subject_code = ? AND year = ? AND sem = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, grade);
            stmt.setString(2, rollNo);
            stmt.setString(3, subjectCode);
            stmt.setInt(4, year);
            stmt.setInt(5, sem);

            // Execute the query
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                out.println("<html><body>");
                out.println("<h1>Grade updated successfully!</h1>");
                out.println("<p><a href='update_grades.html'>Update another grade</a></p>");
                out.println("</body></html>");
            } else {
                out.println("<html><body>");
                out.println("<h1>Failed to update grade. Please check the details and try again.</h1>");
                out.println("<p><a href='UpdateSubjectGrade.html'>Try again</a></p>");
                out.println("</body></html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<html><body>");
            out.println("<h1>Error occurred: " + e.getMessage() + "</h1>");
            out.println("<p><a href='update_grades.html'>Try again</a></p>");
            out.println("</body></html>");
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}

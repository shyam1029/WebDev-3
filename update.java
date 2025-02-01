import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UpdateStudent")
public class update extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/university";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roll_no = request.getParameter("roll_no");
        String fieldName = request.getParameter("field_name");
        String newValue = request.getParameter("new_value");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // SQL update statement
            String sql = "UPDATE student_details SET " + fieldName + " = ? WHERE roll_no = ?";
            
            // Create PreparedStatement
            pstmt = conn.prepareStatement(sql);
            
            // Set parameters
            pstmt.setString(1, newValue);
            pstmt.setString(2, roll_no);
            
            // Execute the statement
            int rowsUpdated = pstmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                // Redirect to the retrieval servlet with reg_no as a parameter
                response.sendRedirect("RetrieveServlet?roll_no=" + roll_no);
            } else {
                response.setContentType("text/html");
                response.getWriter().println("Failed to update student information.");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.getWriter().println("MySQL JDBC Driver not found. Include it in your library path.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.getWriter().println("SQL Error: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

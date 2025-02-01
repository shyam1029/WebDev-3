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

@WebServlet("/FormRequest")
public class fromreq extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/university";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String rollNo = request.getParameter("roll_no");
        String name = request.getParameter("name");
        String stream = request.getParameter("stream");
        String branch = request.getParameter("branch");
        String contact = request.getParameter("phone");
        String formNeeded = request.getParameter("formneeded");
        String purpose = request.getParameter("purpose");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // SQL insert statement
            String sql = "INSERT INTO form_requests (roll_no, name, stream, branch, contact, form_needed, purpose, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'pending')";

            // Create PreparedStatement
            pstmt = conn.prepareStatement(sql);

            // Set parameters
            pstmt.setString(1, rollNo);
            pstmt.setString(2, name);
            pstmt.setString(3, stream);
            pstmt.setString(4, branch);
            pstmt.setString(5, contact);
            pstmt.setString(6, formNeeded);
            pstmt.setString(7, purpose);

            // Execute the statement
            int rowsInserted = pstmt.executeUpdate();

            // Redirect based on the result
            if (rowsInserted > 0) {
                response.sendRedirect("myapplication?roll_no=" + rollNo);
            } else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('Failed to submit form request.'); window.location.href = 'formrequest.html';</script>");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("MySQL JDBC Driver not found. Include it in your library path.");
        } catch (SQLException e) {
            e.printStackTrace();
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

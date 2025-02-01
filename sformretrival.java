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

@WebServlet("/RetrieveForm")
public class sformretrival extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/university";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rollNo = request.getParameter("roll_no");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // SQL query to retrieve form request details
            String sql = "SELECT * FROM form_requests WHERE roll_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            
            // Execute the query
            rs = pstmt.executeQuery();

            // Process the result set
            if (rs.next()) {
                out.println("<html><body>");
                out.println("<h1>Form Request Details</h1>");
                out.println("<p>Roll Number: " + rs.getString("roll_no") + "</p>");
                out.println("<p>Name: " + rs.getString("name") + "</p>");
                out.println("<p>Stream: " + rs.getString("stream") + "</p>");
                out.println("<p>Branch: " + rs.getString("branch") + "</p>");
                out.println("<p>Contact: " + rs.getString("contact") + "</p>");
                out.println("<p>Form Needed: " + rs.getString("form_needed") + "</p>");
                out.println("<p>Purpose: " + rs.getString("purpose") + "</p>");
                out.println("<p>Status: " + rs.getString("status") + "</p>");
                out.println("</body></html>");
            } else {
                out.println("<script>alert('No form request found for the given roll number.'); window.location.href = 'formrequest.html';</script>");
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
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

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

@WebServlet("/DisplayForm")
public class formdisplay extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/university";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            // SQL query to retrieve all form request details
            String sql = "SELECT * FROM form_requests";
            pstmt = conn.prepareStatement(sql);

            // Execute the query
            rs = pstmt.executeQuery();

            // Generate HTML output for form request details
            out.println("<html>");
            out.println("<head><title>View Form Requests</title></head>");
            out.println("<body>");
            out.println("<h1>Form Requests</h1>");
            out.println("<a href='Admin.html'>Back</a>");

            while (rs.next()) {
                String rollNo = rs.getString("roll_no");
                String name = rs.getString("name");
                String stream = rs.getString("stream");
                String branch = rs.getString("branch");
                String contact = rs.getString("contact");
                String formNeeded = rs.getString("form_needed");
                String purpose = rs.getString("purpose");
                String status = rs.getString("status");

                out.println("<div>");
                out.println("<h2>Request Details</h2>");
                out.println("<p><strong>Roll Number:</strong> " + rollNo + "</p>");
                out.println("<p><strong>Name:</strong> " + name + "</p>");
                out.println("<p><strong>Stream:</strong> " + stream + "</p>");
                out.println("<p><strong>Branch:</strong> " + branch + "</p>");
                out.println("<p><strong>Contact:</strong> " + contact + "</p>");
                out.println("<p><strong>Form Needed:</strong> " + formNeeded + "</p>");
                out.println("<p><strong>Purpose:</strong> " + purpose + "</p>");
                out.println("<p><strong>Status:</strong> " + status + "</p>");
                
                // Form to update the status
                out.println("<form action='UpdateStatus' method='post'>");
                out.println("<input type='hidden' name='roll_no' value='" + rollNo + "'>");
                out.println("<label for='new_status'>Update Status:</label>");
                out.println("<select name='new_status' required>");
                out.println("<option value='Pending'" + (status.equals("Pending") ? " selected" : "") + ">Pending</option>");
                out.println("<option value='Approved'" + (status.equals("Approved") ? " selected" : "") + ">Approved</option>");
                out.println("</select>");
                out.println("<button type='submit'>Update Status</button>");
                out.println("</form>");

                out.println("<hr>");
                out.println("</div>");
            }

            out.println("</body>");
            out.println("</html>");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            out.println("<html><body><p>Error: " + e.getMessage() + "</p></body></html>");
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

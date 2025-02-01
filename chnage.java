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

@WebServlet("/UpdatePassword")
public class chnage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/university";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String regNo = request.getParameter("reg_no");
        String oldName = request.getParameter("password");
        String newName = request.getParameter("new_password");

        Connection conn = null;
        PreparedStatement pstmt = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // SQL update statement
            String sql = "UPDATE student_details SET password = ? WHERE roll_no = ? AND password = ?";

            // Create PreparedStatement
            pstmt = conn.prepareStatement(sql);

            // Set parameters
            pstmt.setString(1, newName);
            pstmt.setString(2, regNo);
            pstmt.setString(3, oldName);

            // Execute the statement
            int rowsUpdated = pstmt.executeUpdate();

            // Send response
            if (rowsUpdated > 0) {
                response.sendRedirect("index.html");
            } else {
                out.println("<html><body>");
                out.println("<h1>No student found with the given registration number and old name.</h1>");
                out.println("<a href='./Change Password.html'>try again</a>");
                out.println("</body></html>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            out.println("SQL Error: " + e.getMessage());
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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FeeDetails")
public class feedetails extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/university";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String rollNo = request.getParameter("roll_no");
        String name = request.getParameter("name");
        String branch = request.getParameter("branch");
        String stream = request.getParameter("stream");
        String amountStr = request.getParameter("amount");
        String dateOfPaymentStr = request.getParameter("date_of_paying");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Convert amount and date_of_payment from string to appropriate types
        double amount = Double.parseDouble(amountStr);
        Date dateOfPayment;

        // Validate date format
        try {
            dateOfPayment = Date.valueOf(dateOfPaymentStr);
        } catch (IllegalArgumentException e) {
            out.println("<html><body><h1>Invalid date format. Please use yyyy-MM-dd.</h1></body></html>");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // SQL insert statement
            String sql = "INSERT INTO fee_details (roll_no, name, branch, stream, amount, data_of_payment) VALUES (?, ?, ?, ?, ?, ?)";

            // Create PreparedStatement
            pstmt = conn.prepareStatement(sql);

            // Set parameters
            pstmt.setString(1, rollNo);
            pstmt.setString(2, name);
            pstmt.setString(3, branch);
            pstmt.setString(4, stream);
            pstmt.setDouble(5, amount);
            pstmt.setDate(6, dateOfPayment);

            // Execute the statement
            int rowsInserted = pstmt.executeUpdate();

            // Redirect based on the result
            if (rowsInserted > 0) {
                // Redirect to the retrieval servlet with roll_no as a parameter
                response.sendRedirect("RetrieveFeeDetailsServlet?roll_no=" + rollNo);
            } else {
                out.println("<html><body><h1>Failed to insert fee details.</h1></body></html>");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            out.println("<html><body><h1>MySQL JDBC Driver not found. Include it in your library path.</h1></body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<html><body><h1>SQL Error: " + e.getMessage() + "</h1></body></html>");
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
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

@WebServlet("/RetrieveFeeDetailsServlet")
public class feeretrive extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/university";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rollNo = request.getParameter("roll_no");
        

        // JDBC objects
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // SQL query to retrieve fee details by roll number and name
            String sql = "SELECT * FROM fee_details WHERE roll_no = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, rollNo);
          

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Process the result set
            if (resultSet.next()) {
                  out.println("<html><head><title>University Management System</title><link rel='stylesheet' href='styles.css'></head><body>");

                out.println(" <header><div class='logo-container'>");
                out.println(" <img src='university-logo.avif' height='100px' width='100px'><br><br>");
                out.println("<h1><strong> SHMN University</strong></h1>");
                out.println("</div><br><br>");
                out.println("<nav>");
                out.println("<h3>student fee details<h3>");
               
                out.println("<a href='./Change Password.html'>Change Password</a>");
                out.println("<a href='./fromrequest.html'>From Request</a> ");
                out.println("<a href='myapplication?roll_no="+rollNo+"' method='post'>myapplications</a>");
                out.println("<a href='stufee.html'>Student fee</a>");
                out.println("<a href='./index.html'>Log Out</a>");
                out.println("</nav>");
                out.println("</header>");

                out.println("<h1>Fee Details</h1>");
                out.println("<p>Roll Number: " + resultSet.getString("roll_no") + "</p>");
                out.println("<p>Name: " + resultSet.getString("name") + "</p>");
                out.println("<p>Branch: " + resultSet.getString("branch") + "</p>");
                out.println("<p>Stream: " + resultSet.getString("stream") + "</p>");
                out.println("<p>Amount: " + resultSet.getDouble("amount") + "</p>");
                out.println("<p>Date of Payment: " + resultSet.getDate("data_of_payment") + "</p>");
                out.println("<footer> <div class='footer'> <div>  <div class='footer-col-2'><img src='university-logo.avif' alt='my company name' style='max-width:80px; height:auto; border-radius:100%; id='first'><h1><strong>SHMN University</strong></h1></div> </div></div></div></footer>");
               
                out.println("</body></html>");
            } else {
                out.println("<html><head><title>University Management System</title><link rel='stylesheet' href='styles.css'></head><body>");
                out.println(" <header><div class='logo-container'>");
                out.println(" <img src='university-logo.avif' height='100px' width='100px'><br><br>");
                out.println("<h1><strong> SHMN University</strong></h1>");
                out.println("</div><br><br>");
                out.println("<nav>");
                out.println("<h3>student fee details<h3>");
               
                out.println("<a href='./Change Password.html'>Change Password</a>");
                out.println("<a href='./fromrequest.html'>From Request</a> ");
                out.println("<a href='myapplication?roll_no="+rollNo+"' method='post'>myapplications</a>");
                out.println("<a href='stufee.html'>Student fee</a>");
                out.println("<a href='./index.html'>Log Out</a>");
                out.println("</nav>");
                out.println("</header>");
                out.println("<h1>can not found</h1>");
                 out.println("<footer> <div class='footer'> <div>  <div class='footer-col-2'><img src='university-logo.avif' alt='my company name' style='max-width:80px; height:auto; border-radius:100%; id='first'><h1><strong>SHMN University</strong></h1></div> </div></div></div></footer>");
               
                out.println("</body></html>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            out.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

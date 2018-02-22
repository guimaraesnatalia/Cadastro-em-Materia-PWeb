/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Natalia
 */

@WebServlet(name = "ListaDisciplinas", urlPatterns = {"/ListaDisciplinas"})
public class ListaDisciplinas extends HttpServlet {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";        
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/listaweb1";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); 
            Connection conn;
            String matricula = request.getParameter("matricula");
            out.println("<html>");
            out.println("<head>");
            out.println("<title style>Lista de Disciplinas</title>");
            out.println("<style type=\"text/css\">" +
                "<!-- " +
                "body{" +
"                background-color:#FAFAFA;}" +
"            footer{" +
"                background: #336;" +
"                border-top: #FFF;" +
"                position: fixed;" +
"                bottom: 0px;" +
"                right: 0px;" +
"                left: 0px;" +
"                color: #FFF;" +
"                font-size: 20px;" +
"            }" +
"            table{" +
"            	border-radius: 20px;" +
"            	background: #e6f2ff;" +
"            	border:1px solid #336;" +
"            	position: relative;" +
"            	width: 900px;" +
"            	left: 280px;" +
"            	padding: 5px;" +
"            }" +
"            thead{" +
"            	border-radius: 20px;" +
"            	background: #336;" +
"            	color: #FFF;" +
"            }" +
"            #button{" +
"            	position: relative;" +
"            	left: 600px;" +
"            	bottom: 40px;" +
"            }"
                    + "#errado{"
                    + "color:red;"
                    + "border:2px solid red;"
                    + "width:200px;"
                    + "left:250px;}"+
"               #buttonHome{" +
"            	position: relative;" +
"            	left: 690px;" +
"            	bottom: 60px;" +
"            }" +
                "//--></style>");
            out.println("</head>");
            out.println("<body>");
	       try {
	            Class.forName(JDBC_DRIVER );
	            conn = DriverManager.getConnection(DATABASE_URL, 
                            "root", "" );
	            Statement st = conn.createStatement();
                    Statement stmt = conn.createStatement();
                    Statement st1 = conn.createStatement();
                    
                    ResultSet rec2 = st1.executeQuery(
                    "SELECT * FROM alunos");
                    boolean exists = false;
                    while(rec2.next()){
                        if(matricula.equals(rec2.getString(1))){
                            exists = true;
                        }
                    }
                    
                    if(exists == false){
                        out.println("<div id=\"errado\"><h2>Matricula não encontrada</h2>");
                         out.println("<span id=\"buttonTentar\"><input type=\"button\" value=\"Tentar Novamente\" onClick=\"history.go(-1)\" /></span><div>");
                    }else{
	            ResultSet rec = st.executeQuery(
	                "SELECT * " +
	                "FROM turmas " +
	                "ORDER BY codDisciplina");
                    out.println("<table><thead>");
	            out.println("<th>Codigo da Disciplina</td><th>Nome da Disciplina</th>" +
                            "<th>Carga Horária</th>" + "<th>Código da Turma</th>" +
                            "<th>Horário 1</th>" + "<th>Horário 2</th>" +
                            "<th>Horário 3</th>" + "<th>Escolha</th>" + "</thead>");
                    out.println("<form action=\"MatriculaAluno\"> ");
                    
	            while(rec.next()) {
                        String cDis = rec.getString(1);
                                ResultSet rec1 = stmt.executeQuery(
	                "SELECT * " +
	                "FROM disciplinas " +
                        "WHERE codDisciplina = '"+ cDis+"'");
                               
                        while(rec1.next()){
                            out.println("<tbody><tr><td>"+rec.getString(1)+"<br /></td>"
                                        + "<td>"+ rec1.getString(2) + "<br /></td>"
                                        + "<td>" + rec1.getString(3) + "<br /></td>"
                                        + "<td>" + rec.getString(2) + "<br /></td>"
                                        + "<td>"+ rec.getString(3) + "<br /></td>");
                            
                            if(rec.getString(4) != null){
                                out.println( "<td>" + rec.getString(4) + "<br /></td>" );
                            }else{
                                out.println( "<td> <br /></td>" );
                            }
                                         
                            if(rec.getString(5) != null){
                                out.println( "<td>" + rec.getString(5) + "<br /></td>" );
                            }else{
                                out.println( "<td> <br /></td>" );
                            }
                            
                                out.println( "<td><input type=\"radio\" name='opt' value='" + rec.getString(2) + "'</td></tr>");
                    }
                    
	        }
                    out.println("<tbody></table>");
                    out.println("<br><br><br>");
                    out.println("<br/><span id=\"button\"><input type='submit' value='Enviar' name='enviar' /></span>");
                    out.println("<input type='hidden' name='matricula' value='"+matricula+"'/>");
                    out.println("</form>");
                    out.println("<form>");
                    out.println("<span id=\"buttonHome\"><input type=\"button\" value=\"Home\" onClick=\"history.go(-1)\" /></span>");
                    out.println("</form>");
                    }
	            st.close();
                    stmt.close();
	        } catch (SQLException s) {
	            out.println("SQL Error: " + s.toString() + " "
	                + s.getErrorCode() + " " + s.getSQLState());
	        } catch (Exception e) {
	            out.println("Error: " + e.toString()
	                + e.getMessage());
	        }
	out.println("<footer>\n" +
"		Natália de Souza Guimarães - 2017.1\n" +
"	</footer>");
        out.println("</body>");
        out.println("</html>");
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Natalia
 */
@WebServlet(name = "MatriculaAluno", urlPatterns = {"/MatriculaAluno"})
public class MatriculaAluno extends HttpServlet {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";        
    static final String DATABASE_URL = "jdbc:mysql://localhost/listaweb1";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); 
        Connection conn;
        
    try {
          Class.forName(JDBC_DRIVER );
          conn = DriverManager.getConnection(DATABASE_URL, 
                            "root", "" );
          Statement st = conn.createStatement();
          Statement stmt = conn.createStatement();
          
        String matricula = request.getParameter("matricula");
        String nomeDisciplina = null;
        String codDisciplina = null;
        String codTurma =  request.getParameter("opt") ;
        String cargaHoraria = null;
        
        //Identifica e atribui ao código da disciplina referente à turma
        ResultSet rec = st.executeQuery(
	                "SELECT * " +
	                "FROM turmas ");
                 
	    while(rec.next()) {
                if(codTurma == null ? rec.getString(2) == null : codTurma.equals(rec.getString(2))){
                    codDisciplina = rec.getString(1);
                    break;
                }
            }
        
        //Identifica o nome e a carga horária da disciplina selecionada a partir do código da disciplina
        ResultSet rec1 = stmt.executeQuery(
	                "SELECT * " +
	                "FROM disciplinas " +
                        "WHERE codDisciplina = '"+ codDisciplina+"'");
                               
            while(rec1.next()){
                if(codDisciplina == null ? rec.getString(1) == null : codDisciplina.equals(rec.getString(1))){
                    nomeDisciplina = rec1.getString(2);
                    cargaHoraria = rec1.getString(3);
                    break;
                }  
            }
           
        //Atualiza o banco de dados "aluno", adicionando a disciplina e a turma selecionada    
        String str = "UPDATE alunos Set codTurma = '" + codTurma + "',"
                    + " codDisciplina = '" + codDisciplina + "'"
                    + " WHERE matricula='" + matricula + "'";            
        st.executeUpdate(str);
                   
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Aluno Matriculado</title>"); 
        out.println("<style type=\"text/css\">" +
"        body{" +
"            background-color:#FAFAFA;" +
"        }" +
                "#botoes{"
                + "position:relative;"
                + "bottom:40px;"
                + "left: 250px"
                + "}"+
"        footer{" +
"            background: #336;" +
"            border-top: #FFF;" +
"            position: fixed;" +
"            bottom: 0px;" +
"            right: 0px;" +
"            left: 0px;" +
"            color: #FFF;" +
"            font-size: 20px;" +
"        }" +
"        #part{" +
"        	color: #336;" +
"        	position: relative;" +
"        	left: 350px;"+ 
"               top:60px" +
"        }" +
"        #Status{" +
"           color: #00cc44;" +
"            position: relative;" +
"            top: 0px;" +
"            right: 0px;" +
"            left: 0px;" +
"            font-size: 25px;" +
"            border-bottom: 2px solid black;" +
"        }" +
"        #Disciplina{" +
"               border-radius: 10px;" +
"		background: #cce6ff;" +
"           	border:2px solid #336;" +
"           	position: relative;" +
"           	top: 40px;" +
"           	width: 700px;" +
"           	left: 250px;" +
"		}" +
"	#Participantes{" +
"		border-radius: 20px;" +
"          	background: #e6f2ff;" +
"           	border:1px solid #336;" +
"           	position: relative;" +
"           	width: 700px;" +
"           	left: 250px;" +
"           	padding: 5px;" +
"		}" +
"	#Participanteshead{" +
"		border-radius: 20px;" +
"           	background: #336;" +
"        	color: #FFF;" +
"		}" +
"	#item{" +
"		color: #336;" +
"		}" +
"       #button{" +
"            	position: relative;" +
"            	left: 550px;" +
"            	bottom: 40px;}" +
"       #buttonHome{" +
"            	position: relative;" +
"            	left: 640px;" +
"            	bottom: 40px;" +
"            }" +
"	</style>");
        out.println("</head>");
        out.println("<body>");                  
        out.println("<span id=\"Status\">Aluno matriculado com Sucesso!</span><br /><br/>");
       
        //Tabela de informação da disciplina
        out.println("<table id=\"Disciplina\"><tr><td>Código Disciplina:</td><td> "+codDisciplina+"</td></tr>");
        out.println("<tr><td>Nome da Disciplina: </td><td>"+nomeDisciplina+"</td></tr>");
        out.println("<tr><td>Carga Horária: </td><td>"+cargaHoraria+"</td></tr></table>");
        
        //Tabela de informação dos alunos presentes na turma
        out.println("<span id=\"part\"><h2>Participantes</h2></span>");
        out.println("<table id=\"Participantes\"><thead id=\"Participanteshead\"><th>Matrícula</th><th>Nome</th><th>Curso</th></thead><tbody>");
            
        rec = st.executeQuery(
	        "SELECT * " +
	        "FROM alunos");
                 
        while(rec.next()) {
            if(codTurma == null ? rec.getString(4) == null : codTurma.equals(rec.getString(4))){
                out.println("<tr><td>"+rec.getString(1)+"</td><td>"+rec.getString(2)+"</td>");
                ResultSet rec2 = stmt.executeQuery(
	                "SELECT * " +
	                "FROM cursos " +
                        "WHERE codigo = '"+ rec.getString(3)+"'");
                               
                while(rec2.next()){
                                   
                    out.println("<td>"+rec2.getString(2)+"</td></tr>");
                }
            }
        }
        out.println("</tbody></table>");
        out.println("<br /><br><br><br><span id=\"button\"><input type=\"button\" value=\"Lista de Disciplinas\" onClick=\"history.go(-1)\" /></span>");
        out.println("<span id=\"buttonHome\"><input type=\"button\" value=\"Home\" onClick=\"history.go(-2)\" /></span>");
        out.println("<footer>" +
"		Natália de Souza Guimarães - 2017.1" +
"	</footer>");
        out.println("</body>");
        out.println("</html>");
            
        st.close();
        stmt.close();
        conn.close();
             
    }catch (SQLException s) {
           out.println("SQL Error: " + s.toString() + " "
             + s.getErrorCode() + " " + s.getSQLState());
      }catch (Exception e) {
           out.println("Error: " + e.toString()
             + e.getMessage());
          }
        out.close();
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(MatriculaAluno.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(MatriculaAluno.class.getName()).log(Level.SEVERE, null, ex);
        }
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
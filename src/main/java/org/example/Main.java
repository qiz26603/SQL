package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static String jdbcURL = "jdbc:mysql://admin.neosoft.vn:3306/kawhfhmf_NeoSQL";
    private static String USER_NAME = "kawhfhmf_neo_user";
    private static String PASSWORD = "NeoSQL2009!";
    private static int bookidfind = 0;
    private static int mssvfind = 0;
    public static void Create() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL, USER_NAME, PASSWORD);
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE Book (BookID INT NOT NULL, Bookname CHAR(30) NOT NULL, PublicationDate TEXT, Author CHAR(20) NOT NULL, BookKind TEXT, Mode CHAR(15), PRIMARY KEY (BookID));");
        statement.executeUpdate("CREATE TABLE SinhvienBook (MSSV INT NOT NULL, SVName CHAR(20) NOT NULL, Class TEXT, Majors TEXT, PRIMARY KEY (MSSV));");
        statement.executeUpdate("CREATE TABLE ManagerBook (ManagerID INT NOT NULL, Name CHAR(20) NOT NULL, BookID INT NOT NULL, Bookname VARCHAR(30) NOT NULL, DateAdded TEXT, MSSV INT NOT NULL, BorrowedBooks VARCHAR(30), BookStatus CHAR(10), BorrowedDay TEXT, PRIMARY KEY (ManagerID), FOREIGN KEY (BookID) REFERENCES Book(BookID), FOREIGN KEY (MSSV) REFERENCES SinhvienBook(MSSV));");
        System.out.println("all table created");
        connection.close();
    }
    public static void Drop(Connection connection, Statement statement) throws SQLException{
        statement.executeUpdate("DROP TABLE ManagerBook;");
        statement.executeUpdate("DROP TABLE Book;");
        statement.executeUpdate("DROP TABLE SinhvienBook;");
        System.out.println("all table dropped");
        connection.close();
    }
    public static void InsertBook(int BookID, String bookname, String PublicDate, String Author, String BookKind, String Mode) throws SQLException{
        Connection connection = DriverManager.getConnection(jdbcURL, USER_NAME, PASSWORD);
        Statement statement = connection.createStatement();
        String lenh = ", \"" + bookname + "\", \"" + PublicDate + "\", \"" + Author + "\", \"" + BookKind + "\", \"" + Mode + "\");";
        statement.executeUpdate("INSERT INTO Book(BookID,Bookname,PublicationDate,Author,BookKind,Mode) VALUES (" + BookID + lenh);
        System.out.println("INSERTED Book");
        connection.close();
    }
    public static void InsertSinhvienBook(int MSSV, String SVName, String Class, String Majors) throws SQLException{
        Connection connection = DriverManager.getConnection(jdbcURL, USER_NAME, PASSWORD);
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO SinhvienBook (MSSV,SVName,Class,Majors) VALUES (" + MSSV + ", \"" + SVName + "\", \"" + Class + "\", \"" + Majors + "\");");
        System.out.println("INSERTED SinhvienBook");
        connection.close();
    }
    public static void InsertManagerBook(int ManagerID, String Name, int BookID, String Bookname, String DateAdded, int MSSV, String BorrowedBooks, String BookStatus, String BorrowedDay) throws SQLException{
        Connection connection = DriverManager.getConnection(jdbcURL, USER_NAME, PASSWORD);
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO ManagerBook (ManagerID,Name,BookID,Bookname,DateAdded,MSSV,BorrowedBooks,BookStatus,BorrowedDay) VALUES (" + ManagerID + ", \"" + Name + "\", " + BookID + ", \"" + Bookname + "\", \"" + DateAdded + "\", " + MSSV + ", \"" + BorrowedBooks + "\", \"" + BookStatus + "\", \"" + BorrowedDay + "\");");
        System.out.println("INSERTED ManagerBook");
        connection.close();
    }
    public static void QueryManageBook() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int idsach = 0;
        String enter = "";
        boolean flag = false;
        ArrayList <Integer> a = new ArrayList<Integer>();
        Connection connection = DriverManager.getConnection(jdbcURL, USER_NAME, PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet result1 = statement.executeQuery("SELECT BookID, Bookname FROM Book;");
        do {
            while (result1.next()){
                System.out.println(result1.getInt(1) + " | " + result1.getString(2));
                a.add(result1.getInt(1));
            }
//            for (int i = 0; i < a.size(); i++){
//                System.out.println(a.get(i).intValue());
//            }
            System.out.println("Mời bạn nhập ID sách muốn xem tại tất cả các bảng liên quan: ");
            idsach = sc.nextInt();
            enter = sc.nextLine();
            for (int i = 0; i < a.size(); i++){
                if (idsach == a.get(i).intValue()){
                    flag = true;
                }
            }
            if (!flag){
                System.out.println("ID sách bạn muốn tìm không hợp lệ hoặc không tồn tại. Vui lòng nhập lại.");
            }
        } while (!flag);
        ResultSet result = statement.executeQuery("SELECT ManagerID, Name, BookID, Bookname, DateAdded, MSSV, BookStatus, BorrowedDay FROM ManagerBook WHERE BookID = " + idsach + ";");
        while (result.next()){
            bookidfind = result.getInt(3);
            mssvfind = result.getInt(6);
            System.out.println(result.getInt(1) + " | " + result.getString(2) + " | " + result.getInt(3) + " | " + result.getString(4) + " | " + result.getString(5) + " | " + result.getInt(6) + " | " + result.getString(7) + " | " + result.getString(8));
            QuerySinhvienBook();
        }
    }
    public static void QueryBook() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL, USER_NAME, PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT BookID, Bookname, Author, BookKind FROM Book WHERE BookID = " + bookidfind + ";");
        while (result.next()){
            System.out.println(result.getInt(1) + " | " + result.getString(2) + " | " + result.getString(3) + " | " + result.getString(4));
        }
    }
    public static void QuerySinhvienBook() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL, USER_NAME, PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT MSSV, SVName, Class, Majors FROM SinhvienBook WHERE MSSV = " + mssvfind + ";");
        while (result.next()){
            System.out.println(result.getInt(1) + " | " + result.getString(2) + " | " + result.getString(3) + " | " + result.getString(4));
        }
    }
    public static void TruyvanIDSach() throws SQLException {
        QueryManageBook();
        QueryBook();
        System.out.println("Truy vấn thành công.");
    }
    public static void main(String[] args){
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, USER_NAME, PASSWORD);
            Statement statement = connection.createStatement();
//                      Drop(connection, statement);
//                      Create();
            Scanner sc = new Scanner(System.in);
            int bookid, mssv, managerID;
            String bookname, PublicDate, author, bookkind, mode, svname, classes, majors, borrowbook, bookstatus, managername, dateadded, borrowedDay;
            String ignore;
            boolean flagsai, flagtiep, flagadd = true;
            do{
                int choosee = 0;
                System.out.println("Bạn có muốn thêm dữ liệu vào các bảng không ?\n1)Có      0)Không");
                choosee = sc.nextInt();
                ignore = sc.nextLine();
                if (choosee == 1){
                    do {
                        int choose = 0;
                        flagsai = false;
                        flagtiep = false;
                        System.out.println("Mời bạn chọn thêm dữ liệu vào bảng nào: \n1) TABLE Book\n2) TABLE SinhvienBook\n3) TABLE ManagerBook\nLựa chọn của bạn là: ");
                        choose = sc.nextInt();
                        if (choose == 1) {
                            System.out.println("Mời bạn nhập các thông tin sau để thêm dữ liệu: ");
                            System.out.println("TABLE Book:\nID sách: ");
                            bookid = sc.nextInt();
                            ignore = sc.nextLine();
                            System.out.println("Tên sách: ");
                            bookname = sc.nextLine();
                            System.out.println("Ngày sản xuất sách: ");
                            PublicDate = sc.nextLine();
                            System.out.println("Tác giả: ");
                            author = sc.nextLine();
                            System.out.println("Loại sách: ");
                            bookkind = sc.nextLine();
                            System.out.println("Trạng thái sách: ");
                            mode = sc.nextLine();
                            InsertBook(bookid, bookname, PublicDate, author, bookkind, mode);
                        }
                        else if (choose == 2) {
                            System.out.println("TABLE SinhvienBook:\nMSSV: ");
                            mssv = sc.nextInt();
                            ignore = sc.nextLine();
                            System.out.println("Tên sinh viên: ");
                            svname = sc.nextLine();
                            System.out.println("Lớp: ");
                            classes = sc.nextLine();
                            System.out.println("Ngành học: ");
                            majors = sc.nextLine();
                            InsertSinhvienBook(mssv, svname, classes, majors);
                        }
                        else if (choose == 3) {
                            System.out.println("TABLE ManagerBook:\nID nhập sách: ");
                            managerID = sc.nextInt();
                            ignore = sc.nextLine();
                            System.out.println("Tên quản lý: ");
                            managername = sc.nextLine();
                            System.out.println("ID sách: ");
                            bookid = sc.nextInt();
                            ignore = sc.nextLine();
                            System.out.println("Tên sách: ");
                            bookname = sc.nextLine();
                            System.out.println("Ngày nhập sách: ");
                            dateadded = sc.nextLine();
                            System.out.println("MSSV: ");
                            mssv = sc.nextInt();
                            ignore = sc.nextLine();
                            System.out.println("Sách mượn: ");
                            borrowbook = sc.nextLine();
                            System.out.println("Trạng thái sách mượn: ");
                            bookstatus = sc.nextLine();
                            System.out.println("Ngày sinh viên trả sách: ");
                            borrowedDay = sc.nextLine();
                            InsertManagerBook(managerID, managername, bookid, bookname, dateadded, mssv, borrowbook, bookstatus, borrowedDay);
                        }
                        else {
                            System.out.println("Bạn đã chọn sai. Vui lòng chọn lại. ");
                            flagsai = true;
                        }
                        boolean flagagain = false;
                        do {
                            if (choose == 1 || choose == 2 || choose == 3) {
                                flagagain = false;
                                int choosecontinue = 0;
                                System.out.println("Bạn có muốn tiếp tục thêm dữ liệu vào bảng khác không? \n1) Có\n0) Không\nLựa chọn của bạn là: ");
                                choosecontinue = sc.nextInt();
                                if (choosecontinue == 1) {
                                    flagtiep = true;
                                } else if (choosecontinue == 0) {
                                    flagtiep = false;
                                } else {
                                    System.out.println("Bạn đã chọn lựa không hợp lệ. Vui lòng chọn lại.");
                                    flagagain = true;
                                }
                            }
                        } while (flagagain);
                    } while (flagsai || flagtiep);
                }
                else if (choosee == 0) {
                    System.out.println("Không thêm dữ liệu, tiến hành bước tiếp theo: TRUY VẤN");
                }
                else {
                    flagadd = false;
                }
            } while (!flagadd);
            TruyvanIDSach();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}

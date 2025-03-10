package manguardShift;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Shift {

	public static void main(String[] args) {
		viewAvailableShifts();

	}

	public static Connection connect() {

		final String URL = "jdbc:mysql://Ibrahims-MacBook-Pro.local:3306/shiftdatabase?useSSL=false&serverTimezone=UTC";;
		final String USER = "root";
		final String PASSWORD = "ElaIbo2024";

		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void employeeTable(int employee_id, String name, String department ) {
		// this method adds shift to the database into SQL.
		
		String sql = "INSERT INTO Employees (employee_id, name, department) VALUES (?,?,?)";
		// this is query to add values into the table.
		
		try (Connection conn = Shift.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			//connect to the sql server vie "connect" method and pass through the query by using prepareStatement object in Connection class.

			pstmt.setInt(1, employee_id);
			pstmt.setString(2, name);
			pstmt.setString(3, department);

			pstmt.executeUpdate();
			
			
			System.out.println("Employee added to employee list.");
		} catch (SQLException e) {
			System.out.println("Try different data!!!");;
		}
	}

	public static void shiftTable(int shift_id, int employee_id, int shift_date, String shift_time ) {
		// this method adds shift to the database into SQL.
		// sql de shift_date columnun datatype i int olarak degistirildi. cunku, date formatini bulamadim :( not BUL!!!!
		// employee_id , shift tablenda foregn key, yani eger shift date ve time eklemek istersen, shift tableinda ki employee_id ile
			// employees tableinda ki employee_id match olmak zorunda.
		
		String sql = "INSERT INTO Shifts (shift_id, employee_id, shift_date, shift_time) VALUES (?,?,?,?)";
		// this is query to add values into the table.
		
		try (Connection conn = Shift.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			//connect to the sql server vie "connect" method and pass through the query by using prepareStatement object in Connection class.

			pstmt.setInt(1, shift_id);
			pstmt.setInt(2, employee_id);
			pstmt.setInt(3, shift_date);
			pstmt.setString(4, shift_time);

			pstmt.executeUpdate();
			
			System.out.println("shift time and date added to shift list.");
			
		} catch (SQLException e) {
			System.out.println("Try different date!!!");;
		}
	}
	
	public static void viewAvailableShifts() {
		// see available shifts in Shifts table in SQL
		
		String sql ="select shift_date,shift_id,shift_time from Shifts where employee_id is null;";

		try (Connection conn = Shift.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			System.out.println("Available Shifts:");
			while (rs.next()) {
				System.out.println("Shift ID: " + rs.getInt("shift_id")+
						", Date: " + rs.getString("shift_date") + 
						", Time: " + rs.getString("shift_time"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void shiftRequest(int newEmployeeId, int shiftId) {
		// this table hold information of workers who took a shift from available shifts
		// shift_id and shift_time in MySQL server, datatype made unique. because only 1 person can take it
		String sql = "UPDATE Shift_Requests SET employee_id = ? WHERE shift_id = ?";
		String deleteRequestSql = "DELETE FROM Shifts WHERE shift_id = ?";

		try (Connection conn = Shift.connect();
				PreparedStatement updateStmt = conn.prepareStatement(sql);
				PreparedStatement deleteStmt = conn.prepareStatement(deleteRequestSql)) {

			// Vardiyayı yeni çalışana aktarma
			updateStmt.setInt(1, newEmployeeId);
			updateStmt.setInt(2, shiftId);
			updateStmt.executeUpdate();

			// Shifts tablosundan bu vardiyayı silme
			deleteStmt.setInt(1, shiftId);
			deleteStmt.executeUpdate();

			System.out.println("Shift successfully taken by employee ID: " + newEmployeeId);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

package tests;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ui.Patient;
import utils.PatientManagement;

public class PatientManagementTest {

	@Before
	public void setUp() throws Exception {
		Date testDate = new Date();
		Date testChangeDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		Patient patient = new Patient("John", "Doe", "FilePath", testDate, "Some notes");
		patient.setFile(System.getProperty("user.dir") + "\\src\\resources\\patients\\" + patient.getUID());
		PatientManagement.exportPatient(patient);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}

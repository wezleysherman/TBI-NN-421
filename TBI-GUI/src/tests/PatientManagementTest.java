package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.NoSuchPaddingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ui.Patient;
import utils.PatientManagement;

public class PatientManagementTest {
	private Patient patient;
	private String uid;
	Date testDate;
	Date testChangeDate;

	@Before
	public void setUp() throws Exception {
		testDate = new Date();
		testChangeDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		patient = new Patient("John", "Doe", "FilePath", testDate, "Some notes");
		uid = patient.getUID();
		patient.setFile(System.getProperty("user.dir") + "\\src\\resources\\patients\\" + uid);
		PatientManagement.exportPatient(patient);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRead() throws Exception {
		patient = (Patient)PatientManagement.importPatient(System.getProperty("user.dir") + "\\src\\resources\\patients\\", patient.getUID());
		assertEquals(patient.getDate(), testDate);
		assertEquals(patient.getFirstName(), "John");
		assertEquals(patient.getLastName(), "Doe");
		assertEquals(patient.getFile(), "FilePath");
		assertEquals(patient.getNotes(), "Some notes");
		assertEquals(36, patient.getUID().length());
		
		assertNotEquals(patient.getDate(), testChangeDate);
		assertNotEquals(patient.getFile(), "The wrong file path");
	}

}

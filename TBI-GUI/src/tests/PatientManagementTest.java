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
	private Patient patient2;
	private String uid;
	Date testDate;

	@Before
	public void setUp() throws Exception {
		testDate = new Date();

		try{
			patient = new Patient("John", "Doe", testDate, "Some notes");
			uid = patient.getUID();
			PatientManagement.exportPatient(patient);

			patient2 = new Patient("Bob", "Smith", testDate, "Some notes");
			uid = patient2.getUID();
			PatientManagement.exportPatient(patient2);
		} catch(Exception e){

		}
	}

	@Test
	public void testRead() throws Exception {
		patient = (Patient)PatientManagement.importPatient(System.getProperty("user.dir") + "\\src\\resources\\patients\\", patient.getUID());
		assertEquals(patient.getDate(), testDate);
		assertEquals(patient.getFirstName(), "John");
		assertEquals(patient.getLastName(), "Doe");
		assertEquals(patient.getFile(), System.getProperty("user.dir") + "\\src\\resources\\patients\\" + patient.getUID());
		assertEquals(patient.getNotes(), "Some notes");
		assertEquals(36, patient.getUID().length());

		patient2 = (Patient)PatientManagement.importPatient(System.getProperty("user.dir") + "\\src\\resources\\patients\\", patient2.getUID());
		assertEquals(patient2.getDate(), testDate);
		assertEquals(patient2.getFirstName(), "Bob");
		assertEquals(patient2.getLastName(), "Smith");
		assertEquals(patient2.getFile(), System.getProperty("user.dir") + "\\src\\resources\\patients\\" + patient2.getUID());
		assertEquals(patient2.getNotes(), "Some notes");
		assertEquals(36, patient2.getUID().length());
	}

}

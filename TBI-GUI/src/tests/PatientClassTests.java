package tests;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import utils.Patient;
import utils.PatientManagement;
import utils.Scan;

import static org.junit.Assert.*;

public class PatientClassTests {

	@Test
	public void PatientTestInitGet() {
		Date testDate = new Date();
		Date testChangeDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		Patient patient = new Patient("John", "Doe", /*"FilePath",*/ testDate, "Some notes");
		assertEquals(patient.getDate(), testDate);
		assertEquals(patient.getFirstName(), "John");
		assertEquals(patient.getLastName(), "Doe");
		//assertEquals(patient.getDirectory(), "FilePath");
		assertEquals(patient.getNotes(), "Some notes");
		assertEquals(36, patient.getUID().length());
		
		assertNotEquals(patient.getDate(), testChangeDate);
		//assertNotEquals(patient.getDirectory(), "The wrong file path");
	}
	
	@Test
	public void PatientTestSet() {
		Date testDate = new Date();
		Date testChangeDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
		Patient patient = new Patient("John", "Doe", testDate, "Some notes");
		
		patient.setDate(testChangeDate);
		patient.setFirstName("Jane");
		patient.setLastName("McJane");
		patient.setNotes("A whole lot of notes.");
		
		assertNotEquals(patient.getDate(), testDate);
		assertNotEquals(patient.getFirstName(), "John");
		assertNotEquals(patient.getLastName(), "Doe");
		assertNotEquals(patient.getNotes(), "Some notes");
		
		assertEquals(patient.getDate(), testChangeDate);
		assertEquals(patient.getFirstName(), "Jane");
		assertEquals(patient.getLastName(), "McJane");
		assertEquals(patient.getFile(), new File(buildDefaultPath(),patient.getUID()).getAbsolutePath());
		assertEquals(patient.getNotes(), "A whole lot of notes.");
	}
	
	@Test
	public void PatientScanOrderTest() {
		Date date1 = new Date();
		Date date2 = new Date(date1.getYear() - 1, 1, 21);
		Date date3 = new Date(date1.getYear() + 1, 1, 21);
		Patient patient = new Patient("John", "Doe", new Date(), "Some notes");
		Scan scan1 = new Scan(date1, new File(buildImagePath("porcupine.jpg")), "scan 1");
		Scan scan2 = new Scan(date2, new File(buildImagePath("terrier2.jpg")), "scan 2");
		Scan scan3 = new Scan(date3, new File(buildImagePath("whale.png")), "scan 3");
		patient.addRawScan(scan2);
		patient.addRawScan(scan1);
		assertEquals(date1, patient.getLastRawScanDate());
		
		patient.addRawScan(scan3);
		assertEquals(date3, patient.getLastRawScanDate());
		
		patient.delRawScan(0);
		assertEquals(scan1, patient.getRawScan(0));
	}
	
	@Test
	public void PatientScanAnalysisTest() throws Exception {
		Patient patient = new Patient("John", "Doe", new Date(), "Some notes");
		Date date1 = new Date();
		Scan scan1 = new Scan(date1, new File(buildImagePath("whale.jpg")), "scan 1");
		patient.addRawScan(scan1);
		
		scan1 = patient.getRawScan(0);
		patient.analyzeScan(scan1);
		assertEquals(scan1.getLabel(), "killer whale");
		assertTrue(scan1.getNotes().contains("killer whale"));
	}

	private static String buildDefaultPath() {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "patients");
		return f.getAbsolutePath();
	}
	
	private static String buildImagePath(String filename) {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "tensor_test_images");
		f = new File(f.getAbsolutePath(), filename);
		return f.getAbsolutePath();
	}
}

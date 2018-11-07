package tests;

import ui.Patient;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.*;

public class PatientClassTests {

	@Test
	public void PatientTestInitGet() {
		Date testDate = new Date();
		Date testChangeDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		Patient patient = new Patient("John", "Doe", "FilePath", testDate, "Some notes");
		assertEquals(patient.getDate(), testDate);
		assertEquals(patient.getFirstName(), "John");
		assertEquals(patient.getLastName(), "Doe");
		assertEquals(patient.getDirectory(), "FilePath");
		assertEquals(patient.getNotes(), "Some notes");
		
		assertNotEquals(patient.getDate(), testChangeDate);
		assertNotEquals(patient.getDirectory(), "The wrong file path");
	}
	
	@Test
	public void PatientTestSet() {
		Date testDate = new Date();
		Date testChangeDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
		Patient patient = new Patient("John", "Doe", "FilePath", testDate, "Some notes");
		
		patient.setDate(testChangeDate);
		patient.setDirectory("newFilePath");
		patient.setFirstName("Jane");
		patient.setLastName("McJane");
		patient.setNotes("A whole lot of notes.");
		
		assertNotEquals(patient.getDate(), testDate);
		assertNotEquals(patient.getFirstName(), "John");
		assertNotEquals(patient.getLastName(), "Doe");
		assertNotEquals(patient.getDirectory(), "FilePath");
		assertNotEquals(patient.getNotes(), "Some notes");
		
		assertEquals(patient.getDate(), testChangeDate);
		assertEquals(patient.getFirstName(), "Jane");
		assertEquals(patient.getLastName(), "McJane");
		assertEquals(patient.getDirectory(), "newFilePath");
		assertEquals(patient.getNotes(), "A whole lot of notes.");
	}
}

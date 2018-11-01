package tests;

import ui.Patient;

import java.util.Date;

import org.junit.jupiter.api.Test;

class PatientClassTests {

	@Test
	void PatientTestInitGet() {
		Date testDate = new Date();
		Date testChangeDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

		Patient patient = new Patient("John", "Doe", "FilePath", testDate, "Some notes");
		assert(patient.getDate() == testDate);
		assert(patient.getFirstName().equals("John"));
		assert(patient.getLastName().equals("Doe"));
		assert(patient.getFile().equals("FilePath"));
		assert(patient.getNotes().equals("Some notes"));
		
		assert(patient.getDate() != testChangeDate);
		assert(!patient.getFile().equals("The wrong file path"));
	}
	
	@Test
	void PatientTestSet() {
		Date testDate = new Date();
		Date testChangeDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
		Patient patient = new Patient("John", "Doe", "FilePath", testDate, "Some notes");
		
		patient.setDate(testChangeDate);
		patient.setFile("newFilePath");
		patient.setFirstName("Jane");
		patient.setLastName("McJane");
		patient.setNotes("A whole lot of notes.");
		
		assert(patient.getDate() != testDate);
		assert(!patient.getFirstName().equals("John"));
		assert(!patient.getLastName().equals("Doe"));
		assert(!patient.getFile().equals("FilePath"));
		assert(!patient.getNotes().equals("Some notes"));
		
		assert(patient.getDate() == testChangeDate);
		assert(patient.getFirstName().equals("Jane"));
		assert(patient.getLastName().equals("McJane"));
		assert(patient.getFile().equals("newFilePath"));
		assert(patient.getNotes().equals("A whole lot of notes."));
	}
}

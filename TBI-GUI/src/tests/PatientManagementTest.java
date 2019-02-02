package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import utils.Patient;
import utils.PatientEntry;
import utils.PatientManagement;

public class PatientManagementTest {
	private Patient patient;
	private Patient patient2;
	private Patient patientEdit;
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
			
			patientEdit = new Patient("Jack", "Doe", testDate, "Some notes");
			uid = patientEdit.getUID();
			PatientManagement.exportPatient(patientEdit);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testRead() throws Exception {
		patient = (Patient)PatientManagement.importPatient(PatientManagement.getDefaultPath(), patient.getUID());
		assertEquals(patient.getDate(), testDate);
		assertEquals(patient.getFirstName(), "John");
		assertEquals(patient.getLastName(), "Doe");
		assertEquals(patient.getFile(), new File(PatientManagement.getDefaultPath(), patient.getUID()).getAbsolutePath());
		assertEquals(patient.getNotes(), "Some notes");
		assertEquals(36, patient.getUID().length());

		patient2 = (Patient)PatientManagement.importPatient(PatientManagement.getDefaultPath(), patient2.getUID());
		assertEquals(patient2.getDate(), testDate);
		assertEquals(patient2.getFirstName(), "Bob");
		assertEquals(patient2.getLastName(), "Smith");
		assertEquals(patient2.getFile(), new File(PatientManagement.getDefaultPath(),patient2.getUID()).getAbsolutePath());
		assertEquals(patient2.getNotes(), "Some notes");
		assertEquals(36, patient2.getUID().length());
	}
	
	@Test
	public void testDelete() throws Exception {
		Patient temp = new Patient("To", "Delete", testDate, "Some notes");
		String uid = temp.getUID();
		PatientManagement.exportPatient(temp);
		
		PatientManagement.deletePatient(PatientManagement.getDefaultPath(), uid);
		File f = new File(PatientManagement.getDefaultPath(), uid);
		assertTrue(!f.exists());
		assertTrue(!PatientManagement.getPatientList().containsKey(uid));
	}
	
	@Test
	public void testEdit() throws Exception {
		patientEdit = (Patient)PatientManagement.importPatient(PatientManagement.getDefaultPath(), patientEdit.getUID());
		assertEquals(patientEdit.getDate(), testDate);
		assertEquals(patientEdit.getFirstName(), "Jack");
		assertEquals(patientEdit.getLastName(), "Doe");
		assertEquals(patientEdit.getFile(), new File(PatientManagement.getDefaultPath(), patientEdit.getUID()).getAbsolutePath());
		assertEquals(patientEdit.getNotes(), "Some notes");
		assertEquals(36, patientEdit.getUID().length());
		
		patientEdit.setFirstName("Edited First");
		patientEdit.setLastName("Edited Last");
		
		PatientManagement.exportPatient(patientEdit);
		patientEdit = PatientManagement.importPatient(PatientManagement.getDefaultPath(), patientEdit.getUID());
		
		assertEquals(patientEdit.getFirstName(), "Edited First");
		assertEquals(patientEdit.getLastName(), "Edited Last");
	}
	
	@Test
	public void testSpecialCharacters() throws Exception{
		patientEdit.setFirstName("!@#$%^&*()_+-=  ");
		patientEdit.setLastName("~`<>,.?:;'{}[]/\"\\|");
		
		PatientManagement.exportPatient(patientEdit);
		patientEdit = PatientManagement.importPatient(PatientManagement.getDefaultPath(), patientEdit.getUID());
		
		assertEquals(patientEdit.getFirstName(), "!@#$%^&*()_+-=  ");
		assertEquals(patientEdit.getLastName(), "~`<>,.?:;'{}[]/\"\\|");
	}
	
	@Test
	public void testPatientList() throws Exception {
		Hashtable patientList = PatientManagement.getPatientList();
		
		PatientEntry temp = (PatientEntry)patientList.get(patient.getUID());
		assertEquals(temp.name, patient.getFirstName() + " " + patient.getLastName());
		temp = (PatientEntry)patientList.get(patient2.getUID());
		assertEquals(temp.name, patient2.getFirstName() + " " + patient2.getLastName());
	}
	
	@Test
	public void testPatientSort() throws Exception {
		Hashtable patientList = PatientManagement.getPatientList();
		
		PatientEntry temp = (PatientEntry)patientList.get(patient.getUID());
		assertEquals(temp.name, patient.getFirstName() + " " + patient.getLastName());
		temp = (PatientEntry)patientList.get(patient2.getUID());
		assertEquals(temp.name, patient2.getFirstName() + " " + patient2.getLastName());
		
		((PatientEntry)patientList.get(patient2.getUID())).date = new Date(new Date().getYear() + 2, 1, 21);
		((PatientEntry)patientList.get(patient.getUID())).date = new Date(new Date().getYear() + 1, 1, 21);

		LinkedList dateSort = PatientManagement.dateSortPatients();
		for(int i = 1; i < dateSort.size(); i++) {
			assertEquals(-1, ((PatientEntry)dateSort.get(i - 1)).date.compareTo(((PatientEntry)dateSort.get(i)).date));
		}
	}
	
	@Test
	public void testCatchBadKey() throws Exception {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "tests");
		f = new File(f.getAbsolutePath(), "patients");
		
		try {
			PatientManagement.importPatient(f.getAbsolutePath(), patient.getUID());
		} catch (Exception e) {
			if(e.getClass().equals(IOException.class)) {
				assertEquals(e.getMessage(),"Invalid key. Read failed.");
			} else {
				throw e;
			}
		}
	}
	
	@Test
	public void testPatientDNE() throws Exception {
		try {
			PatientManagement.importPatient(PatientManagement.getDefaultPath(), "12");
		} catch (Exception e) {
			if(e.getClass().equals(IOException.class)) {
				assertEquals(e.getMessage(), "The UID you are trying to access does not exist. Read failed.");
			} else {
				throw e;
			}
		}
	}
	/*
	@Test
	public void testCatchSerialization() throws Exception {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "tests");
		f = new File(f.getAbsolutePath(), "patients");
		PatientManagement.setDefaultPath(f.getAbsolutePath());
		PatientManagement.importPatientList();
		
		try {
			PatientManagement.importPatient(f.getAbsolutePath(), patient.getUID());
		} catch (Exception e) {
			if(e.getClass().equals(IOException.class)) {
				assertEquals(e.getMessage(),"You are attempting to access a previous version of the Patient class. Read failed.");
			} else {
				throw e;
			}
		}
		
		PatientManagement.setDefaultPath(PatientManagement.getDefaultPath());
		PatientManagement.importPatientList();
	}
	*/
}

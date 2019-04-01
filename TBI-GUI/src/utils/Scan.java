package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.InputMismatchException;
import java.awt.image.BufferedImage;

import nifti.Nifti1Dataset;

public class Scan implements Serializable, Comparable{

	private Date dateOfScan;
	private File scan;
	private String notes;
	private String label;
	private float labelProb;
	private boolean isNifti = false;
	private Nifti1Dataset nifti;
	private int [][][] niftiArray = null;

	public Scan() {
		this.setDateOfScan(null);
		this.setScan(null);
		this.notes = "";
	}
	
	public Scan(Date dOS, File inScan) {
		this.setDateOfScan(dOS);
		this.setScan(inScan);
		
		if(inScan.getAbsolutePath().contains(".nii")) {
			try {
				createNifti();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isNifti = true;
		}
	}
	
	public Scan(Date dOS, File inScan, String inNotes) {
		this.setDateOfScan(dOS);
		this.setScan(inScan);
		this.setNotes(inNotes);
		
		if(inScan.getAbsolutePath().contains(".nii")) {
			try {
				createNifti();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isNifti = true;
		}
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		notes = notes + "\nLabel:" + label;
	}

	public float getLabelProb() {
		return labelProb;
	}

	public void setLabelProb(float labelProb) {
		this.labelProb = labelProb;
		notes = notes + "\nProbability:" + labelProb;
	}

	public Date getDateOfScan() {
		return dateOfScan;
	}

	public void setDateOfScan(Date dateOfScan) {
		this.dateOfScan = dateOfScan;
	}

	public File getScan() {
		return scan;
	}

	public void setScan(File scan) {
		this.scan = scan;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public int compareTo(Object other) {
		if(other.getClass() != Scan.class) {
			throw new InputMismatchException("You tried to compare a scan with a not-scan.");
		}
		return ((Scan)other).getDateOfScan().compareTo(this.dateOfScan);
	}
	
	//nifti-only methods
	private void createNifti() throws FileNotFoundException, IOException {
		nifti = new Nifti1Dataset(scan.getAbsolutePath());
		nifti.readHeader();
		getNiftiArray();
	}
	
	public boolean isNifti() {
		return isNifti;
	}
	
	public Nifti1Dataset getNifti() {
		return nifti;
	}
	
	public int [][][] getNiftiArray() throws IOException{
		if(!isNifti) {
			return new int[0][0][0];
		}else if (niftiArray == null){
			double [][][] voxels = nifti.readDoubleVol((short)0);
			niftiArray = new int[voxels.length][voxels[0].length][voxels[0][0].length];
			for(int i = 0; i < voxels.length; i++) {
				for(int j = 0; j < voxels[i].length; j++) {
					for(int k = 0; k < voxels[i][j].length; k++) {
						niftiArray[i][j][k] = (int)voxels[i][j][k];
					}
				}
			}
		}
		return niftiArray;
	}
	
	public int getNumNiftiSlices() throws IOException{
		if(!isNifti) {
			return 0;
		}else if (niftiArray == null){
			getNiftiArray();
		}
		
		return niftiArray.length;
	}
	
	public int [][] getNiftiSlice(int slice) throws IOException{
		if(!isNifti) {
			return new int[0][0];
		}else if (niftiArray == null){
			getNiftiArray();
		}
		
		return niftiArray[slice];
	}
	
	//for testing purposes
	public static void main(String [] args) throws FileNotFoundException, IOException {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "tensor_test_images");
		f = new File(f.getAbsolutePath(), "knee.nii");

		Scan nscan = new Scan(new Date(), f);
		Nifti1Dataset n = nscan.getNifti();
		
		double [][][] voxels = n.readDoubleVol((short)0);
		int [][][] ints = nscan.getNiftiArray();
		int [][] slice = nscan.getNiftiSlice((int)(nscan.getNumNiftiSlices()/2));
		
		//BufferedImage img = new BufferedImage(slice[0].length, slice.length, BufferedImage.);
	}
	
}

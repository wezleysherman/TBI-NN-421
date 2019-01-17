# Authors: Wezley Sherman
#
# Python script to be used for mass deidentification of DICOM files
#
# Python os isdir documentation:
# https://docs.python.org/dev/library/os.path.html#os.path.isdir
# Python glob documentation:
# https://pymotw.com/2/glob/
import pydicom
import glob
import sys
import os
import tqdm

def fetch_dicoms(directory):
	''' fetch_dicoms handles loading in a directory of dicom images

		Input:
			- directory: The image directory we are importing from
		Output:
			- Array of dicom images
	'''
	dicoms = []
	for file in glob.glob(directory + '/*/*/*/*/*/*', recursive=True):
		dicoms.append(pydicom.dcmread(file))
	return dicoms

def anonimize_dicoms(dicom_files):
	''' anonimize_dicoms handles mass deidentifying an array of dicom images

		Input:
			- dicom_files: an array containing all dicom files we are deidentifying
		Output:
			- Array of anonymized dicom images
	'''
	anon_dicoms = []
	for dicom in dicom_files:
		# Create a copy of the dicom so we don't accidentally mutate the original
		anon_com = dicom
		anon_com.PatientName = ''
		anon_com.PatientID = ''
		anon_com.PatientAge = ''
		anon_com.PatientBirthDate = ''
		anon_com.StudyID = ''
		anon_dicoms.append(anon_com)
	return anon_dicoms

def save_dicoms(dicoms, output_dir):
	''' save_dicoms handles saving an array of dicom images to the associated output folder

		Input:
			- dicoms: an array of dicom images to write out
			- output_dir: The image directory we are saving dicoms to
	'''
	for i, dicom in tqdm.tqdm(enumerate(dicoms)):
		save_dir = output_dir + '/anon-' + str(i) + '.dcm' 
		pydicom.write_file(save_dir, dicom)

def anon_directory(input_folder, output_folder):
	''' anon_directory handles anonimizing dicoms in a directory and saving them out to a new directory

		Input:
			- input_folder: Input directory we are importing dicoms from
			- output_folder: Output directory we are exporting the anonimized dicoms to
	'''
	if not os.path.isdir(input_folder):
		print("The input directory doesn't exist!")
		return
	if not os.path.isdir(output_folder):
		print("INFO: The output directory doesn't exist.\n Creating output directory")
		os.mkdir(output_folder)
		print("INFO: Output direcotry created!")

	dicom_files = fetch_dicoms(input_folder)

	if len(dicom_files) == 0:
		print("ERROR: The input directory is empty! There is nothing to convert")
		return

	anon_coms = anonimize_dicoms(dicom_files)
	save_dicoms(anon_coms, output_folder)

if __name__ == '__main__':
	if len(sys.argv) < 3:
		print("ERROR: You are missing parameters!\n Use: python3 DICOMAnonimizer.py [input folder] [output folder]")
	else:
		anon_directory(sys.argv[1], sys.argv[2])
		print("INFO: Done anonymizing directory! Outputs saved to: " + str(sys.argv[2]))

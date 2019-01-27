# Authors: Wezley Sherman
#
# Reference Attributes: Pydicom documentation, glob documentation
# https://pydicom.github.io/pydicom/stable/getting_started.html
# https://pymotw.com/2/glob/
#
# This class is a part of the BSSCS Net Framework to import DICOM images and convert them from DICOM to NIFTI
from DICOMImporter import DICOMImporter
from CTConverter.DICOM_operations import DICOM_2_NIFTI, prepare_scans
import pandas as pd
import numpy as np
import pydicom

class BSSCS_DATA_PROCESSOR:
	def __init__(self, dicom_path, nifti_path, output_path):
		self.path = path_to_data
		self.dicoms_orig = []
		self.dicoms_norm = []
		self.nifti = []
		self.dicom_path = dicom_path
		self.nifti_path = nifti_path
		self.dicom_output = output_path
		self.total_dicoms = 0
		self.total_nifti = 0
		self.import_dicom_data()
		self.normalize_dicom_data()
		self.convert_dicom_to_nifti()


	def import_dicom_data(self):
		''' Handles mass importing all DICOMs from a folder
			
			Params:
				- None

			Returns: 
				- None	
		'''
		self.dicoms_orig = DICOMImporter.open_dicom_from_folder(self.dicom_path)
		self.total_dicoms = len(self.dicoms_orig)


	def convert_dicom_to_nifti(self):
		''' Handles converting normalized dicoms to nifti
			
			Params:
				- None

			Returns: 
				- None	
		'''
		dicom_path = prepare_scans.convert(self.output_path)
		self.nifti = DICOM_2_NIFTI.get_nifti_array(dicom_path)
		self.total_nifti = len(self.nifti)
		

	def normalize_dicom_data(self):
		''' Handles normalizing imported dicom data
			
			Params:
				- None

			Returns: 
				- None	
		'''
		self.dicom_norm = DICOMImporter.deidentify_dicom_images(self.dicoms_orig)
		for i, dicom in enumerate(self.dicom_norm):
			pydicom.write_file(self.output_path + '/anon-' + str(i) + '.dcm')


	def fetch_norm_dicoms(self):
		''' Handles returning the normalized dicom array
			
			Params:
				- None

			Returns: 
				- dicoms: array -- array of normalized dicoms
		'''
		return self.dicoms_norm

	def fetch_nifti_files(self):
		''' Handles returning the nifti files
			
			Params:
				- None

			Returns: 
				- nifti: array -- array of converted nifti files
		'''
		return self.nifti
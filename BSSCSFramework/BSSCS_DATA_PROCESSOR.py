# Authors: Wezley Sherman
#
# Reference Attributes: Pydicom documentation, glob documentation
# https://pydicom.github.io/pydicom/stable/getting_started.html
# https://pymotw.com/2/glob/
#
# This class is a part of the BSSCS Net Framework to import DICOM images and convert them from DICOM to NIFTI
from DICOMImporter import DICOMImporter
from CTConverter.DICOM_operations import DICOM_2_NIFTI, decompress_DICOM, prepare_scans
import pandas as pd
import numpy as np


class BSSCS_DATA_PROCESSOR:
	def __init__(self, path_to_data):
		self.path = path_to_data
		self.dicoms_orig = []
		self.dicoms_norm = []
		self.nifti = []

	def import_dicom_data(self):
		''' Handles mass importing all DICOMs from a folder
			
			Params:
				- None

			Returns: 
				- None	
		'''
		return None # lol

	def convert_dicom_to_nifti(self):
		''' Handles converting normalized dicoms to nifti
			
			Params:
				- None

			Returns: 
				- None	
		'''
		return None

	def normalize_dicom_data(self):
		''' Handles normalizing imported dicom data
			
			Params:
				- None

			Returns: 
				- None	
		'''
		return None

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
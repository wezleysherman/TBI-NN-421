# Authors: Wezley Sherman
#
# Reference Attributes: Pydicom documentation, glob documentation
# https://pydicom.github.io/pydicom/stable/getting_started.html
# https://pymotw.com/2/glob/
#
# This class is a part of the BSSCS Net Framework to import DICOM images
#
# BSSCS Docs Importer location: BSSCS_DOCS/dicom.html

import pydicom
import glob
import numpy

class DICOMImporter:
	def open_dicom_file(path):
		''' BSSCS open a dicom file

			Input:
				- path: file path to the dicom image
			Returns:
				- dicom file
		'''
		dicom_file = pydicom.dcmread(path)
		return dicom_file

	def get_dicom_pixel_array(dicom_image):
		''' BSSCS get the pixel array from the dicom image

			Input:
				- dicom_image: image we want the pixel array from
			Returns:
				- pixel array for dicom
		'''
		return dicom_image.pixel_array

	def open_dicom_from_folder(path):
		''' BSSCS open dicoms from folder

			Input:
				- path: file path to the dicom images
			Returns:
				- array of dicoms
		'''
		dicom_images = []
		for dicom_path in glob.glob(path + 'DeidentifiedDICOMS/*.dcm'):
			dicom_file = pydicom.dcmread(dicom_path)
			dicom_images.append(dicom_file)
		return dicom_images

	def deidentify_dicom_images(dicom_array):
		''' BSSCS deidentify an array of dicom images

			Input:
				- dicom_array: array of dicom images to deidentify
			Returns:
				- deidentified dicom images
		'''
		return_dicom = []
		for dicom in dicom_array:
			dicom.PatientName = 'Anonymized'
			dicom.PatientID = '0'
			dicom.PatientAge = '0'
			return_dicom.append(dicom)
		return return_dicom

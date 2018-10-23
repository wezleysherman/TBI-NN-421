# Authors: Wezley Sherman
# Reference Attributes: Pydicom documentation, glob documentation
# https://pydicom.github.io/pydicom/stable/getting_started.html
# https://pymotw.com/2/glob/
#
# This class is a part of the BSSCS Net Framework to import DICOM images
#

import pydicom
import glob
import numpy

class DICOMImporter:
	def open_dicom_file(path):
		dicom_file = pydicom.dcmread(path)
		return dicom_file

	def get_dicom_pixel_array(dicom_image):
		# TO-DO add decompression algorithms for compressed DICOMS?
		return dicom_image.PixelData

	def open_dicom_from_folder(path):
		dicom_images = []
		for dicom_path in glob.glob(path + '/*.dcm'):
			dicom_file = pydicom.dcmread(dicom_path)
			dicom_images.append(dicom_file)
		return dicom_images

	def deidentify_dicom_images(dicom_array):
		return_dicom = []
		for dicom in dicom_array:
			dicom.PatientName = 'Anonymized'
			dicom.PatientID = '0'
			dicom.PatientAge = '0'
			return_dicom.append(dicom)
		return return_dicom


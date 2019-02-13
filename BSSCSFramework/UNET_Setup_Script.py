from UNET_Data import UNET_DATA
from UNET_Graph import BSSCS_UNET
from DICOMImporter import DICOMImporter
from CTConverter.DICOM_operations import DICOM_2_NIFTI, decompress_DICOM, prepare_scans
import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt

def convert_dicom_to_nifti(path):
	''' Responsible for converting DICOM images to nifti Images

		Parameters:
			- path: string -- The path of our main DICOM folder

		Returns:
			- dicom_files_arr: array -- array of loaded DICOMS
			- as_nifti: array -- array of loaded NIFTI files
	'''
	# Convert DICOM to NIFTI
	dicom_files_arr = DICOMImporter.open_dicom_from_folder(path)
	dicom_path = prepare_scans.convert(path + 'DeidentifiedDICOMS/')
	as_nifti = DICOM_2_NIFTI.get_nifti_array(dicom_path)#	
	return dicom_files_arr, as_nifti


def test_unet(image_path, csv_path):
	''' Responsible for testing the UNET output to ensure the implementation is correct

		Parameters:
			- image_path: string -- path to test image
			- csv_path: string -- path to training CSV

	'''
	unet_data = UNET_DATA(images_path=image_path, csv_path=csv_path, label_classes=28)


def test_unet_training(image_path, csv_path, batch_size, iterations):
	print(image_path)
	print(csv_path)
	unet_data = UNET_DATA(images_path=image_path, csv_path=csv_path)
	unet = BSSCS_UNET(iterations, batch_size, unet_data)
	print(unet_data.get_next_batch())
	unet.train_unet()

test_unet(image_path="Data_right/Train/train", csv_path="Data_right/train.csv")

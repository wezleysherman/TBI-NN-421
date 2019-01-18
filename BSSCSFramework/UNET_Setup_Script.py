from UNET_Data import UNET_DATA
from UNET_Graph import BSSCS_UNET
from DICOMImporter import DICOMImporter
from CTConverter.DICOM_operations import DICOM_2_NIFTI, decompress_DICOM, prepare_scans

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

convert_dicom_to_nifti('Data/')
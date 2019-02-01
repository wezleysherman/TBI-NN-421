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


def test_unet(image_path):
	''' Responsible for testing the UNET output to ensure the implementation is correct

		Parameters:
			- image_path: string -- path to test image

		returns:
			output_img: arr -- pixel array for the output image

	'''
	unet = BSSCS_UNET(1, 1, None)
	dicom = DICOMImporter.open_dicom_file(image_path)
	dicom_arr = np.reshape(DICOMImporter.get_dicom_pixel_array(dicom), (1, 512, 512, 1))
	input_ph = tf.placeholder(tf.float32, shape=[None, 512, 512, 1]) # Placeholder vals were given by paper in initial layer -- these numbers were referenced from the paper.
	graph = unet.generate_unet_arch(input_ph)
	with tf.Session() as session:
		tf.global_variables_initializer().run()
		output = session.run(graph, feed_dict={input_ph: dicom_arr})
		print(output)
		print(output[0])
		print(output[0][0].shape)
		img = np.reshape(output[0], [2, 508, 508])
		plt.imshow(img[1])
		plt.show()
		plt.imshow(img[0])
		plt.show()


test_unet('test_dicom/test_dicom.dcm')

from DICOMImporter import DICOMImporter
from BSSCS_CNN import BSSCS_CNN
import tensorflow as tf

def test_dicom_importer():
	print(DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm'))
	print(DICOMImporter.open_dicom_from_folder('test_dicom'))
	dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
	#print(DICOMImporter.get_dicom_pixel_array(dicom_file))

	dicom_files_arr = DICOMImporter.open_dicom_from_folder('test_dicom')
	anon_dicoms = DICOMImporter.deidentify_dicom_images(dicom_files_arr)
	print(anon_dicoms)

def test_cnn_creator():
	bsscs = BSSCS_CNN()
	input_placeholder = tf.placeholder(tf.float32, shape=[None, 512, 512, 1])
	cnn_block_full = bsscs.create_cnn_block(input=input_placeholder, filters=64, kernel_size=[3, 3], cnn_strides=2, pool_size=[2, 2], pooling_strides=2)
	cnn_partial_cnn = bsscs.create_partial_cnn(input=input_placeholder, filters=64, kernel_size=[3, 3], cnn_strides=2)
	cnn_partial_pooling = bsscs.create_partial_pooling(input=input_placeholder, pool_size=[2, 2], pooling_strides=2)

	cnn_full_network = bsscs.create_convolution_network(input=input_placeholder, cnn_kernels=[[2, 2], [4, 4]], cnn_filters=[64, 128], pooling_filters=[[2, 2], [2, 2]])
	print('cnn full block: ' + str(cnn_block_full))
	print('cnn partial cnn: ' + str(cnn_partial_cnn))
	print('cnn partial pooling: ' + str(cnn_partial_pooling))
	print('full cnn network: ' + str(cnn_full_network))

test_cnn_creator()
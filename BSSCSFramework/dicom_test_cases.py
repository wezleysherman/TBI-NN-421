from DICOMImporter import DICOMImporter
from BSSCS_CNN import BSSCS_CNN
from BSSCS_IMG_PROCESSING import BSSCS_IMG_PROCESSING
import tensorflow as tf

def test_dicom_importer():
	print(DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm'))
	print(DICOMImporter.open_dicom_from_folder('test_dicom'))
	dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
	print(DICOMImporter.get_dicom_pixel_array(dicom_file))
	dicom_files_arr = DICOMImporter.open_dicom_from_folder('test_dicom')
	anon_dicoms = DICOMImporter.deidentify_dicom_images(dicom_files_arr)
	print(anon_dicoms)

def test_cnn_creator():
	bsscs = BSSCS_CNN()
	input_placeholder = tf.placeholder(tf.float32, shape=[None, 512, 512, 1])
	cnn_block_full = bsscs.create_cnn_block(input=input_placeholder, filters=64, kernel_size=[3, 3], cnn_strides=2, pool_size=[2, 2], pooling_strides=2)
	cnn_partial_cnn = bsscs.create_partial_cnn(input=input_placeholder, filters=64, kernel_size=[3, 3], cnn_strides=2)
	cnn_partial_pooling = bsscs.create_partial_pooling(input=input_placeholder, pool_size=[2, 2], pooling_strides=2)

	cnn_block_full_shape = bsscs.create_cnn_block(input_shape=[None, 256, 256, 1], filters=64, kernel_size=[3, 3], cnn_strides=2, pool_size=[2, 2], pooling_strides=2)
	cnn_partial_cnn_shape = bsscs.create_partial_cnn(input_shape=[None, 256, 256, 1], filters=64, kernel_size=[3, 3], cnn_strides=2)
	cnn_partial_pooling_shape = bsscs.create_partial_pooling(input_shape=[None, 256, 256, 1], pool_size=[2, 2], pooling_strides=2)

	cnn_full_network = bsscs.create_convolution_network(input_shape=[None, 512, 512, 1], cnn_kernels=[[2, 2], [4, 4]], cnn_filters=[64, 128], pooling_filters=[[2, 2], [2, 2]])
	print('cnn full block: ' + str(cnn_block_full))
	print('cnn partial cnn: ' + str(cnn_partial_cnn))
	print('cnn partial pooling: ' + str(cnn_partial_pooling))
	print('cnn full block with shape: ' + str(cnn_block_full_shape))
	print('cnn partial cnn with shape: ' + str(cnn_partial_cnn_shape))
	print('cnn partial pooling with shape: ' + str(cnn_partial_pooling_shape))
	print('full cnn network: ' + str(cnn_full_network))

def test_cnn_vis():
	bsscs = BSSCS_CNN()
	cnn_full_network = bsscs.create_convolution_network(input_shape=[None, 512, 512, 1], cnn_kernels=[[2, 2], [4, 4]], cnn_filters=[64, 128], pooling_filters=[[2, 2], [2, 2]])

	print('full cnn network: ' + str(cnn_full_network))

	dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
	dicom_pixel_arr = DICOMImporter.get_dicom_pixel_array(dicom_file)
 	
 	# images for UI will use this method
	conv_output = bsscs.get_convolutional_filters(dicom_pixel_arr, cnn_full_network)
	print(conv_output)

def test_img_proc():
	dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
	dicom_file_pixel = DICOMImporter.get_dicom_pixel_array(dicom_file)
	contrasted_img = BSSCS_IMG_PROCESSING.contrast_image([dicom_file_pixel], 2)
	scaled_img = BSSCS_IMG_PROCESSING.scale_image([dicom_file_pixel], 0.5)

test_cnn_vis()
test_cnn_creator()
test_dicom_importer()
test_img_proc()


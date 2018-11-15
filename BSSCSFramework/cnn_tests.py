''' Authors: Wezley Sherman
	Unit Tests for the CNN framework implementation
	Unit Test Reference: https://docs.python.org/2/library/unittest.html
'''
from DICOMImporter import DICOMImporter
from BSSCS_CNN import BSSCS_CNN
import numpy as np
import unittest
import tensorflow as tf

class CNNFrameworkTests(unittest.TestCase):
	def test_cnn_block(self):
		''' Handles testing that a cnn block is created

			preconditions: no CNN block
			postconditions: CNN block lulz
		'''
		bsscs = BSSCS_CNN()
		input_ph = tf.placeholder(tf.float32, shape=[None, 512, 512, 1]) # 512 x 512 image with 1 channel
		cnn_block = bsscs.create_cnn_block(input=input_ph, filters=64, kernel_size=[3, 3], cnn_strides=2, pool_size=[2, 2], pooling_strides=2)
		self.assertTrue(cnn_block != None)

	def test_cnn_partial_blocks(self):
		''' Handles testing that a partial cnn block is created

			preconditions: no CNN partial block
			postconditions: CNN partial blocks
		'''
		bsscs = BSSCS_CNN()
		input_ph = tf.placeholder(tf.float32, shape=[None, 512, 512, 1])
		cnn_partial_cnn =  bsscs.create_partial_cnn(input=input_ph, filters=64, kernel_size=[3, 3], cnn_strides=2)
		cnn_partial_pooling = bsscs.create_partial_pooling(input=input_ph, pool_size=[2, 2], pooling_strides=2)
		self.assertTrue(cnn_partial_cnn != None)
		self.assertTrue(cnn_partial_pooling != None)

	def test_full_network(self):
		''' Handles testing the creation of a full CNN Network

			preconditions: No full network
			postconditions: Has full network
		'''
		bsscs = BSSCS_CNN()
		cnn_full_network = bsscs.create_convolution_network(input_shape=[None, 512, 512, 1], cnn_kernels=[[2, 2], [4, 4]], cnn_filters=[64, 128], pooling_filters=[[2, 2], [2, 2]])
		self.assertTrue(cnn_full_network != None)

if __name__ == '__main__':
	unittest.main()